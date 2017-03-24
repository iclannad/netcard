package blink.com.blinkcard320.Tool.System;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Ruanjiahui on 2016/9/3.
 */
public class MyToast {


    public static void Toast(Context context, int resid) {
        if (context != null)
            Toast.makeText(context, context.getResources().getString(resid), Toast.LENGTH_SHORT).show();
    }

}
