package com.example.administrator.data_sdk.SystemUtil;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Administrator on 2016/1/12.
 */
public class MyConfiguration {

    //    public static String path = "/data/data/com.example.administrator.dreamproject/Properties.dat";
    private static Properties properties = new Properties();


    public static String getLocalPath(Context context) {
        return "data/data/" + SystemTool.getPackageName(context);
    }

    /**
     * 保存配置文件的操作方法
     *
     * @param key
     * @param setting
     * @param path
     * @param file_name
     */
    public static void writeConfiguration(String key, String setting, String path, String file_name) {
        properties.put(key, setting);

        try {
            FileOutputStream out = new FileOutputStream(path + "/" + file_name, false);
            properties.store(out, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取配置文件的操作方法
     *
     * @param key
     * @param path
     * @param file_name
     * @return
     */
    public static String readProperties(String key, String path, String file_name) {
        Properties properties = new Properties();
        try {
            FileInputStream in = new FileInputStream(path + "/" + file_name);
            //获取数据
            properties.load(in);
            if (properties.get(key) != null) {
                return properties.get(key).toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
