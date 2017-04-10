package blink.com.blinkcard320.Controller.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.zxing.MipcaActivityCapture;
import com.example.administrator.data_sdk.CommonIntent;
import com.example.administrator.ui_sdk.Applications;
import com.example.administrator.ui_sdk.DensityUtil;
import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import blink.com.blinkcard320.Controller.ActivityCode;
import blink.com.blinkcard320.Controller.NetCardController;
import blink.com.blinkcard320.Moudle.Comment;
import blink.com.blinkcard320.Moudle.skin.SkinConfig;
import blink.com.blinkcard320.R;
import blink.com.blinkcard320.Tool.System.MyToast;
import blink.com.blinkcard320.Tool.System.Tools;
import blink.com.blinkcard320.Tool.Thread.HandlerImpl;
import blink.com.blinkcard320.Tool.Utils.SharedPrefsUtils;
import blink.com.blinkcard320.View.MyPersonalProgressDIalog;
import blink.com.blinkcard320.View.MyProgressDIalog;
import blink.com.blinkcard320.View.pop.Dao.LoginDAO;
import blink.com.blinkcard320.View.pop.DropDownPopWindows;
import blink.com.blinkcard320.application.MyApplication;
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
                    Log.e(TAG, "handleMessage: 点击条目了");
                    break;
                case 2:
                    int delIndex = data.getInt("delIndex");
                    String removeuser = mDownPopWindows.getdatas(delIndex);
                    mDownPopWindows.removedatasItem(delIndex);

                    mDownPopWindows.AdapterUpdate();
                    initActivityEditText.setText("");
                    activityInitEditPasswd.setText("");

                    Log.e(TAG, "handleMessage: 删除的操作");
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
        if (init_idLinear == null) {
            Log.e(TAG, "init: init_idLinear空指针");
        }

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

        initSaveAutoLoginData();
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
//            //首先获取内外IP和端口
//            ID = initActivityEditText.getText().toString();
//            password = activityInitEditPasswd.getText().toString();
//
//            if (ID.length() == 0) {
//                MyToast.Toast(context, R.string.error_input_null_user);
//                return;
//            }
//
//            if (password.length() == 0) {
//                MyToast.Toast(context, R.string.error_input_null_userpw);
//                return;
//            }
//
//            if (ID.length() >= 48) {
//                MyToast.Toast(context, R.string.error_user_long);
//                return;
//            }
//            if (password.length() > 15) {
//                MyToast.Toast(context, R.string.error_pw_long);
//                return;
//            }
//            //首先获取内外网IP和端口
//            NetCardController.WANT(ID, password, this);
            boolean isOnline = Tools.isOnline(this);
            if (isOnline) {
                startWantRequest();
            } else {
                Toast.makeText(this, "当前网络不可用,请检查连接", Toast.LENGTH_SHORT).show();
            }


        }

        // 下面为checkbox登录选择框逻辑
        if (v.getId() == R.id.activity_checkbox_autologin) {
            isAutoLogin = activityCheckboxAutologin.isChecked();
            if (!isRemeberPwd) {
                activityCheckboxAutologin.setChecked(false);
                isAutoLogin = false;
            }
            Log.e(TAG, "Click: activity_checkbox_autologin " + activityCheckboxAutologin.isChecked());
        }
        if (v.getId() == R.id.activity_checkbox_remeberpassword) {
            isRemeberPwd = activityCheckboxRemeberpassword.isChecked();
            if (!isRemeberPwd) {
                activityCheckboxAutologin.setChecked(false);
                isAutoLogin = false;
            }
            Log.e(TAG, "Click: activity_checkbox_remeberpassword " + activityCheckboxRemeberpassword.isChecked());
        }

        if (v.getId() == R.id.init_activity_button_sweep) {
            Log.e(TAG, "Click: 扫描二维码");
            Log.e(TAG, "onClick: 扫描二维码");
            Log.e("Ruan", Tools.isCamera(this) + "");
            if (Tools.isCamera(this)) {
                Intent intent = new Intent();
                intent.setClass(this, MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, 1);
            } else {
                Toast.makeText(this, R.string.Camera, Toast.LENGTH_SHORT).show();
            }
        }

        if (v.getId() == R.id.init_downImage) {

            mDownPopWindows = new DropDownPopWindows(Login.this, handler, init_idLinear.getWidth());
            mDownPopWindows.initPopuWindow(initActivityEditText, activityInitEditPasswd);
            mDownPopWindows.popupWindwShowing(initActivityEditText);
            Log.e(TAG, "Click: 弹出pop窗口");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: ");
        switch (requestCode) {
            case 1:
                Log.e(TAG, "onActivityResult: case 1");
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String reslut = bundle.getString("result");
                    if (reslut.length() > 20) {
                        //UIHelper.ToastMessageNetError(context, R.string.error_input_long);
                        Toast.makeText(this, R.string.error_input_long, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.e("qrcode:", "pw=====" + bundle.getString("result"));
                    initActivityEditText.setText(bundle.getString("result"));
                    activityInitEditPasswd.setText("123456");
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
        if (position == ActivityCode.WANT && wantCount.get() == 0) {
            Log.e(TAG, "myHandler: wantCount.get()===" + wantCount.get());
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
                    //返回成功之后
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
                    MyToast.Toast(context, R.string.activity_init_login_error_len);
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

            saveAutoLoginData();

        }

        // 申请与子服务器成功后会走这个方法，通过TCP方法与服务器连接
        if (position == ActivityCode.RelayMsg) {
            Log.e(TAG, "onSuccess: " + "申请与子服务器成功");
            RelayMsgRsp relayMsgRsp = (RelayMsgRsp) object;
//            Log.e(TAG, "myHandler: " + relayMsgRsp.getIP());
//            Log.e(TAG, "myHandler: " + relayMsgRsp.getPORT());
//            byte[] uuid = relayMsgRsp.getUUID();
//            for (byte b :
//                    uuid) {
//                Log.e(TAG, "myHandler: " + b);
//            }
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

                saveAutoLoginData();
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

        }

        if (position == ActivityCode.RelayMsg) {
            Log.e(TAG, "myError: 访问子服务器失败");
            Login.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //打洞失败则申请子服务器
                    MyPersonalProgressDIalog.getInstance(Login.this).dissmissProgress();
                    Toast.makeText(Login.this, "访问子服务器失败", Toast.LENGTH_SHORT).show();
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
     * 保存自动登录的数据
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
