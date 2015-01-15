package threadpoolshell.initMachineQueue;

import utils.HttpPostUtils;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class InitMachineQueueTask implements Runnable, Serializable {

    private final int taskIndex;

    private final String urlStr;//"http://dev-fb-dessertshop.shinezone.com/version/dev_qa/j7/j7.php?/Cgi/ClearUserHttpByParam";//这个需要修改正式环境的url

    private final int perTaskNumber;// = 5;

    private String result;//返回结果

    private final int dbIndex;//数据库索引

    private final int start;//开始游标

    private final int tableIndex;//表名索引

    private final int storageIndex;// 存储索引

    public InitMachineQueueTask(int taskIndex, String urlStr, int perTaskNumber, int dbIndex, int start, int tableIndex, int storageIndex) {
        this.taskIndex = taskIndex;
        this.urlStr = urlStr;
        this.perTaskNumber = perTaskNumber;
        this.dbIndex = dbIndex;
        this.start = start;
        this.tableIndex = tableIndex;
        this.storageIndex = storageIndex;
    }

    public int getDbIndex() {
        return this.dbIndex;
    }

    public int getTableIndex() {
        return this.tableIndex;
    }

    @Override
    public void run() {
        System.out.println("开始执行任务:" + this.taskIndex + " dbIndex:" + this.dbIndex + " talbeIndex:" + this.tableIndex + " urlStr:" + urlStr);
        HashMap mp = new HashMap();
        mp.put("perTaskNumber", String.valueOf(this.perTaskNumber));
        mp.put("dbIndex", String.valueOf(this.dbIndex));
        mp.put("start", String.valueOf(this.start));
        mp.put("tableIndex", String.valueOf(this.tableIndex));
        String ret = getURLContent(urlStr, mp);
        this.result = ret;
    }

    public int getStorageIndex() {
        return this.storageIndex;
    }

    public String getResult() {
        return this.result;
    }

    public Object getTask() {
        return this.taskIndex;
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
