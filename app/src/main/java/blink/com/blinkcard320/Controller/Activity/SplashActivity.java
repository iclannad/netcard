package blink.com.blinkcard320.Controller.Activity;

import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import blink.com.blinkcard320.Controller.Activity.base.MyBaseActivity;
import blink.com.blinkcard320.Moudle.Comment;
import blink.com.blinkcard320.Moudle.skin.SkinConfig;
import blink.com.blinkcard320.R;
import blink.com.blinkcard320.Tool.Utils.SharedPrefsUtils;

/**
 * Created by Administrator on 2017/4/5.
 */
public class SplashActivity extends BaseActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private View view;
    private RelativeLayout splash_linear;
    private Timer timer;
    public static final String SHAREDPREFERENCES_NAME = "first_pref";

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
        setContent(view);

        final boolean isFirstEnter = SharedPrefsUtils.getBooleanPreference(this, SHAREDPREFERENCES_NAME, true);

        int skinValue = SharedPrefsUtils.getIntegerPreference(this, SkinConfig.SKIN_CONFIG, SkinConfig.SKIN_DEFAULT_VALUE);
        splash_linear.setBackgroundResource(skinValue);
        setTopColor(skinValue);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isFirstEnter) {
                    Intent intent = new Intent(SplashActivity.this, LeadActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, Login.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }

                timer.cancel();
            }
        }, 1500, 1500);

        // 创建需要的文件夹
        String downFileDir = SharedPrefsUtils.getStringPreference(this, Comment.DOWNFILE);
        if (downFileDir == null) {
            downFileDir = Environment.getExternalStorageDirectory().toString();
        }
        File fileDown = new File(downFileDir);
        if (!fileDown.exists()) {
            fileDown.mkdirs();
            Log.e(TAG, "init: 已经创建了文件下载的文件夹");
        }

        String picFileDir = SharedPrefsUtils.getStringPreference(this, Comment.PICTUREFILE);
        if (picFileDir == null) {
            picFileDir = Environment.getExternalStorageDirectory().toString();
        }
        File filePic = new File(picFileDir);
        if (!filePic.exists()) {
            filePic.mkdirs();
            Log.e(TAG, "init: 已经创建了文件上传的文件夹");
        }

    }

}
