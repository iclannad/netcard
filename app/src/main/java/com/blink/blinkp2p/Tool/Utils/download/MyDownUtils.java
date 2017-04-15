package com.blink.blinkp2p.Tool.Utils.download;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.blink.blinkp2p.Controller.ActivityCode;
import com.blink.blinkp2p.Moudle.Comment;
import com.blink.blinkp2p.Moudle.DownorUpload;
import com.blink.blinkp2p.Moudle.Item;
import com.blink.blinkp2p.R;
import com.blink.blinkp2p.View.DownUpCallback;
import com.blink.blinkp2p.heart.HeartController;

import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

import smart.blink.com.card.bean.DownLoadingRsp;

/**
 * Created by Administrator on 2017/4/13.
 * <p/>
 * 注意：在这里我并没有考虑失败的情况 后面补
 */
public class MyDownUtils implements Runnable, ThreadHandlerImpl, DownloadingImpl {

    private static final String TAG = MyDownUtils.class.getSimpleName();

    private static Context context;

    public static int taskCount = 0;   // 已经启动了的任务数
    public static int currentTaskCount = 0;    //　当前正在下载的任务数
    public static Thread maintenceThread = null;      // 维护下载任务的线程
    public static boolean isNeedMonitorTask = false;
    public static ArrayList<Integer> finishTask = new ArrayList<>();    // 存放下载完成的任务

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
     * 获取所有的下载任务
     *
     * @return
     */
    public static ArrayList<Object> getAllDownloadingTask() {
        ArrayList<Object> allDownloadingTask = new ArrayList<>();

        for (int i = 0; i < Comment.downlist.size(); i++) {
            DownTask downTask = Comment.downlist.get(i);
            if (downTask.status == 2) {
                continue;
            }
            Object object = getItem(context.getResources().getDrawable(R.mipmap.download), downTask.name, downTask.speed, downTask.progress + "%", downTask.progress);
            allDownloadingTask.add(object);
        }
        return allDownloadingTask;
    }

    public MyDownUtils(Context context) {
        this.context = context;
        // 如果任务列表中有任务
        if (Comment.downlist.size() > 0) {
            isNeedMonitorTask = true;
        }

        //　开启一个维护任务线程
        maintenceThread = new Thread(this);
        maintenceThread.start();

        //　停止心跳线程
        HeartController.stopHeart();
        Log.e(TAG, "finishTask: 所有的任务将开始下载");
    }


    /**
     * 对应着维护线程run方法
     */
    @Override
    public void run() {
        while (isNeedMonitorTask) {
            //　开启任务，最多同时能开启6个线程
            while (taskCount < Comment.downlist.size() && currentTaskCount <= 5) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DownTask downTask = Comment.downlist.get(taskCount);
                downTask.status = 1;
                DownorUpload downorUpload = new DownorUpload();
                downorUpload.setName(downTask.name);
                downorUpload.setFLAG(DownorUpload.DOWN);
                downorUpload.setPath(downTask.path);
                // 开启一个下载任务的逻辑
                new ＭyDownloadThread(taskCount, downorUpload, context, this, this).start();

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
            if (taskCount >= Comment.downlist.size()) {
                isNeedMonitorTask = false;
                taskCount = 0;
            }
        }
    }

    /**
     * 下载完成一个任务的回调
     */
    @Override
    public void finishTask(int position) {

        DownTask downTask = Comment.downlist.get(position);
        downTask.speed = "";
        downTask.progress = 100;
        downTask.status = 2;

        currentTaskCount--;
        if (currentTaskCount == 0) {
            Log.e(TAG, "finishTask: 所有的任务下载完毕");
            // 重新开启心跳线程
            Comment.downlist.clear();
            HeartController.startHeart();
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
        DownLoadingRsp downLoadingRsp = (DownLoadingRsp) object;
        DownTask downTask = Comment.downlist.get(position);
        downTask.speed = downLoadingRsp.getSpeed();

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
