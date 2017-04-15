package com.blink.blinkp2p.Tool.Utils.upload;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.blink.blinkp2p.Controller.ActivityCode;
import com.blink.blinkp2p.Moudle.Comment;
import com.blink.blinkp2p.Moudle.DownorUpload;
import com.blink.blinkp2p.Moudle.Item;
import com.blink.blinkp2p.R;
import com.blink.blinkp2p.Tool.Utils.download.DownTask;
import com.blink.blinkp2p.Tool.Utils.download.ThreadHandlerImpl;
import com.blink.blinkp2p.View.DownUpCallback;
import com.blink.blinkp2p.heart.HeartController;

import java.text.DecimalFormat;
import java.util.ArrayList;

import smart.blink.com.card.bean.UploadReq;

/**
 * Created by Administrator on 2017/4/13.
 * <p/>
 * 在这个上传类中我现在还没有对上传失败或者同名的处理
 */
public class MyUploadUtils implements Runnable, ThreadHandlerImpl, UploadingImpl {

    private static final String TAG = MyUploadUtils.class.getSimpleName();

    private static Context context;
    public static int taskCount = 0;   // 已经启动了的任务数
    public static int currentTaskCount = 0;    //　当前正在下载的任务数
    public static Thread maintenceThread = null;      // 维护下载任务的线程
    public static boolean isNeedMonitorTask = false;
    public static DownUpCallback downUpCallback;

    private static Object getItem(Drawable drawable, String title, String speed, String present, int progress) {
        Item item = new Item();
        item.setListImage(drawable);
        item.setListText(title);
        item.setListRightText(present);
        item.setListRightText1(speed);
        item.setProgressBar(progress);
        item.setHeight((int) context.getResources().getDimension(R.dimen.itemHeight));

        return item;
    }

    /**
     * 获取所有的上传任务
     *
     * @return
     */
    public static ArrayList<Object> getAllUploadingTask() {
        ArrayList<Object> allUploadingTask = new ArrayList<>();
        for (int i = 0; i < Comment.uploadlist.size(); i++) {
            UploadTask uploadTask = Comment.uploadlist.get(i);
            if (uploadTask.status == 2) {
                continue;
            }

            Object object = getItem(context.getResources().getDrawable(R.mipmap.upload), uploadTask.name, uploadTask.speed, uploadTask.progress + "%", uploadTask.progress);
            allUploadingTask.add(object);
        }
        return allUploadingTask;
    }

    public MyUploadUtils(Context context) {
        this.context = context;

        // 判断是否需要开启上传任务
        if (Comment.Uploadlist.size() > 0) {
            isNeedMonitorTask = true;
        }

        //　开启一个维护任务线程
        maintenceThread = new Thread(this);
        maintenceThread.start();

        //　停止心跳线程
        HeartController.stopHeart();
        Log.e(TAG, "finishTask: 所有的任务将开始上传");
    }


    @Override
    public void run() {
        while (isNeedMonitorTask) {
            while (taskCount < Comment.Uploadlist.size() && currentTaskCount <= 5) {
                // 开启一个上传任务的逻辑
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                UploadTask uploadTask = Comment.uploadlist.get(taskCount);
                uploadTask.status = 1;
                DownorUpload downorUpload = new DownorUpload();
                downorUpload.setName(uploadTask.name);
                downorUpload.setPath(uploadTask.path);

                new MyUploadThread(taskCount, downorUpload, context, this, this).start();

                // 任务标记　自加
                taskCount++;
                currentTaskCount++;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //　如果所有任务都启动完毕之后，就不需要再启动任务
            if (taskCount >= Comment.Uploadlist.size()) {
                isNeedMonitorTask = false;
                taskCount = 0;
                Comment.Uploadlist.clear();
            }

        }
    }

    /**
     * 任务下载完成的回调
     *
     * @param position
     */
    @Override
    public void finishTask(int position) {
        UploadTask uploadTask = Comment.uploadlist.get(position);
        uploadTask.status = 2;
        uploadTask.speed = "";
        uploadTask.progress = 100;

        currentTaskCount--;
        if (currentTaskCount == 0) {
            Comment.uploadlist.clear();
            Log.e(TAG, "finishTask: 所有任务上传完毕");
            HeartController.startHeart();
        }

        if (downUpCallback != null) {
            downUpCallback.Call(ActivityCode.Upload, null);
        }
    }

    /**
     * 任务在上传过程中的回调
     *
     * @param position
     * @param object
     */
    @Override
    public void Uploading(int position, Object object) {
        UploadReq uploadReq = (UploadReq) object;
        UploadTask uploadTask = Comment.uploadlist.get(position);
        uploadTask.speed = uploadReq.getSpeed();

        DecimalFormat df = new DecimalFormat("0.00");
        String db = df.format((double) uploadReq.getBlockID() / (double) uploadReq.getBlockSize());
        double d = Double.parseDouble(db) * 100;
        int present = (int) d;
        uploadTask.progress = present;

        if (downUpCallback != null) {
            downUpCallback.Call(ActivityCode.Upload, null);
        }
    }

    public static void setProgress(DownUpCallback downUpCallback) {
        MyUploadUtils.downUpCallback = downUpCallback;
    }

}
