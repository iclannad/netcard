package com.blink.blinkp2p.Tool.Utils.upload;

import android.content.Context;
import android.util.Log;

import com.blink.blinkp2p.Moudle.Comment;
import com.blink.blinkp2p.Moudle.DownorUpload;
import com.blink.blinkp2p.Tool.Utils.download.ThreadHandlerImpl;
import com.blink.blinkp2p.heart.HeartController;

/**
 * Created by Administrator on 2017/4/13.
 * <p/>
 * 在这个上传类中我现在还没有对上传失败或者同名的处理
 */
public class MyUploadUtils implements Runnable, ThreadHandlerImpl {

    private static final String TAG = MyUploadUtils.class.getSimpleName();

    private Context context;
    public static int taskCount = 0;   // 已经启动了的任务数
    public static int currentTaskCount = 0;    //　当前正在下载的任务数
    public static Thread maintenceThread = null;      // 维护下载任务的线程
    public static boolean isNeedMonitorTask = false;

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
        Log.e(TAG, "MyUploadUtils: isNeedMonitorTask===" + isNeedMonitorTask);
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
                new MyUploadThread((DownorUpload) Comment.Uploadlist.get(taskCount), context, this).start();

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

    @Override
    public void finishTask() {
        currentTaskCount--;
        if (currentTaskCount == 0) {
            Log.e(TAG, "finishTask: 所有任务上传完毕");
            HeartController.startHeart();
        }

    }
}
