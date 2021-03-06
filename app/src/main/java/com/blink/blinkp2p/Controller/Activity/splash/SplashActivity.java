package com.blink.blinkp2p.Controller.Activity.splash;

import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blink.blinkp2p.Controller.Activity.LeadActivity;
import com.blink.blinkp2p.Controller.Activity.login.Login;
import com.blink.blinkp2p.R;
import com.blink.blinkp2p.Tool.System.Tools;
import com.blink.blinkp2p.View.MyPersonalProgressDIalog;
import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;

import java.io.File;

import com.blink.blinkp2p.Moudle.Comment;
import com.blink.blinkp2p.Moudle.skin.SkinConfig;


import com.blink.blinkp2p.Tool.Utils.SharedPrefsUtils;

/**
 * Created by Administrator on 2017/4/5.
 */
public class SplashActivity extends BaseActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private View view;
    private RelativeLayout splash_linear;
    //    private Timer timer;

    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;
    private static final long SPLASH_DELAY_MILLIS = 1000;
    public final static int VersionNoUpdate = 1;
    public final static int VersionUpdate = 2;
    public final static int VersionOvertime = 3;

    public static final String SHAREDPREFERENCES_NAME = "first_pref";

    CheckVersionThread cvt;
    private TextView splashLoad = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME:
                    goHome();
                    break;
                case GO_GUIDE:
                    goGuide();
                    break;
                case 888:
                    MyInit();
                    break;
                case SplashActivity.VersionNoUpdate:
                    handler.sendEmptyMessage(888);
                    break;
                case SplashActivity.VersionUpdate:
                    MyPersonalProgressDIalog.getInstance(SplashActivity.this).setContent("从服务器下载文件中……").showProgressDialog();
                    cvt.downloadApk();
                    break;
                case SplashActivity.VersionOvertime:
                    handler.sendEmptyMessage(888);
                    break;
            }
        }
    };

    /**
     * handler 发送888信号所对应的操作
     */
    private void MyInit() {
        handler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
    }

    private boolean isFirstEnter = true;

    // 进入登录页面或者引导页
    private void goHome() {

        //isFirstEnter = Tools.isFirstRunApplication(SplashActivity.this);
        // 为了兼容旧版本的数据
        if (Tools.isFirstRunApplication(SplashActivity.this)) {
            goGuide();
            return;
        }
        splashLoad.setText("加载完成");
        Intent intent = new Intent(SplashActivity.this, Login.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }

    private void goGuide() {
        Intent intent = new Intent(SplashActivity.this, LeadActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }


    /**
     * Start()
     */
    @Override
    public void init() {
        // 不设置标题栏
        setTileBar(0);
        setFitWindows(true);
        view = LayoutInflater.from(this).inflate(R.layout.activity_splash, null);
        splash_linear = (RelativeLayout) view.findViewById(R.id.splash_Linear);
        splashLoad = (TextView) view.findViewById(R.id.splashLoad);
        setContent(view);

        //isFirstEnter = SharedPrefsUtils.getBooleanPreference(this, SHAREDPREFERENCES_NAME, true);

        /**-----------------------------------------------------------------------------------------*/
        /**
         * 此处为了兼容旧版本才这么写
         */
        String skinString = Tools.ReadSkinConfig(context);
        Log.e(TAG, "init: skinString===" + skinString);
        if (skinString != null) {
            int position = Integer.parseInt(skinString);
            Log.e(TAG, "init: 皮肤设置position===" + position);
            // 把值存放在本地中
            SharedPrefsUtils.setIntegerPreference(this, SkinConfig.SKIN_CONFIG, SkinConfig.skinArray[position]);
            SharedPrefsUtils.setIntegerPreference(this, SkinConfig.SKIN_SELECT_ICON, position);
        }
        /**-----------------------------------------------------------------------------------------*/

        //　设置皮肤
        int skinValue = SharedPrefsUtils.getIntegerPreference(this, SkinConfig.SKIN_CONFIG, SkinConfig.SKIN_DEFAULT_VALUE);
        splash_linear.setBackgroundResource(skinValue);
        setTopColor(skinValue);


        // 创建需要的文件夹
        String downFileDir = SharedPrefsUtils.getStringPreference(this, Comment.DOWNFILE);
        if (downFileDir == null) {
            downFileDir = Environment.getExternalStorageDirectory().toString() + "/B_LINK_P2P_files_down";
            SharedPrefsUtils.setStringPreference(this, Comment.DOWNFILE, downFileDir);
        }
        File fileDown = new File(downFileDir);
        if (!fileDown.exists()) {
            fileDown.mkdirs();
            Log.e(TAG, "init: 已经创建了文件下载的文件夹");
        }

        String picFileDir = SharedPrefsUtils.getStringPreference(this, Comment.PICTUREFILE);
        if (picFileDir == null) {
            picFileDir = Environment.getExternalStorageDirectory().toString() + "/B_LINK_P2P_files_picture";
            SharedPrefsUtils.setStringPreference(this, Comment.PICTUREFILE, picFileDir);
        }
        File filePic = new File(picFileDir);
        if (!filePic.exists()) {
            filePic.mkdirs();
            Log.e(TAG, "init: 已经创建了文件上传的文件夹");
        }

        cvt = new CheckVersionThread(handler, this);
    }

}
