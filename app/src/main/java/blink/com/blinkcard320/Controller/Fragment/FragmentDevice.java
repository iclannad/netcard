package blink.com.blinkcard320.Controller.Fragment;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.data_sdk.CommonIntent;
import com.example.administrator.ui_sdk.DensityUtil;
import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;

import java.io.File;
import java.net.DatagramSocket;
import java.util.ArrayList;

import blink.com.blinkcard320.Controller.Activity.MainActivity;
import blink.com.blinkcard320.Controller.ActivityCode;
import blink.com.blinkcard320.Controller.NetCardController;
import blink.com.blinkcard320.Moudle.Item;
import blink.com.blinkcard320.Moudle.skin.SkinConfig;
import blink.com.blinkcard320.R;
import blink.com.blinkcard320.Tool.Adapter.LGAdapter;
import blink.com.blinkcard320.Tool.Protocol;
import blink.com.blinkcard320.Tool.System.Tools;
import blink.com.blinkcard320.Tool.Thread.HandlerImpl;
import blink.com.blinkcard320.Tool.Utils.SharedPrefsUtils;
import blink.com.blinkcard320.Tool.Utils.UIHelper;
import blink.com.blinkcard320.View.DialogClick;
import blink.com.blinkcard320.View.MyProgressDIalog;
import blink.com.blinkcard320.camera.CameraActivity;
import blink.com.blinkcard320.heart.SendHeartThread;
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

//    public FragmentDevice(OnFragmentToActivity onfragmenttoactivity) {
//        // TODO Auto-generated constructor stub
//
//    }

    private LGAdapter adapter = null;
    private GridView fragmentdevice_gridview = null;

//    @Override
//    public void onAttach(Activity activity) {
//        // TODO Auto-generated method stub
//
//        super.onAttach(activity);
//    }

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
//        mDatagramSocket = ((UdpSocket) getActivity().getApplication())
//                .getState();
//        mgridview = (MGridView) mView.findViewById(R.id.gridview);
//        fragmentDeviceBack = (LinearLayout) mView.findViewById(R.id.fragmentDeviceBack);
        list = new ArrayList<>();
        for (int i = 0; i < str.length; i++) {
            list.add(getItem(image[i], str[i]));
        }


        fragmentdevice_gridview = (GridView) view.findViewById(R.id.fragmentdevice_gridview);
        fragmentDeviceBack = (LinearLayout) view.findViewById(R.id.fragmentDeviceBack);


        adapter = new LGAdapter(context, list, "GridView");
        fragmentdevice_gridview.setAdapter(adapter);
        fragmentdevice_gridview.setOnItemClickListener(this);
//        mdevicegridadapter = new DeviceGridViewAdapter(context, list);
//        mgridview.setAdapter(mdevicegridadapter);
//        mgridview.setOnItemClickListener(this);
//        mgridview.setOnItemLongClickListener(this);
//        fragmentDeviceBack.setBackgroundResource(SkinConfig.getInstance().getColor());


//        tv_os = (TextView) mView.findViewById(R.id.fragment_os);
//        tv_os.setText(PCINOF.GetInstance().getOS());
//        tv_cpu = (TextView) mView.findViewById(R.id.fragment_cpu);
//        tv_cpu.setText(PCINOF.GetInstance().getCPU());
//        tv_memory = (TextView) mView.findViewById(R.id.fragment_memory);
//        tv_memory.setText(PCINOF.GetInstance().getMemory());
//        tv_time = (TextView) mView.findViewById(R.id.fragment_time);
//        tv_time.setText(PCINOF.GetInstance().getTime());
//        tv_user = (TextView) mView.findViewById(R.id.fragment_pcusername);
//        tv_user.setText(PCINOF.GetInstance().getUsername());
//        fragment_deviceTop = (LinearLayout) mView.findViewById(R.id.fragment_deviceTop);


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

//    private void getpcinof() {
//        MyProgressDIalog.getInstance(getActivity()).init();
//        MyProgressDIalog.getInstance(getActivity())
//                .showProgressDialog();
//        SendOpeartionThread tr = new SendOpeartionThread(
//                InitActivity.mPc_ip, InitActivity.mPc_port,
//                UdpSocket.getState(), Protocol.GETPCINFO);
//        tr.start();
//    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        switch (arg2) {
            case 4:
                // 访部电脑文件的时候，先暂时关闭发送心跳的线程
                // 释放心跳线程的资源
                SendHeartThread.isClose = true;
                synchronized (SendHeartThread.HeartLock) {
                    SendHeartThread.HeartLock.notify();
                }

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
                Log.e(TAG, "onItemClick: " + "定时关机");
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
                        File f = new File(Tools.getPic(context));
                        Uri u = Uri.fromFile(f);
                        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                        startActivityForResult(intent, 1);
                    } catch (Exception e) {
                        CommonIntent.IntentResActivity(getActivity(), CameraActivity.class, 1);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

//        if (mCameraManager.getInstance().getpath() == null) {
//            return;
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle(R.string.phone_update);
//        View view = LayoutInflater.from(getActivity()).inflate(
//                R.layout.dialog_camera, null);
//        ImageView iv = (ImageView) view
//                .findViewById(R.id.imageview_dialog_camera);
//        TextView tv = (TextView) view
//                .findViewById(R.id.textview_dialog_camerapath);
//        Button b = (Button) view.findViewById(R.id.button_dialog_cameradismiss);
//        Button upload = (Button) view.findViewById(R.id.button_dialog_cameraupload);
//        tv.setText(mCameraManager.getInstance().getpath());
//        final File f = new File(mCameraManager.getInstance().getpath());
//        bmp = mCameraManager.getInstance().getbitmap(f.getPath());
//        if (bmp == null) {
//            return;
//        }
//        iv.setImageBitmap(mCameraManager.getInstance().ResizeBitmap(bmp, 480));
//        builder.setView(view);
//        final AlertDialog dialog = builder.create();
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
////                bmp.recycle();
//                dialog.dismiss();
//            }
//        });
//        upload.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                TransportManagement.getInstance().initUpload(new MainHandler(getActivity()), getActivity());
//                TransportManagement.getInstance().getUpload().addFile(f);
////                bmp.recycle();
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void Enter(int position) {
        //锁屏的操作
        if (position == ActivityCode.LOOKPC) {
            // 访部电脑文件的时候，先暂时关闭发送心跳的线程
            // 释放心跳线程的资源
            SendHeartThread.isClose = true;
            synchronized (SendHeartThread.HeartLock) {
                SendHeartThread.HeartLock.notify();
            }
            //锁屏
            NetCardController.LOOKPC(this);
        }
        //重启
        if (position == ActivityCode.Restart) {
            // 访部电脑文件的时候，先暂时关闭发送心跳的线程
            // 释放心跳线程的资源
            SendHeartThread.isClose = true;
            synchronized (SendHeartThread.HeartLock) {
                SendHeartThread.HeartLock.notify();
            }
            //重启
            NetCardController.Restart(0, this);
        }

        //立即关机
        if (position == ActivityCode.Shutdown) {
            // 访部电脑文件的时候，先暂时关闭发送心跳的线程
            // 释放心跳线程的资源
            SendHeartThread.isClose = true;
            synchronized (SendHeartThread.HeartLock) {
                SendHeartThread.HeartLock.notify();
            }
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
            // 重新开启一个心跳线程
            SendHeartThread sendHeartThread = new SendHeartThread(MainActivity.heartHandler);
            SendHeartThread.isClose = false;
            sendHeartThread.start();

            LookPCRsp lookPCRsp = (LookPCRsp) object;
            if (lookPCRsp.getSuccess() == 0) {
                if (BlinkWeb.STATE == BlinkWeb.TCP) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyProgressDIalog.setDialogSuccess(context, R.string.main_handler_lock_recved);
                        }
                    });
                } else {
                    MyProgressDIalog.setDialogSuccess(context, R.string.main_handler_lock_recved);
                }

            }
        }

        // 电脑重启返回的结果
        if (position == ActivityCode.Restart) {
            // 重新开启一个心跳线程
            SendHeartThread sendHeartThread = new SendHeartThread(MainActivity.heartHandler);
            SendHeartThread.isClose = false;
            sendHeartThread.start();

            RestartRsp restartRsp = (RestartRsp) object;
            if (restartRsp.getSuccess() == 0) {
                if (BlinkWeb.STATE == BlinkWeb.TCP) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyProgressDIalog.setDialogSuccess(context, R.string.main_handler_restart_recved);
                        }
                    });
                } else {
                    MyProgressDIalog.setDialogSuccess(context, R.string.main_handler_restart_recved);
                }

            }
        }

        // 电脑关机返回的结果
        if (position == ActivityCode.Shutdown) {
            // 重新开启一个心跳线程
            SendHeartThread sendHeartThread = new SendHeartThread(MainActivity.heartHandler);
            SendHeartThread.isClose = false;
            sendHeartThread.start();

            ShutdownRsp shutdownRsp = (ShutdownRsp) object;
            if (shutdownRsp.getSuccess() == 0) {
                //MyProgressDIalog.setDialogSuccess(context, R.string.main_handler_shutdown_recved);
                UIHelper.ToastMessageNetError(context, R.string.main_handler_shutdown_recved);
            }
        }

        // 修改pc密码返回的结果
        if (position == ActivityCode.ChangePcPwd) {
            // 重新开启一个心跳线程
            SendHeartThread sendHeartThread = new SendHeartThread(MainActivity.heartHandler);
            SendHeartThread.isClose = false;
            sendHeartThread.start();

            ChangePcPwdRsp changePcPwdRsp = (ChangePcPwdRsp) object;
            int value = changePcPwdRsp.getSuccess();
            if (value == 0) {
                //MyProgressDIalog.setDialogSuccess(context, R.string.main_handler_change_sucess);
                UIHelper.ToastMessageNetError(context, R.string.main_handler_change_sucess);
            } else {
                //MyProgressDIalog.setDialogSuccess(context, R.string.main_handler_original_error);
                UIHelper.ToastMessageNetError(context, R.string.main_handler_original_error);
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
        Log.e(TAG, "myError: 错误的操作");
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
