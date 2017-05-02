package com.blink.blinkp2p.Tool.Utils.download;

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
import com.blink.blinkp2p.Tool.Utils.upload.MyUploadThread;
import com.blink.blinkp2p.View.DownUpCallback;
import com.blink.blinkp2p.heart.HeartController;
import com.example.administrator.data_sdk.Crash.LogException;

import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.CRC32;

import smart.blink.com.card.bean.DownLoadingRsp;

/**
 * Created by Administrator on 2017/4/13.
 * <p/>
 * 注意：在这里我并没有考虑失败的情况 后面补
 */
public class MyDownUtils implements Runnable, ThreadHandlerImpl, DownloadingImpl {

    private static final String TAG = MyDownUtils.class.getSimpleName();

    private static Context context;

    //    public static int taskCount = 0;   // 已经启动了的任务数
//    public static int currentTaskCount = 0;    //　当前正在下载的任务数
    public static AtomicInteger taskCount = new AtomicInteger(0);
    public static AtomicInteger currentTaskCount = new AtomicInteger(0);

    public static Thread maintenceThread = null;      // 维护下载任务的线程
    public static boolean isNeedMonitorTask = false;
    public static ArrayList<Integer> finishTask = new ArrayList<>();    // 存放下载完成的任务

    public static DownUpCallback downUpCallback;

    public static void releaseResource() {
        taskCount.set(0);
        context = null;
        currentTaskCount.set(0);
        maintenceThread = null;
        isNeedMonitorTask = false;
        downUpCallback = null;
    }

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
     * 获取所有的下载任务
     *
     * @return
     */
    public static ArrayList<Object> getAllDownloadingTask() {
        ArrayList<Object> allDownloadingTask = new ArrayList<>();

        if (context != null && Comment.downlist.size() > 0) {
            for (int i = 0; i < Comment.downlist.size(); i++) {
                DownTask downTask = Comment.downlist.get(i);
                // 如果任务下载完成的就不会在传输列表中显示
                if (downTask.status == 2) {
                    continue;
                }
                Object object = getItem(downTask.id, downTask.status, context.getResources().getDrawable(R.mipmap.download), downTask.name, downTask.speed, downTask.progress + "%", downTask.progress);
                allDownloadingTask.add(object);
            }
        }
        return allDownloadingTask;
    }

    public MyDownUtils(Context context) {
        this.context = context;
        //Log.e(TAG, "MyDownUtils: Comment.downlist.size()===" + Comment.downlist.size());
        // 如果任务列表中有任务
        if (Comment.downlist.size() > 0) {
            isNeedMonitorTask = true;
        } else {
            return;
        }


        //　开启一个维护任务线程
        if (maintenceThread == null) {
            maintenceThread = new Thread(this);
            maintenceThread.start();
        }

        //　停止心跳线程
        //HeartController.stopHeart();
        //Log.e(TAG, "finishTask: 所有的任务将开始下载");
    }


    /**
     * 对应着维护线程run方法
     */
    @Override
    public void run() {
        //Log.e(TAG, "run: isNeedMonitorTask==" + isNeedMonitorTask);
        while (isNeedMonitorTask) {
            //　开启任务，最多同时能开启5个线程
            // taskCount < Comment.downlist.size() && currentTaskCount
            //Log.e(TAG, "run: taskCount.get()==" + taskCount.get());
            //Log.e(TAG, "run: Comment.downlist.size()==" + Comment.downlist.size());
            //Log.e(TAG, "run: currentTaskCount.get()==" + currentTaskCount.get());

            while (taskCount.get() < Comment.downlist.size() && currentTaskCount.get() < 5) {

                //Log.e(TAG, "run: ＭyDownloadThread.isAllowReqDownloadStart==" + ＭyDownloadThread.isAllowReqDownloadStart);
                if (ＭyDownloadThread.isAllowReqDownloadStart) {
                    ＭyDownloadThread.isAllowReqDownloadStart = false;
                    MyUploadThread.isAllowReqUploadStart = false;

                    //Log.e(TAG, "run: taskCount===" + taskCount + " Comment.downlist.size()===" + Comment.downlist.size());
                    final DownTask downTask = Comment.downlist.get(taskCount.get());

                    // 如果当前任务已经在任务列表中删除的话，就不开启下一个任务
                    if (downTask.status == 2) {
                        // 任务标记　自加
                        //taskCount++;
                        taskCount.getAndIncrement();
                        continue;
                    }

                    downTask.status = 1;
                    DownorUpload downorUpload = new DownorUpload();
                    downorUpload.setName(downTask.name);
                    downorUpload.setFLAG(DownorUpload.DOWN);
                    downorUpload.setPath(downTask.path);

                    // 开启一个下载任务的逻辑
                    new ＭyDownloadThread(downTask.id, downorUpload, context, this, this).start();

                    //currentTaskCount++;
                    currentTaskCount.getAndIncrement();
                    // 任务标记　自加
                    //taskCount++;
                    taskCount.getAndIncrement();

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
//                Log.e(TAG, "run: taskCount===" + taskCount + " Comment.downlist.size()===" + Comment.downlist.size());
//                final DownTask downTask = Comment.downlist.get(taskCount.get());
//
//                // 如果当前任务已经在任务列表中删除的话，就不开启下一个任务
//                if (downTask.status == 2) {
//                    // 任务标记　自加
//                    //taskCount++;
//                    taskCount.getAndIncrement();
//                    continue;
//                }
//
//                downTask.status = 1;
//                DownorUpload downorUpload = new DownorUpload();
//                downorUpload.setName(downTask.name);
//                downorUpload.setFLAG(DownorUpload.DOWN);
//                downorUpload.setPath(downTask.path);
//
//                // 开启一个下载任务的逻辑
//                new ＭyDownloadThread(downTask.id, downorUpload, context, this, this).start();
//
//                //currentTaskCount++;
//                currentTaskCount.getAndIncrement();
//                // 任务标记　自加
//                //taskCount++;
//                taskCount.getAndIncrement();
//
//                try {
//                    Thread.sleep(200);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
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
     * 下载完成一个任务的回调
     */
    @Override
    public void finishTask(int position) {
        int size = Comment.downlist.size();
        if (size <= 0) {
            return;
        }

        DownTask downTask = Comment.downlist.get(position);
        downTask.speed = "";
        downTask.progress = 100;
        downTask.status = 2;

        //currentTaskCount--;
        currentTaskCount.getAndDecrement();
        // currentTaskCount == 0
        if (currentTaskCount.get() == 0) {
            Activity activity = (Activity) MyDownUtils.context;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "任务下载完毕", Toast.LENGTH_SHORT).show();
                }
            });
            // 重新开启心跳线程
            Comment.downlist.clear();
            Comment.list.clear();

            //taskCount = 0;
            taskCount.set(0);
            isNeedMonitorTask = false;

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //HeartController.startHeart();
        }
        if (downUpCallback != null) {
            downUpCallback.Call(ActivityCode.Downloading, null);
        }
    }

    /**
     * 下载中的回调
     *
     * @param position
     * @param object
     */
    @Override
    public void downloading(int position, Object object) {
        Log.e(TAG, "downloading: Comment.downlist.size()===" + Comment.downlist.size());
        int size = Comment.downlist.size();
        if (size <= 0) {
            return;
        }


        DownLoadingRsp downLoadingRsp = (DownLoadingRsp) object;
        DownTask downTask = Comment.downlist.get(position);
        downTask.speed = downLoadingRsp.getSpeed();

        Log.e(TAG, "downloading: 下载中的回调：" + downLoadingRsp.getFilename());

        // 当前下载的进行
        DecimalFormat df = new DecimalFormat("0.00");
        String db = df.format((double) downLoadingRsp.getBlockId() / (double) downLoadingRsp.getTotolSize());
        double d = Double.parseDouble(db) * 100;
        int present = (int) d;
        downTask.progress = present;

        if (downUpCallback != null) {
            downUpCallback.Call(ActivityCode.Downloading, null);
        }
    }

    public static void setProgress(DownUpCallback downUpCallback) {
        MyDownUtils.downUpCallback = downUpCallback;
    }


}
