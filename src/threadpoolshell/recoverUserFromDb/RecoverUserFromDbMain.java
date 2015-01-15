package threadpoolshell.recoverUserFromDb;

/**
 * @author Jeff Liu 清除给定用户
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RecoverUserFromDbMain {

    private static int queueDeep;
    private static int corePoolSize;
    private static int maximumPoolSize;
    private static int keepAliveTime;
    private static int totalTaskSize;
    private static int perTaskNumber;//
    private static String uidPath;
    private static String urlStr;

    public RecoverUserFromDbMain() {
        RecoverUserFromDbMain.queueDeep = 10;
        RecoverUserFromDbMain.corePoolSize = 5;
        RecoverUserFromDbMain.maximumPoolSize = 10;
        RecoverUserFromDbMain.keepAliveTime = 3;
        RecoverUserFromDbMain.totalTaskSize = 652788;
    }

    public RecoverUserFromDbMain(String filePath) throws IOException {
        File f = new File(filePath);
        if (f.exists()) {
            Properties prop = new Properties();
            FileInputStream fis;
            try {
                fis = new FileInputStream(filePath);
                try {
                    prop.load(fis);
                } catch (IOException ex) {
                    System.out.println(ex.toString());
                }
                if (!prop.getProperty("queueDeep", "").isEmpty()) {
                    RecoverUserFromDbMain.queueDeep = Integer.valueOf(prop.getProperty("queueDeep"));
                }
                if (!prop.getProperty("corePoolSize", "").isEmpty()) {
                    RecoverUserFromDbMain.corePoolSize = Integer.valueOf(prop.getProperty("corePoolSize"));
                }
                if (!prop.getProperty("maximumPoolSize", "").isEmpty()) {
                    RecoverUserFromDbMain.maximumPoolSize = Integer.valueOf(prop.getProperty("maximumPoolSize"));
                }
                if (!prop.getProperty("keepAliveTime", "").isEmpty()) {
                    RecoverUserFromDbMain.keepAliveTime = Integer.valueOf(prop.getProperty("keepAliveTime"));
                }
                if (!prop.getProperty("totalTaskSize", "").isEmpty()) {
                    RecoverUserFromDbMain.totalTaskSize = Integer.valueOf(prop.getProperty("totalTaskSize"));
                }
                if (!prop.getProperty("urlStr", "").isEmpty()) {
                    RecoverUserFromDbMain.urlStr = prop.getProperty("urlStr");
                }
                if (!prop.getProperty("uidPath", "").isEmpty()) {
                    RecoverUserFromDbMain.uidPath = prop.getProperty("uidPath");
                }
                if (!prop.getProperty("perTaskNumber", "").isEmpty()) {
                    RecoverUserFromDbMain.perTaskNumber = Integer.valueOf(prop.getProperty("perTaskNumber"));
                }
//                prop.put("urlStr", "http://dev-fb-dessertshop.shinezone.com/version/dev_lwy/j7/j7.php?/Cgi/ClearUserHttpByParam");
//                prop.put("uidPath", RecoverUserFromDbTask.class.getResource("/").getPath() + "\\data\\deleteUserId.txt");
//                FileOutputStream fOut = new FileOutputStream(filePath);
//                prop.store(fOut, "save urlString and uidPath");
            } catch (FileNotFoundException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    public void createThreadPool() {
        /*  
         * 创建线程池，最小线程数为2，最大线程数为4，线程池维护线程的空闲时间为3秒，  
         * 使用队列深度为4的有界队列，如果执行程序尚未关闭，则位于工作队列头部的任务将被删除，  
         * 然后重试执行程序（如果再次失败，则重复此过程），里面已经根据队列深度对任务加载进行了控制。  
         */
        RecoverUserFromDbThreadPoolExecutor threadPool = new RecoverUserFromDbThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueDeep),
                new ThreadPoolExecutor.CallerRunsPolicy());
        long startTime = System.currentTimeMillis();
        // 向线程池中添加 totalTaskSize 个任务
        int taskIndex = -1;
        while (true) {
//            if (threadPool.getHasFinish()) {//所有的任务都已经结束
//                break;
//            }
            RecoverUserFromDbTask ttp = new RecoverUserFromDbTask(++taskIndex, RecoverUserFromDbMain.urlStr, RecoverUserFromDbMain.uidPath, RecoverUserFromDbMain.perTaskNumber);
            threadPool.execute(ttp);
            if (taskIndex > RecoverUserFromDbMain.totalTaskSize) {
                break;
            }
        }
        threadPool.shutdown();
        threadPool.isEndTask();
        long endTime = System.currentTimeMillis();
        System.out.println("main thread end! total spend  time " + (endTime - startTime) + "ms");
        System.exit(0);//退出进程
    }

    public static void main(String[] args) throws IOException {
        String filePath = RecoverUserFromDbMain.class.getResource("/").getPath() + "\\data\\recoverUserSetting.properties";
        RecoverUserFromDbMain recoverUserFromDbMain = new RecoverUserFromDbMain(filePath);
        recoverUserFromDbMain.createThreadPool();
    }
}
