package com.blink.blinkp2p.Tool.System;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

import com.blink.blinkp2p.Controller.ActivityCode;
import com.blink.blinkp2p.Tool.Adapter.FileListAdapter;
import com.example.administrator.data_sdk.SystemUtil.SystemTool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ruanjiahui on 2016/12/5.
 */
public class Tools {

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static Bitmap ResizeBitmap(Bitmap bitmap, int newWidth) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float temp = ((float) height) / ((float) width);
        int newHeight = (int) ((newWidth) * temp);
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        bitmap.recycle();
        return resizedBitmap;
    }

    public static Bitmap getbitmap(String filename) {
        String sdStatus = Environment.getExternalStorageState();
        Bitmap mBitmap = null;
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // ���sd�Ƿ����
            Log.i("TestFile",
                    "SD card is not avaiable/writeable right now.");
            return null;
        }

        FileInputStream b = null;

        try {
            b = new FileInputStream(new String(filename));
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = 4;
            mBitmap = BitmapFactory.decodeStream(b, null, bitmapOptions);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (b == null)
                    return null;
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return mBitmap;

    }


    public static String getSystemPath(Context context) {
        return "data/data/" + Tools.getPackageName(context) + "/";
    }

    private static String skinConfig = "SkinConfig";
    private static String FirstRunApplication = "FirstRun.properties";

    public static boolean isFirstSkinConfig(Context context) {
        if (Tools.getProperties(context, skinConfig)) {
            if (!"".equals(Tools.ReadProperties(context, skinConfig, "server")))
                return false;
            return true;
        }
        return true;
    }

    /**
     * 判断是否第一次进入程序
     *
     * @param context
     * @return
     */
    public static boolean isFirstRunApplication(Context context) {
        // 判断配置文件在不在
        if (getProperties(context, FirstRunApplication)) {
            if (ReadProperties(context, FirstRunApplication, "FirstRun").equals(getVersionName(context)))
                return false;
        }
        //　如果不存在就添加配置文件或者更新配置文件的值
        WriteProperties(context, FirstRunApplication, "FirstRun", getVersionName(context));
        return true;
    }


    /**
     * 写入颜色配置
     *
     * @param context
     * @param skin
     */
    public static void WriteSkinConfig(Context context, int skin) {
        Tools.WriteProperties(context, skinConfig, "SkinConfig", String.valueOf(skin));
    }

    /**
     * 读取颜色配置
     *
     * @param context
     */
    public static String ReadSkinConfig(Context context) {
        return Tools.ReadProperties(context, skinConfig, "SkinConfig");
    }


    /**
     * 将配置写入配置文件
     *
     * @param context
     * @param keyName
     * @param keyValue
     */
    public static void WriteProperties(Context context, String ConfigName, String keyName, String keyValue) {
        Properties props = new Properties();
        try {
//            props.load(context.openFileInput(ConfigName));
            OutputStream out = context.openFileOutput(ConfigName, Context.MODE_PRIVATE);
            Enumeration<?> e = props.propertyNames();
            if (e.hasMoreElements()) {
                while (e.hasMoreElements()) {
                    String s = (String) e.nextElement();
                    if (!s.equals(keyName)) {
                        props.setProperty(s, props.getProperty(s));
                    }
                }
            }
            props.setProperty(keyName, keyValue);
            props.store(out, null);
        } catch (FileNotFoundException e) {
            Log.e("Ruan", "config.properties Not Found Exception", e);
        } catch (IOException e) {
            Log.e("Ruan", "config.properties IO Exception", e);
        }

    }

    /**
     * 判断配置文件在不在
     *
     * @param context
     * @param ConfigName
     * @return
     */
    public static boolean getProperties(Context context, String ConfigName) {
        Properties props = new Properties();
        try {
            props.load(context.openFileInput(ConfigName));
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * 读取配置文件
     *
     * @param context
     * @param keyName
     * @return
     */
    public static String ReadProperties(Context context, String ConfigName, String keyName) {
        Properties props = new Properties();
        try {
            props.load(context.openFileInput(ConfigName));
            return props.getProperty(keyName);
        } catch (FileNotFoundException e) {
            Log.e("Ruan", "config.properties Not Found Exception", e);
        } catch (IOException e) {
            Log.e("Ruan", "config.properties IO Exception", e);
        }
        return null;
    }

    /**
     * 获取软件的应用的包名
     *
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        return PackageInfo(context).packageName;
    }

    // 以下是获得版本信息的工具方法
    private static PackageInfo PackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }


    public static boolean isCamera(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean flag = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.CAMERA", Tools.getPackageName(context)));
        if (flag) {
            return true;
        } else {              //没有权限
            return false;
        }

    }


    public static boolean isFile(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean flag = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", Tools.getPackageName(context)));
        boolean flag1 = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.READ_PHONE_STATE", Tools.getPackageName(context)));
        if (flag && flag1) {
            return true;
        } else {              //没有权限
            return false;
        }

    }

    //版本名
    public static String getVersionName(Context context) {
        return PackageInfo(context).versionName;
    }


    public static boolean isEnglish(String english) {
//        String str = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ`~!@#$^&amp;*()-=_+[]{};:\\&apos;\\&quot;\\\\|/?.,&lt;>/% ";
//
//        for ()
        for (int i = 0; i < english.length(); i++) {
            String str = english.charAt(i) + "";
            if (str.getBytes().length > 1)
                return false;
        }
        return true;

    }


    /**
     * 验证邮箱输入是否合法
     *
     * @param strEmail
     * @return
     */
    public static boolean isEmail(String strEmail) {
// String strPattern =
// "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        String strPattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }

    /**
     * 验证是否是手机号码
     *
     * @param str
     * @return
     */
    public static boolean isMobile(String str) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

        Matcher m = p.matcher(str);

        return m.matches();
    }


    /**
     * 获取皮肤设置
     *
     * @param context
     * @return
     */
//    public static SkinConfig getSkinConfig(Context context) {
//        //设置皮肤的操作
//        int position = 0;
//        if (Tools.isFirstSkinConfig(context)) {
//            position = 0;
//            Tools.WriteSkinConfig(context, position);
//        } else
//            position = Integer.parseInt(Tools.ReadSkinConfig(context));
//        SkinConfig skinConfig = SkinConfig.getInstance();
//        skinConfig.setColor(Comment.skinArray[position]);
//        skinConfig.setPosition(position);
//        skinConfig.setSelectColor(Comment.skinselectArray[position]);
//        skinConfig.setButtonColor(Comment.skinbutselectArray[position]);
//        SkinConfig.setSkinConfig(skinConfig);
//
//        return skinConfig;
//    }

    /**
     * 获取图片的名称
     *
     * @param context
     * @return
     */
    public static String getPic(Context context) {
        String name = new DateFormat().format("yyyyMMdd_hhmmss",
                Calendar.getInstance(Locale.CHINA))
                + ".jpg";
        return SystemTool.getPath(context) + "Pic/" + name;
    }


    public static ArrayList<String> sortFileList(String parent) {
        File file = new File(parent + "/");
        String[] files = file.list();
        if (files == null)
            return null;
        if (files.length == 0)
            return null;
        else {
            List<String> dir_list = new ArrayList<String>();
            List<String> file_list = new ArrayList<String>();
            for (int i = 0; i < files.length; i++) {
                File tmp = new File(parent + "/" + files[i]);
                if (tmp.isDirectory())
                    dir_list.add(tmp.getPath());
                else
                    file_list.add(tmp.getPath());
            }
            Collections.sort(dir_list);
            Collections.sort(file_list);
            for (String string : file_list) {
                dir_list.add(string);
            }
            return (ArrayList<String>) dir_list;
        }
    }


    /**
     * 获取文件的路径
     *
     * @param path
     * @param positionFile
     * @return
     */
    public static final String GetFilePath(String path, File positionFile) {
        if (path.equals(".")) {
            Log.d("run", "yes");
            return Environment.getExternalStorageDirectory().getPath();
        } else if (path.equals("..")) {
            return positionFile.getParent();
        } else {
            return path;
        }
    }

    /**
     * 获取文件夹下的子文件
     *
     * @param f
     * @param list
     * @return
     */
    public static final ArrayList<FileListAdapter.Pair<String, Integer>> GetFilechild(File f, ArrayList<FileListAdapter.Pair<String, Integer>> list) {
        ArrayList<String> nlist = new ArrayList<>();
        nlist = sortFileList(f.getPath());
        list.clear();
        if (nlist == null) {
            nlist = new ArrayList<>();
            return list;
        }
        for (int i = 0; i < nlist.size(); i++) {
            FileListAdapter.Pair<String, Integer> pair = new FileListAdapter.Pair<>();
            pair.setA(nlist.get(i));
            File fA = new File(nlist.get(i));
            if (fA.isDirectory()) {
                pair.setB(ActivityCode.DIR);
            } else {
                pair.setB(ActivityCode.FL);
            }
            list.add(pair);
        }
        return list;
    }
}
