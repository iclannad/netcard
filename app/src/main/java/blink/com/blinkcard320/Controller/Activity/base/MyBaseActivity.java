package blink.com.blinkcard320.Controller.Activity.base;

import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;

import blink.com.blinkcard320.Moudle.skin.SkinConfig;
import blink.com.blinkcard320.Tool.Utils.SharedPrefsUtils;

/**
 * Created by Administrator on 2017/3/21.
 */
abstract public class MyBaseActivity extends BaseActivity implements OnDirPath {

    public void initSkinConfig() {
        int skinValue = SharedPrefsUtils.getIntegerPreference(this, SkinConfig.SKIN_CONFIG, SkinConfig.SKIN_DEFAULT_VALUE);
        setTopColor(skinValue);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initSkinConfig();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    // 接口方法
    @Override
    public void update() {

    }
}
