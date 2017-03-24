package blink.com.blinkcard320.Moudle;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import com.google.gson.FieldNamingStrategy;

import java.util.ArrayList;

/**
 * Created by Ruanjiahui on 2017/1/6.
 * <p/>
 * 公共类
 */
public class Comment {

    /**
     * sharedpres中快速启动
     */
    public static final String QUICK_START = "quick_start";
    public static final int REBOOT = 1;
    public static final int SET_TIME_SHUTDOWN = 2;
    public static final int SHUTDOWN = 3;
    public static final int LOCK_PC = 4;
    public static final int ALTER_PWD = 5;
    public static final int GET_UPDATE = 6;
    public static final int CAMERA = 7;
    public static final int DEFAULT = 0;

    /**
     * sharedprefs设置上传下传照片的目录
     */
    public static final String DOWNFILE = "downfile";
    public static final String PICTUREFILE = "picturefile";
    public static final String UPLOADFILE = "uploadfile";
    public static final String FILETYPE = "filetype";

    /**
     * sharedprefs自动登录需要
     */
    public static final String IS_AUTO_LOGIN = "is_auto_login";
    public static final String IS_REMEBER_PWD = "is_remeber_pwd";
    public static final String USERNAME = "login_username";
    public static final String PASSWORD = "login_password";
    public static final String IS_RELOGIN = "is_relogin";


    /**
     * 下载列表的链表
     */
    public static ArrayList<Object> list = new ArrayList<>();

    /**
     * 上传列表的链表
     */
    public static ArrayList<Object> Uploadlist = new ArrayList<>();

    //存储文件的路径
    public static String FilePath = Environment.getExternalStorageDirectory() + "/Blink/App/File";

    /**
     * 打电话的权限
     *
     * @param context
     * @param phone
     */
    public static void SendPhone(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        context.startActivity(intent);
    }
}
