package com.blink.blinkp2p.Tool;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.blink.blinkp2p.Controller.Activity.MainActivity;
import com.blink.blinkp2p.Controller.ActivityCode;
import com.blink.blinkp2p.Tool.Dao.MsgDAO;
import com.blink.blinkp2p.Tool.Thread.HandlerImpl;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.blink.blinkp2p.Controller.NetCardController;
import com.blink.blinkp2p.Moudle.Comment;
import com.blink.blinkp2p.Moudle.DownorUpload;
import blink.com.blinkcard320.R;

import com.blink.blinkp2p.View.DownUpCallback;
import com.blink.blinkp2p.application.MyApplication;
import com.blink.blinkp2p.heart.SendHeartThread;
import smart.blink.com.card.API.BlinkWeb;
import smart.blink.com.card.bean.UploadReq;
import smart.blink.com.card.bean.UploadStartReq;

/**
 * Created by Ruanjiahui on 2017/1/9.
 * <p/>
 * 上传处理类
 */
public class UploadUtils implements HandlerImpl {

    private static final String TAG = UploadUtils.class.getSimpleName();

    private DownorUpload downorUpload = null;
    private static boolean isEnd = true;
    public static int count = 0;
    private static DownUpCallback downUpCallback = null;

    private Context context;

    public UploadUtils(Context context) {
        this.context = context;
        if (isEnd) {
            StartLoad();
        }
    }


    public static void setProgress(DownUpCallback downUpCallback) {
        UploadUtils.downUpCallback = downUpCallback;
    }

    private void StartLoad() {
        if (BlinkWeb.STATE == BlinkWeb.TCP) {
            // 如果当前的连接是与子服务器连接，上传之前就不用发送上传请求了
            // 在发送下载指令之前，先停止心跳，下载完之后再开启
            SendHeartThread.isClose = true;
            synchronized (SendHeartThread.HeartLock) {
                SendHeartThread.HeartLock.notify();
            }

            downorUpload = (DownorUpload) Comment.Uploadlist.get(count);
            Log.e(TAG, "StartLoad: downorUpload.getPath()===" + downorUpload.getPath() + "   downorUpload.getName()===" + downorUpload.getName());
            NetCardController.Upload(downorUpload.getPath(), downorUpload.getName(), this);
            count++;
        } else {
            SendHeartThread.isClose = true;
            synchronized (SendHeartThread.HeartLock) {
                SendHeartThread.HeartLock.notify();
            }

            downorUpload = (DownorUpload) Comment.Uploadlist.get(count);
            NetCardController.UploadStart(downorUpload.getPath(), downorUpload.getName(), this);
            count++;
            isEnd = false;
        }
    }

    private UploadStartReq uploadStartReq = null;

    /**
     * 处理线程返回的更新界面
     *
     * @param position
     * @param object
     */
    @Override
    public void myHandler(int position, Object object) {
        if (position == ActivityCode.UploadStart) {
            uploadStartReq = (UploadStartReq) object;
            int success = uploadStartReq.getSuccess();
            if (success == 0) {
                // 开始上传数据
                NetCardController.Upload(downorUpload.getPath(), downorUpload.getName(), this);
            }
            // 文件已经存在的逻辑，可能在错误待修改
            if (success == 23 || success == 34) {
                final Activity activity = (Activity) this.context;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "文件在电脑已存在，没有权限覆盖", Toast.LENGTH_SHORT).show();
                    }
                });
                SendHeartThread sendHeartThread = new SendHeartThread(MainActivity.heartHandler);
                SendHeartThread.isClose = false;
                sendHeartThread.start();

                Comment.Uploadlist.remove(downorUpload);
                count--;
                // 说明上传任务列表中有任务
                if (count < Comment.Uploadlist.size()) {
                    //上传完一个暂停一秒再上传下一个
                    Log.e(TAG, "如果任务列表中有任务就继续开启下一个任务");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                    StartLoad();
                } else {

                    Log.e(TAG, "myHandler: 没有任务直接清空");
                    isEnd = true;
                    // 当下载完毕之后清空任务列表
                    Comment.Uploadlist.clear();
                    count = 0;  // 清0
                    MyApplication.getInstance().uploadReq = null;
                }

            }
        }

        if (position == ActivityCode.Upload) {
//            if (BlinkWeb.STATE == BlinkWeb.TCP) {
                //----------------------------------------------------------------------------------
                UploadReq uploadReq = (UploadReq) object;
                // 传输界面的接口
                if (downUpCallback != null) {
                    MyApplication.getInstance().uploadReq = uploadReq;
                    downUpCallback.Call(position, uploadReq);
                }

                isEnd = uploadReq.isEnd();
                if (isEnd) {
                    // 当下载完成的时候，将数据保存在本地数据库中
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    MsgDAO msgdao = new MsgDAO(UploadUtils.this.context);
                    msgdao.insertdb(df.format(new Date()),
                            UploadUtils.this.context.getResources().getString(R.string.phone),
                            UploadUtils.this.context.getResources().getString(R.string.send),
                            UploadUtils.this.context.getResources().getString(R.string.pc),
                            null);
                    msgdao.close();
                    Log.e(TAG, "myHandler: 上传完成一个任务就保存在数据库中");

                    if (count < Comment.Uploadlist.size()) {
                        //上传完一个暂停一秒再上传下一个
                        Log.e(TAG, "myHandler: " + "再上传一个文件");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                        StartLoad();
                    } else {

                        // 当下载完毕之后清空任务列表
                        Comment.Uploadlist.clear();
                        count = 0;  // 清0
                        MyApplication.getInstance().uploadReq = null;

                        final Activity activity = (Activity) this.context;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "任务上传完毕", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.e(TAG, "myHandler: " + "所有任务都上传完毕,重新开启心跳线程");
                        SendHeartThread sendHeartThread = new SendHeartThread(MainActivity.heartHandler);
                        SendHeartThread.isClose = false;
                        sendHeartThread.start();
                    }
                }
//            } else {
//                UploadReq uploadReq = (UploadReq) object;
//
//                // 传输界面的接口
//                if (downUpCallback != null) {
//                    MyApplication.getInstance().uploadReq = uploadReq;
//                    downUpCallback.Call(position, uploadReq);
//                }
//
//                isEnd = uploadReq.isEnd();
//                if (isEnd) {
//
//                    // 当下载完成的时候，将数据保存在本地数据库中
//                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    MsgDAO msgdao = new MsgDAO(UploadUtils.this.context);
//                    msgdao.insertdb(df.format(new Date()),
//                            UploadUtils.this.context.getResources().getString(R.string.phone),
//                            UploadUtils.this.context.getResources().getString(R.string.send),
//                            UploadUtils.this.context.getResources().getString(R.string.pc),
//                            null);
//                    msgdao.close();
//                    Log.e(TAG, "myHandler: 上传完成一个任务就保存在数据库中");
//
//                    if (count < Comment.Uploadlist.size()) {
//                        //上传完一个暂停一秒再上传下一个
//                        Log.e(TAG, "myHandler: " + "再上传一个文件");
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                        }
//                        StartLoad();
//                    } else {
//                        // 当下载完毕之后清空任务列表
//                        Comment.Uploadlist.clear();
//                        count = 0;  // 清0
//                        MyApplication.getInstance().uploadReq = null;
//                        final Activity activity = (Activity) this.context;
//                        activity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(activity, "任务上传完毕", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        Log.e(TAG, "myHandler: " + "所有任务都上传完毕,重新开启心跳线程");
//                        SendHeartThread sendHeartThread = new SendHeartThread(MainActivity.heartHandler);
//                        SendHeartThread.isClose = false;
//                        sendHeartThread.start();
//                    }
//                }
//            }
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
        if (downUpCallback != null)
            downUpCallback.Fail(position);
        if (count < Comment.Uploadlist.size()) {
            //下载完一个暂停一秒再下下一个
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            StartLoad();
        } else
            isEnd = true;
    }
}
