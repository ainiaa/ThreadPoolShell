package threadpoolshell;

import utils.HttpPostUtils;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ClearUserTask implements Runnable, Serializable {

    private final int taskIndex;

    private final String urlStr;//"http://dev-fb-dessertshop.shinezone.com/version/dev_lwy/j7/j7.php?/Cgi/ClearUserHttpByParam";//这个需要修改正式环境的url

    private final int perTaskNumber;// = 5;

    private String result;//返回结果

    private final int dbIndex;//数据库索引

    private final int start;//limit start

    private final int end;//limit end

    public ClearUserTask(int taskIndex, String urlStr, int perTaskNumber, int dbIndex, int start, int end) {
        this.taskIndex = taskIndex;
        this.urlStr = urlStr;
        this.perTaskNumber = perTaskNumber;
        this.dbIndex = dbIndex;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        System.out.println("开始执行任务:" + this.taskIndex + " dbIndex:" + this.dbIndex + " urlStr:" + urlStr);
        HashMap mp = new HashMap();
        mp.put("perTaskNumber", String.valueOf(this.perTaskNumber));
        mp.put("dbIndex", String.valueOf(this.dbIndex));
        mp.put("start", String.valueOf(this.start));
        mp.put("end", String.valueOf(this.end));
        String ret = getURLContent(urlStr, mp);
        this.result = ret;
    }

    public int getPerTaskNumber() {
        return this.perTaskNumber;
    }
    
    public int getDbIndex() {
        return this.dbIndex;
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
