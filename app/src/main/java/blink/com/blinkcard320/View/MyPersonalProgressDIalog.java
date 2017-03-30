package blink.com.blinkcard320.View;

import android.content.Context;

/**
 * Created by Administrator on 2017/3/30.
 */
public class MyPersonalProgressDIalog {
    private Context context;
    private static com.gc.materialdesign.widgets.ProgressDialog mProgressDialog;
    private static MyPersonalProgressDIalog Instance = null;

    public MyPersonalProgressDIalog(Context context) {
        this.context = context;
    }

    public static synchronized MyPersonalProgressDIalog getInstance(Context context) {
        if (Instance == null) {
            Instance = new MyPersonalProgressDIalog(context);
        }
        if (context != null) {
            Instance = new MyPersonalProgressDIalog(context);
        }
        return Instance;
    }

    public MyPersonalProgressDIalog setContent(String content) {
//        if (mProgressDialog != null)
//            return mProgressDialog;
        mProgressDialog = new com.gc.materialdesign.widgets.ProgressDialog(context, content);
        return this;
    }

    /**
     * 显示对话框
     */
    public void showProgressDialog() {
        mProgressDialog.setCancelable(false);
        try {
            mProgressDialog.show();
        } catch (Exception e) {

        }
    }

    /**
     * 关闭对话框
     */
    public void dissmissProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

}
