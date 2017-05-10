package com.blink.blinkp2p.application;

import android.app.Application;
import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by Administrator on 2017/4/28.
 */
public class BlinkApplication extends Application {

    private static final String TAG = BlinkApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate: Tencent Bugly");
        CrashReport.initCrashReport(getApplicationContext(), "99a2987532", false);
    }
}
