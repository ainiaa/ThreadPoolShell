package threadpoolshell.study;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import threadpoolshell.ClearUserThreadPoolExecutor;
import threadpoolshell.clearUserByLocal.ClearUserByLocalTask;

public class TestThreadPoolOld {

    private static final int produceTaskSleepTime = 2;

    private static final int produceTaskMaxNumber = 1;

    private static final int corePoolSize = 3;

    private static final int maxPoolSize = 5;

    private static final int keepAliveTime = 3;

    private static final int workQueueNumber = 3;

    public static void main(String[] args) {

        // 构造一个线程池  
        ClearUserThreadPoolExecutor threadPool = new ClearUserThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(workQueueNumber),
                new ThreadPoolExecutor.DiscardOldestPolicy());

//        for (int i = 0; i < produceTaskMaxNumber; i++) {
//            System.out.println("创建任务并提交到线程池中：" + i);
//            ClearUserByLocalTask taskObj = new ClearUserByLocalTask(i);
//            threadPool.execute(taskObj);
//        }
        addTaskToThreadPool(threadPool, 0);
        threadPool.shutdown();
        threadPool.isEndTask();
        System.out.println("main thread end!");
        System.exit(0);//退出进程
    }

    public static void addTaskToThreadPool(ClearUserThreadPoolExecutor threadPool, int start) {
        int end = produceTaskMaxNumber + start;
        for (int i = start; i < end; i++) {
            System.out.println("创建任务并提交到线程池中：" + i);
            ClearUserByLocalTask ttp = new ClearUserByLocalTask(i, "http://dev-fb-dessertshop.shinezone.com/version/dev_lwy/j7/j7.php?/Cgi/ClearUserHttpByParam", "D:/www/GitHub/ThreadPoolShell/build/classes/data/deleteUserId.txt", 5);
            threadPool.execute(ttp);
        }
    }
}
