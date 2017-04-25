package com.blink.blinkp2p.View;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;

/**
 * Created by Administrator on 2017/3/21.
 */
public class MyTimerDialogTime extends TimePickerDialog {
    public MyTimerDialogTime(Context context, int themeResId,
                             OnTimeSetListener listener, int hourOfDay, int minute,
                             boolean is24HourView) {
        super(context, themeResId, listener, hourOfDay, minute, is24HourView);
        // TODO Auto-generated constructor stub
    }

    public MyTimerDialogTime(Context context, OnTimeSetListener callBack,
                             int hourOfDay, int minute, boolean is24HourView) {
        super(context, callBack, hourOfDay, minute, is24HourView);


    }



    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        //	super.onStop();
    }
}
