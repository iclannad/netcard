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
import com.blink.blinkp2p.Tool.Utils.download.DownTask;
import com.blink.blinkp2p.Tool.Utils.download.DownloadingImpl;
import com.blink.blinkp2p.Tool.Utils.download.ThreadHandlerImpl;
import com.blink.blinkp2p.Tool.Utils.download.ＭyDownloadThread;
import com.blink.blinkp2p.View.DownUpCallback;
import com.blink.blinkp2p.heart.HeartController;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import smart.blink.com.card.bean.DownLoadingRsp;

/**
 * Created by Administrator on 2017/4/17.
 */
public class MyTcpDownUtils implements Runnable, ThreadHandlerImpl, DownloadingImpl {

    private static final String TAG = MyTcpDownUtils.class.getSimpleName();

    private static Context context;

    //    public static int taskCount = 0;   // 已经启动了的任务数
//    public static int currentTaskCount = 0;    //　当前正在下载的任务数
    AtomicInteger taskCount = new AtomicInteger(0);
    AtomicInteger currentTaskCount = new AtomicInteger(0);

    public static Thread maintenceThread = null;      // 维护下载任务的线程
    public static boolean isNeedMonitorTask = false;
    public static ArrayList<Integer> finishTask = new ArrayList<>();    // 存放下载完成的任务

    public static DownUpCallback downUpCallback;

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


    public MyTcpDownUtils(Context context) {
        this.context = context;

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


    }

    /**
     * 对应着维护线程run方法
     */
    @Override
    public void run() {
        //Log.e(TAG, "run: Comment.tcpIsTaskStartFlag.get()==" + Comment.tcpIsTaskStartFlag.get());
        // 如果上传列表中有任务，就不会开启下载任务
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
            //　开启任务，最多同时能开启5个线程
            while (taskCount.get() < Comment.downlist.size() && currentTaskCount.get() < 1) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Activity activity = (Activity) context;

                final DownTask downTask = Comment.downlist.get(taskCount.get());

                // 如果当前任务已经在任务列表中删除的话，就不开启下一个任务
                if (downTask.status == 2) {
                    // 任务标记　自加
                    taskCount.getAndIncrement();
                    currentTaskCount.getAndIncrement();
                    finishTask(downTask.id);

                    continue;
                }

                downTask.status = 1;


//                DownorUpload downorUpload = new DownorUpload();
//                downorUpload.setName(downTask.name);
//                downorUpload.setFLAG(DownorUpload.DOWN);
//                downorUpload.setPath(downTask.path);
                // 开启一个Tcp下载任务的逻辑

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (downUpCallback != null) {
                            // 每次开启任务时都是更新一下界面
                            downUpCallback.Call(ActivityCode.Downloading, null);
                        }
                        new ＭyTcpDownloadThread(downTask.id, null, context, MyTcpDownUtils.this, MyTcpDownUtils.this);
                    }
                });

                currentTaskCount.getAndIncrement();
                // 任务标记　自加
                taskCount.getAndIncrement();
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
    public void downloading(int position, Object object) {
        int size = Comment.downlist.size();
        if (size <= 0) {
            return;
        }

        DownLoadingRsp downLoadingRsp = (DownLoadingRsp) object;
        DownTask downTask = Comment.downlist.get(position);
        downTask.speed = downLoadingRsp.getSpeed();

        // 当前下载的进行
        DecimalFormat df = new DecimalFormat("0.00");
        String db = df.format((double) downLoadingRsp.getBlockId() / (double) downLoadingRsp.getTotolSize());
        double d = 0;
        try {
            d = Double.parseDouble(db) * 100;
        }catch (NumberFormatException e) {
            d = 0;
        }

        int present = (int) d;
        downTask.progress = present;

        if (downUpCallback != null) {
            downUpCallback.Call(ActivityCode.Downloading, null);
        }
    }


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

        currentTaskCount.getAndDecrement();
        if (currentTaskCount.get() == 0 && taskCount.get() >= Comment.downlist.size()) {
            Activity activity = (Activity) MyTcpDownUtils.context;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "任务下载完毕", Toast.LENGTH_SHORT).show();
                }
            });

            Comment.tcpIsTaskStartFlag.set(false);
            // 重新开启心跳线程
            Comment.downlist.clear();
            Comment.list.clear();

            taskCount.set(0);
            isNeedMonitorTask = false;

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            HeartController.startHeart();
        }
        if (downUpCallback != null) {
            downUpCallback.Call(ActivityCode.Downloading, null);
        }
    }

    public static void setProgress(DownUpCallback downUpCallback) {
        MyTcpDownUtils.downUpCallback = downUpCallback;
    }

}
