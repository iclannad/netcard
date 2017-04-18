package com.blink.blinkp2p.Controller.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.blink.blinkp2p.Controller.Activity.MainActivity;
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
import smart.blink.com.card.Udp.UdpSocket;

/**
 * Created by Administrator on 2017/4/8.
 */
public class NetWorkStateReceiver extends BroadcastReceiver {

    private static final String TAG = NetWorkStateReceiver.class.getSimpleName();
    public static Timer timer;
    public static boolean timerFlag = true;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive: ");

        boolean online = Tools.isOnline(context);
        if (online) {
//            // 网络状态发生变化
//            if (timer == null) {
//                timer = new Timer();
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        timerFlag = true;
//                        timer.cancel();
//                        timer = null;
//                    }
//                }, 1000);
//            }
//            if (timerFlag) {
//                timerFlag = false;
//                // 停止当前的心跳
//                HeartController.stopHeart();
//                // 重新登录的逻辑，先暂时这么写，测试能不能登录
//                MyApplication.wantCount.set(0);
//                MyApplication.helloCount.set(0);
//
//                // 释放Tcp资源
//                TcpSocket.closeTcpSocket();
//                // 释放Udp的资源
//                UdpSocket.closeUdpSocket();
//
//                // 弹出重新连接的对话框
//                try {
//                    ReconnectDialog.CreateYesNoDialog(context, context
//                                    .getResources().getString(R.string.askbreak), context
//                                    .getResources().getString(R.string.askreconnect),
//                            context.getResources().getString(R.string.login),
//                            context.getResources().getString(R.string.reconnect),
//                            MainActivity.heartHandler);
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                }
//            }
        } else {
            Log.e(TAG, "onReceive: 当前网络不可用");
            TcpSocket.closeTcpSocket();
            UdpSocket.closeUdpSocket();
//            // 重新跳到登录界面
//            context.startActivity(new Intent(context, Login.class));
//            MyApplication.getInstance().exit();
//            Toast.makeText(context, R.string.attention_net_error, Toast.LENGTH_SHORT).show();
        }
    }
}

