package com.blink.blinkp2p.Moudle;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.blink.blinkp2p.Controller.receiver.NetWorkStateReceiver;
import com.blink.blinkp2p.Tool.Utils.download.DownTask;
import com.blink.blinkp2p.Tool.Utils.download.MyDownUtils;
import com.blink.blinkp2p.Tool.Utils.download.tcp.MyTcpDownUtils;
import com.blink.blinkp2p.Tool.Utils.download.tcp.MyTcpUploadUtils;
import com.blink.blinkp2p.Tool.Utils.download.ＭyDownloadThread;
import com.blink.blinkp2p.Tool.Utils.upload.MyUploadThread;
import com.blink.blinkp2p.Tool.Utils.upload.MyUploadUtils;
import com.blink.blinkp2p.Tool.Utils.upload.UploadTask;
import com.blink.blinkp2p.heart.HeartController;
import com.google.gson.FieldNamingStrategy;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import smart.blink.com.card.Tcp.MyDown;
import smart.blink.com.card.Tcp.MyUpload;
import smart.blink.com.card.Tcp.TcpSocket;
import smart.blink.com.card.Tool.ReqDownUp;
import smart.blink.com.card.Udp.Down;
import smart.blink.com.card.Udp.UdpSocket;
import smart.blink.com.card.Udp.Upload;

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
    public static final String IS_NEED_CLEAR_OLDER_PWD = "is_need_clear_older_pwd";

    /**
     * 这是为了兼容旧版本
     */
    public final static String LOGINDATA = "logindata";

    /**
     * 这是为了兼容旧版本，快截启动
     */
    public final static String ICON_QUITSTART = "QuitStart";
    public final static String ICON_RESTART = "RESTART";
    public final static String ICON_LOCLPC = "LOOKPC";
    public final static String ICON_CHANGEPASSWD = "CHANGEPW";
    public final static String ICON_CAMERA = "CAMERA";
    public final static String ICON_TIMESHUTDOWN = "TIMESHUTDOWN";
    public final static String ICON_SHUTDOWN = "SHUTDOWN";
    public final static String ICON_GETUPDATE = "GETUPDATE";
    public final static String ICON_UPDATE = "UPDATE";


    /**
     * 外网下载的标志位
     */
    public static AtomicBoolean tcpIsTaskStartFlag = new AtomicBoolean(false);

    /**
     * 下载列表的链表
     */
    public static ArrayList<Object> list = new ArrayList<>();
    public static ArrayList<DownTask> downlist = new ArrayList<>();

    public static final int DOWNLOAD = 1;
    public static final int UPLOAD = 2;

    /**
     * 上传列表的链表
     */
    public static ArrayList<Object> Uploadlist = new ArrayList<>();
    public static ArrayList<UploadTask> uploadlist = new ArrayList<>();

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

    /**
     * 默认的时候允许接收广播接收者
     */
    public static boolean isReceivedBroadCast = true;

    /**
     * 释放系统的资源
     */
    public static void releaseSystemResource() {
        HeartController.stopHeart();
        HeartController.isStart = false;

        // 释放Tcp资源
        TcpSocket.closeTcpSocket();
        // 释放Udp的资源
        UdpSocket.closeUdpSocket();

        // 清空任务列表中的任务
        Comment.list.clear();
        Comment.Uploadlist.clear();
        Comment.uploadlist.clear();
        Comment.downlist.clear();
        Comment.tcpIsTaskStartFlag.set(false);

        NetWorkStateReceiver.isFirstTimer = true;
        MyUploadUtils.isNeedMonitorTask = false;
        MyDownUtils.isNeedMonitorTask = false;
        MyTcpUploadUtils.isNeedMonitorTask = false;
        MyTcpDownUtils.isNeedMonitorTask = false;

        // 不允许接收广播
        Comment.isReceivedBroadCast = false;

        MyUploadThread.isAllowReqUploadStart = true;
        ＭyDownloadThread.isAllowReqDownloadStart = true;

        ReqDownUp.releaseResource();
        MyDownUtils.releaseResource();
        MyUploadUtils.releaseResource();

        // 如果释放资源的时候有任务正在下载，就关闭
        Down.isStart = false;
        Upload.isStart = false;

        MyDown.isStart = false;
        MyDown.releaseResource();

        MyUpload.isStart = false;
        MyUpload.releaseResource();

    }
}
