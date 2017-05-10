package com.blink.blinkp2p.Controller.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.blink.blinkp2p.Controller.Activity.MainActivity;
import com.blink.blinkp2p.Controller.Activity.TransSportActivity;
import com.blink.blinkp2p.Moudle.Comment;
import com.blink.blinkp2p.R;
import com.blink.blinkp2p.View.ReconnectDialog;
import com.blink.blinkp2p.application.MyApplication;
import com.blink.blinkp2p.Controller.Activity.login.Login;
import com.blink.blinkp2p.Tool.System.Tools;
import com.blink.blinkp2p.heart.HeartController;

import java.security.PublicKey;
import java.util.Timer;
import java.util.TimerTask;

import smart.blink.com.card.Tcp.TcpSocket;
import smart.blink.com.card.Tool.ReqDownUp;
import smart.blink.com.card.Udp.UdpSocket;

/**
 * Created by Administrator on 2017/4/8.
 */
public class NetWorkStateReceiver extends BroadcastReceiver {

    private static final String TAG = NetWorkStateReceiver.class.getSimpleName();
    public static Timer timer;
    public static boolean isFirstTimer = true;
    public static Context context;


    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.e(TAG, "onReceive: ");
        // 不处理第一次事件
        if (isFirstTimer) {
            isFirstTimer = false;
            return;
        }

        Log.e(TAG, "onReceive: !Comment.isReceivedBroadCast===" + (!Comment.isReceivedBroadCast));
        // 判断是否允许接收网络变换的广播
        if (!Comment.isReceivedBroadCast) {
            return;
        }

        // 本次网络变换广播事件处理完成之前不会再处理类似事件
        Comment.isReceivedBroadCast = false;

        // 5s内只能接收一次网络变化请求
        if (timer == null) {
            this.context = context;

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(-1);

                    timer.cancel();
                    timer = null;
                }
            }, 8000);
        }

    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            // 如果网络变化的时候，如果任务列表中有任务就应该清空任务列表
            clearAllTask();

            boolean online = Tools.isOnline(context);
            if (online) {
                HeartController.stopHeart();
                // 重新登录的逻辑，先暂时这么写，测试能不能登录
                MyApplication.wantCount.set(0);
                MyApplication.helloCount.set(0);

//                // 释放Tcp资源
//                TcpSocket.closeTcpSocket();
//                // 释放Udp的资源
//                UdpSocket.closeUdpSocket();

                Comment.releaseSystemResource();


                // 弹出重新连接的对话框
                try {
                    ReconnectDialog.CreateYesNoDialog(context, context
                                    .getResources().getString(R.string.askbreak), context
                                    .getResources().getString(R.string.askreconnect),
                            context.getResources().getString(R.string.login),
                            context.getResources().getString(R.string.reconnect),
                            MainActivity.heartHandler);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            } else {
                //isFirstTimer = true;

                Comment.releaseSystemResource();
//                TcpSocket.closeTcpSocket();
//                UdpSocket.closeUdpSocket();

                // 重新跳到登录界面
                context.startActivity(new Intent(context, Login.class));
                MyApplication.getInstance().exit();
                Toast.makeText(context, R.string.attention_net_error, Toast.LENGTH_SHORT).show();
            }

        }
    };

    /**
     * 清空下载和上传列表中的任务
     */
    private void clearAllTask() {
        if (Comment.downlist.size() > 0) {
            Comment.downlist.clear();
            Comment.list.clear();
        }
        if (Comment.uploadlist.size() > 0) {
            Comment.uploadlist.clear();
            Comment.Uploadlist.clear();
        }
        Comment.tcpIsTaskStartFlag.set(false);
    }
}

