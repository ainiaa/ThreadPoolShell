/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threadpoolshell.common;

/**
 *
 * @author Administrator
 */
public class TaskData {

    private final int taskIndex;
    private String result;
    private int perTaskNumber;

    public TaskData(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    public int getTaskIndex() {
        return taskIndex;
    }
    
    public String getResult() {
        return taskIndex + " ";
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getPerTaskNumber() {
        return this.perTaskNumber;
    }

    public void setPerTaskNumber(int perTaskNumber) {
        this.perTaskNumber = perTaskNumber;
    }
}
