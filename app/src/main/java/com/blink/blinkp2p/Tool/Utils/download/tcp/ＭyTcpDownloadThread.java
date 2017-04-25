package com.blink.blinkp2p.Tool.Utils.download.tcp;

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
import com.blink.blinkp2p.Tool.Utils.download.DownloadingImpl;
import com.blink.blinkp2p.Tool.Utils.download.ThreadHandlerImpl;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import smart.blink.com.card.Tcp.File.FileWrite;
import smart.blink.com.card.Tool.FileWriteStream;
import smart.blink.com.card.Tool.Util;
import smart.blink.com.card.bean.DownLoadStartByServerRsp;
import smart.blink.com.card.bean.DownLoadingRsp;

/**
 * Created by Administrator on 2017/4/17.
 */
public class ＭyTcpDownloadThread implements HandlerImpl {

    private static final String TAG = ＭyTcpDownloadThread.class.getSimpleName();
    private DownorUpload downorUpload;
    private Context context;
    private ThreadHandlerImpl threadHandler;
    private DownloadingImpl downloading;
    private int position;

    public ＭyTcpDownloadThread(int position, DownorUpload downorUpload, Context context, ThreadHandlerImpl threadHandler, DownloadingImpl downloading) {
        this.downorUpload = downorUpload;
        this.context = context;
        this.threadHandler = threadHandler;
        this.position = position;
        this.downloading = downloading;

        startDownload();
    }

    /**
     * 开始下载
     */
    private void startDownload() {
        downorUpload = (DownorUpload) Comment.list.get(this.position);
        String downLoadFileNamePath = downorUpload.getPath();
        NetCardController.DownloadStart(downLoadFileNamePath, this);
    }

    /**
     * 处理线程返回的更新界面
     *
     * @param position
     * @param object
     */
    @Override
    public void myHandler(int position, Object object) {
        if (position == ActivityCode.DownloadStart) {

            DownLoadStartByServerRsp downLoadStartByServerRsp = (DownLoadStartByServerRsp) object;
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
        }
        if (position == ActivityCode.Downloading) {
            DownLoadingRsp downLoadingRsp = (DownLoadingRsp) object;

            // 如果当前的回调只是更新ui，那么不会开启一个任务
            if (!downLoadingRsp.isEnd()) {
                downloading.downloading(this.position, downLoadingRsp);
                return;
            }

            if (context != null) {
                // 当下载完成的时候，将数据保存在本地数据库中
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                MsgDAO msgdao = new MsgDAO(context);
                msgdao.insertdb(df.format(new Date()),
                        context.getResources().getString(R.string.pc),
                        context.getResources().getString(R.string.send),
                        context.getResources().getString(R.string.phone),
                        null);
                msgdao.close();
            }

            // 下载完成后的回调
            threadHandler.finishTask(this.position);
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
        if (position == ActivityCode.DownloadStart) {
            Activity activity = (Activity) context;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "任务下载失败:" + downorUpload.getName(), Toast.LENGTH_SHORT).show();
                    threadHandler.finishTask(ＭyTcpDownloadThread.this.position);
                }
            });
        }
        if (position == ActivityCode.Downloading) {
            Activity activity = (Activity) context;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "任务下载失败:" + downorUpload.getName(), Toast.LENGTH_SHORT).show();
                    threadHandler.finishTask(ＭyTcpDownloadThread.this.position);
                }
            });
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

}
