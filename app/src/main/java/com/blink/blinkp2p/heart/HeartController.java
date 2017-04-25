package com.blink.blinkp2p.heart;

import com.blink.blinkp2p.Controller.Activity.MainActivity;
import com.blink.blinkp2p.Moudle.Comment;

/**
 * 控制心跳线程的开关和关闭
 */
public class HeartController {

    // 判断心跳是否开启，如果开启的话就不会再开启
    public static boolean isStart = false;

    /**
     * 开启心跳的线程
     */
    public static void startHeart() {
        // 如果Tcp模式下载有任务正在下载，不会开启心跳，避免过多的干扰
        if (Comment.tcpIsTaskStartFlag.get()) {
            return;
        }
        if (isStart) {
            return;
        }

        SendHeartThread sendHeartThread = new SendHeartThread(MainActivity.heartHandler);
        SendHeartThread.isClose = false;
        sendHeartThread.start();

        isStart = true;
    }


    /**
     * 关闭心跳的线程
     */
    public static void stopHeart() {

        if (!isStart) {
            return;
        }

        // 释放心跳线程的资源
        SendHeartThread.isClose = true;
        synchronized (SendHeartThread.HeartLock) {
            SendHeartThread.HeartLock.notify();
        }

        isStart = false;
    }
}
