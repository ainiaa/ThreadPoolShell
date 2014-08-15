package threadpoolshell;

/**
 * @author Jeff Liu 清除给定用户
 */
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FinalClearUserThreadPoolExecutor {

    private static final int queueDeep = 10;
    private static final int corePoolSize = 5;
    private static final int maximumPoolSize = 10;
    private static final int keepAliveTime = 3;
    private static final int totalTaskSize = 652788;

    public void createThreadPool() {
        /*  
         * 创建线程池，最小线程数为2，最大线程数为4，线程池维护线程的空闲时间为3秒，  
         * 使用队列深度为4的有界队列，如果执行程序尚未关闭，则位于工作队列头部的任务将被删除，  
         * 然后重试执行程序（如果再次失败，则重复此过程），里面已经根据队列深度对任务加载进行了控制。  
         */
        MyThreadPoolExecutor threadPool = new MyThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueDeep),
                new ThreadPoolExecutor.CallerRunsPolicy());

        // 向线程池中添加 totalTaskSize 个任务
        for (int i = 0; i < totalTaskSize; i++) {
            ThreadPoolTask ttp = new ThreadPoolTask(i);
//            System.out.println("put i:" + i);
            threadPool.execute(ttp);
        }

        threadPool.shutdown();//关闭线程池

        threadPool.isEndTask();
        System.out.println("main thread end!");
        System.exit(0);//退出进程
    }

    public static void main(String[] args) {
        FinalClearUserThreadPoolExecutor test = new FinalClearUserThreadPoolExecutor();
        test.createThreadPool();
    }
}
