package com.blink.blinkp2p.View;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.blink.blinkp2p.Tool.Protocol;


/**
 * Created by Administrator on 2017/3/22.
 */
public class ReconnectDialog {

    private static MyPersonalDialog dialog = null;

    public static void setDialogNull() {
        dialog = null;
    }


    public static void CreateYesNoDialog(final Context context, String title,
                                         String msg, String buttontext, String cancelText, final Handler handler) {

        if (dialog != null) {
            return;
        }
        dialog = new MyPersonalDialog(context, title, msg, buttontext,
                cancelText);


        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(Protocol.YES);
                dialog.dismiss();
                dialog = null;
            }
        });
        dialog.setOnCancelButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(Protocol.NO);
                dialog.dismiss();
                dialog = null;
            }
        });


        try {
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
        } catch (Exception e) {
            dialog.dismiss();
            dialog = null;
        }




    }
}
