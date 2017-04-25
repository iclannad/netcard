package com.blink.blinkp2p.Tool.Utils.download.tcp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.blink.blinkp2p.Controller.ActivityCode;
import com.blink.blinkp2p.Controller.NetCardController;
import com.blink.blinkp2p.Moudle.Comment;
import com.blink.blinkp2p.Moudle.DownorUpload;
import com.blink.blinkp2p.R;
import com.blink.blinkp2p.Tool.Dao.MsgDAO;
import com.blink.blinkp2p.Tool.Thread.HandlerImpl;
import com.blink.blinkp2p.Tool.Utils.download.DownloadingImpl;
import com.blink.blinkp2p.Tool.Utils.download.ThreadHandlerImpl;
import com.blink.blinkp2p.Tool.Utils.upload.UploadingImpl;

import java.text.SimpleDateFormat;
import java.util.Date;

import smart.blink.com.card.API.Protocol;
import smart.blink.com.card.bean.UploadReq;

/**
 * Created by Administrator on 2017/4/18.
 */
public class MyTcpUploadThread implements HandlerImpl {

    private static final String TAG = MyTcpUploadThread.class.getSimpleName();
    private DownorUpload downorUpload;
    private Context context;
    private ThreadHandlerImpl threadHandler;
    private UploadingImpl uploading;
    private int position;

    public MyTcpUploadThread(int position, DownorUpload downorUpload, Context context, ThreadHandlerImpl threadHandler, UploadingImpl uploading) {
        this.downorUpload = downorUpload;
        this.context = context;
        this.threadHandler = threadHandler;
        this.position = position;
        this.uploading = uploading;

        startUpload();
    }

    /**
     * 开始上传任务
     */
    private void startUpload() {
        downorUpload = (DownorUpload) Comment.Uploadlist.get(this.position);
        Log.e(TAG, "startUpload: downorUpload.getPath()===" + downorUpload.getPath() + " downorUpload.getName()===" + downorUpload.getName());
        NetCardController.Upload(downorUpload.getPath(), downorUpload.getName(), this);
    }


    /**
     * 处理线程返回的更新界面
     *
     * @param position
     * @param object
     */
    @Override
    public void myHandler(int position, Object object) {
        if (position == ActivityCode.Upload) {
            UploadReq uploadReq = (UploadReq) object;
            if (!uploadReq.isEnd()) {
                // 下载中的回调
                uploading.Uploading(this.position, uploadReq);
                return;
            }

            if (context != null) {
                // 当下载完成的时候，将数据保存在本地数据库中
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                MsgDAO msgdao = new MsgDAO(context);
                msgdao.insertdb(df.format(new Date()),
                        context.getResources().getString(R.string.phone),
                        context.getResources().getString(R.string.send),
                        this.context.getResources().getString(R.string.pc),
                        null);
                msgdao.close();
            }

            // 上传完成后的回调
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
        if (position == ActivityCode.Upload) {
            Activity activity = (Activity) context;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "run:任务上传失败");
                    Toast.makeText(context, "任务上传失败:" + downorUpload.getName(), Toast.LENGTH_SHORT).show();
                    threadHandler.finishTask(MyTcpUploadThread.this.position);
                }
            });
        }
    }
}
