package threadpoolshell.initMachineQueue;

/**
 * @author Jeff Liu 清除给定用户
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class InitMachineQueueMain {

    private static int queueDeep;
    private static int corePoolSize;
    private static int maximumPoolSize;
    private static int keepAliveTime;
    private static int perTaskNumber;//
    private static String urlStr;
    private static final int sleepTimeInterval = 500;//队列满的时候 sleep的毫秒数

    public InitMachineQueueMain() {
        InitMachineQueueMain.queueDeep = 10;
        InitMachineQueueMain.corePoolSize = 5;
        InitMachineQueueMain.maximumPoolSize = 10;
        InitMachineQueueMain.keepAliveTime = 3;
    }

    public InitMachineQueueMain(String filePath) throws IOException {
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
                    InitMachineQueueMain.queueDeep = Integer.valueOf(prop.getProperty("queueDeep"));
                }
                if (!prop.getProperty("corePoolSize", "").isEmpty()) {
                    InitMachineQueueMain.corePoolSize = Integer.valueOf(prop.getProperty("corePoolSize"));
                }
                if (!prop.getProperty("maximumPoolSize", "").isEmpty()) {
                    InitMachineQueueMain.maximumPoolSize = Integer.valueOf(prop.getProperty("maximumPoolSize"));
                }
                if (!prop.getProperty("keepAliveTime", "").isEmpty()) {
                    InitMachineQueueMain.keepAliveTime = Integer.valueOf(prop.getProperty("keepAliveTime"));
                }
                if (!prop.getProperty("urlStr", "").isEmpty()) {
                    InitMachineQueueMain.urlStr = prop.getProperty("urlStr");
                }
                if (!prop.getProperty("perTaskNumber", "").isEmpty()) {
                    InitMachineQueueMain.perTaskNumber = Integer.valueOf(prop.getProperty("perTaskNumber"));
                }
            } catch (FileNotFoundException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    public void createThreadPool() {
        InitMachineQueueThreadPoolExecutor threadPool = new InitMachineQueueThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueDeep),
                new ThreadPoolExecutor.CallerRunsPolicy());
        long startTime = System.currentTimeMillis();
        // 向线程池中添加 totalTaskSize 个任务
        int taskIndex = -1;
        while (true) {
            if (getQueueSize(threadPool.getQueue()) >= queueDeep) {
                System.out.println("队列已满，等" + sleepTimeInterval + "毫秒再添加任务");
                try {
                    Thread.sleep(sleepTimeInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                ++taskIndex;
                HashMap<Integer, List<Integer>> storageModel = threadPool.getStorageModel();
                int storageIndex = threadPool.getRealStorageIndex(taskIndex);
                if (storageIndex == -1) {//所有的任务都已经结束
                    break;
                }
                
                List<Integer> storageInfo = storageModel.get(storageIndex);

                if (storageInfo != null) {//某些时候 会出现  NullpointerException 
                    int dbIndex = storageInfo.get(0);
                    int start = 0;
                    int tableIndex = storageInfo.get(1);
                    InitMachineQueueTask ttp = new InitMachineQueueTask(taskIndex, InitMachineQueueMain.urlStr, InitMachineQueueMain.perTaskNumber, dbIndex, start, tableIndex, storageIndex);
                    threadPool.execute(ttp);
                } else {
                    break;
                }
            }
        }

        
        threadPool.shutdown();
        threadPool.isEndTask();

        long endTime = System.currentTimeMillis();
        System.out.println("main thread end! total spend  time " + (endTime - startTime) + "ms");
        System.exit(0);//退出进程
    }

    private int getQueueSize(Queue queue) {
        return queue.size();
    }

    public static void main(String[] args) throws IOException {
        String filePath = InitMachineQueueMain.class.getResource("/").getPath() + "\\data\\setting.properties";
        InitMachineQueueMain clearUserThreadPoolExecutor = new InitMachineQueueMain(filePath);
        clearUserThreadPoolExecutor.createThreadPool();
    }
}
