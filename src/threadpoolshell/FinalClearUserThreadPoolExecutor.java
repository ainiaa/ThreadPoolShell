package threadpoolshell;

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

public class FinalClearUserThreadPoolExecutor {

    private static int queueDeep;
    private static int corePoolSize;
    private static int maximumPoolSize;
    private static int keepAliveTime;
    private static int totalTaskSize;
    private static int perTaskNumber;//
    private static String uidPath;
    private static String urlStr;

    public FinalClearUserThreadPoolExecutor() {
        FinalClearUserThreadPoolExecutor.queueDeep = 10;
        FinalClearUserThreadPoolExecutor.corePoolSize = 5;
        FinalClearUserThreadPoolExecutor.maximumPoolSize = 10;
        FinalClearUserThreadPoolExecutor.keepAliveTime = 3;
        FinalClearUserThreadPoolExecutor.totalTaskSize = 652788;
    }

    public FinalClearUserThreadPoolExecutor(String filePath) throws IOException {
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
                    FinalClearUserThreadPoolExecutor.queueDeep = Integer.valueOf(prop.getProperty("queueDeep"));
                }
                if (!prop.getProperty("corePoolSize", "").isEmpty()) {
                    FinalClearUserThreadPoolExecutor.corePoolSize = Integer.valueOf(prop.getProperty("corePoolSize"));
                }
                if (!prop.getProperty("maximumPoolSize", "").isEmpty()) {
                    FinalClearUserThreadPoolExecutor.maximumPoolSize = Integer.valueOf(prop.getProperty("maximumPoolSize"));
                }
                if (!prop.getProperty("keepAliveTime", "").isEmpty()) {
                    FinalClearUserThreadPoolExecutor.keepAliveTime = Integer.valueOf(prop.getProperty("keepAliveTime"));
                }
                if (!prop.getProperty("totalTaskSize", "").isEmpty()) {
                    FinalClearUserThreadPoolExecutor.totalTaskSize = Integer.valueOf(prop.getProperty("totalTaskSize"));
                }
                if (!prop.getProperty("urlStr", "").isEmpty()) {
                    FinalClearUserThreadPoolExecutor.urlStr = prop.getProperty("urlStr");
                }
                if (!prop.getProperty("uidPath", "").isEmpty()) {
                    FinalClearUserThreadPoolExecutor.uidPath = prop.getProperty("uidPath");
                }
                if (!prop.getProperty("perTaskNumber", "").isEmpty()) {
                    FinalClearUserThreadPoolExecutor.perTaskNumber = Integer.valueOf(prop.getProperty("perTaskNumber"));
                }
//                prop.put("urlStr", "http://dev-fb-dessertshop.shinezone.com/version/dev_lwy/j7/j7.php?/Cgi/ClearUserHttpByParam");
//                prop.put("uidPath", ThreadPoolTask.class.getResource("/").getPath() + "\\data\\deleteUserId.txt");
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
        MyThreadPoolExecutor threadPool = new MyThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueDeep),
                new ThreadPoolExecutor.CallerRunsPolicy());
        long startTime = System.currentTimeMillis();
        // 向线程池中添加 totalTaskSize 个任务
        for (int taskIndex = 0; taskIndex < totalTaskSize; taskIndex++) {
            ThreadPoolTask ttp = new ThreadPoolTask(taskIndex, FinalClearUserThreadPoolExecutor.urlStr, FinalClearUserThreadPoolExecutor.uidPath, FinalClearUserThreadPoolExecutor.perTaskNumber);
            threadPool.execute(ttp);
        }

        threadPool.shutdown();//关闭线程池

        threadPool.isEndTask();
        long endTime = System.currentTimeMillis();
        System.out.println("main thread end! total spend  time " + (endTime - startTime) + "ms");
        System.exit(0);//退出进程
    }

    public static void main(String[] args) throws IOException {
        String filePath = FinalClearUserThreadPoolExecutor.class.getResource("/").getPath() + "\\data\\setting.properties";
        FinalClearUserThreadPoolExecutor clearUserThreadPoolExecutor = new FinalClearUserThreadPoolExecutor(filePath);
        clearUserThreadPoolExecutor.createThreadPool();
    }
}
