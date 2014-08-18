package threadpoolshell.study;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
public class FileUtil {

    public static void main(String[] args) throws IOException {
//        System.out.println(
//                Thread.currentThread().getContextClassLoader().getResource(""));
//        System.out.println(FileUtil.class.getClassLoader().getResource(""));
//        System.out.println(ClassLoader.getSystemResource(""));
//        System.out.println(FileUtil.class.getResource(""));
//        System.out.println(FileUtil.class.getResource("/"));
//        System.out.println(new File("").getAbsolutePath());
//        System.out.println(System.getProperty("user.dir"));

        File directory = new File("");//参数为空 
        String courseFile = directory.getCanonicalPath();
        System.out.println(courseFile);

        String filePath = FileUtil.class.getResource("/").getPath() + "\\data\\setting.properties";
        File f = new File(FileUtil.class.getResource("/").getPath());
        System.out.println(f);

//        File f = new File(filePath);
        if (f.exists()) {
            Properties prop = new Properties();
            FileInputStream fis;
            try {
                fis = new FileInputStream(filePath);
                try {
                    prop.load(fis);
                } catch (IOException ex) {
                }
                if (!prop.getProperty("queueDeep", "").isEmpty()) {
                    System.out.println("queueDeep:" + prop.getProperty("queueDeep"));
                }
                if (!prop.getProperty("corePoolSize", "").isEmpty()) {
                    System.out.println("corePoolSize:" + prop.getProperty("corePoolSize"));
                }
                if (!prop.getProperty("maximumPoolSize", "").isEmpty()) {
                    System.out.println("maximumPoolSize:" + prop.getProperty("maximumPoolSize"));
                }
                if (!prop.getProperty("keepAliveTime", "").isEmpty()) {
                    System.out.println("keepAliveTime:" + prop.getProperty("keepAliveTime"));
                }
                if (!prop.getProperty("totalTaskSize", "").isEmpty()) {
                    String t = prop.getProperty("totalTaskSize");
                    int totalTaskSize = Integer.valueOf(t);
                    System.out.println("totalTaskSize:" + totalTaskSize);
                }
            } catch (FileNotFoundException ex) {
            }
        }
    }
}
