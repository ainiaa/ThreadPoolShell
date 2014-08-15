package threadpoolshell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ThreadPoolTask implements Runnable, Serializable {

    private final Object attachData;

    private static final String urlStr = "http://dev-fb-dessertshop.shinezone.com/version/dev_main/j7/j7.php?/Cgi/ClearUserHttpByParam";//这个需要修改正式环境的url

    private static final String uidPath = "D:\\www\\GitHub\\ThreadPoolShell\\src\\threadpoolshell\\deleteUserId.txt";

    private static final int perTaskNumber = 5;

    public ThreadPoolTask(Object tasks) {
        System.out.println("ThreadPoolTask:" + tasks);
        this.attachData = tasks;
        System.out.println("ThreadPoolTask this.attachData :" + this.attachData);
    }

    @Override
    public void run() {

        System.out.println("开始执行任务：file:" + this.attachData);
        long startTime = System.currentTimeMillis();
//        String tmpFilePath = "D:\\www\\test\\splitUser\\0\\0.txt";
//      String content = readTxtFile(uidPath + attachData + ".txt");  
//        String content = readTxtFile(tmpFilePath);
        String content = readTxtFileRangeRow(uidPath);
//        String content = readTxtFileRangeRow(tmpFilePath);
        HashMap mp = new HashMap();
        mp.put("uid", content);
        String ret = getURLContent(urlStr, mp);
        System.out.println("执行任务：ret:" + ret);
        long endTime = System.currentTimeMillis();
        System.out.println("执行任务：执行时间:" + (endTime - startTime) + "ms");
    }

    public Object getTask() {
        return this.attachData;
    }

    public String readTxtFileRangeRow(String filePath) {
        StringBuilder contents;
        contents = new StringBuilder();
        try {
            String encoding = "UTF-8";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) { //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                System.out.println(this.attachData);
                int i = Integer.valueOf(this.attachData.toString());//(Integer) this.attachData;直接这样会报 错的

                int start = i * ThreadPoolTask.perTaskNumber;
                int end = start + ThreadPoolTask.perTaskNumber;
                int index = -1;
                while ((lineTxt = bufferedReader.readLine()) != null) {
//                    System.out.println(lineTxt);
                    index++;
                    if (index < start) {
                        continue;
                    } else if (index >= end) {
                        break;
                    } else if (index != start) {
                        contents.append(",");
                    }
                    contents.append(lineTxt.trim());
                }
                read.close();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (IOException e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        System.out.println("contents:" + contents.toString());
        return contents.toString();
    }

    /**
     * 这个只是测试的时候用
     *
     * @param args
     */
//    public static void main(String[] args) {
//        ThreadPoolTask tpt = new ThreadPoolTask("1");
//        tpt.run();
//    }
    /**
     *
     * 功能：Java读取txt文件的内容 步骤：1：先获得文件句柄 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
     * 3：读取到输入流后，需要读取生成字节流 4：一行一行的输出。readline()。 备注：需要考虑的是异常情况
     *
     * @param filePath
     * @return
     *
     */
    public static String readTxtFile(String filePath) {
        StringBuilder contents;
        contents = new StringBuilder();
        try {

            String encoding = "UTF-8";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) { //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    System.out.println(lineTxt);
                    if (!lineTxt.isEmpty()) {
                        contents.append(",");
                    }
                    contents.append(lineTxt.trim());
                }
                read.close();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (IOException e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return contents.toString();
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
