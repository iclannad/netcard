package blink.com.blinkcard320.heart;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import blink.com.blinkcard320.Controller.Activity.Login;
import blink.com.blinkcard320.Controller.Activity.MainActivity;
import blink.com.blinkcard320.Controller.NetCardController;
import blink.com.blinkcard320.R;
import blink.com.blinkcard320.Tool.Protocol;
import blink.com.blinkcard320.Tool.Thread.HandlerImpl;
import blink.com.blinkcard320.View.ReconnectDialog;
import blink.com.blinkcard320.application.MyApplication;

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
                // 与服务器失去连接的逻辑
                SendHeartThread sendHeartThread = (SendHeartThread) msg.obj;
                sendHeartThread.interrupt();    // 中断当前线程
                Log.e(TAG, "handleMessage: " + "60秒后依旧没有接收到报务器返回的心跳包，所以判定与服务器连接失败");
                // 重新登录的逻辑，先暂时这么写，测试能不能登录
                MyApplication.wantCount.set(0);
                MyApplication.helloCount.set(0);
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
                context.startActivity(new Intent(context, Login.class));
                MyApplication.getInstance().exit();
                break;
            case Protocol.NO:
                Log.e(TAG, "handleMessage: Protocol.NO");
                NetCardController.WANT(MyApplication.userName, MyApplication.userPassword, handler);
                break;
            case Protocol.RECONNECT_LOSS:
                break;
            default:
                break;
        }
    }
}
