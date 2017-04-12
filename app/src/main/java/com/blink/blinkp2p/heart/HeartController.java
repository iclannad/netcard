package com.blink.blinkp2p.heart;

import com.blink.blinkp2p.Controller.Activity.MainActivity;

/**
 * 控制心跳线程的开关和关闭
 */
public class HeartController {

    /**
     * 开启心跳的线程
     */
    public static void startHeart() {
        SendHeartThread sendHeartThread = new SendHeartThread(MainActivity.heartHandler);
        SendHeartThread.isClose = false;
        sendHeartThread.start();
    }


    /**
     * 关闭心跳的线程
     */
    public static void stopHeart() {
        // 释放心跳线程的资源
        SendHeartThread.isClose = true;
        synchronized (SendHeartThread.HeartLock) {
            SendHeartThread.HeartLock.notify();
        }
    }
}
