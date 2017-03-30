package blink.com.blinkcard320.View;

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

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.widgets.Dialog;
import com.gc.materialdesign.widgets.ProgressDialog;

import java.net.DatagramSocket;
import java.util.Calendar;
import java.util.Date;

import blink.com.blinkcard320.Controller.NetCardController;
import blink.com.blinkcard320.R;
import blink.com.blinkcard320.Tool.System.MyToast;
import blink.com.blinkcard320.Tool.Thread.HandlerImpl;
import blink.com.blinkcard320.Tool.Utils.UIHelper;
import blink.com.blinkcard320.Tool.Utils.Utils;
import cn.pedant.SweetAlert.SweetAlertDialog;


//import com.blink.blinkp2p.getdata.sender.SendOpeartionMsg;
//import com.blink.blinkp2p.getdata.sender.SendOpeartionThread;
//import com.blink.blinkp2p.getdata.sender.tcp.LookAndOthers;
//import com.blink.blinkp2p.model.LG.LG;
//import com.blink.blinkp2p.model.socket.UdpSocket;
//import com.blink.blinkp2p.model.util.UIHelper;
//import com.blink.blinkp2p.model.util.Util;
//import com.blink.blinkp2p.model.values.Protocol;
//import com.blink.blinkp2p.ui.activity.InitActivity;
//import com.blink.blinkp2p.ui.activity.MainActivity;
//import com.blink.blinkp2p.ui.activity.SettingsActivity;
//import com.blink.blinkp2p.ui.view.MyTimerDialogTime;
//import com.gc.materialdesign.views.ButtonFlat;

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

//    public static void CreateDialog(final Context context, int ResourceId,
//                                    final int Operation, final int wait, final Handler handler) {
//        String sure = context.getResources().getString(R.string.confirm);
//        String name = context.getResources().getString(ResourceId);
//        new AlertDialog.Builder(context)
//                .setTitle(name)
//                .setMessage(sure + name + "?")
//                .setPositiveButton(sure, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // TODO Auto-generated method stub
//                        if (MainActivity.connectionType == Protocol.UDP) {
//                            MyProgressDIalog.getInstance(context).init();
//                            MyProgressDIalog.getInstance(context)
//                                    .showProgressDialog();
//                            SendOpeartionThread tr = new SendOpeartionThread(
//                                    InitActivity.mPc_ip, InitActivity.mPc_port,
//                                    UdpSocket.getState(), Operation);
//                            tr.start();
//                        } else {
//                            MyProgressDIalog.getInstance(context).init();
//                            MyProgressDIalog.getInstance(context)
//                                    .showProgressDialog();
//                            new CountTime(Operation, handler).start();
//                            new LookAndOthers(Operation, "" + wait).start();
//                        }
//                    }
//                })
//                .setNegativeButton(
//                        context.getResources().getString(R.string.no), null)
//                .show();
//    }

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
//								if (MainActivity.connectionType == Protocol.UDP) {
//									SendOpeartionThread tr = new SendOpeartionThread(
//											InitActivity.mPc_ip,
//											InitActivity.mPc_port, UdpSocket
//													.getState(), Operation);
//									tr.start();
//								} else {
//									new CountTime(Operation,
//											MainActivity.MainHandler).start();
//									new LookAndOthers(Operation, "" + wait)
//											.start();
//								}
                            }
                        }).show();
    }

//    public static void CreateDialog(final Context context, String name,
//                                    final int Operation, final int wait, final Handler handler) {
//        String sure = context.getResources().getString(R.string.confirm);
//        new AlertDialog.Builder(context)
//                .setTitle(name)
//                .setMessage(sure + name + "?")
//                .setPositiveButton(sure, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // TODO Auto-generated method stub
//                        if (MainActivity.connectionType == Protocol.UDP) {
//                            MyProgressDIalog.getInstance(context).init();
//                            MyProgressDIalog.showProgressDialog();
//                            SendOpeartionThread tr = new SendOpeartionThread(
//                                    InitActivity.mPc_ip, InitActivity.mPc_port,
//                                    UdpSocket.getState(), Operation);
//                            tr.start();
//                        } else {
//                            MyProgressDIalog.getInstance(context).init();
//                            MyProgressDIalog.getInstance(context)
//                                    .showProgressDialog();
//                            new CountTime(Operation, handler).start();
//                            new LookAndOthers(Operation, "" + wait).start();
//                        }
//                    }
//                })
//                .setNegativeButton(
//                        context.getResources().getString(R.string.no), null)
//                .show();
//    }

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

//    public static void CreateEditTextDialog(final Context context,
//                                            String title, String msg, final int Operation,
//                                            final Handler mHandler) {
//        final EditText e = new EditText(context);
//        new AlertDialog.Builder(context)
//                .setTitle(title)
//                .setIcon(android.R.drawable.ic_dialog_info)
//                .setView(e)
//                .setNegativeButton("ȡ��", null)
//                .setPositiveButton(
//                        context.getResources().getString(R.string.confirm),
//                        new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                // TODO Auto-generated method stub
//                                String msg = e.getText().toString();
//                                if (msg.length() == 0) {
//                                    mHandler.sendEmptyMessage(Protocol.EMPTY);
//                                    return;
//                                }
//                                if (MainActivity.connectionType == Protocol.UDP) {
//                                    if (Operation == Protocol.SETFILEUP) {
//                                        SettingsActivity.mTextView_ups = e
//                                                .getText().toString();
//                                    }
//                                    SendOpeartionMsg tr = new SendOpeartionMsg(
//                                            InitActivity.mPc_ip,
//                                            InitActivity.mPc_port, UdpSocket
//                                            .getState(), Operation,
//                                            msg, mHandler);
//                                    tr.start();
//                                    MyProgressDIalog.getInstance(context)
//                                            .init();
//                                    MyProgressDIalog.getInstance(context)
//                                            .showProgressDialog();
//                                } else {
//                                    MyProgressDIalog.getInstance(context)
//                                            .init();
//                                    MyProgressDIalog.getInstance(context)
//                                            .showProgressDialog();
//                                    new CountTime(Operation, mHandler).start();
//                                    new LookAndOthers(Operation, msg).start();
//                                }
//                            }
//                        }).show();
//
//    }

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
//			dissmissProgress();
//			e.printStackTrace();
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

//    public static void seetDialogTimeOver(int text, Context context) {
//        if (sProgressdialog != null) {
//            sProgressdialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
//            sProgressdialog.setTitleText("");
//            sProgressdialog.setContentText(context.getResources().getString(text));
//        } else {
//            dissmissProgress();
//            UIHelper.ToastMessageNetError(context, text);
//        }
//    }

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
                        // TODO Auto-generated method stub
//                        MyProgressDIalog.getInstance(context).init();
//                        MyProgressDIalog.getInstance(context)
//                                .showProgressDialog();
                        Date d = new Date();
                        int hours = d.getHours();
                        int min = d.getMinutes();
                        int msgtime = (hours * 60 + min)
                                - (hourOfDay * 60 + minute);
//                        if (MainActivity.connectionType == Protocol.UDP) {
//                            LG.i(getClass(), "min===" + msgtime);
//                            SendOpeartionMsg tr = new SendOpeartionMsg(
//                                    InitActivity.mPc_ip, InitActivity.mPc_port,
//                                    ds, Protocol.SHUTDOWN, -msgtime + "",
//                                    MainActivity.MainHandler);
//                            tr.start();
//                        } else {
//                            new LookAndOthers(Protocol.SHUTDOWN, -msgtime + "")
//                                    .start();
//                            new CountTime(Protocol.SHUTDOWN,
//                                    MainActivity.MainHandler).start();
//                        }

                        Log.e(TAG, "onTimeSet: " + msgtime);
                        NetCardController.Shutdown(-msgtime, mHandler);

                    }
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
        timePickerDialog.show();

    }

//    public static void CreateYesNoDialog(final Context context, String title,
//                                         String msg, String buttontext, String cancelText,
//                                         final Handler handler) {
//
//        final Dialog dialog = new Dialog(context, title, msg, buttontext,
//                cancelText);
//        dialog.setOnAcceptButtonClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                handler.sendEmptyMessage(Protocol.YES);
//                dialog.dismiss();
//            }
//        });
//        dialog.setOnCancelButtonClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                handler.sendEmptyMessage(Protocol.NO);
//                dialog.dismiss();
//            }
//        });
//        try {
//            dialog.show();
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//            dialog.dismiss();
//        }
//
//    }

//    public static void CreateYesNoDialog(final Context context, int titleid,
//                                         String msg, int buttontextId, int cancelTextId,
//                                         final Handler handler) {
//        String title = context.getResources().getString(titleid);
//        String buttontext = context.getResources().getString(buttontextId);
//        String cancelText = context.getResources().getString(cancelTextId);
//        final Dialog dialog = new Dialog(context, title, msg, buttontext,
//                cancelText);
//        dialog.setOnAcceptButtonClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                handler.sendEmptyMessage(Protocol.YES);
//                dialog.dismiss();
//            }
//        });
//        dialog.setOnCancelButtonClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                handler.sendEmptyMessage(Protocol.NO);
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//    }

    //    public static void OpenCountTimeThread(Handler handler, int OpeartionLoss) {
//
//        CountTime ct = new CountTime(OpeartionLoss, handler);
//        ct.start();
//
//    }
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
