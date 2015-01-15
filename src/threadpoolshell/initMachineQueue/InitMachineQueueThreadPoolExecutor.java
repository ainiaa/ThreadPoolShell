package threadpoolshell.initMachineQueue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

public class InitMachineQueueThreadPoolExecutor extends ThreadPoolExecutor {

    private boolean hasFinish = false;

    public InitMachineQueueThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        this.initDbIndexHashSet();
    }

    public InitMachineQueueThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    public InitMachineQueueThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        this.initDbIndexHashSet();
    }

    public InitMachineQueueThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.initDbIndexHashSet();
    }

    private void initDbIndexHashSet() {
        int tableIndex = 0;
        this.storageModel = new HashMap();
        this.storageIndexList = new ArrayList();
        for (int index = 0; index < 40; index++) {
            if (index % 4 == 0 && index != 0) {
                tableIndex++;
            }
            storageIndexList.add(index);
            this.storageModel.put(index, new ArrayList(Arrays.asList(new Integer[]{index % 4, tableIndex})));
        }
        /*
         this.dbIndexHashSet = new HashSet();
         this.dbIndexHashSet.add(0);//0,0
         this.dbIndexHashSet.add(1);//1,0
         this.dbIndexHashSet.add(2);//2,0
         this.dbIndexHashSet.add(3);//3,0
         this.dbIndexHashSet.add(4);//0,1
         this.dbIndexHashSet.add(5);//1,1
         this.dbIndexHashSet.add(6);//2,1
         this.dbIndexHashSet.add(7);//3,1
         this.dbIndexHashSet.add(8);//0,2
         this.dbIndexHashSet.add(9);//1,2
         this.dbIndexHashSet.add(10);//2,2
         this.dbIndexHashSet.add(11);//3,2
         this.dbIndexHashSet.add(12);//0,3
         this.dbIndexHashSet.add(13);//1,3
         this.dbIndexHashSet.add(14);//2,3
         this.dbIndexHashSet.add(15);//3,3
         this.dbIndexHashSet.add(16);//0,4
         this.dbIndexHashSet.add(17);//1,4
         this.dbIndexHashSet.add(18);//2,4
         this.dbIndexHashSet.add(19);//3,4
         this.dbIndexHashSet.add(20);//0,5
         this.dbIndexHashSet.add(21);//1,5
         this.dbIndexHashSet.add(22);//2,5
         this.dbIndexHashSet.add(23);//3,5
         this.dbIndexHashSet.add(24);//0,6
         this.dbIndexHashSet.add(25);//1,6
         this.dbIndexHashSet.add(26);//2,6
         this.dbIndexHashSet.add(27);//3,6
         this.dbIndexHashSet.add(28);//0,7
         this.dbIndexHashSet.add(29);//1,7
         this.dbIndexHashSet.add(30);//2,7
         this.dbIndexHashSet.add(31);//3,7
         this.dbIndexHashSet.add(32);//0,8
         this.dbIndexHashSet.add(33);//1,8
         this.dbIndexHashSet.add(34);//2,8
         this.dbIndexHashSet.add(35);//3,8
         this.dbIndexHashSet.add(36);//0,9
         this.dbIndexHashSet.add(37);//1,9
         this.dbIndexHashSet.add(38);//2,9
         this.dbIndexHashSet.add(39);//3,9
         */
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
            if (r instanceof InitMachineQueueTask) {
                InitMachineQueueTask task;
                task = (InitMachineQueueTask) r;
                String result = task.getResult();
                System.out.println(result);
                int storageIndex = task.getStorageIndex();
                if ("-1".equals(result)) {//返回-1 意思为 没有改dbIndex 或者 该dbIndex 没有数据需要清理 直接结束任务
                    int dbIndex = task.getDbIndex();
                    int tableIndex = task.getTableIndex();
                    List<Integer> entity = new ArrayList();
                    entity.add(dbIndex);
                    entity.add(tableIndex);
                    boolean removeStatus = removeMapItemByValue(this.storageModel, this.storageIndexList, entity);
                    if (removeStatus) {
                        System.out.println("信息不匹配 需要删除的信息 dbIndex:"
                                + dbIndex + " tableIndex:" + tableIndex + " 删除成功"
                        );
                    } else {
                        System.out.println("信息不匹配 需要删除的信息 dbIndex:"
                                + dbIndex + " tableIndex:" + tableIndex + " 删除失败"
                        );
                    }
                    /**
                     * if (this.storageModel.containsKey(storageIndex)) { int
                     * currentDbIndex =
                     * this.storageModel.get(storageIndex).get(0); int
                     * currentTableIndex =
                     * this.storageModel.get(storageIndex).get(1); if (dbIndex
                     * == currentDbIndex && tableIndex == currentTableIndex) {
                     * System.out.println("storageIndex" + storageIndex +
                     * "所有任务执行结束"); this.storageModel.remove(storageIndex); }
                     * else { System.out.println("信息不匹配 需要删除的信息 dbIndex:" +
                     * dbIndex + " tableIndex:" + tableIndex + " === 当前索引对应的信息为
                     * dbIndex:" + currentDbIndex + " currentTableIndex:" +
                     * currentTableIndex + " 删除失败"); } } else {
                     * System.out.println("storageIndex" + storageIndex + "
                     * storageModel 中 没有找到相关信息"); }
                     */
                } else {
                    System.out.println("storageIndex" + storageIndex + " result:" + result);
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

    public int getRealStorageIndex(int taskIndex) {
        int index;
        int size = this.storageIndexList.size();
        if (size > 0) {
            int storageIndexKey = taskIndex % size;
            index = this.storageIndexList.get(storageIndexKey);
        } else {
            index = -1;
        }
        return index;
    }

    private boolean removeMapItemByValue(HashMap<Integer, List<Integer>> map, List<Integer> storageIndexList, List<Integer> entity) {
        Set<Integer> key = map.keySet();
        boolean removeStatus = false;
        for (Iterator it = key.iterator(); it.hasNext();) {
            int s = (Integer) it.next();
            List<Integer> value = map.get(s);
            if (compare(value, entity)) {
                map.remove(s);
                for (int i = 0; i < storageIndexList.size(); i++) {
                    if (s == storageIndexList.get(i)) {
                        storageIndexList.remove(i);
                        break;
                    }
                }
                removeStatus = true;
                break;
            }
        }
        return removeStatus;
    }

    public static <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
        if (a.size() != b.size()) {
            return false;
        }
        Collections.sort(a);
        Collections.sort(b);
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i))) {
                return false;
            }
        }
        return true;
    }

    private HashMap<Integer, List<Integer>> storageModel;
    private List<Integer> storageIndexList;

    public HashMap<Integer, List<Integer>> getStorageModel() {
        return this.storageModel;
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
