package com.blink.blinkp2p.Tool.Utils.upload;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.blink.blinkp2p.Controller.ActivityCode;
import com.blink.blinkp2p.Controller.NetCardController;
import com.blink.blinkp2p.Moudle.DownorUpload;
import com.blink.blinkp2p.R;
import com.blink.blinkp2p.Tool.Dao.MsgDAO;
import com.blink.blinkp2p.Tool.Thread.HandlerImpl;
import com.blink.blinkp2p.Tool.Utils.download.ThreadHandlerImpl;
import com.blink.blinkp2p.Tool.Utils.download.ＭyDownloadThread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TransferQueue;

import smart.blink.com.card.bean.UploadReq;
import smart.blink.com.card.bean.UploadStartReq;

/**
 * Created by Administrator on 2017/4/13.
 * <p/>
 * 在这个上传类中我现在还没有对上传失败或者同名的处理
 */
public class MyUploadThread extends Thread implements HandlerImpl {

    private static final String TAG = MyUploadThread.class.getSimpleName();

    private DownorUpload downorUpload;
    private Context context;
    private ThreadHandlerImpl threadHandler;
    private int position;
    private UploadingImpl uploading;

    private Timer uploadingstarttimer;

    // 检测是否允许发送请求上传的数据
    // 以后尽量不这样写代码，这样的话关联太多了,尽量高内聚低耦合
    public static boolean isAllowReqUploadStart = true;


    public MyUploadThread(int position, DownorUpload downorUpload, Context context, ThreadHandlerImpl threadHandler, UploadingImpl uploading) {
        this.downorUpload = downorUpload;
        this.context = context;
        this.threadHandler = threadHandler;
        this.position = position;
        this.uploading = uploading;


    }

    @Override
    public void run() {
        // 如果20s后无响应，则说明请求下载任务失败
        uploadingstarttimer = new Timer();
        uploadingstarttimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (uploadingstarttimer != null) {
                    uploadingstarttimer.cancel();
                    uploadingstarttimer = null;
                    MyUploadThread.this.myError(ActivityCode.UploadStart, -1);
                }
            }
        }, 20000);
        Log.e(TAG, "run: 开始请求上传 downorUpload.getName()===" + downorUpload.getName());
        NetCardController.UploadStart(downorUpload.getPath(), downorUpload.getName(), this);
    }

    /**
     * 处理线程返回的更新界面
     *
     * @param position
     * @param object
     */
    @Override
    public void myHandler(int position, Object object) {

        if (position == ActivityCode.UploadStart) {
            // 允许下次下载请求
            MyUploadThread.isAllowReqUploadStart = true;
            ＭyDownloadThread.isAllowReqDownloadStart = true;

            Log.e(TAG, "myHandler: 请求上传文件成功: + downorUpload.getName()===" + downorUpload.getName());
            // 有数据过来说明请求下载成功
            if (uploadingstarttimer != null) {
                Log.e(TAG, "myHandler: 取消定时器 downorUpload.getName()===" + downorUpload.getName());
                uploadingstarttimer.cancel();
                uploadingstarttimer = null;
            }

            UploadStartReq uploadStartReq = (UploadStartReq) object;
            int success = uploadStartReq.getSuccess();
            // 上传请求成功
            if (success == 0) {
                // 开始上传数据
                NetCardController.Upload(downorUpload.getPath(), downorUpload.getName(), this);
            }

            // 如果文件在电脑已经存在的话就直接标志任务完成
            if (success == 23 || success == 34) {
                Activity activity = (Activity) context;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "上传路径已存在此文件:" + downorUpload.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
                threadHandler.finishTask(this.position);
            }
        }

        if (position == ActivityCode.Upload) {
            UploadReq uploadReq = (UploadReq) object;

            if (!uploadReq.isEnd()) {
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

            Log.e(TAG, "myHandler: 文件上传成功后的回调 downorUpload.getName()===" + downorUpload.getName());

            // 上传一个任务成功后的回调
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

        if (position == ActivityCode.UploadStart) {
            MyUploadThread.isAllowReqUploadStart = true;
            ＭyDownloadThread.isAllowReqDownloadStart = true;

            Log.e(TAG, "myError: ActivityCode.UploadStart 文件请求上传失败:" + downorUpload.getName());
            Activity activity = (Activity) context;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "任务上传失败:" + downorUpload.getName(), Toast.LENGTH_SHORT).show();
                    threadHandler.finishTask(MyUploadThread.this.position);
                }
            });
        }

        if (position == ActivityCode.Upload) {
            Log.e(TAG, "myError: ActivityCode.UploadStart 文件上传失败:" + downorUpload.getName());
            Activity activity = (Activity) context;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "任务上传失败:" + downorUpload.getName(), Toast.LENGTH_SHORT).show();
                    threadHandler.finishTask(MyUploadThread.this.position);
                }
            });
        }
    }
}
