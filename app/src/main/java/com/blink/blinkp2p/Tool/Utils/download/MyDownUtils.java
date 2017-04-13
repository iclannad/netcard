package com.blink.blinkp2p.Tool.Utils.download;

import android.content.Context;
import android.util.Log;

import com.blink.blinkp2p.Moudle.Comment;
import com.blink.blinkp2p.Moudle.DownorUpload;
import com.blink.blinkp2p.heart.HeartController;

/**
 * Created by Administrator on 2017/4/13.
 * <p/>
 * 注意：在这里我并没有考虑失败的情况 后面补
 */
public class MyDownUtils implements Runnable, ThreadHandlerImpl {

    private static final String TAG = MyDownUtils.class.getSimpleName();

    private Context context;

    public static int taskCount = 0;   // 已经启动了的任务数
    public static int currentTaskCount = 0;    //　当前正在下载的任务数
    public static Thread maintenceThread = null;      // 维护下载任务的线程
    public static boolean isNeedMonitorTask = false;

    public MyDownUtils(Context context) {
        this.context = context;
        // 如果任务列表中有任务
        if (Comment.list.size() > 0) {
            isNeedMonitorTask = true;
        }
        //　开启一个维护任务线程
        maintenceThread = new Thread(this);
        maintenceThread.start();

        //　停止心跳线程
        HeartController.stopHeart();
        Log.e(TAG, "finishTask: 所有的任务将开始下载");
        Log.e(TAG, "MyDownUtils: isNeedMonitorTask===" + isNeedMonitorTask);
    }


    /**
     * 对应着维护线程run方法
     */
    @Override
    public void run() {
        while (isNeedMonitorTask) {
            //　开启任务，最多同时能开启５个线程
            while (taskCount < Comment.list.size() && currentTaskCount <= 5) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 开启一个下载任务的逻辑
                new ＭyDownloadThread((DownorUpload) Comment.list.get(taskCount), context, this).start();

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
            if (taskCount >= Comment.list.size()) {
                isNeedMonitorTask = false;
                taskCount = 0;
                Comment.list.clear();
            }
        }
    }

    /**
     * 下载完成一个任务的回调
     */
    @Override
    public void finishTask() {
        currentTaskCount--;
        if (currentTaskCount == 0) {
            Log.e(TAG, "finishTask: 所有的任务下载完毕");
            // 重新开启心跳线程
            HeartController.startHeart();
        }
    }
}
