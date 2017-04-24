package com.blink.blinkp2p.Tool;


import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.blink.blinkp2p.Controller.ActivityCode;
import com.blink.blinkp2p.Controller.NetCardController;
import com.blink.blinkp2p.Moudle.Comment;
import com.blink.blinkp2p.Moudle.DownorUpload;
import com.blink.blinkp2p.R;
import com.blink.blinkp2p.Tool.Dao.MsgDAO;
import com.blink.blinkp2p.Tool.Thread.HandlerImpl;
import com.blink.blinkp2p.Tool.Utils.SharedPrefsUtils;
import com.blink.blinkp2p.View.DownUpCallback;
import com.blink.blinkp2p.application.MyApplication;
import com.blink.blinkp2p.heart.HeartController;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

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

        // 释放心跳线程的资源
        HeartController.stopHeart();

        Log.e(TAG, "StartLoad: downLoadFileNamePath===" + downLoadFileNamePath);
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
//                Log.e(TAG, "myHandler: " + "接收到下载请求，开始下载");
//                Log.e(TAG, "myHandler: path===" + path);
//                Log.e(TAG, "myHandler: downLoadFileNamePath===" + downLoadFileNamePath);
//                Log.e(TAG, "myHandler: downLoadStartRsp.getTotalblock()===" + downLoadStartRsp.getTotalblock());
                NetCardController.DownLoading(path, downLoadFileNamePath, downLoadStartRsp.getTotalblock(), this);
            }

        }

        if (position == ActivityCode.Downloading) {
            DownLoadingRsp downLoadingRsp = (DownLoadingRsp) object;
            // 存放在全局变量中
            MyApplication.getInstance().downLoadingRsp = downLoadingRsp;
            // 更新界面调用
            if (downUpCallback != null) {
                downUpCallback.Call(position, downLoadingRsp);
            }
            // 如果当前的回调只是更新ui，那么不会开启一个任务
            if (!downLoadingRsp.isEnd()) {
                return;
            }

            // 当下载完成的时候，将数据保存在本地数据库中
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            MsgDAO msgdao = new MsgDAO(DownUtils.this.context);
            msgdao.insertdb(df.format(new Date()),
                    DownUtils.this.context.getResources().getString(R.string.pc),
                    DownUtils.this.context.getResources().getString(R.string.send),
                    DownUtils.this.context.getResources().getString(R.string.phone),
                    null);
            msgdao.close();
            //Log.e(TAG, "myHandler: 下载完成一个任务就保存在数据库中");

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
                MyApplication.getInstance().downLoadingRsp = null;
                final Activity activity = (Activity) this.context;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "任务下载完毕", Toast.LENGTH_SHORT).show();
                    }
                });
                //Log.e(TAG, "myHandler: 任务下载完毕，重新开启心跳线程");
                // 下载完毕，已经没有任务在下载
                isEnd = true;

                HeartController.startHeart();
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
                //Log.e(TAG, "handleDownload: true_name===" + true_name);
                // 获取下载的路径
                String temp = SharedPrefsUtils.getStringPreference(context, Comment.DOWNFILE);
                String res = temp + "/" + true_name;
                File true_file = new File(res);
                // 如果文件存在的话就重命名
                int i = 1;
                while (true_file.exists()) {
                    // 获取文件的后缀名
                    String prefix = true_name.substring(true_name.lastIndexOf(".") + 1);
                    // 获取文件后缀名前面的部分
                    String name = true_name.substring(0, true_name.lastIndexOf("."));
                    String newFileName = temp + "/" + name + "(" + i + ")" + "." + prefix;
                    //Log.e(TAG, "handleDownload: 新的名字是newFileName===" + newFileName);
                    true_file = new File(newFileName);
                    //true_file = new File(res + "(" + i + ")");
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
        Log.e(TAG, "myError: position==" + position);
        if (position == ActivityCode.DownloadStart) {
            Log.e(TAG, "myError: 开始请求下载失败");
        }

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
