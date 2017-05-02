package com.blink.blinkp2p.Controller.Activity.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.zxing.MipcaActivityCapture;
import com.blink.blinkp2p.Controller.Activity.MainActivity;
import com.blink.blinkp2p.Controller.ActivityCode;
import com.blink.blinkp2p.Controller.NetCardController;
import com.blink.blinkp2p.Moudle.skin.SkinConfig;
import com.blink.blinkp2p.R;
import com.blink.blinkp2p.Tool.System.MyToast;
import com.blink.blinkp2p.Tool.Thread.HandlerImpl;
import com.blink.blinkp2p.Tool.Utils.SharedPrefsUtils;
import com.blink.blinkp2p.View.MyPersonalProgressDIalog;
import com.blink.blinkp2p.View.MyProgressDIalog;
import com.blink.blinkp2p.View.pop.Dao.LoginDAO;
import com.blink.blinkp2p.View.pop.DropDownPopWindows;
import com.blink.blinkp2p.application.MyApplication;
import com.example.administrator.data_sdk.CommonIntent;
import com.example.administrator.ui_sdk.Applications;
import com.example.administrator.ui_sdk.DensityUtil;
import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import com.blink.blinkp2p.Moudle.Comment;


import com.blink.blinkp2p.Tool.System.Tools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.db.table.Id;

import smart.blink.com.card.Tcp.MyDown;
import smart.blink.com.card.Tcp.MyUpload;
import smart.blink.com.card.Udp.Down;
import smart.blink.com.card.Udp.Upload;
import smart.blink.com.card.bean.ConnectPcRsp;
import smart.blink.com.card.bean.RelayMsgRsp;
import smart.blink.com.card.bean.WantRsp;

/**
 * Created by Ruanjiahui on 2017/1/4.
 */
public class Login extends BaseActivity implements HandlerImpl {

    private static final String TAG = Login.class.getSimpleName();

    private RelativeLayout loginEditRelative = null;
    private RelativeLayout loginScanRelative = null;
    private TextView initActivityButtonWant;
    private RelativeLayout initCheckLinear;
    private CheckBox activityCheckboxAutologin;
    private TextView initAuto;
    private CheckBox activityCheckboxRemeberpassword;
    private TextView initPassword;
    private EditText activityInitEditPasswd;
    private RelativeLayout initIdLinear;
    private EditText initActivityEditText;
    private ImageView initDownImage;
    private ImageView loginLogo;
    private Button initActivityButtonSweep;


    //用户ID和密码
    private String ID = null;
    private String password = null;
    private AtomicInteger wantCount;
    private AtomicInteger helloCount;

    // 存储是否需要自动登录
    private boolean isAutoLogin = false;
    private boolean isRemeberPwd = false;
    private boolean isReLogin = false;
    private boolean isNeedToClearOlderPwd = false;
    // 为了方式混乱和上面用户分开，不同的逻辑
    private String loginUserName = "";
    private String loginPwd = "";

    private DropDownPopWindows mDownPopWindows;
    private RelativeLayout init_idLinear = null;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Bundle data = msg.getData();
            switch (msg.what) {
                case 1:
                    int selIndex = data.getInt("selIndex");
                    initActivityEditText.setText(mDownPopWindows.getdatas(selIndex));
                    //LoginSetData.getIntance(Login.this).setPasswd(initActivityEditText, activityInitEditPasswd);
                    mDownPopWindows.dismiss();
                    break;
                case 2:
                    int delIndex = data.getInt("delIndex");
                    // 删除保存在本地的数据
                    String removeuser = mDownPopWindows.getdatas(delIndex);
                    Log.e(TAG, "handleMessage: removeuser===" + removeuser);

                    // 删除本地的数据
                    String userlist = SharedPrefsUtils.getStringPreference(Login.this, Comment.LOGINDATA);
                    Log.e(TAG, "handleMessage: userlist===" + userlist);

                    Gson g = new Gson();
                    Type lt = new TypeToken<List<LoginBeanGson>>() {
                    }.getType();
                    ArrayList<LoginBeanGson> arraylist = g.fromJson(userlist, lt);
                    Log.e(TAG, "handleMessage: arraylist.size()==" + arraylist.size());
                    for (int i = 0; i < arraylist.size(); i++) {
                        LoginBeanGson t = arraylist.get(i);
                        if (t.getUsername().equals(removeuser)) {
                            arraylist.remove(i);
                            break;
                        }
                    }

                    if (arraylist.size() == 0) {
                        SharedPrefsUtils.setStringPreference(Login.this, Comment.LOGINDATA, null);
                    } else {
                        // 将数据保存在本地
                        SharedPrefsUtils.setStringPreference(Login.this, Comment.LOGINDATA, g.toJson(arraylist));
                    }


                    mDownPopWindows.removedatasItem(delIndex);

                    mDownPopWindows.AdapterUpdate();
                    initActivityEditText.setText("");
                    activityInitEditPasswd.setText("");
                    break;
//                case Read_SharePerence:
//                    if ((boolean) msg.obj) {
//                        Intent intent = new Intent(InitActivity.this,
//                                MainActivity.class);
//                        startActivity(intent);
//                    } else {
//                        // imageview_load.setVisibility(View.GONE);
//                    }
//                    break;
            }
        }

        ;
    };

    /**
     * Start()
     */
    @Override
    public void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.netcard_login, null);

        setTileBar(0);

        init_idLinear = (RelativeLayout) view.findViewById(R.id.init_idLinear);

        loginEditRelative = (RelativeLayout) view.findViewById(R.id.loginEditRelative);
        loginScanRelative = (RelativeLayout) view.findViewById(R.id.loginScanRelative);
        initActivityButtonWant = (TextView) view.findViewById(R.id.init_activity_button_want);
        initCheckLinear = (RelativeLayout) view.findViewById(R.id.init_checkLinear);

        activityCheckboxAutologin = (CheckBox) view.findViewById(R.id.activity_checkbox_autologin);
        initAuto = (TextView) view.findViewById(R.id.init_auto);

        activityCheckboxRemeberpassword = (CheckBox) view.findViewById(R.id.activity_checkbox_remeberpassword);
        initPassword = (TextView) view.findViewById(R.id.init_password);

        activityInitEditPasswd = (EditText) view.findViewById(R.id.activity_init_edit_passwd);

        initIdLinear = (RelativeLayout) view.findViewById(R.id.init_idLinear);

        initActivityEditText = (EditText) view.findViewById(R.id.init_activity_edit_text);

        initDownImage = (ImageView) view.findViewById(R.id.init_downImage);
        loginLogo = (ImageView) view.findViewById(R.id.loginLogo);
        initActivityButtonSweep = (Button) view.findViewById(R.id.init_activity_button_sweep);
        initActivityButtonSweep.setOnClickListener(this);
        initDownImage.setOnClickListener(this);


        // 给布局设置高度，用代码设置方便屏幕适配
        DensityUtil.setHeight(loginEditRelative, BaseActivity.height / 5 * 3);
        DensityUtil.setHeight(loginScanRelative, BaseActivity.height / 5 * 2);

        setContent(view);

        initActivityButtonWant.setOnClickListener(this);

        // 防止服务器多次发送数据过来引起的逻辑混乱
        wantCount = MyApplication.wantCount;
        wantCount.set(0);
        helloCount = MyApplication.helloCount;
        helloCount.set(0);

        // 先暂时注释
        //initSaveAutoLoginData();

        initSaveAutoLoginDataCompatOldVersion();
    }

    /**
     * 这个是为了兼容老版本的数据
     */
    private void initSaveAutoLoginDataCompatOldVersion() {
        isReLogin = SharedPrefsUtils.getBooleanPreference(this, Comment.IS_RELOGIN, false);
        isNeedToClearOlderPwd = SharedPrefsUtils.getBooleanPreference(this, Comment.IS_NEED_CLEAR_OLDER_PWD, false);

        LoginBeanGson l = getLoginBeanGson();
        if (l != null) {
            // 得到数据的处理逻辑
            isRemeberPwd = l.isRemeber();
            isAutoLogin = l.isAutologin();

            activityCheckboxAutologin.setChecked(isAutoLogin);
            activityCheckboxRemeberpassword.setChecked(isRemeberPwd);
            if (isRemeberPwd) {
                loginUserName = l.getUsername();
                loginPwd = l.getPassword();

                // 调试代码用，会删除
                initActivityEditText.setText(loginUserName);
                activityInitEditPasswd.setText(loginPwd);
            } else {
                // 调用代码用
                initActivityEditText.setText("");
                activityInitEditPasswd.setText("");
                loginUserName = "";
                loginPwd = "";
            }
        }
        // 如果是重新登录的话就不需要自动登录
        if (!isReLogin) {
            // 判断是需要自动登录
            if (isAutoLogin) {
                startWantRequest();
            }
        }

        // 清空原来的密码标志位,将sharedPrefs保存的密码置为空
        if (isNeedToClearOlderPwd) {
            activityInitEditPasswd.setText("");

            //　修改用户的信息
            String userName = MyApplication.userName;
            String userlist = SharedPrefsUtils.getStringPreference(this, Comment.LOGINDATA);

            Gson g = new Gson();
            Type lt = new TypeToken<List<LoginBeanGson>>() {
            }.getType();
            ArrayList<LoginBeanGson> arraylist;
            try {
                if (userlist == null) {
                    arraylist = new ArrayList<>();
                } else {
                    arraylist = g.fromJson(userlist, lt);
                }

            } catch (Exception e) {
                arraylist = new ArrayList<>();
                arraylist.add(g.fromJson(userlist, LoginBeanGson.class));
            }

            for (LoginBeanGson t : arraylist) {
                if (t.getUsername().equals(userName)) {
                    t.setPassword("");
                    t.setTime(System.currentTimeMillis());
                    t.setRemeber(isRemeberPwd);
                    t.setAutologin(isAutoLogin);
                    break;
                }
            }
            // 将数据保存在本地
            SharedPrefsUtils.setStringPreference(Login.this, Comment.LOGINDATA, g.toJson(arraylist));
        }
        // 设置checkbox的点击事件
        activityCheckboxAutologin.setOnClickListener(this);
        activityCheckboxRemeberpassword.setOnClickListener(this);
    }

    private LoginBeanGson getLoginBeanGson() {
        String userlist = SharedPrefsUtils.getStringPreference(this, Comment.LOGINDATA);
        Log.e(TAG, "initSaveAutoLoginDataCompatOldVersion: userlist===" + userlist);

        // 如果获取不到数据的话就直接返回一个空
        if (userlist == null) {
            return null;
        }
        Gson g = new Gson();
        LoginBeanGson l = null;
        try {
            List<LoginBeanGson> retList = g.fromJson(userlist,
                    new TypeToken<List<LoginBeanGson>>() {
                    }.getType());
            l = retList.get(0);
        } catch (Exception e) {

            l = g.fromJson(userlist, LoginBeanGson.class);
        } finally {
            return l;
        }
    }

    /**
     * 初始化自动登录
     */
    private void initSaveAutoLoginData() {
        isAutoLogin = SharedPrefsUtils.getBooleanPreference(this, Comment.IS_AUTO_LOGIN, false);
        isRemeberPwd = SharedPrefsUtils.getBooleanPreference(this, Comment.IS_REMEBER_PWD, false);
        isReLogin = SharedPrefsUtils.getBooleanPreference(this, Comment.IS_RELOGIN, false);

        activityCheckboxAutologin.setChecked(isAutoLogin);
        activityCheckboxRemeberpassword.setChecked(isRemeberPwd);
        if (isRemeberPwd) {
            loginUserName = SharedPrefsUtils.getStringPreference(this, Comment.USERNAME);
            loginPwd = SharedPrefsUtils.getStringPreference(this, Comment.PASSWORD);

            // 调试代码用，会删除
            initActivityEditText.setText(loginUserName);
            activityInitEditPasswd.setText(loginPwd);
        } else {
            // 调用代码用
            initActivityEditText.setText("");
            activityInitEditPasswd.setText("");
            loginUserName = "";
            loginPwd = "";
        }

        // 如果是重新登录的话就不需要自动登录
        if (!isReLogin) {
            // 判断是需要自动登录
            if (isAutoLogin) {
                startWantRequest();
            }
        }

        // 设置checkbox的点击事件
        activityCheckboxAutologin.setOnClickListener(this);
        activityCheckboxRemeberpassword.setOnClickListener(this);
    }

    /**
     * 在onStart中初始化皮肤
     */
    @Override
    protected void onStart() {
        super.onStart();
        // 添加皮肤的设置
        int skinValue = SharedPrefsUtils.getIntegerPreference(this, SkinConfig.SKIN_CONFIG, SkinConfig.SKIN_DEFAULT_VALUE);
        initActivityButtonWant.setBackgroundResource(skinValue);
        /**
         * 我一开始用setTextColor(int)这个方法，但是这个方法不起作用
         *
         * 最后在Stack Overflow找到答案
         *
         * The documentation is not very verbose about this,
         * but you cannot use just the R.color integer when calling setTextColor.
         * You need to call getResources().getColor(R.color.YOURCOLOR) to set a color properly
         *
         *
         */
        initAuto.setTextColor(getResources().getColor(skinValue));
        initPassword.setTextColor(getResources().getColor(skinValue));

        initImageviewActivityButtonSweep(skinValue);

    }

    /**
     * 给二维码扫描的按钮设定颜色
     *
     * @param skinValue
     */
    private void initImageviewActivityButtonSweep(int skinValue) {
        switch (skinValue) {
            case R.color.MainColorBlue:
                initActivityButtonSweep.setBackgroundResource(R.mipmap.scanblue);
                break;
            case R.color.MainColorGreen:
                initActivityButtonSweep.setBackgroundResource(R.mipmap.scangreen);
                break;
            case R.color.MainColorPuplor:
                initActivityButtonSweep.setBackgroundResource(R.mipmap.scanpoplur);
                break;
            case R.color.MainColorRed:
                initActivityButtonSweep.setBackgroundResource(R.mipmap.scanred);
                break;
            default:
                initActivityButtonSweep.setBackgroundResource(R.mipmap.scanblue);
                break;
        }
    }

    @Override
    public void Click(View v) {
        super.Click(v);

        if (v.getId() == R.id.init_activity_button_want) {
            boolean isOnline = Tools.isOnline(this);
            if (isOnline) {
                startWantRequest();
            } else {
                Toast.makeText(this, "当前网络不可用,请检查连接", Toast.LENGTH_SHORT).show();
            }
        }

        // 自动登录选择框
        if (v.getId() == R.id.activity_checkbox_autologin) {
            isAutoLogin = activityCheckboxAutologin.isChecked();
            if (!isRemeberPwd) {
                activityCheckboxAutologin.setChecked(false);
                isAutoLogin = false;
            }
        }
        // 记住密码选择框
        if (v.getId() == R.id.activity_checkbox_remeberpassword) {
            isRemeberPwd = activityCheckboxRemeberpassword.isChecked();
            if (!isRemeberPwd) {
                activityCheckboxAutologin.setChecked(false);
                isAutoLogin = false;
            }
            Log.e(TAG, "Click: activity_checkbox_remeberpassword " + activityCheckboxRemeberpassword.isChecked());
        }

        if (v.getId() == R.id.init_activity_button_sweep) {
            Log.e("Ruan", Tools.isCamera(this) + "");
            if (Tools.isCamera(this)) {
                Intent intent = new Intent();
                intent.setClass(this, MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, 1);
            } else {

                Toast.makeText(this, R.string.Camera, Toast.LENGTH_SHORT).show();
                // 摄像头权限还未得到用户的同意
                //requestCameraPermission();
            }
        }

        // 弹出pop窗口
        if (v.getId() == R.id.init_downImage) {
            mDownPopWindows = new DropDownPopWindows(Login.this, handler, init_idLinear.getWidth());
            mDownPopWindows.initPopuWindow(initActivityEditText, activityInitEditPasswd);
            mDownPopWindows.popupWindwShowing(initActivityEditText);
        }
    }

//    private void requestCameraPermission() {
//        // 摄像头权限已经被拒绝
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                Manifest.permission.CAMERA)) {
//            // 如果用户已经拒绝劝降，那么提供额外的权限说明
////            Snackbar.make(mLayout, R.string.permission_camera_rationale,
////                    Snackbar.LENGTH_INDEFINITE)
////                    .setAction(R.string.ok, new View.OnClickListener() {
////                        @Override
////                        public void onClick(View view) {
////                            ActivityCompat.requestPermissions(MainActivity.this,
////                                    new String[]{Manifest.permission.CAMERA},
////                                    REQUEST_CAMERA);
////                        }
////                    })
////                    .show();
//        } else {
//            // 摄像头还没有被拒绝，直接申请
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
//                    REQUEST_CAMERA);
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String reslut = bundle.getString("result");
                    if (reslut.length() > 20) {
                        //UIHelper.ToastMessageNetError(context, R.string.error_input_long);
                        Toast.makeText(this, R.string.error_input_long, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    initActivityEditText.setText(bundle.getString("result"));
                    activityInitEditPasswd.setText("");
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {

                        public void run() {
                            // 设置自动获取焦点
                            InputMethodManager inputManager = (InputMethodManager) activityInitEditPasswd.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.showSoftInput(activityInitEditPasswd, 0);
                        }

                    }, 500);
                }
                break;
        }
    }

    /**
     * 开始want请求
     */
    private void startWantRequest() {
        //首先获取内外IP和端口
        ID = initActivityEditText.getText().toString();
        password = activityInitEditPasswd.getText().toString();

        if (ID.length() == 0) {
            MyToast.Toast(context, R.string.error_input_null_user);
            wantCount.set(0);
            return;
        }

        if (password.length() == 0) {
            MyToast.Toast(context, R.string.error_input_null_userpw);
            wantCount.set(0);
            return;
        }

        if (ID.length() >= 48) {
            MyToast.Toast(context, R.string.error_user_long);
            wantCount.set(0);
            return;
        }
        if (password.length() > 15) {
            MyToast.Toast(context, R.string.error_pw_long);
            wantCount.set(0);
            return;
        }
        //首先获取内外网IP和端口
        NetCardController.WANT(ID, password, this);
        // 登录提示
        MyPersonalProgressDIalog.getInstance(this).setContent("正在连接电脑").showProgressDialog();

    }

    /**
     * 处理线程返回的更新界面
     *
     * @param position
     * @param object
     */
    @Override
    public void myHandler(int position, Object object) {
        Log.e(TAG, "myHandler: wantCount.get()===" + wantCount.get());
        if (position == ActivityCode.WANT && wantCount.get() == 0) {
            wantCount.getAndIncrement();
            WantRsp wantRsp = (WantRsp) object;
            // 请求不成功，250ms后重新标志位清0
            if (wantRsp.getSuccess() != 0) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        synchronized (this) {
                            wantCount.set(0);
                            Login.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    MyPersonalProgressDIalog.getInstance(Login.this).dissmissProgress();
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
                    //继续打洞
                    NetCardController.HELLO(this);
                    break;
                case 1:
                    MyToast.Toast(context, R.string.activity_init_passwd_error);
                    break;
                case 2:
                    MyToast.Toast(context, R.string.activity_init_pc_offline);
                    break;
                case 3:
                    MyToast.Toast(context, R.string.activity_init_others_already_connected);
                    break;
                case 4:
                    MyToast.Toast(context, R.string.activity_init_user_error);
                    break;
            }
        }

        if (position == ActivityCode.HELLO && helloCount.get() == 0) {

            Log.e(TAG, "myHandler: helloCount.get()===" + helloCount.get());
            helloCount.getAndIncrement();
            //打洞成功
            CommonIntent.IntentActivity(context, MainActivity.class);
            Applications.getInstance().removeOneActivity(this);

            // 记住用户名和密码
            MyApplication.userName = ID;
            MyApplication.userPassword = password;

            // 登录成功的话就往数据库中插入数据
            LoginDAO loginDAO = new LoginDAO(this);
            loginDAO.add(ID);

            MyProgressDIalog.dissmissProgress();

            MyToast.Toast(context, R.string.login_success);
            finish();

            // 允许接收网络变换的广播
            Comment.isReceivedBroadCast = true;
            Down.isStart = true;
            Upload.isStart = true;

            MyDown.isStart = true;
            MyUpload.isStart = true;

            //saveAutoLoginData();
            SaveAutoLoginDataCompatOldVersion();

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
                //打洞成功
                CommonIntent.IntentActivity(context, MainActivity.class);
                Applications.getInstance().removeOneActivity(this);

                // 记住用户名和密码
                MyApplication.userName = ID;
                MyApplication.userPassword = password;

                // 登录成功的话就往数据库中插入数据
                LoginDAO loginDAO = new LoginDAO(this);
                loginDAO.add(ID);

                // 关闭掉对话框
                MyPersonalProgressDIalog.getInstance(this).dissmissProgress();
                MyToast.Toast(context, R.string.login_success);

                finish();

                // 允许接收网络变换的广播
                Comment.isReceivedBroadCast = true;
                Down.isStart = true;
                Upload.isStart = true;

                MyDown.isStart = true;
                MyUpload.isStart = true;
                //saveAutoLoginData();
                SaveAutoLoginDataCompatOldVersion();
            }
        }
    }

    /**
     * 为了兼容老版本保存自动登录的数据
     */
    private void SaveAutoLoginDataCompatOldVersion() {
        // 重新置为false
        SharedPrefsUtils.setBooleanPreference(this, Comment.IS_RELOGIN, false);
        SharedPrefsUtils.setBooleanPreference(this, Comment.IS_NEED_CLEAR_OLDER_PWD, false);

        String userlist = SharedPrefsUtils.getStringPreference(this, Comment.LOGINDATA);

        Gson g = new Gson();
        Type lt = new TypeToken<List<LoginBeanGson>>() {
        }.getType();
        ArrayList<LoginBeanGson> arraylist;
        try {
            if (userlist == null) {
                arraylist = new ArrayList<>();
            } else {
                arraylist = g.fromJson(userlist, lt);
            }

        } catch (Exception e) {
            arraylist = new ArrayList<>();
            arraylist.add(g.fromJson(userlist, LoginBeanGson.class));
        }

        boolean iswirte = true;
        for (LoginBeanGson t : arraylist) {
            if (t.getUsername().equals(ID)) {
                t.setPassword(password);
                t.setTime(System.currentTimeMillis());
                t.setRemeber(isRemeberPwd);
                t.setAutologin(isAutoLogin);
                iswirte = false;
                break;
            }
        }

        if (iswirte) {
            LoginBeanGson t = new LoginBeanGson();
            t.setUsername(ID);
            t.setPassword(password);
            t.setTime(System.currentTimeMillis());
            t.setRemeber(isRemeberPwd);
            t.setAutologin(isAutoLogin);
            arraylist.add(t);
        }

        // 重新排序
        SortComparator<LoginBeanGson> cmp = new SortComparator<>();
        Collections.sort(arraylist, cmp);

        // 将数据保存在本地
        SharedPrefsUtils.setStringPreference(Login.this, Comment.LOGINDATA, g.toJson(arraylist));

    }


    /**
     * 错误的操作
     *
     * @param position
     * @param error
     */
    @Override
    public void myError(int position, int error) {
        if (position == ActivityCode.HELLO) {
            Login.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //打洞失败则申请子服务器
                    MyPersonalProgressDIalog.getInstance(Login.this).dissmissProgress();
                    MyPersonalProgressDIalog.getInstance(Login.this).setContent("正通过服务器连接").showProgressDialog();
                    NetCardController.RelayMsg(Login.this);
                }
            });

        }

        if (position == ActivityCode.WANT) {
            Log.e(TAG, "myError: 连接主服务器失败");
            Login.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //打洞失败则申请子服务器
                    MyPersonalProgressDIalog.getInstance(Login.this).dissmissProgress();
                    Toast.makeText(Login.this, "访问主服务器失败", Toast.LENGTH_SHORT).show();

                    wantCount.set(0);
                }
            });
        }

        if (position == ActivityCode.RelayMsg) {
            Log.e(TAG, "myError: 访问子服务器失败");
            Login.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //打洞失败则申请子服务器
                    MyPersonalProgressDIalog.getInstance(Login.this).dissmissProgress();
                    Toast.makeText(Login.this, "访问子服务器失败", Toast.LENGTH_SHORT).show();

                    wantCount.set(0);
                }
            });
        }

        if (position == ActivityCode.ConnectPC) {
            Log.e(TAG, "myError: 连接子服务器失败");
            Login.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //打洞失败则申请子服务器
                    MyPersonalProgressDIalog.getInstance(Login.this).dissmissProgress();
                    Toast.makeText(Login.this, "连接子服务器失败", Toast.LENGTH_SHORT).show();

                    wantCount.set(0);
                }
            });
        }
    }

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this,
                    getResources().getString(R.string.activity_main_exit),
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            Log.e(TAG, "onBackPressed: 关闭屏幕常亮");
            getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            finish();
            // 退出当前的程序
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);

        }
    }


    /**
     * 保存自动登录的数据 暂先不用
     */
    private void saveAutoLoginData() {
        SharedPrefsUtils.setBooleanPreference(this, Comment.IS_AUTO_LOGIN, isAutoLogin);
        SharedPrefsUtils.setBooleanPreference(this, Comment.IS_REMEBER_PWD, isRemeberPwd);
        // 重新置为false
        SharedPrefsUtils.setBooleanPreference(this, Comment.IS_RELOGIN, false);

        if (isRemeberPwd) {
            SharedPrefsUtils.setStringPreference(this, Comment.USERNAME, ID);
            SharedPrefsUtils.setStringPreference(this, Comment.PASSWORD, password);
        } else {
            SharedPrefsUtils.setStringPreference(this, Comment.USERNAME, "");
            SharedPrefsUtils.setStringPreference(this, Comment.PASSWORD, "");
        }
    }

}
