package com.example.administrator.data_sdk.FileUtil;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2016/3/14.
 * <p/>
 * 这个类是管理所有的文件关于的路径
 * <p/>
 * 包括文件的路径是否存在等等
 */
public class FilePath {


    /**
     * 判断该路径有没有文件
     * <p/>
     * 如果返回@true则说明该文件存在 @false则说明文件不存在
     *
     * @param path
     */
    public static boolean FileFail(String path) {
        File file = new File(path);

        return file.exists();
    }

    /**
     * 判断有没有SD卡
     * 如果返回@true说明拥有SD卡，@false则没有SD卡
     * 如果没有SD则默认储存手机储存
     * 如果有SD则储存到SD卡
     *
     * @param path
     * @return
     */
    public static String SDCard(Context context , String path) {
        if (path.equals(Environment.MEDIA_MOUNTED))
            return path;
        return context.getFilesDir().getAbsolutePath();
    }

    /**
     * 删除指定路径的文件
     *
     * @param path
     */
    public static void DeleteFile(String path) {
        File file = new File(path);
        file.delete();
    }

    /**
     * 这个是获取系统SD卡的路径
     * 默认自动判断如果手机没有SD卡则自动获取本地储存
     * @param context
     * @return
     */
    public static String getFilePath(Context context){
        return SDCard(context , Environment.getExternalStorageDirectory() + "");
    }
}
