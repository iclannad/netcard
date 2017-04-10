package com.blink.blinkp2p.Controller.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.blink.blinkp2p.R;
import com.blink.blinkp2p.application.MyApplication;
import com.blink.blinkp2p.Controller.Activity.Login;
import com.blink.blinkp2p.Tool.System.Tools;

import smart.blink.com.card.Tcp.TcpSocket;

/**
 * Created by Administrator on 2017/4/8.
 */
public class NetWorkStateReceiver extends BroadcastReceiver {

    private static final String TAG = NetWorkStateReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean online = Tools.isOnline(context);
        if (online) {
        } else {
            Log.e(TAG, "onReceive: 当前网络不可用");
            TcpSocket.closeTcpSocket();
            // 重新跳到登录界面
            context.startActivity(new Intent(context, Login.class));
            MyApplication.getInstance().exit();
            Toast.makeText(context, R.string.attention_net_error, Toast.LENGTH_SHORT).show();
        }
    }
}

