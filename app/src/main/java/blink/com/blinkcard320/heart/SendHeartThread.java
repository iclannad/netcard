package blink.com.blinkcard320.heart;

import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 发送心跳包维持与服务器的连接
 */
public class SendHeartThread extends Thread {

    private static final String TAG = SendHeartThread.class.getSimpleName();
    public static AtomicInteger timeCount = new AtomicInteger(0);
    public static Object HeartLock = new Object();
    public static int HeartLossMax = 6;
    public HeartHandler handler;

    public static final int HEART_LOSS = 3;
    public static final int HEART_CONTINUE = 4;

    private static SendHeartThread instance = null;
    public static boolean isClose = false;

    /**
     * 构造方法
     *
     * @param heartHandler
     */
    public SendHeartThread(HeartHandler heartHandler) {
        this.handler = heartHandler;
    }


    @Override
    public void run() {
        Log.e(TAG, "run: " + "开始发送心跳的线程");
        timeCount.set(0);
        while (timeCount.get() < HeartLossMax && !isClose) {
            try {
                /**
                 * 由于他封装的发送心跳的方法不能在子线程调用
                 */
                Message msg = Message.obtain();
                msg.what = HEART_CONTINUE;
                handler.sendMessage(msg);
                synchronized (HeartLock) {
                    HeartLock.wait(5000);
                }
                timeCount.getAndIncrement();
            } catch (InterruptedException e) {
                System.err.println("sleep interrupted");

                return;
            }

        }

        /**
         * 退出当前的程序
         */
        if (isClose) {
            Log.e(TAG, "run: " + "线程已经退出");
            return;
        }

        // 发送的心跳包服务器没有反应
        Message msg = Message.obtain();
        msg.obj = SendHeartThread.this;
        msg.what = HEART_LOSS;
        handler.sendMessage(msg);
    }
}
