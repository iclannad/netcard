package com.blink.blinkp2p.Tool.Utils.upload;

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
import com.blink.blinkp2p.Tool.Utils.download.DownTask;
import com.blink.blinkp2p.Tool.Utils.download.ThreadHandlerImpl;
import com.blink.blinkp2p.Tool.Utils.download.ＭyDownloadThread;
import com.blink.blinkp2p.View.DownUpCallback;
import com.blink.blinkp2p.heart.HeartController;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import smart.blink.com.card.bean.UploadReq;

/**
 * Created by Administrator on 2017/4/13.
 * <p/>
 * 在这个上传类中我现在还没有对上传失败或者同名的处理
 */
public class MyUploadUtils implements Runnable, ThreadHandlerImpl, UploadingImpl {

    private static final String TAG = MyUploadUtils.class.getSimpleName();

    private static Context context;

    public static AtomicInteger taskCount = new AtomicInteger(0);
    public static AtomicInteger currentTaskCount = new AtomicInteger(0);

    public static Thread maintenceThread = null;
    public static boolean isNeedMonitorTask = false;
    public static DownUpCallback downUpCallback;

    // 当重新登录时需要释放资源
    public static void releaseResource() {
        taskCount.set(0);
        context = null;
        currentTaskCount.set(0);
        maintenceThread = null;
        isNeedMonitorTask = false;
        downUpCallback = null;
    }


    // 传输界面中每个下载任务条目的数据
    private static Object getItem(int id, int status, Drawable drawable, String title, String speed, String present, int progress) {
        Item item = new Item();
        item.setListImage(drawable);
        item.setListText(title);
        item.setListRightText(present);
        item.setListRightText1(speed);
        item.setProgressBar(progress);
        item.setHeight((int) context.getResources().getDimension(R.dimen.itemHeight));

        item.id = id;
        item.status = status;

        return item;
    }

    /**
     * 获取所有的上传任务
     *
     * @return
     */
    public static ArrayList<Object> getAllUploadingTask() {
        ArrayList<Object> allUploadingTask = new ArrayList<>();

        if (context != null && Comment.uploadlist.size() > 0) {
            for (int i = 0; i < Comment.uploadlist.size(); i++) {
                UploadTask uploadTask = Comment.uploadlist.get(i);
                if (uploadTask.status == 2) {
                    continue;
                }

                Object object = getItem(uploadTask.id, uploadTask.status, context.getResources().getDrawable(R.mipmap.upload), uploadTask.name, uploadTask.speed, uploadTask.progress + "%", uploadTask.progress);
                allUploadingTask.add(object);
            }
        }
        return allUploadingTask;
    }

    /**
     * 构造方法
     */
    public MyUploadUtils(Context context) {
        this.context = context;

        // 判断是否需要开启上传任务
        if (Comment.uploadlist.size() > 0) {
            isNeedMonitorTask = true;
        } else {
            return;
        }

        //　开启一个维护任务线程
        if (maintenceThread == null) {
            maintenceThread = new Thread(this);
            maintenceThread.start();
        }

    }

    @Override
    public void run() {
//        Log.e(TAG, "run: isNeedMonitorTask===" + isNeedMonitorTask);
        while (isNeedMonitorTask) {
            Log.e(TAG, "run: taskCount.get()===" + taskCount.get());
            Log.e(TAG, "run: Comment.uploadlist.size()===" + Comment.uploadlist.size());
            Log.e(TAG, "run: currentTaskCount.get()===" + currentTaskCount.get());
            while (taskCount.get() < Comment.uploadlist.size() && currentTaskCount.get() < 5) {

                if (MyUploadThread.isAllowReqUploadStart) {
                    MyUploadThread.isAllowReqUploadStart = false;
                    ＭyDownloadThread.isAllowReqDownloadStart = false;

                    UploadTask uploadTask = Comment.uploadlist.get(taskCount.get());

                    // 如果当前任务已经被删除的话就不需求开启下载
                    if (uploadTask.status == 2) {
                        taskCount.getAndIncrement();

                        // 允许下次上传下载请求
                        MyUploadThread.isAllowReqUploadStart = true;
                        ＭyDownloadThread.isAllowReqDownloadStart = true;
                        continue;
                    }

                    // 更新任务的状态
                    uploadTask.status = 1;
                    DownorUpload downorUpload = new DownorUpload();
                    downorUpload.setName(uploadTask.name);
                    downorUpload.setPath(uploadTask.path);

                    // 上传任务的逻辑
                    new MyUploadThread(uploadTask.id, downorUpload, context, this, this).start();

                    // 任务标记　自加
                    taskCount.getAndIncrement();
                    currentTaskCount.getAndIncrement();

                    // 开启一个上传任务的逻辑
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        maintenceThread = null;
    }

    /**
     * 任务下载完成的回调
     *
     * @param position
     */
    @Override
    public void finishTask(int position) {
        Log.e(TAG, "finishTask: position===" + position);
        int size = Comment.uploadlist.size();
        if (size <= 0) {
            return;
        }

        UploadTask uploadTask = Comment.uploadlist.get(position);

        // 根据打印日志
        if (uploadTask.status == 2) {
            return;
        }

        uploadTask.status = 2;
        uploadTask.speed = "";
        uploadTask.progress = 100;

        currentTaskCount.getAndDecrement();
        //Log.e(TAG, "finishTask: currentTaskCount.get()===" + currentTaskCount.get());
        //Log.e(TAG, "finishTask: taskCount.get()===" + taskCount.get());
        //Log.e(TAG, "finishTask: Comment.uploadlist.size()===" + Comment.uploadlist.size());

        if (currentTaskCount.get() == 0 && taskCount.get() >= Comment.uploadlist.size()) {
            Comment.uploadlist.clear();
            Comment.Uploadlist.clear();

            taskCount.set(0);
            isNeedMonitorTask = false;  // 关闭开启任务的while循环

            Activity activity = (Activity) MyUploadUtils.context;
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

            //HeartController.startHeart();
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
        int size = Comment.uploadlist.size();
        if (size <= 0) {
            return;
        }
        if (position >= Comment.uploadlist.size()) {
            return;
        }

        UploadReq uploadReq = (UploadReq) object;
        UploadTask uploadTask = Comment.uploadlist.get(position);
        uploadTask.speed = uploadReq.getSpeed();

        // 计算进度条
        DecimalFormat df = new DecimalFormat("0.00");
        String db = df.format((double) uploadReq.getBlockID() / (double) uploadReq.getBlockSize());
        double d = Double.parseDouble(db) * 100;
        int present = (int) d;
        Log.e(TAG, "Uploading: uploadReq.getFilename()===" + uploadReq.getFilename() + "   present===" + present);
        uploadTask.progress = present;

        // 传输列表中刷新任务
        if (downUpCallback != null) {
            downUpCallback.Call(ActivityCode.Upload, null);
        }
    }

    public static void setProgress(DownUpCallback downUpCallback) {
        MyUploadUtils.downUpCallback = downUpCallback;
    }

}
