package com.blink.blinkp2p.Controller.Activity.slidingmenu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blink.blinkp2p.Controller.Activity.base.MyBaseActivity;
import com.blink.blinkp2p.Controller.ActivityCode;
import com.blink.blinkp2p.R;
import com.blink.blinkp2p.Tool.Thread.HandlerImpl;

import com.blink.blinkp2p.Controller.Activity.login.Login;
import com.blink.blinkp2p.Controller.NetCardController;
import com.blink.blinkp2p.Moudle.Comment;
import com.blink.blinkp2p.Moudle.skin.SkinConfig;

import com.blink.blinkp2p.Tool.Utils.SharedPrefsUtils;
import com.blink.blinkp2p.Tool.Utils.UIHelper;
import com.blink.blinkp2p.View.MyProgressDIalog;
import com.blink.blinkp2p.application.MyApplication;

import smart.blink.com.card.bean.ChangePwdRsp;

/**
 * Created by Administrator on 2017/3/20.
 */
public class AlterUserPWDActivity extends MyBaseActivity implements HandlerImpl {

    private static final String TAG = AlterUserPWDActivity.class.getSimpleName();
    private View view;

    private EditText mEditOriginal;
    private EditText mPassFirst;
    private EditText mPassSecond;
    private Button mBtnOk;
    public static ProgressDialog progress;

    /**
     * Start()
     */
    @Override
    public void init() {
        setTileBar(R.layout.base_top);

        LayoutInflater inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.activity_change_passwd, null);

        // activity的布局
        setContent(view);

        MyApplication.getInstance().addActivity(this);

        // 标题的文字
        setTitle(getResources().getString(R.string.change_passwd));
        // 左边的文字
        setLeftTitle(getResources().getString(R.string.back));
        // 右边的文字隐藏
        setRightTitleVisiable(false);
        // 标题设颜色
        setTopTitleColor(R.color.WhiteSmoke);
        // 左边文字设颜色
        setLeftTitleColor(R.color.WhiteSmoke);

        // 标题栏设颜色   (到时会修改成可设置皮肤)
        setTopColor(R.color.actionbarcolor);

        initOnclickEvent();
    }

    /**
     * 初始化点击事件
     */
    private void initOnclickEvent() {
        mEditOriginal = (EditText) findViewById(R.id.change_passwd_original);
        mPassFirst = (EditText) findViewById(R.id.change_passwd_new_first);
        mPassSecond = (EditText) findViewById(R.id.change_passwd_new_second);
        mEditOriginal.setFocusable(true);
        mEditOriginal.requestFocus();
        mBtnOk = (Button) findViewById(R.id.change_passwd_btn);

        progress = new ProgressDialog(this);
        // 按钮设置背景颜色
        //mBtnOk.setBackgroundDrawable(getResources().getDrawable(SkinConfig.getInstance().getButtonColor()));
        mBtnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String original = mEditOriginal.getText().toString();
                String first = mPassFirst.getText().toString();
                String second = mPassSecond.getText().toString();
                if (original.length() == 0 || first.length() == 0 || second.length() == 0) {
                    UIHelper.ToastMessageNetError(AlterUserPWDActivity.this, R.string.error_input_null_userpw);
                    return;
                }
                if (original.length() > 15 || first.length() > 15 || second.length() > 15) {
                    UIHelper.ToastMessageNetError(AlterUserPWDActivity.this, R.string.error_input_long);
                    return;
                }
                if (first.length() < 6 || second.length() < 6) {
                    UIHelper.ToastMessageNetError(AlterUserPWDActivity.this, R.string.error_input_shortsix);
                    return;
                }
                if (!first.equals(second)) {
                    UIHelper.ToastMessageNetError(AlterUserPWDActivity.this, R.string.error_pw_noeq);
                    return;
                }

                // 提示
                MyProgressDIalog myProgressDIalog = new MyProgressDIalog(AlterUserPWDActivity.this);
                myProgressDIalog.init();
                myProgressDIalog.showProgressDialog();
                myProgressDIalog.setmsg(R.string.change_passwd);

                //NetCardController.ChangePwd("112233445566", "123456", "654321", AlterUserPWDActivity.this);
                String userId = MyApplication.userName;
                NetCardController.ChangePwd(userId, original, second, AlterUserPWDActivity.this);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // 添加皮肤的设置
        int skinValue = SharedPrefsUtils.getIntegerPreference(this, SkinConfig.SKIN_CONFIG, SkinConfig.SKIN_DEFAULT_VALUE);
        mBtnOk.setBackgroundResource(skinValue);
    }

    /**
     * 处理线程返回的更新界面
     *
     * @param position
     * @param object
     */
    @Override
    public void myHandler(int position, Object object) {
        if (position == ActivityCode.ChangePwd) {
            ChangePwdRsp changePwdRsp = (ChangePwdRsp) object;
            int value = changePwdRsp.getSuccess();
            // value: 0表示成功，其它表示原密码错误
            if (value == 0) {
                MyProgressDIalog.dissmissProgress();
                Toast.makeText(this, this.getResources().getString(R.string.main_handler_change_sucess), Toast.LENGTH_SHORT).show();
                // 重新跳到登录界面
                startActivity(new Intent(this, Login.class));
                // 重新登录
                SharedPrefsUtils.setBooleanPreference(this, Comment.IS_RELOGIN, true);
                // 如果修改密码成功以后，重新登录必须清空原来的密码
                SharedPrefsUtils.setBooleanPreference(this, Comment.IS_NEED_CLEAR_OLDER_PWD, true);

                MyApplication.getInstance().exit();
            } else {
                MyProgressDIalog.dissmissProgress();
                UIHelper.ToastMessageNetError(this, R.string.main_handler_original_error);
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

    }
}
