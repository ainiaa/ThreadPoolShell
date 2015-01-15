/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threadpoolshell.common;

import java.io.Serializable;
import java.util.Map;
import utils.HttpPostUtils;

/**
 *
 * @author Administrator
 */
public class CommonTask implements Runnable, Serializable {

    private final TaskData taskData;

    public CommonTask(TaskData taskData) {
        System.out.println("SimplyThreadPoolTask:" + taskData);
        this.taskData = taskData;
        System.out.println("SimplyThreadPoolTask this.attachData :" + this.taskData);
    }

//    public CommonTask(int taskIndex, String urlStr, int perTaskNumber, int dbIndex) {
//        this.taskIndex = taskIndex;
//        this.urlStr = urlStr;
//        this.perTaskNumber = perTaskNumber;
//        this.dbIndex = dbIndex;
//    }
    @Override
    public void run() {
//        System.out.println("开始执行任务:" + this.taskIndex + " dbIndex:" + this.dbIndex + " urlStr:" + urlStr);
//        HashMap mp = new HashMap();
//        mp.put("perTaskNumber", String.valueOf(this.perTaskNumber));
//        mp.put("dbIndex", String.valueOf(this.dbIndex));
//        String ret = getURLContent(urlStr, mp);
//        this.result = ret;
    }

//    public int getDbIndex() {
//        return this.dbIndex;
//    }
//
    public String getResult() {
        return taskData.getResult();
    }

    public int getTaskIndex() {
        return taskData.getTaskIndex();
    }

    public int getPerTaskNumber() {
        return taskData.getPerTaskNumber();
    }

    /**
     * 程序中访问http数据接口
     *
     * @param urlStr
     * @param paramMap
     * @return
     */
    public static String getURLContent(String urlStr, Map<String, String> paramMap) {
        String result = HttpPostUtils.httpPost(urlStr, paramMap);
        return result;
    }
}
