package blink.com.blinkcard320.Controller.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.data_sdk.CommonIntent;
import com.example.administrator.ui_sdk.DensityUtil;
import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;
import com.example.administrator.ui_sdk.MyBaseActivity.NavActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import blink.com.blinkcard320.Controller.Activity.slidingmenu.AboutActivity;
import blink.com.blinkcard320.Controller.Activity.slidingmenu.AlterUserPWDActivity;
import blink.com.blinkcard320.Controller.Activity.slidingmenu.FeedbackActivity;
import blink.com.blinkcard320.Controller.Activity.slidingmenu.SettingsActivity;
import blink.com.blinkcard320.Controller.Activity.slidingmenu.ShopActivity;
import blink.com.blinkcard320.Controller.Activity.slidingmenu.UserinfoActivity;
import blink.com.blinkcard320.Controller.Activity.slidingmenu.settings.QuickStartActivity;
import blink.com.blinkcard320.Controller.ActivityCode;
import blink.com.blinkcard320.Controller.Fragment.FragmentDevice;
import blink.com.blinkcard320.Controller.Fragment.FragmentFileManager;
import blink.com.blinkcard320.Controller.Fragment.FragmentFilePC;
import blink.com.blinkcard320.Controller.Fragment.FragmentFilePhone;
import blink.com.blinkcard320.Controller.Fragment.FragmentToActivity;
import blink.com.blinkcard320.Controller.NetCardController;
import blink.com.blinkcard320.Controller.receiver.NetWorkStateReceiver;
import blink.com.blinkcard320.Moudle.Comment;
import blink.com.blinkcard320.Moudle.DownorUpload;
import blink.com.blinkcard320.Moudle.Item;
import blink.com.blinkcard320.Moudle.skin.SkinConfig;
import blink.com.blinkcard320.R;
import blink.com.blinkcard320.Tool.Adapter.LGAdapter;
import blink.com.blinkcard320.Tool.System.MyToast;
import blink.com.blinkcard320.Tool.System.Tools;
import blink.com.blinkcard320.Tool.Thread.HandlerImpl;
import blink.com.blinkcard320.Tool.UploadUtils;
import blink.com.blinkcard320.Tool.Utils.SharedPrefsUtils;
import blink.com.blinkcard320.Tool.Utils.UIHelper;
import blink.com.blinkcard320.Tool.Utils.Utils;
import blink.com.blinkcard320.View.DialogClick;
import blink.com.blinkcard320.View.MyDialog;
import blink.com.blinkcard320.View.MyPersonalProgressDIalog;
import blink.com.blinkcard320.View.MyProgressDIalog;
import blink.com.blinkcard320.application.MyApplication;
import blink.com.blinkcard320.camera.CameraActivity;
import blink.com.blinkcard320.heart.HeartHandler;
import blink.com.blinkcard320.heart.SendHeartThread;
import smart.blink.com.card.API.Protocol;
import smart.blink.com.card.bean.ChangePcPwdRsp;
import smart.blink.com.card.bean.ConnectPcRsp;
import smart.blink.com.card.bean.LookPCRsp;
import smart.blink.com.card.bean.RelayMsgRsp;
import smart.blink.com.card.bean.RestartRsp;
import smart.blink.com.card.bean.ShutdownRsp;
import smart.blink.com.card.bean.WantRsp;

/**
 * Created by Ruanjiahui on 2017/1/4.
 */
public class MainActivity extends NavActivity implements View.OnClickListener, FragmentToActivity, AdapterView.OnItemClickListener, DialogClick, HandlerImpl {


    private static final String TAG = MainActivity.class.getSimpleName();

    private long exitTime = 0;

    private LinearLayout activity_ll_myfiles = null;
    private LinearLayout activity_device_tab = null;
    private LinearLayout activity_ll_devices = null;

    private ArrayList<Object> list = null;
    private ListView activity_left_ListView = null;
    private LGAdapter adapter = null;

    private ImageView imageview_quitstart;
    private TextView drawerlayout_textview_password;
    private TextView drawerlayout_textview_uid;
    private View view;
    private View leftview;
    public static HeartHandler heartHandler;

    public String name;
    public String path;

    /**
     * 跳转Fragment
     *
     * @param targetFragment
     */
    public void intentFragment(Fragment targetFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_device_framelayout, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commitAllowingStateLoss();
    }

    @Override
    public void Click(View v) {
        super.Click(v);
        isMenu = true;
        setLeftTitleVisiable(false);
        setLeftImage(R.mipmap.menu, (int) getResources().getDimension(R.dimen.origin), (int) getResources().getDimension(R.dimen.origin));
        if (v.getId() == R.id.activity_ll_myfiles) {
            setTitle(getResources().getString(R.string.my_file));
            activity_ll_myfiles.setBackgroundResource(R.color.GreySmork);
            activity_ll_devices.setBackgroundResource(R.color.White);
            intentFragment(new FragmentFileManager());
            return;
        }
        if (v.getId() == R.id.activity_ll_devices) {
            setTitle(getResources().getString(R.string.my_devices));
            activity_ll_myfiles.setBackgroundResource(R.color.White);
            activity_ll_devices.setBackgroundResource(R.color.GreySmork);
            intentFragment(new FragmentDevice());
            return;
        }
        if (v.getId() == R.id.drawerlayout_textview_password) {
            jumpToAlterUserPWDActivity();
            return;
        }
    }

    /**
     * 跳到修改用户密码的activity
     */
    private void jumpToAlterUserPWDActivity() {
        intent = new Intent();
        intent.setClass(MainActivity.this, AlterUserPWDActivity.class);
        startActivity(intent);
    }

    /**
     * 这个是四个导航的点击事件
     * 这个是抽象方法不需要在主类实现
     *
     * @param position
     */
    @Override
    public void setNavClick(int position) {

    }

    /**
     * 这个也是一个抽象的方法不需要主类实现
     * 这个是程序的初始化的方法
     */
    @Override
    public void Nav() {
        view = LayoutInflater.from(context).inflate(R.layout.activity_drawlayout_cotains, null);
        leftview = LayoutInflater.from(context).inflate(R.layout.activity_drawlayout_left, null);

        // 添加到全局变量中
        MyApplication.getInstance().addActivity(this);

        //去除标题文件
        setRightTitleVisiable(false);
        setLeftTitleVisiable(false);
        setLeftImage(R.mipmap.menu, (int) getResources().getDimension(R.dimen.origin), (int) getResources().getDimension(R.dimen.origin));
        setTitle(getResources().getString(R.string.my_devices));
        setTopColor(R.color.MainColorBlue);
        setTopTitleColor(R.color.White);
        setNav(0);

        initSlidingMenuData();

        // ListView条目点击
        activity_left_ListView = (ListView) leftview.findViewById(R.id.activity_left_ListView);
        // 设置用户名
        drawerlayout_textview_uid = (TextView) leftview.findViewById(R.id.drawerlayout_textview_uid);
        drawerlayout_textview_uid.setText(MyApplication.userName);

        drawerlayout_textview_password = (TextView) leftview.findViewById(R.id.drawerlayout_textview_password);
        activity_left_ListView.setOnItemClickListener(this);
        drawerlayout_textview_password.setOnClickListener(this);

        activity_ll_myfiles = (LinearLayout) view.findViewById(R.id.activity_ll_myfiles);
        activity_ll_devices = (LinearLayout) view.findViewById(R.id.activity_ll_devices);
        activity_device_tab = (LinearLayout) view.findViewById(R.id.activity_device_tab);


        DensityUtil.setRelHeight(activity_device_tab, BaseActivity.height / 12, new int[]{RelativeLayout.ALIGN_PARENT_BOTTOM});

        //设置布局文件为主布局
        setNavContent(view);
        //将布局文件设置成菜单
        setNavLeftContent(leftview);


        adapter = new LGAdapter(context, list, "ListView");
        activity_left_ListView.setAdapter(adapter);


        intentFragment(new FragmentDevice());

        activity_ll_myfiles.setOnClickListener(this);
        activity_ll_devices.setOnClickListener(this);

        Log.e(TAG, "onClick: 开启屏幕常亮");
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initHeartThread();


    }

    /**
     * 初始化心跳的进程
     */
    private void initHeartThread() {
        heartHandler = new HeartHandler(this, this);
        SendHeartThread sendHeartThread = new SendHeartThread(heartHandler);
        SendHeartThread.isClose = false;
        sendHeartThread.start();
        //MyApplication.getInstance().addThread(sendHeartThread);
    }

    @Override
    protected void onStart() {
        super.onStart();
        int skinValue = SharedPrefsUtils.getIntegerPreference(this, SkinConfig.SKIN_CONFIG, SkinConfig.SKIN_DEFAULT_VALUE);
        initSkinConfig(skinValue);
        initImageview_quitstart(skinValue);
    }

    // 设置皮肤
    public void initSkinConfig(int skinValue) {
        setTopColor(skinValue);
        setMenuBackColor(skinValue);
        if (adapter != null) {
            adapter.setSkinValue(skinValue);
            adapter.notifyDataSetChanged();
        }
    }


    /**
     * 对快捷启动的按钮进行初始化
     *
     * @param
     */
    private void initImageview_quitstart(int skinValue) {
        // 快捷启动
        imageview_quitstart = (ImageView) view.findViewById(R.id.activity_imageview_quitstart);
        int option = SharedPrefsUtils.getIntegerPreference(context, Comment.QUICK_START, Comment.DEFAULT);
        switch (option) {
            case Comment.DEFAULT:
                // 根据皮肤设定不同图片
                imageview_quitstart.setBackgroundDrawable(null);
                //imageview_quitstart.setImageResource(R.mipmap.base_add);
                setQuickStartIcon(skinValue);
                imageview_quitstart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 快捷启动的界面
                        GoQuickStart();
                    }
                });
                break;
            case Comment.REBOOT:
                setQuickStartToReboot();
                break;
            case Comment.SET_TIME_SHUTDOWN:
                setQuickStartToSetTimeShutdown();
                break;
            case Comment.SHUTDOWN:
                setQuickStartToShutdown();
                break;
            case Comment.LOCK_PC:
                setQuickStartToLockPc();
                break;
            case Comment.ALTER_PWD:
                setQuickStartToAlterPWD();
                break;
            case Comment.GET_UPDATE:
                setQuickStartToGetUpdate();
                break;
            case Comment.CAMERA:
                setQuickStartToCamera();
                break;
        }

    }

    private void initSlidingMenuData() {
        list = new ArrayList<>();
        list.add(getItem(getResources().getString(R.string.actionbar_setting), getResources().getDrawable(R.mipmap.menu_setting)));
        list.add(getItem(getResources().getString(R.string.actionbar_feedback), getResources().getDrawable(R.mipmap.menu_feedback)));
        list.add(getItem(getResources().getString(R.string.actionbar_useinfo), getResources().getDrawable(R.mipmap.menu_info)));
        list.add(getItem(getResources().getString(R.string.actionbar_about), getResources().getDrawable(R.mipmap.menu_about)));
        list.add(getItem(getResources().getString(R.string.actionbar_shop), getResources().getDrawable(R.mipmap.menu_shop)));
        list.add(getItem(getResources().getString(R.string.actionbar_customer), getResources().getDrawable(R.mipmap.customer)));
        list.add(getItem(getResources().getString(R.string.actionbar_exit), getResources().getDrawable(R.mipmap.menu_exit)));


        setMenuWidth(BaseActivity.width / 5 * 3);
        setMenuBackColor(R.color.MainColorBlue);
    }

    private boolean isOpen = false;
    private boolean isMenu = true;

    @Override
    public void setLeftCLick() {
        if (isMenu)
            if (!isOpen) {
                isOpen = true;
                setOpenMenu();
            } else {
                isOpen = false;
                setCloseMenu();
            }
        else {
            setLeftImage(R.mipmap.menu, (int) getResources().getDimension(R.dimen.origin), (int) getResources().getDimension(R.dimen.origin));
            setTitle(getResources().getString(R.string.my_file));
            intentFragment(new FragmentFileManager());
        }
    }


    private Item getItem(String text, Drawable drawable) {
        Item item = new Item();
        item.setListImage(drawable);
        item.setListText(text);
        item.setHeight((int) getResources().getDimension(R.dimen.itemHeight));
        item.setImageSize((int) getResources().getDimension(R.dimen.origin));
        item.setSelector(R.drawable.buttonblue);
        item.setListTextColor(R.color.White);
        return item;
    }

    @Override
    public void toActivity(int position) {
        setLeftTitleVisiable(true);
        setLeftTitleColor(R.color.White);
        setLeftImage(R.mipmap.left, (int) getResources().getDimension(R.dimen.marginBiggest), (int) getResources().getDimension(R.dimen.marginBiggest));
        isMenu = false;
        if (position == ActivityCode.PhoneFile) {
            setTitle(getResources().getString(R.string.fragment_phonefile));
            intentFragment(new FragmentFilePhone());
        }
        if (position == ActivityCode.ComputerFile) {
            setTitle(getResources().getString(R.string.fragment_pcfile));
            intentFragment(new FragmentFilePC());
        }
    }


    private Intent intent = null;

    /**
     * ListView条目点击事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                // 设置
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case 1:
                // 反馈
                intent = new Intent(MainActivity.this, FeedbackActivity.class);
                startActivity(intent);
                break;
            case 2:
                // 使用说明
                intent = new Intent();
                intent.setClass(MainActivity.this, UserinfoActivity.class);
                startActivity(intent);
                break;
            case 3:
                // 关于
                intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            case 4:
                // 必联商城
                intent = new Intent(MainActivity.this, ShopActivity.class);
                startActivity(intent);
                break;
            case 5:
                // 联系客服
                contactToCustomerService();
                break;
            case 6:
                // 重新登录
                // 必联商城
                intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
                // 重新登录
                SharedPrefsUtils.setBooleanPreference(this, Comment.IS_RELOGIN, true);
                break;

            default:
                break;
        }
    }

    /**
     * 联系客服
     */
    private void contactToCustomerService() {
        final MyDialog myDialog = new MyDialog(this, R.style.mydialog);
        myDialog.setShareText(Utils.Phone);
        myDialog.DialogClick(new DialogClick() {
            @Override
            public void Enter(int position) {
                Comment.SendPhone(context, Utils.Phone);
                myDialog.dismiss();
            }

            @Override
            public void Canel(int position) {
                myDialog.dismiss();
            }
        }, 0);
        myDialog.setThemeColor(R.color.actionbarcolor);
        myDialog.show();
    }

    /**
     * 根据skin value设定图标
     *
     * @param skinValue
     */
    private void setQuickStartIcon(int skinValue) {
        switch (skinValue) {
            case R.color.MainColorBlue:
                imageview_quitstart.setImageResource(R.mipmap.addblue);
                break;
            case R.color.MainColorGreen:
                imageview_quitstart.setImageResource(R.mipmap.addgreen);
                break;
            case R.color.MainColorPuplor:
                imageview_quitstart.setImageResource(R.mipmap.addpoplure);
                break;
            case R.color.MainColorRed:
                imageview_quitstart.setImageResource(R.mipmap.addred);
                break;
            default:
                imageview_quitstart.setImageResource(R.mipmap.addblue);
                break;
        }
    }


    /**
     * 设置快捷方式为重启电脑
     */
    private void setQuickStartToReboot() {
        imageview_quitstart.setBackgroundDrawable(null);
        imageview_quitstart.setImageResource(R.mipmap.icon_restart);
        imageview_quitstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyProgressDIalog.createSweetDialog(MainActivity.this, getResources().getString(R.string.instruction_reboot), MainActivity.this, ActivityCode.Restart);
            }
        });
    }

    /**
     * 设置快捷方式为定时关闭电脑
     */
    private void setQuickStartToSetTimeShutdown() {
        imageview_quitstart.setBackgroundDrawable(null);
        imageview_quitstart.setImageResource(R.mipmap.icon_timeshowdown);
        imageview_quitstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 访部电脑文件的时候，先暂时关闭发送心跳的线程
                // 释放心跳线程的资源
                SendHeartThread.isClose = true;
                synchronized (SendHeartThread.HeartLock) {
                    SendHeartThread.HeartLock.notify();
                }

                MyProgressDIalog.CreateDialogTime(
                        MainActivity.this.getResources().getString(R.string.instruction_wshutdown),
                        MainActivity.this,
                        MainActivity.this);
            }
        });
    }

    /**
     * 设置快捷方式为关闭电脑
     */
    private void setQuickStartToShutdown() {
        imageview_quitstart.setBackgroundDrawable(null);
        imageview_quitstart.setImageResource(R.mipmap.icon_shutdown);
        imageview_quitstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyProgressDIalog.createSweetDialog(MainActivity.this, getResources().getString(R.string.instruction_shutdown), MainActivity.this, ActivityCode.Shutdown);
            }
        });
    }

    /**
     * 设置快捷方式为锁定电脑
     */
    private void setQuickStartToLockPc() {
        imageview_quitstart.setBackgroundDrawable(null);
        imageview_quitstart.setImageResource(R.mipmap.icon_lockdevices);
        imageview_quitstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyProgressDIalog.createSweetDialog(MainActivity.this, getResources().getString(R.string.instruction_lockingdevice), MainActivity.this, ActivityCode.LOOKPC);
            }
        });
    }

    /**
     * 设置快捷方式为修改密码
     */
    private void setQuickStartToAlterPWD() {
        imageview_quitstart.setBackgroundDrawable(null);
        imageview_quitstart.setImageResource(R.mipmap.icon_changepw);
        imageview_quitstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 和旧版对比，已作过修改
                MyProgressDIalog.CreateChangePCPWDialog(MainActivity.this, ActivityCode.ChangePcPwd, MainActivity.this);
            }
        });
    }

    /**
     * 设置快捷方式为获得更新
     */
    private void setQuickStartToGetUpdate() {
        imageview_quitstart.setBackgroundDrawable(null);
        imageview_quitstart.setImageResource(R.mipmap.icon_update);
        imageview_quitstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyProgressDIalog.CreateNolmalDialog(MainActivity.this, R.string.update, R.string.IsNewVersion);
            }
        });
    }

    /**
     * 设置快捷方式为相机
     */
    private void setQuickStartToCamera() {
        imageview_quitstart.setBackgroundDrawable(null);
        imageview_quitstart.setImageResource(R.mipmap.icon_videofile);
        imageview_quitstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.isCamera(MainActivity.this)) {
                    try {
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // 设定拍照以后文件的保存路径
                        name = new DateFormat().format("yyyyMMdd_hhmmss",
                                Calendar.getInstance(Locale.CHINA))
                                + ".jpg";

                        path = SharedPrefsUtils.getStringPreference(MainActivity.this, Comment.PICTUREFILE);
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
                        Toast.makeText(MainActivity.this, "开启相机时出现异常", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // 调用拍照功能后的回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: 拍完相片回调");
        switch (requestCode) {
            case 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getResources().getString(R.string.photo));
                View view = LayoutInflater.from(MainActivity.this).inflate(
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
                        // TODO Auto-generated method stub
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
                        new UploadUtils(MainActivity.this);

                        bmp.recycle();
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "开始上传图片", Toast.LENGTH_SHORT).show();
                        path = null;
                        name = null;
                    }
                });
                break;
        }

    }

    /**
     * 跳到快捷启动的界面
     */
    private void GoQuickStart() {
        Intent i = new Intent(MainActivity.this, QuickStartActivity.class);
        startActivity(i);
    }

    @Override
    public void Enter(int position) {
        Log.e(TAG, "Enter: " + position);
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
        if (position == ActivityCode.Restart) {
            // 访部电脑文件的时候，先暂时关闭发送心跳的线程
            // 释放心跳线程的资源
            SendHeartThread.isClose = true;
            synchronized (SendHeartThread.HeartLock) {
                SendHeartThread.HeartLock.notify();
            }

            NetCardController.Restart(0, this);
        }
        if (position == ActivityCode.Shutdown) {
            // 访部电脑文件的时候，先暂时关闭发送心跳的线程
            // 释放心跳线程的资源
            SendHeartThread.isClose = true;
            synchronized (SendHeartThread.HeartLock) {
                SendHeartThread.HeartLock.notify();
            }

            NetCardController.Shutdown(0, this);
        }
    }

    @Override
    public void Canel(int position) {
        Log.e(TAG, "Canel: " + "点击Canel");
    }

    /**
     * 处理线程返回的更新界面
     *
     * @param position
     * @param object
     */
    @Override
    public void myHandler(int position, Object object) {

        if (position == ActivityCode.WANT && MyApplication.wantCount.get() == 0) {

            MyApplication.wantCount.getAndIncrement();
            WantRsp wantRsp = (WantRsp) object;

            // 请求不成功，250ms后重新标志位清0
            if (wantRsp.getSuccess() != 0) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        synchronized (this) {
                            MyApplication.wantCount.set(0);
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    MyPersonalProgressDIalog.getInstance(MainActivity.this).dissmissProgress();
                                    MainActivity.this.startActivity(new Intent(MainActivity.this, Login.class));
                                    MyApplication.getInstance().exit();
                                }
                            });
                            Log.e(TAG, "run: " + "我已经清除了标志位");
                            this.cancel();
                        }
                    }
                }, 250, 250);
            }

            switch (wantRsp.getSuccess()) {
                case 0:
                    //返回成功之后
                    //继续打洞
                    NetCardController.HELLO(this);
                    break;
                case 1:
                    MyToast.Toast(context, R.string.activity_init_passwd_error);

                    break;
                case 2:
                    //MyToast.Toast(context, R.string.activity_init_pc_offline);
                    UIHelper.ToastSetSuccess(this, R.string.activity_init_pc_offline);

                    break;
                case 3:
                    MyToast.Toast(context, R.string.activity_init_others_already_connected);

                    break;
                case 4:
                    MyToast.Toast(context, R.string.activity_init_login_error_len);

                    break;
            }
        }

        // helloCount.get() 因为服务器可能为了防治丢包而多发几次数据
        if (position == ActivityCode.HELLO && MyApplication.helloCount.get() == 0) {

            MyApplication.helloCount.getAndIncrement();
            //打洞成功
            CommonIntent.IntentActivity(context, MainActivity.class);
            MyPersonalProgressDIalog.getInstance(this).dissmissProgress();
            Log.e(TAG, "myHandler: " + "重连成功");
            UIHelper.ToastSetSuccess(this, R.string.reconnect_success);
            initHeartThread();
        }

        // 参考Fragment里面的代码
        if (position == ActivityCode.LOOKPC) {
            // 重新开启一个心跳线程
            SendHeartThread sendHeartThread = new SendHeartThread(MainActivity.heartHandler);
            SendHeartThread.isClose = false;
            sendHeartThread.start();

            LookPCRsp lookPCRsp = (LookPCRsp) object;
            if (lookPCRsp.getSuccess() == 0) {
                MyProgressDIalog.setDialogSuccess(context, R.string.main_handler_lock_recved);
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
                MyProgressDIalog.setDialogSuccess(context, R.string.main_handler_restart_recved);
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

        // 接收到务器返回的心跳
        if (position == ActivityCode.Heart) {
            SendHeartThread.timeCount.set(0);
            //Log.e(TAG, "myHandler: " + "接收到服务器返回的心跳");

        }

        // 申请与子服务器成功后会走这个方法，通过TCP方法与服务器连接
        if (position == ActivityCode.RelayMsg) {
            Log.e(TAG, "onSuccess: " + "申请与子服务器成功");
            NetCardController.CONNECT_TO_SUBSERVER(this);
        }

        // 与子服务器连接成功
        if (position == ActivityCode.ConnectPC) {
            ConnectPcRsp connectPcRsp = (ConnectPcRsp) object;
            Log.e(TAG, "myHandler: result位为：" + connectPcRsp.getSuccess());
            if (connectPcRsp.getSuccess() == 0) {
                MyPersonalProgressDIalog.getInstance(this).dissmissProgress();
                Log.e(TAG, "myHandler: " + "重连成功");
                UIHelper.ToastSetSuccess(this, R.string.reconnect_success);
                //打洞成功
                CommonIntent.IntentActivity(context, MainActivity.class);
                initHeartThread();
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

        Log.e(TAG, "myError: ===" + position );

        if (position == ActivityCode.HELLO) {
            //打洞失败则申请子服务器
            MyPersonalProgressDIalog.getInstance(this).dissmissProgress();
            MyPersonalProgressDIalog.getInstance(this).setContent("正通过服务器连接").showProgressDialog();
            NetCardController.RelayMsg(this);
        }

        if (position == ActivityCode.LOOKPC) {
            // 重新开启一个心跳线程
            SendHeartThread sendHeartThread = new SendHeartThread(MainActivity.heartHandler);
            SendHeartThread.isClose = false;
            sendHeartThread.start();

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MyProgressDIalog.seetDialogTimeOver(R.string.main_handler_lock_lost, MainActivity.this);
                }
            });
        }
        if (position == ActivityCode.Restart) {
            // 重新开启一个心跳线程
            SendHeartThread sendHeartThread = new SendHeartThread(MainActivity.heartHandler);
            SendHeartThread.isClose = false;
            sendHeartThread.start();

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MyProgressDIalog.seetDialogTimeOver(R.string.main_handler_restart_lost, MainActivity.this);
                }
            });
        }

        if (position == ActivityCode.Shutdown) {
            // 重新开启一个心跳线程
            SendHeartThread sendHeartThread = new SendHeartThread(MainActivity.heartHandler);
            SendHeartThread.isClose = false;
            sendHeartThread.start();

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MyProgressDIalog.seetDialogTimeOver(R.string.main_handler_shutdown_lost, MainActivity.this);
                }
            });
        }

        if (position == ActivityCode.ChangePcPwd) {
            // 重新开启一个心跳线程
            SendHeartThread sendHeartThread = new SendHeartThread(MainActivity.heartHandler);
            SendHeartThread.isClose = false;
            sendHeartThread.start();

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MyProgressDIalog.seetDialogTimeOver(R.string.main_handler_change_lost, MainActivity.this);
                }
            });
        }
    }

    NetWorkStateReceiver netWorkStateReceiver;

    @Override
    protected void onResume() {
        if (netWorkStateReceiver == null) {
            netWorkStateReceiver = new NetWorkStateReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStateReceiver, filter);
        Log.e(TAG, "onResume: " + "注册了一个广播接收者");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (netWorkStateReceiver != null) {
            unregisterReceiver(netWorkStateReceiver);
            netWorkStateReceiver = null;
            Log.e(TAG, "onDestroy: 关闭广播接收者");
        }

        // 释放心跳线程的资源
        SendHeartThread.isClose = true;
        synchronized (SendHeartThread.HeartLock) {
            SendHeartThread.HeartLock.notify();
        }

        MyApplication.wantCount.set(0);
        MyApplication.helloCount.set(0);
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this,
                    getResources().getString(R.string.activity_main_exit),
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            // 退出当前的程序
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);

        }
    }
}
