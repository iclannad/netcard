package com.blink.blinkp2p.heart;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.blink.blinkp2p.Controller.Activity.login.Login;
import com.blink.blinkp2p.Controller.NetCardController;
import com.blink.blinkp2p.Controller.receiver.NetWorkStateReceiver;
import com.blink.blinkp2p.Moudle.Comment;
import com.blink.blinkp2p.R;
import com.blink.blinkp2p.Tool.Protocol;
import com.blink.blinkp2p.Tool.System.Tools;
import com.blink.blinkp2p.Tool.Thread.HandlerImpl;
import com.blink.blinkp2p.View.MyPersonalProgressDIalog;
import com.blink.blinkp2p.View.ReconnectDialog;
import com.blink.blinkp2p.application.MyApplication;

import smart.blink.com.card.Tcp.TcpSocket;
import smart.blink.com.card.Udp.UdpSocket;

/**
 * Created by Administrator on 2017/3/21.
 */
public class HeartHandler extends Handler {

    private static final String TAG = HeartHandler.class.getSimpleName();
    Context context;
    HandlerImpl handler;

    public HeartHandler(Context context, HandlerImpl handler) {
        this.context = context;
        this.handler = handler;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        int result = msg.what;
        switch (result) {
            case SendHeartThread.HEART_CONTINUE:
                NetCardController.Heart(handler);
                break;
            case SendHeartThread.HEART_LOSS:
//                HeartController.stopHeart();
                // 与服务器失去连接的逻辑
                SendHeartThread sendHeartThread = (SendHeartThread) msg.obj;
                sendHeartThread.interrupt();    // 中断当前线程
                Log.e(TAG, "handleMessage: " + "60秒后依旧没有接收到报务器返回的心跳包，所以判定与服务器连接失败");
                // 重新登录的逻辑，先暂时这么写，测试能不能登录
                MyApplication.wantCount.set(0);
                MyApplication.helloCount.set(0);

//                // 释放Tcp资源
//                TcpSocket.closeTcpSocket();
//                // 释放Udp的资源
//                UdpSocket.closeUdpSocket();
//
//                // 清空任务列表中的任务
//                Comment.list.clear();
//                Comment.Uploadlist.clear();
//                Comment.uploadlist.clear();
//                Comment.downlist.clear();
//                Comment.tcpIsTaskStartFlag.set(false);
                Comment.releaseSystemResource();

                // 弹出重新连接的对话框
                try {
                    ReconnectDialog.CreateYesNoDialog(context, context
                                    .getResources().getString(R.string.askbreak), context
                                    .getResources().getString(R.string.askreconnect),
                            context.getResources().getString(R.string.login),
                            context.getResources().getString(R.string.reconnect),
                            this);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

                break;
            case Protocol.YES:
                Log.e(TAG, "handleMessage: Protocol.YES");
                // 重新跳到登录界面
                NetWorkStateReceiver.isFirstTimer = true;
                context.startActivity(new Intent(context, Login.class));
                MyApplication.getInstance().exit();
                break;
            case Protocol.NO:
                if (!Tools.isOnline(context)) {
                    Toast.makeText(context, R.string.attention_net_error, Toast.LENGTH_SHORT).show();
                    // 重新跳到登录界面
                    NetWorkStateReceiver.isFirstTimer = true;
                    context.startActivity(new Intent(context, Login.class));
                    MyApplication.getInstance().exit();
                    return;
                }

                MyPersonalProgressDIalog.getInstance(context).setContent("正重新连接").showProgressDialog();
                NetCardController.WANT(MyApplication.userName, MyApplication.userPassword, handler);
                break;
            case Protocol.RECONNECT_LOSS:
                break;
            default:
                break;
        }
    }
}
