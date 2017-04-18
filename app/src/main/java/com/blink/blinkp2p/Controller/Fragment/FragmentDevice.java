package com.blink.blinkp2p.Controller.Fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blink.blinkp2p.Controller.Activity.MainActivity;
import com.blink.blinkp2p.Controller.ActivityCode;
import com.blink.blinkp2p.R;
import com.blink.blinkp2p.Tool.Thread.HandlerImpl;
import com.blink.blinkp2p.Tool.Utils.download.tcp.MyTcpUploadUtils;
import com.blink.blinkp2p.Tool.Utils.upload.MyUploadUtils;
import com.blink.blinkp2p.Tool.Utils.upload.UploadTask;
import com.blink.blinkp2p.View.MyPersonalProgressDIalog;
import com.blink.blinkp2p.heart.HeartController;
import com.example.administrator.ui_sdk.DensityUtil;
import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;

import java.io.DataOutput;
import java.io.File;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.blink.blinkp2p.Controller.NetCardController;
import com.blink.blinkp2p.Moudle.Comment;
import com.blink.blinkp2p.Moudle.DownorUpload;
import com.blink.blinkp2p.Moudle.Item;
import com.blink.blinkp2p.Moudle.skin.SkinConfig;


import com.blink.blinkp2p.Tool.Adapter.LGAdapter;
import com.blink.blinkp2p.Tool.System.Tools;
import com.blink.blinkp2p.Tool.UploadUtils;
import com.blink.blinkp2p.Tool.Utils.SharedPrefsUtils;
import com.blink.blinkp2p.View.DialogClick;
import com.blink.blinkp2p.View.MyProgressDIalog;
import com.blink.blinkp2p.heart.SendHeartThread;

import smart.blink.com.card.API.BlinkWeb;
import smart.blink.com.card.bean.ChangePcPwdRsp;
import smart.blink.com.card.bean.LookPCRsp;
import smart.blink.com.card.bean.RestartRsp;
import smart.blink.com.card.bean.ShutdownRsp;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FragmentDevice extends Fragment implements OnItemClickListener, OnItemLongClickListener, DialogClick, HandlerImpl {

    private static final String TAG = FragmentDevice.class.getSimpleName();
    private View view;
    private DatagramSocket mDatagramSocket;
    public static ProgressDialog progress;
    //    private MGridView mgridview;
//    private static DeviceGridViewAdapter mdevicegridadapter;
    private ArrayList<Object> list;
    //    private DevicesHandle mhanDevicesHandle;
    private Context context = null;
//    public static TextView tv_cpu, tv_memory, tv_time, tv_os, tv_user;

//    private LinearLayout fragment_deviceTop = null;

    private int[] image = {R.mipmap.icon_restart, R.mipmap.icon_timeshowdown,
            R.mipmap.icon_shutdown, R.mipmap.icon_lockdevices,
            R.mipmap.icon_changepw, R.mipmap.icon_update,
            R.mipmap.icon_videofile, R.mipmap.icon_other1, R.mipmap.icon_other2};


    private LinearLayout fragmentDeviceBack = null;


    private int[] str = new int[]{
            R.string.instruction_reboot,
            R.string.instruction_wshutdown,
            R.string.instruction_shutdown,
            R.string.instruction_lockingdevice,
            R.string.instruction_AlterPassword,
            R.string.instruction_getupdate,
            R.string.instruction_Camera,
            R.string.fragmentdeviceItem,
            R.string.fragmentdeviceItem};


    private LGAdapter adapter = null;
    private GridView fragmentdevice_gridview = null;
    private static String name;
    private String path;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_device, null);

        context = getActivity();
//        if (getActivity() != null)
//            context = getActivity();
        initview();
//        mhanDevicesHandle = new DevicesHandle(getActivity());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void initview() {
        list = new ArrayList<>();
        for (int i = 0; i < str.length; i++) {
            list.add(getItem(image[i], str[i]));
        }

        fragmentdevice_gridview = (GridView) view.findViewById(R.id.fragmentdevice_gridview);
        fragmentDeviceBack = (LinearLayout) view.findViewById(R.id.fragmentDeviceBack);


        adapter = new LGAdapter(context, list, "GridView");
        fragmentdevice_gridview.setAdapter(adapter);
        fragmentdevice_gridview.setOnItemClickListener(this);

        DensityUtil.setRelHeight(fragmentDeviceBack, BaseActivity.height / 4);
//		DensityUtil.setHeight(mgridview , DensityUtil.getHeight(getActivity()) - DensityUtil.getHeight(getActivity()) / 4 - DensityUtil.getHeight(getActivity()) / 12 );
    }


    private Object getItem(int Drawable, int title) {
        Item item = new Item();

        item.setGridImage(getResources().getDrawable(Drawable));
        item.setGridText(getResources().getString(title));
        item.setHeight(BaseActivity.width / 3);
        item.setGridImageSize((int) getResources().getDimension(R.dimen.searchLogo));

        return item;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        switch (arg2) {
            case 4:
                // 访部电脑文件的时候，先暂时关闭发送心跳的线程
                // 释放心跳线程的资源
                HeartController.stopHeart();

                // 和旧版对比，已作过修改
                MyProgressDIalog.CreateChangePCPWDialog(getActivity(), ActivityCode.ChangePcPwd, this);
                break;
            case 0:
                MyProgressDIalog.createSweetDialog(getActivity(), getResources().getString(R.string.instruction_reboot), this, ActivityCode.Restart);
                break;
            case 1:
                MyProgressDIalog.CreateDialogTime(
                        getActivity().getResources().getString(R.string.instruction_wshutdown),
                        getActivity(),
                        this);
                break;
            case 2:
                MyProgressDIalog.createSweetDialog(getActivity(), getResources().getString(R.string.instruction_shutdown), this, ActivityCode.Shutdown);
                break;
            case 3:
                MyProgressDIalog.createSweetDialog(context, getResources().getString(R.string.instruction_lockingdevice), this, ActivityCode.LOOKPC);
                break;
            case 6:
                if (Tools.isCamera(getActivity())) {
                    try {
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // 设定拍照以后文件的保存路径
                        name = new DateFormat().format("yyyyMMdd_hhmmss",
                                Calendar.getInstance(Locale.CHINA))
                                + ".jpg";

                        path = SharedPrefsUtils.getStringPreference(getActivity(), Comment.PICTUREFILE);
                        if (path == null) {
                            path = Environment.getExternalStorageDirectory().toString();
                        }
                        Log.e(TAG, "onItemClick: path===" + path);

                        File f = new File(path, name);
                        Uri u = Uri.fromFile(f);
                        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                        startActivityForResult(intent, 1);
                    } catch (Exception e) {
                        // 如果异常的话就跳转到自己定义CameraActivity
                        //CommonIntent.IntentResActivity(getActivity(), CameraActivity.class, 1);
                        Toast.makeText(getActivity(), "开启相机时出现异常", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(getActivity(), getResources().getString(R.string.Camera_no_permission), Toast.LENGTH_SHORT).show();
                break;
            case 5:
                MyProgressDIalog.CreateNolmalDialog(getActivity(), R.string.update, R.string.IsNewVersion);
                break;
            default:
                return;
        }
    }

    // 调用拍照功能后的回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getResources().getString(R.string.photo));
                View view = LayoutInflater.from(getActivity()).inflate(
                        R.layout.dialog_camera, null);

                if (name == null || path == null) {
                    Log.e(TAG, "onActivityResult: name==null or path==null");
                    return;
                }

                ImageView iv = (ImageView) view
                        .findViewById(R.id.imageview_dialog_camera);
                TextView tv = (TextView) view
                        .findViewById(R.id.textview_dialog_camerapath);
                Button b = (Button) view.findViewById(R.id.button_dialog_cameradismiss);
                Button upload = (Button) view
                        .findViewById(R.id.button_dialog_cameraupload);

                File f = new File(path, name);
                tv.setText(f.getName());
                final Bitmap bmp = Tools.getbitmap(f.getPath());
                if (bmp == null) {
                    Log.e(TAG, "onActivityResult: bmp==null");
                    return;
                }
                iv.setImageBitmap(Tools.ResizeBitmap(bmp, 480));
                builder.setView(view);
                final AlertDialog dialog = builder.show();
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bmp.recycle();
                        dialog.dismiss();
                        path = null;
                        name = null;
                    }
                });
                upload.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // 上传图片到电脑
                        DownorUpload downorUpload = new DownorUpload();
                        downorUpload.setName(name);
                        downorUpload.setFLAG(DownorUpload.UPLOAD);
                        downorUpload.setPath(path);
                        Comment.Uploadlist.add(downorUpload);

                        UploadTask uploadTask = new UploadTask();
                        uploadTask.name = name;
                        uploadTask.path = path;
                        uploadTask.id = Comment.uploadlist.size();
                        uploadTask.progress = 0;
                        uploadTask.status = 0;
                        uploadTask.speed = "0";
                        Comment.uploadlist.add(uploadTask);

                        //new UploadUtils(getActivity());

                        if (BlinkWeb.STATE == BlinkWeb.TCP) {
                            new MyTcpUploadUtils(getActivity());
                        } else {
                            // 测试多任务同时上传
                            new MyUploadUtils(getActivity());
                        }

                        bmp.recycle();
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "开始上传图片", Toast.LENGTH_SHORT).show();
                        path = null;
                        name = null;
                    }
                });
                break;
        }
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
        return false;
    }

    @Override
    public void Enter(int position) {

        if (position == ActivityCode.LOOKPC) {
            HeartController.stopHeart();
            //锁屏
            NetCardController.LOOKPC(this);
        }

        if (position == ActivityCode.Restart) {
            HeartController.stopHeart();

            //重启
            NetCardController.Restart(0, this);
        }

        if (position == ActivityCode.Shutdown) {
            HeartController.stopHeart();
            //关机
            NetCardController.Shutdown(0, this);
        }
    }

    @Override
    public void Canel(int position) {

    }

    /**
     * 处理线程返回的更新界面
     *
     * @param position
     * @param object
     */
    @Override
    public void myHandler(int position, Object object) {

        if (position == ActivityCode.LOOKPC) {
            HeartController.startHeart();

            LookPCRsp lookPCRsp = (LookPCRsp) object;
            if (lookPCRsp.getSuccess() == 0) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyProgressDIalog.setDialogSuccess(context, R.string.main_handler_lock_recved);
                    }
                });
            }
        }

        // 电脑重启返回的结果
        if (position == ActivityCode.Restart) {
            HeartController.startHeart();

            RestartRsp restartRsp = (RestartRsp) object;
            if (restartRsp.getSuccess() == 0) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyProgressDIalog.setDialogSuccess(context, R.string.main_handler_restart_recved);
                    }
                });
            }
        }

        // 电脑关机返回的结果
        if (position == ActivityCode.Shutdown) {
            HeartController.startHeart();

            ShutdownRsp shutdownRsp = (ShutdownRsp) object;
            if (shutdownRsp.getSuccess() == 0) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyProgressDIalog.setDialogSuccess(context, R.string.main_handler_shutdown_recved);
                    }
                });
            }
        }

        // 定时关机返回的结果
        if (position == ActivityCode.setTimeShutdown) {
            HeartController.startHeart();
            ShutdownRsp shutdownRsp = (ShutdownRsp) object;
            if (shutdownRsp.getSuccess() == 0) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyPersonalProgressDIalog.getInstance(getActivity()).dissmissProgress();
                        Toast.makeText(context,R.string.main_handler_shutdown_recved,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        // 修改pc密码返回的结果
        if (position == ActivityCode.ChangePcPwd) {
            HeartController.startHeart();

            ChangePcPwdRsp changePcPwdRsp = (ChangePcPwdRsp) object;
            int value = changePcPwdRsp.getSuccess();
            if (value == 0) {
                MyPersonalProgressDIalog.getInstance(getActivity()).dissmissProgress();
                MyProgressDIalog.setDialogSuccess(context, R.string.main_handler_change_sucess);
            } else {
                MyPersonalProgressDIalog.getInstance(getActivity()).dissmissProgress();
                MyProgressDIalog.seetDialogTimeOver(R.string.main_handler_original_error, context);
            }
        }
    }

    /**
     * 错误的操作
     *
     * @param position
     * @param error
     */
    @Override
    public void myError(int position, int error) {
        if (position == ActivityCode.LOOKPC) {
            HeartController.startHeart();
            FragmentDevice.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MyProgressDIalog.seetDialogTimeOver(R.string.main_handler_lock_lost, FragmentDevice.this.getActivity());
                }
            });
        }
        if (position == ActivityCode.Restart) {
            HeartController.startHeart();

            FragmentDevice.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MyProgressDIalog.seetDialogTimeOver(R.string.main_handler_restart_lost, FragmentDevice.this.getActivity());
                }
            });
        }

        if (position == ActivityCode.Shutdown) {
            HeartController.startHeart();

            FragmentDevice.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MyProgressDIalog.seetDialogTimeOver(R.string.main_handler_shutdown_lost, FragmentDevice.this.getActivity());
                }
            });
        }

        if (position == ActivityCode.setTimeShutdown) {
            HeartController.startHeart();

            FragmentDevice.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MyPersonalProgressDIalog.getInstance(getActivity()).dissmissProgress();
                    Toast.makeText(context, R.string.main_handler_shutdown_lost, Toast.LENGTH_SHORT).show();

                }
            });
        }

        if (position == ActivityCode.ChangePcPwd) {
            HeartController.startHeart();

            FragmentDevice.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MyPersonalProgressDIalog.getInstance(getActivity()).dissmissProgress();
                    Toast.makeText(context, R.string.main_handler_change_lost, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initSkinConfig();
    }

    // 重新设皮肤
    public void initSkinConfig() {
        int skinValue = SharedPrefsUtils.getIntegerPreference(getActivity(), SkinConfig.SKIN_CONFIG, SkinConfig.SKIN_DEFAULT_VALUE);
        fragmentDeviceBack.setBackgroundResource(skinValue);
    }
}
