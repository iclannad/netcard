package blink.com.blinkcard320.Tool;


import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


import java.io.File;
import java.io.FileWriter;

import blink.com.blinkcard320.Controller.Activity.FilePreviewActivity;
import blink.com.blinkcard320.Controller.Activity.Login;
import blink.com.blinkcard320.Controller.Activity.MainActivity;
import blink.com.blinkcard320.Controller.ActivityCode;
import blink.com.blinkcard320.Controller.NetCardController;
import blink.com.blinkcard320.Moudle.Comment;
import blink.com.blinkcard320.Moudle.DownorUpload;
import blink.com.blinkcard320.Tool.Thread.HandlerImpl;
import blink.com.blinkcard320.Tool.Utils.SharedPrefsUtils;
import blink.com.blinkcard320.View.DownUpCallback;
import blink.com.blinkcard320.application.MyApplication;
import blink.com.blinkcard320.heart.SendHeartThread;
import smart.blink.com.card.API.BlinkWeb;
import smart.blink.com.card.Tool.FileWriteStream;
import smart.blink.com.card.Tool.Util;
import smart.blink.com.card.bean.DownLoadStartByServerRsp;
import smart.blink.com.card.bean.DownLoadStartRsp;
import smart.blink.com.card.bean.DownLoadingRsp;


/**
 * Created by Ruanjiahui on 2017/1/6.
 * <p/>
 * 下载处理类
 */
public class DownUtils implements HandlerImpl {

    private static final String TAG = DownUtils.class.getSimpleName();

    private DownorUpload downorUpload = null;
    private static boolean isEnd = true;
    public static int count = 0;
    private static DownUpCallback downUpCallback = null;
    Context context;


    public DownUtils(Context context) {
        this.context = context;
        if (isEnd) {
            StartLoad();
        }
    }

    public static void setProgress(DownUpCallback downUpCallback) {
        DownUtils.downUpCallback = downUpCallback;
    }

    private String downLoadFileNamePath;

    // 同一时段只能下载一个任务
    private void StartLoad() {
        downorUpload = (DownorUpload) Comment.list.get(count);
        downLoadFileNamePath = downorUpload.getPath();

        // 在发送下载指令之前，先停止心跳，下载完之后再开启
        // 释放心跳线程的资源
        SendHeartThread.isClose = true;
        synchronized (SendHeartThread.HeartLock) {
            SendHeartThread.HeartLock.notify();
        }

        NetCardController.DownloadStart(downLoadFileNamePath, this);
        count++; //先暂时注释
        isEnd = false;
    }


    private DownLoadStartRsp downLoadStartRsp = null;
    private DownLoadStartByServerRsp downLoadStartByServerRsp = null;

    /**
     * 处理线程返回的更新界面 （下载）
     *
     * @param position
     * @param object
     */
    @Override
    public void myHandler(int position, Object object) {
        if (position == ActivityCode.DownloadStart) {
            if (BlinkWeb.STATE == BlinkWeb.TCP) {
                downLoadStartByServerRsp = (DownLoadStartByServerRsp) object;
                if (downLoadStartByServerRsp.getSuccess() == 0) {
                    // 如果能进来这里的话就说明解析数据成功了
                    Log.e(TAG, "myHandler: " + "与子服务器连接时，发送的下载请求成功，接下来进行下载的过程，会调用DownLoading方法");
                    // cmd = 9
                    // 创建下载的文件
                    handleDownload(9, downLoadStartByServerRsp.getTotalBlock(), downLoadStartByServerRsp.getFileName());
                    // 获取下载的路径
                    String downFilePath = SharedPrefsUtils.getStringPreference(context, Comment.DOWNFILE);
                    String path = null;
                    if (downFilePath != null) {
                        path = downFilePath;
                    } else {
                        path = Environment.getExternalStorageDirectory() + "";
                    }

                    // 开始下载文件                文件下载的路径           文件名                   请求下载文件的总块数              方法的回调
                    NetCardController.DownLoading(path, downLoadStartByServerRsp.getFileName(), downLoadStartByServerRsp.getTotalBlock(), this);
                }
            } else {
                downLoadStartRsp = (DownLoadStartRsp) object;
                // 正在下载
                // 获取下载的路径
                String downFilePath = SharedPrefsUtils.getStringPreference(context, Comment.DOWNFILE);
                String path = null;
                if (downFilePath != null) {
                    path = downFilePath;
                } else {
                    path = Environment.getExternalStorageDirectory() + "";
                }
                Log.e(TAG, "myHandler: " + "接收到下载请求，开始下载");
                NetCardController.DownLoading(path, downLoadFileNamePath, downLoadStartRsp.getTotalblock(), this);
            }

        }

        if (position == ActivityCode.Downloading) {
            if (BlinkWeb.STATE == BlinkWeb.TCP) {
                Log.e(TAG, "myHandler: 下载成功的回调,再次开启心跳线程");

                //Comment.list.remove(0); // 删除任务列表中的第一个任务
                if (count < Comment.list.size()) {
                    //下载完一个暂停一秒再下下一个
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    StartLoad();
                } else {
                    // 当下载完毕之后将所有的任务都清空
                    Comment.list.clear();
                    count = 0;  // 重新清0
                    final Activity activity = (Activity) this.context;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "任务下载完毕", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // 下载完毕，已经没有任务在下载
                    isEnd = true;

                    SendHeartThread sendHeartThread = new SendHeartThread(MainActivity.heartHandler);
                    SendHeartThread.isClose = false;
                    sendHeartThread.start();
                }

            } else {
                DownLoadingRsp downLoadingRsp = (DownLoadingRsp) object;
                // 存放在全局变量中
                MyApplication.getInstance().downLoadingRsp = downLoadingRsp;
                // 更新界面调用
                if (downUpCallback != null) {
                    // 这条语句在子线程调用
                    downUpCallback.Call(position, downLoadingRsp);
                }

                Log.e("Ruan", downLoadingRsp.getSpeed() + "--");
                isEnd = downLoadingRsp.isEnd();
                if (isEnd) {
                    //Comment.list.remove(0); // 删除任务列表中的第一个任务
                    if (count < Comment.list.size()) {
                        //下载完一个暂停一秒再下下一个
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                        StartLoad();
                    } else {
                        // 当下载完毕之后将所有的任务都清空
                        Comment.list.clear();
                        count = 0;  // 重新清0
                        final Activity activity = (Activity) this.context;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "任务下载完毕", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            }

        }
    }

    /**
     * 当在外网请求下载成功的时候
     *
     * @param cmd
     * @param file_len
     * @param s
     */
    private void handleDownload(int cmd, long file_len, String s) {
        String fileName = s.replaceFirst(cmd + " " + file_len + " ", "");
        String true_name = Util.getTrueName(fileName);
        Log.e(TAG, "handleDownload: ");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                Log.e(TAG, "handleDownload: true_name" + true_name);
                // 获取下载的路径
                String temp = SharedPrefsUtils.getStringPreference(context, Comment.DOWNFILE);
                String res = temp + "/" + true_name;
                File true_file = new File(res);
                // 如果文件存在的话就重命名
                int i = 1;
                while (true_file.exists()) {
                    true_file = new File(res + "(" + i + ")");
                    i++;
                }
                if (true_file.length() != 0) {
                    FileWriter fw = new FileWriter(true_file);
                    fw.write("");
                    fw.close();
                }

                FileWriteStream.OpenFile(true_file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "handleDownload: " + "内存卡没有正常挂载");
        }
    }

    /**
     * 错误的操作
     *
     * @param position
     * @param error
     */
    @Override
    public void myError(int position, int error) {
        if (downUpCallback != null)
            downUpCallback.Fail(position);
        if (count < Comment.list.size()) {
            //下载完一个暂停一秒再下下一个
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            StartLoad();
        } else
            isEnd = true;
    }
}
