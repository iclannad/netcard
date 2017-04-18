package com.blink.blinkp2p.Tool.Utils.upload;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.blink.blinkp2p.Controller.ActivityCode;
import com.blink.blinkp2p.Controller.NetCardController;
import com.blink.blinkp2p.Moudle.DownorUpload;
import com.blink.blinkp2p.Tool.Thread.HandlerImpl;
import com.blink.blinkp2p.Tool.Utils.download.ThreadHandlerImpl;

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


    public MyUploadThread(int position, DownorUpload downorUpload, Context context, ThreadHandlerImpl threadHandler, UploadingImpl uploading) {
        this.downorUpload = downorUpload;
        this.context = context;
        this.threadHandler = threadHandler;
        this.position = position;
        this.uploading = uploading;
    }

    @Override
    public void run() {
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

            // 上传一个任务成功后的回调
            threadHandler.finishTask(this.position);
            Log.e(TAG, "myHandler: 上传一个任务成功");
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

        }

        if (position == ActivityCode.Upload) {

        }
    }
}
