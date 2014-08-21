package threadpoolshell.recoverUserFromDb;

import java.util.concurrent.*;

public class RecoverUserFromDbThreadPoolExecutor extends ThreadPoolExecutor {

    private boolean hasFinish = false;

    public RecoverUserFromDbThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public RecoverUserFromDbThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    public RecoverUserFromDbThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public RecoverUserFromDbThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    /**
     * (non-Javadoc) * @see
     * java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.Runnable,
     * java.lang.Throwable)
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        // TODO Auto-generated method stub
        super.afterExecute(r, t);
        synchronized (this) {
            if (r instanceof RecoverUserFromDbTask) {
                RecoverUserFromDbTask task;
                task = (RecoverUserFromDbTask) r;
                if ("-1".equals(task.result)) {
                    System.out.println("所有任务执行结束");
                    this.hasFinish = true;
                    this.shutdown();
                }
            }
            if (this.getActiveCount() == 1) {//已执行完任务之后的最后一个线程
                this.hasFinish = true;
                this.notify();
            }
        }
    }

    public boolean getHasFinish() {
        return this.hasFinish;
    }

    public void isEndTask() {
        synchronized (this) {
            while (this.hasFinish == false) {
                try {
                    this.wait();
                } catch (InterruptedException e) { // TODO Auto-generated catch block 
                    e.printStackTrace();
                }
            }
        }
    }
}