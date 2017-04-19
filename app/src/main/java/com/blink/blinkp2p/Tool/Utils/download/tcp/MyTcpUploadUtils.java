package com.blink.blinkp2p.Tool.Utils.download.tcp;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import com.blink.blinkp2p.Controller.ActivityCode;
import com.blink.blinkp2p.Moudle.Comment;
import com.blink.blinkp2p.Moudle.DownorUpload;
import com.blink.blinkp2p.Moudle.Item;
import com.blink.blinkp2p.R;
import com.blink.blinkp2p.Tool.Utils.download.ThreadHandlerImpl;
import com.blink.blinkp2p.Tool.Utils.upload.MyUploadThread;
import com.blink.blinkp2p.Tool.Utils.upload.UploadTask;
import com.blink.blinkp2p.Tool.Utils.upload.UploadingImpl;
import com.blink.blinkp2p.View.DownUpCallback;
import com.blink.blinkp2p.heart.HeartController;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import smart.blink.com.card.bean.UploadReq;

/**
 * Created by Administrator on 2017/4/18.
 */
public class MyTcpUploadUtils implements Runnable, ThreadHandlerImpl, UploadingImpl {
    private static final String TAG = MyTcpUploadUtils.class.getSimpleName();

    private static Context context;
    //    public static int taskCount = 0;   // 已经启动了的任务数
//    public static int currentTaskCount = 0;
    AtomicInteger taskCount = new AtomicInteger(0);
    AtomicInteger currentTaskCount = new AtomicInteger(0);

    public static Thread maintenceThread = null;
    public static boolean isNeedMonitorTask = false;
    public static DownUpCallback downUpCallback;

    private static Object getItem(int id, Drawable drawable, String title, String speed, String present, int progress) {
        Item item = new Item();
        item.setListImage(drawable);
        item.setListText(title);
        item.setListRightText(present);
        item.setListRightText1(speed);
        item.setProgressBar(progress);
        item.setHeight((int) context.getResources().getDimension(R.dimen.itemHeight));

        item.id = id;

        return item;
    }

    /**
     * 获取所有的上传任务
     *
     * @return
     */
    public static ArrayList<Object> getAllUploadingTask() {
        ArrayList<Object> allUploadingTask = new ArrayList<>();
        if (context != null && Comment.uploadlist.size() > 0)
            for (int i = 0; i < Comment.uploadlist.size(); i++) {
                UploadTask uploadTask = Comment.uploadlist.get(i);
                if (uploadTask.status == 2) {
                    continue;
                }

                Object object = getItem(uploadTask.id, context.getResources().getDrawable(R.mipmap.upload), uploadTask.name, uploadTask.speed, uploadTask.progress + "%", uploadTask.progress);
                allUploadingTask.add(object);
            }
        return allUploadingTask;
    }

    public MyTcpUploadUtils(Context context) {

        this.context = context;

        // 判断是否需要开启上传任务
        if (Comment.uploadlist.size() > 0) {
            isNeedMonitorTask = true;
        }

        //　开启一个维护任务线程
        if (maintenceThread == null) {
            maintenceThread = new Thread(this);
            maintenceThread.start();
        }

        //　停止心跳线程
        //HeartController.stopHeart();
        Log.e(TAG, "MyTcpUploadUtils: 所有的任务将开始上传");
    }

    @Override
    public void run() {
        Log.e(TAG, "run: Comment.tcpIsTaskStartFlag.get()==" + Comment.tcpIsTaskStartFlag.get() );
        // 下载列表中是否有任务
        while (Comment.tcpIsTaskStartFlag.get()) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        HeartController.stopHeart();
        Comment.tcpIsTaskStartFlag.set(true);

        while (isNeedMonitorTask) {
            //Log.e(TAG, "run: taskCount===" + taskCount + " Comment.uploadlist.size()===" + Comment.uploadlist.size() + " currentTaskCount===" + currentTaskCount);
            while (taskCount.get() < Comment.uploadlist.size() && currentTaskCount.get() < 1) {
                // 开启一个上传任务的逻辑
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final UploadTask uploadTask = Comment.uploadlist.get(taskCount.get());

                // 如果当前任务已经被删除的话就不需求开启下载
                if (uploadTask.status == 2) {
                    taskCount.getAndIncrement();
                    currentTaskCount.getAndIncrement();
                    finishTask(uploadTask.id);
                    continue;
                }

                uploadTask.status = 1;
                final DownorUpload downorUpload = new DownorUpload();
                downorUpload.setName(uploadTask.name);
                downorUpload.setPath(uploadTask.path);

                Activity activity = (Activity) context;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new MyTcpUploadThread(uploadTask.id, null, context, MyTcpUploadUtils.this, MyTcpUploadUtils.this);
                    }
                });

                // 任务标记　自加
                taskCount.getAndIncrement();
                currentTaskCount.getAndIncrement();
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        maintenceThread = null;
    }

    @Override
    public void finishTask(int position) {
        UploadTask uploadTask = Comment.uploadlist.get(position);
        uploadTask.status = 2;
        uploadTask.speed = "";
        uploadTask.progress = 100;

        currentTaskCount.getAndDecrement();
        if (currentTaskCount.get() == 0 && taskCount.get() >= Comment.uploadlist.size()) {
            Comment.tcpIsTaskStartFlag.set(false);
            Comment.uploadlist.clear();
            taskCount.set(0);
            isNeedMonitorTask = false;  // 关闭开启任务的while循环

            Activity activity = (Activity) MyTcpUploadUtils.context;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "任务上传完毕", Toast.LENGTH_SHORT).show();
                }
            });

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            HeartController.startHeart();
        }

        if (downUpCallback != null) {
            downUpCallback.Call(ActivityCode.Upload, null);
        }
    }

    @Override
    public void Uploading(int position, Object object) {
        int size = Comment.uploadlist.size();
        if (size <= 0) {
            return;
        }

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
        MyTcpUploadUtils.downUpCallback = downUpCallback;
    }

}
