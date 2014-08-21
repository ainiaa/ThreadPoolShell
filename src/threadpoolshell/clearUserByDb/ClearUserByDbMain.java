package threadpoolshell.clearUserByDb;

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

public class ClearUserByDbMain {

    private static int queueDeep;
    private static int corePoolSize;
    private static int maximumPoolSize;
    private static int keepAliveTime;
    private static int perTaskNumber;//
    private static String urlStr;

    public ClearUserByDbMain() {
        ClearUserByDbMain.queueDeep = 10;
        ClearUserByDbMain.corePoolSize = 5;
        ClearUserByDbMain.maximumPoolSize = 10;
        ClearUserByDbMain.keepAliveTime = 3;
    }

    public ClearUserByDbMain(String filePath) throws IOException {
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
                    ClearUserByDbMain.queueDeep = Integer.valueOf(prop.getProperty("queueDeep"));
                }
                if (!prop.getProperty("corePoolSize", "").isEmpty()) {
                    ClearUserByDbMain.corePoolSize = Integer.valueOf(prop.getProperty("corePoolSize"));
                }
                if (!prop.getProperty("maximumPoolSize", "").isEmpty()) {
                    ClearUserByDbMain.maximumPoolSize = Integer.valueOf(prop.getProperty("maximumPoolSize"));
                }
                if (!prop.getProperty("keepAliveTime", "").isEmpty()) {
                    ClearUserByDbMain.keepAliveTime = Integer.valueOf(prop.getProperty("keepAliveTime"));
                }
                if (!prop.getProperty("urlStr", "").isEmpty()) {
                    ClearUserByDbMain.urlStr = prop.getProperty("urlStr");
                }
                if (!prop.getProperty("perTaskNumber", "").isEmpty()) {
                    ClearUserByDbMain.perTaskNumber = Integer.valueOf(prop.getProperty("perTaskNumber"));
                }
            } catch (FileNotFoundException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    public void createThreadPool() {
        ClearUserByDbThreadPoolExecutor threadPool = new ClearUserByDbThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueDeep),
                new ThreadPoolExecutor.CallerRunsPolicy());
        long startTime = System.currentTimeMillis();
        // 向线程池中添加 totalTaskSize 个任务
        int taskIndex = -1;
        while (true) {
            ++taskIndex;
            Integer[] dbIndexArray = threadPool.getDbIndexHashSet();
            if (dbIndexArray.length < 1) {//所有的任务都已经结束
                break;
            }
            int dbIndex = dbIndexArray[taskIndex % dbIndexArray.length];
            ClearUserByDbTask ttp = new ClearUserByDbTask(taskIndex, ClearUserByDbMain.urlStr, ClearUserByDbMain.perTaskNumber, dbIndex);
            threadPool.execute(ttp);
        }

        threadPool.shutdown();
        threadPool.isEndTask();

        long endTime = System.currentTimeMillis();
        System.out.println("main thread end! total spend  time " + (endTime - startTime) + "ms");
        System.exit(0);//退出进程
    }

    public static void main(String[] args) throws IOException {
        String filePath = ClearUserByDbMain.class.getResource("/").getPath() + "\\data\\setting.properties";
        ClearUserByDbMain clearUserThreadPoolExecutor = new ClearUserByDbMain(filePath);
        clearUserThreadPoolExecutor.createThreadPool();
    }
}
