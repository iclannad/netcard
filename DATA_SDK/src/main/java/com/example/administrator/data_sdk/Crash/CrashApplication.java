package com.example.administrator.data_sdk.Crash;

/**
 * Created by Administrator on 2016/1/22.
 */

import android.app.Application;

public class CrashApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }
}
