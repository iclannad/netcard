package blink.com.blinkcard320.application;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2017/3/20.
 */
public class MyApplication extends Application {

    public static MyApplication instance = null;
    private LinkedList<Activity> activityList = new LinkedList<Activity>();
    private LinkedList<Thread> threadLinkedList = new LinkedList<>();

    public static AtomicInteger wantCount = new AtomicInteger();
    public static AtomicInteger helloCount = new AtomicInteger();


    public static String userName = "";
    public static String userPassword = "";


    public void addActivity(Activity a) {
        activityList.add(a);
    }

    public void addThread(Thread t) {
        threadLinkedList.add(t);
    }

    public static MyApplication getInstance() {
        if (instance == null) {
            return instance = new MyApplication();
        }
        return instance;
    }

    private void exitapp() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        for (Thread thread : threadLinkedList) {
            if (thread.isAlive()) {
                thread.interrupt();
            }
        }
    }

    public void exit() {
        exitapp();
    }


}
