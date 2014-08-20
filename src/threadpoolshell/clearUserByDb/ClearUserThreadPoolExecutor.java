package threadpoolshell;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.*;

public class ClearUserThreadPoolExecutor extends ThreadPoolExecutor {

    private boolean hasFinish = false;

    public ClearUserThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        this.initDbIndexHashSet();
    }

    public ClearUserThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    public ClearUserThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        this.initDbIndexHashSet();
    }

    public ClearUserThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.initDbIndexHashSet();
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
            if (r instanceof ClearUserTask) {
                ClearUserTask task;
                task = (ClearUserTask) r;
                String result = task.getResult();
                System.out.println(result);
                if ("-1".equals(result)) {//返回-1 意思为 没有改dbIndex 或者 该dbIndex 没有数据需要清理 直接结束任务
                    int dbIndex = task.getDbIndex();
                    if (this.dbIndexHashSet.contains(dbIndex)) {
                        this.dbIndexHashSet.remove(dbIndex);
                        System.out.println("dbIndex" + dbIndex + "所有任务执行结束");
                    }
                } else {
                    int dbIndex = task.getDbIndex();
                    int taskIndex = Integer.valueOf(task.getTask().toString());
                    System.out.println("dbIndex" + dbIndex + " result:" + result);
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
