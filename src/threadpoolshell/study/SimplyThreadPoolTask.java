package threadpoolshell.study;

import java.io.Serializable;

public class SimplyThreadPoolTask implements Runnable, Serializable {

    private final Object attachData;

    public SimplyThreadPoolTask(Object tasks) {
        System.out.println("SimplyThreadPoolTask:" + tasks);
        this.attachData = tasks;
        System.out.println("SimplyThreadPoolTask this.attachData :" + this.attachData);
    }

    @Override
    public void run() {
        System.out.println("执行任务：file:" + this.attachData);
    }

    public Object getTask() {
        return this.attachData;
    }
}
