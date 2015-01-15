/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threadpoolshell.common;

import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Administrator
 */
public class CommonThreadPoolExecutor extends ThreadPoolExecutor {

    private boolean hasFinish = false;
    private static int start = 0;
    private static int end = 5;

    public CommonThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        this.initDbIndexHashSet();
    }

    public CommonThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    public CommonThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        this.initDbIndexHashSet();
    }

    public CommonThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.initDbIndexHashSet();
    }

    public static int getStart(int taskIndex, int pageSize) {
        int currentStart;
        if (taskIndex == 0) {
            currentStart = CommonThreadPoolExecutor.start;
        } else {
            currentStart = threadpoolshell.ClearUserThreadPoolExecutor.getEnd(taskIndex, pageSize) + pageSize;
        }
        if (currentStart < 0) {
            currentStart = 0;
        }
        return currentStart;
    }

    public static int getEnd(int taskIndex, int pageSize) {
        int currentEnd;
        if (taskIndex == 0) {
            currentEnd = pageSize;
        } else {
            currentEnd = CommonThreadPoolExecutor.end + pageSize;
        }
        if (currentEnd < 0) {
            currentEnd = 0;
        }
        return currentEnd;
    }

    private void initDbIndexHashSet() {
        this.dbIndexHashSet = new HashSet();
        this.dbIndexHashSet.add(0);
        this.dbIndexHashSet.add(1);
        this.dbIndexHashSet.add(2);
        this.dbIndexHashSet.add(3);
    }

    /**
     * (non-Javadoc) * @see
     * java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.Runnable,
     * java.lang.Throwable)
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        synchronized (this) {
//            System.out.println("自动调用了....afterEx 此时getActiveCount()值:" + this.getActiveCount());
            if (r instanceof CommonTask) {
                CommonTask task;
                task = (CommonTask) r;
                System.out.println(task.getResult());
                if ("-1".equals(task.getResult())) {//返回-1 意思为 没有改dbIndex 或者 该dbIndex 没有数据需要清理 直接结束任务
                    CommonThreadPoolExecutor.start = 0;
                    CommonThreadPoolExecutor.end = 0;
                } else {
                    int taskIndex = task.getTaskIndex();
                    System.out.println("dbIndex" + taskIndex + " result:" + taskIndex);
                    CommonThreadPoolExecutor.start -= task.getPerTaskNumber();
                    CommonThreadPoolExecutor.end -= task.getPerTaskNumber();
                }
            } else {
                System.out.println(r);
            }
            if (this.getActiveCount() == 1) {//已执行完任务之后的最后一个线程
                this.hasFinish = true;
                this.notify();
            }
        }
    }

    private HashSet<Integer> dbIndexHashSet;

    public Integer[] getDbIndexHashSet() {
        return this.dbIndexHashSet.toArray(new Integer[this.dbIndexHashSet.size()]);
    }

    public boolean getHasFinish() {
        return this.hasFinish;
    }

    public void isEndTask() {
        synchronized (this) {
            while (this.hasFinish == false) {
//                System.out.println("等待线程池所有任务结束: wait...");
                try {
                    this.wait();
                } catch (InterruptedException e) { // TODO Auto-generated catch block 
                    e.printStackTrace();
                }
            }
        }
    }
}
