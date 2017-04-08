package blink.com.blinkcard320.Controller.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import blink.com.blinkcard320.Controller.Activity.Login;
import blink.com.blinkcard320.R;
import blink.com.blinkcard320.Tool.System.Tools;
import blink.com.blinkcard320.application.MyApplication;
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

