package blink.com.blinkcard320.Tool.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import blink.com.blinkcard320.R;

/**
 * Created by Administrator on 2017/3/20.
 */
public class UIHelper {
    public static void ToastMessageNetError(Context cont, String msg) {
        View toastRoot = ((Activity) cont).getLayoutInflater().inflate(R.layout.toast, null);
        TextView message = (TextView) toastRoot.findViewById(R.id.message);
        ImageView iv=(ImageView) toastRoot.findViewById(R.id.notice_image);
        iv.setImageResource(R.mipmap.notice_failure);
        message.setText(msg);
        Toast toast=new Toast(cont);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setView(toastRoot);
        toast.show();
    }

    public static void ToastMessageNetError(Context cont, int resourceId) {
        View toastRoot = ((Activity) cont).getLayoutInflater().inflate(R.layout.toast, null);
        TextView message = (TextView) toastRoot.findViewById(R.id.message);
        ImageView iv=(ImageView) toastRoot.findViewById(R.id.notice_image);
        iv.setImageResource(R.mipmap.notice_failure);
        message.setText(cont.getResources().getString(resourceId));
        Toast toast=new Toast(cont);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setView(toastRoot);
        toast.show();
    }

    public static void ToastSetSuccess(Context cont,String msg){

        View toastRoot = ((Activity) cont).getLayoutInflater().inflate(R.layout.toast, null);
        TextView message = (TextView) toastRoot.findViewById(R.id.message);
        ImageView iv=(ImageView) toastRoot.findViewById(R.id.notice_image);
        iv.setImageResource(R.mipmap.notice_succeed);

        message.setText(msg);
        Toast toast=new Toast(cont);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setView(toastRoot);
        toast.show();

    }

    public static void ToastSetSuccess(Context cont,int resourceId){

        View toastRoot = ((Activity) cont).getLayoutInflater().inflate(R.layout.toast, null);
        TextView message = (TextView) toastRoot.findViewById(R.id.message);
        ImageView iv=(ImageView) toastRoot.findViewById(R.id.notice_image);
        iv.setImageResource(R.mipmap.notice_succeed);

        message.setText(cont.getResources().getString(resourceId));
        Toast toast=new Toast(cont);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setView(toastRoot);
        toast.show();

    }


    public static void ToastMessage(Context cont, String msg) {
        Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
    }


    public static void ToastMessage(Context cont, int msg) {
        Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
    }

    public static void ToastMessage(Context cont, String msg, int time) {
        Toast.makeText(cont, msg, time).show();
    }

    public static View.OnClickListener finish(final Activity activity){
        return new View.OnClickListener() {
            public void onClick(View v) {
                activity.finish();
            }
        };
    }
}
