package com.blink.blinkp2p.Tool.Thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by Administrator on 2016/9/3.
 */
public class MyThread {

    /**
     * 开启线程
     *
     * @param position 开启线程的标识
     * @param thread   开启线程的接口
     */
    public static void StartThread(int position, ThreadImpl thread) {
        new Thread(new MyRunnable(position, thread)).start();
    }

    /**
     * 开启Handler处理事件
     *
     * @param position
     * @param handler
     * @param object
     */
    public static void StartHandler(final int position, final HandlerImpl handler, final Object object) {
        Message message = new Message();
        message.obj = object;
        new Handler(Looper.getMainLooper()) {

            @Override
            public void dispatchMessage(Message msg) {
                handler.myHandler(position, msg.obj);
            }
        }.sendMessage(message);
    }

    /**
     * 处理错误的事件
     *
     * @param position
     * @param handler
     */
    public static void HandlerError(final int position, final int error , final HandlerImpl handler) {
        Message message = new Message();
        new Handler(Looper.getMainLooper()) {

            @Override
            public void dispatchMessage(Message msg) {
                handler.myError(position , error);
            }
        }.sendMessage(message);
    }


}
