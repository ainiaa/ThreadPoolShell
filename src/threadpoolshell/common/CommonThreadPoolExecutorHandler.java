/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threadpoolshell.common;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Administrator
 */
public class CommonThreadPoolExecutorHandler {

    private static final int queueDeep = 2;
    private static final int corePoolSize = 1;
    private static final int maximumPoolSize = 2;
    private static final int keepAliveTime = 3;
    private static final int totalTaskSize = 10;

    public void createThreadPool() {
        /*  
         * 创建线程池，最小线程数为2，最大线程数为4，线程池维护线程的空闲时间为3秒，  
         * 使用队列深度为4的有界队列，如果执行程序尚未关闭，则位于工作队列头部的任务将被删除，  
         * 然后重试执行程序（如果再次失败，则重复此过程），里面已经根据队列深度对任务加载进行了控制。  
         */
        CommonThreadPoolExecutor threadPool = new CommonThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueDeep),
                new ThreadPoolExecutor.CallerRunsPolicy());
        //如果使用new ThreadPoolExecutor.DiscardOldestPolicy(); 当添加的任务数量大约maximumPoolSize的时候就会有任务被删除不被执行
        //new ThreadPoolExecutor.CallerRunsPolicy() 当添加的任务数量大约maximumPoolSize的时候就会重复添加当前任务直至被执行
        //new ThreadPoolExecutor.AbortPolicy() 当添加的任务数量大约maximumPoolSize的时候就会抛出异常
        //new ThreadPoolExecutor.DiscardPolicy()当添加的任务数量大约maximumPoolSize的时候当前任务被删除

        // 向线程池中添加 totalTaskSize 个任务
        for (int i = 0; i < totalTaskSize; i++) {
            TaskData td = new TaskData(i);
            CommonTask ttp = new CommonTask(td);
//            System.out.println("put i:" + i);
            threadPool.execute(ttp);
        }

        threadPool.shutdown();//关闭线程池

        threadPool.isEndTask();
        System.out.println("main thread end!");
        System.exit(0);//退出进程
    }

    public static void main(String[] args) {
        CommonThreadPoolExecutorHandler test = new CommonThreadPoolExecutorHandler();
        test.createThreadPool();
    }
}
