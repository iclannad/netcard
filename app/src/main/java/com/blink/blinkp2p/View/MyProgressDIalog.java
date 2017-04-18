package com.blink.blinkp2p.View;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.blink.blinkp2p.Controller.NetCardController;
import com.blink.blinkp2p.R;
import com.blink.blinkp2p.Tool.System.MyToast;
import com.blink.blinkp2p.Tool.Thread.HandlerImpl;
import com.blink.blinkp2p.Tool.Utils.UIHelper;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.widgets.Dialog;
import com.gc.materialdesign.widgets.ProgressDialog;

import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MyProgressDIalog {

    private static final String TAG = MyProgressDIalog.class.getSimpleName();

    private Context context;
    private static ProgressDialog mProgressDialog;
    private static MyProgressDIalog Instance = null;

    public static synchronized MyProgressDIalog getInstance(Context context) {
        if (Instance == null) {
            Instance = new MyProgressDIalog(context);
        }
        if (context != null) {
            Instance = new MyProgressDIalog(context);
        }
        return Instance;
    }

    public static void seetDialogTimeOver(int text, Context context) {
        if (sProgressdialog != null) {
            sProgressdialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
            sProgressdialog.setTitleText("");
            sProgressdialog.setContentText(context.getResources().getString(text));
        } else {
            dissmissProgress();
            UIHelper.ToastMessageNetError(context, text);
        }
    }

    public static void CreateNolmalDialog(final Context context, String title,
                                          String msg) {
        Dialog dialog = new Dialog(context, title, msg);
        dialog.setOnAcceptButtonClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialog.setOnCancelButtonClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        try {
            dialog.show();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void CreateNolmalDialog(final Context context, int titleid,
                                          int msgid, final Handler mHandler, final int yessend,
                                          final int nosend) {
        String title = context.getResources().getString(titleid);
        String msg = context.getResources().getString(msgid);
        Dialog dialog = new Dialog(context, title, msg);
        dialog.setOnAcceptButtonClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessage(yessend);
            }
        });
        dialog.setOnCancelButtonClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessage(nosend);
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void CreateNolmalDialog(final Context context, int titleId,
                                          int msgId) {
        String title = context.getResources().getString(titleId);
        String msg = context.getResources().getString(msgId);
        Dialog dialog = new Dialog(context, title, msg);
        dialog.setOnAcceptButtonClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialog.setOnCancelButtonClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        try {
            dialog.show();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    private static SweetAlertDialog sProgressdialog = null;

    public static void createSweetDialog(final Context context, String name, final DialogClick dialogClick, final int position) {
        String sure = context.getResources().getString(R.string.confirm);
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(name)
                .setContentText(sure + name + "?")
                .setCancelText(context.getResources().getString(R.string.cancel))
                .setConfirmText(context.getResources().getString(R.string.confirm))
                .showCancelButton(true)
                .setCancelClickListener(
                        new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                                dialogClick.Canel(position);
                            }
                        })
                .setConfirmClickListener(
                        new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                                dialogClick.Enter(position);
                                sDialog = null;
                                sProgressdialog = new SweetAlertDialog(context,
                                        SweetAlertDialog.PROGRESS_TYPE);
                                sProgressdialog.setTitleText("Loading");
                                sProgressdialog.setCancelable(false);
                                sProgressdialog.show();
                            }
                        }).show();
    }


    /**
     * 修改电脑锁屏密码
     *
     * @param context
     * @param Operation
     * @param mHandler
     */
    public static void CreateChangePCPWDialog(final Context context,
                                              final int Operation, final HandlerImpl mHandler) {
        LayoutInflater factory = LayoutInflater.from(context);
        View DialogView = factory.inflate(R.layout.activity_dialog_pchangepw,
                null);
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setView(DialogView);
        final AlertDialog adi = ad.create();
        adi.show();
        ButtonFlat cancelbutton = (ButtonFlat) DialogView
                .findViewById(R.id.button_cancel);
        cancelbutton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                adi.dismiss();
            }
        });
        final EditText oldpwedittext = (EditText) adi
                .findViewById(R.id.edittext_oldpw);
        final EditText newpwedittext = (EditText) adi
                .findViewById(R.id.edittext_newpw);
        ButtonFlat surebutton = (ButtonFlat) DialogView
                .findViewById(R.id.button_accept);
        surebutton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String newpw_s = newpwedittext.getText().toString();
                String oldpw_s = oldpwedittext.getText().toString();
                if (newpw_s.length() == 0 || oldpw_s.length() == 0) {
                    UIHelper.ToastMessageNetError(context,
                            R.string.error_input_null_userpw);
                    return;
                }
                if (newpw_s.length() >= 50) {
                    UIHelper.ToastMessageNetError(context,
                            R.string.error_pw_long);
                    return;
                }
                // 登录提示
                MyPersonalProgressDIalog.getInstance(context).setContent("修改电脑锁屏密码中...").showProgressDialog();
                // 向服务器请求修改锁屏密码
                NetCardController.ChangePcPwd(oldpw_s, newpw_s, mHandler);

                Log.e(TAG, "onClick: " + "开始修改密码，新的密码是：" + newpw_s);
                adi.dismiss();
            }
        });
        TextView tv = (TextView) adi.findViewById(R.id.txt_setpw);
        tv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                adi.dismiss();

            }
        });

    }

    public MyProgressDIalog(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    public void init() {
        if (mProgressDialog != null)
            return;
        mProgressDialog = new ProgressDialog(context, context.getResources()
                .getString(R.string.activity_init_connect_pc));
    }


    public void setTile(String name) {
        mProgressDialog.setTitle(context.getResources().getString(
                R.string.activity_init_cannot_connect_server));
    }

    public void setmsg(String text) {
        // mProgressDialog.dismiss();
        mProgressDialog.setTitle(text);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    public void setmsg(int id) {
        // mProgressDialog.dismiss();
        mProgressDialog.setTitle(context.getResources().getString(id));
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    public static void showProgressDialog() {
        mProgressDialog.setCancelable(false);
        try {
            mProgressDialog.show();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void setDialogSuccess(Context cnt, int text) {
        if (sProgressdialog != null) {
            sProgressdialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
            sProgressdialog.setTitleText("");
            sProgressdialog.setContentText(cnt.getResources().getString(text));
        } else {
            dissmissProgress();
            MyToast.Toast(cnt, text);
        }
    }

    public static void dissmissProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        if (sProgressdialog != null) {
            sProgressdialog.dismiss();
            sProgressdialog = null;
        }
    }

    public static void CreateDialogTime(String name, final Context context,
                                        final HandlerImpl mHandler) {
        Calendar c;
        c = Calendar.getInstance();
        MyTimerDialogTime timePickerDialog = new MyTimerDialogTime(context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        Date d = new Date();
                        int hours = d.getHours();
                        int min = d.getMinutes();
                        int msgtime = (hours * 60 + min)
                                - (hourOfDay * 60 + minute);
                        // 登录提示
                        MyPersonalProgressDIalog.getInstance(context).setContent("向电脑发送关闭信息...").showProgressDialog();
                        Log.e(TAG, "onTimeSet: " + msgtime);
                        NetCardController.setTimeShutdown(-msgtime, mHandler);

                    }
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
        timePickerDialog.show();

    }



    // 对话框的内容设置
    public void setContent(String content) {
//        if (mProgressDialog != null) {
//            mProgressDialog.setTitle(content);
//        } else {
//            mProgressDialog = new ProgressDialog(context, content);
//        }
        mProgressDialog = new ProgressDialog(context, content);
    }


}
