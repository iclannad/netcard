package blink.com.blinkcard320.Controller.Activity;

import android.graphics.Color;
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

import com.example.administrator.data_sdk.CommonIntent;
import com.example.administrator.ui_sdk.Applications;
import com.example.administrator.ui_sdk.DensityUtil;
import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;

import java.util.concurrent.atomic.AtomicInteger;

import blink.com.blinkcard320.Controller.ActivityCode;
import blink.com.blinkcard320.Controller.NetCardController;
import blink.com.blinkcard320.Moudle.Comment;
import blink.com.blinkcard320.Moudle.skin.SkinConfig;
import blink.com.blinkcard320.R;
import blink.com.blinkcard320.Tool.System.MyToast;
import blink.com.blinkcard320.Tool.Thread.HandlerImpl;
import blink.com.blinkcard320.Tool.Utils.SharedPrefsUtils;
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


//    public static int wantCount = 0;
//    public static int helloCount = 0;

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

    /**
     * Start()
     */
    @Override
    public void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.netcard_login, null);

        setTileBar(0);


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

        DensityUtil.setHeight(loginEditRelative, BaseActivity.height / 5 * 3);
        DensityUtil.setHeight(loginScanRelative, BaseActivity.height / 5 * 2);

        setContent(view);

        // 调用代码用
        initActivityEditText.setText("112233445566");
        activityInitEditPasswd.setText("123456");

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

            startWantRequest();
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
            return;
        }

        if (password.length() == 0) {
            MyToast.Toast(context, R.string.error_input_null_userpw);
            return;
        }

        if (ID.length() >= 48) {
            MyToast.Toast(context, R.string.error_user_long);
            return;
        }
        if (password.length() > 15) {
            MyToast.Toast(context, R.string.error_pw_long);
            return;
        }
        //首先获取内外网IP和端口
        NetCardController.WANT(ID, password, this);
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
            Log.e(TAG, "onSuccess: " + "开始申请与子服务器进行连接");
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
            //打洞失败则申请子服务器
            NetCardController.RelayMsg(this);
            Log.e(TAG, "myHandler: " + "我要开始申请进行服务器连接");
        }

        if (position == ActivityCode.WANT) {

        }

        if (position == ActivityCode.RelayMsg) {

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
