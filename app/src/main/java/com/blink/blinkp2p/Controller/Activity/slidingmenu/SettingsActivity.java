package com.blink.blinkp2p.Controller.Activity.slidingmenu;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.blink.blinkp2p.Controller.Activity.base.MyBaseActivity;
import com.blink.blinkp2p.Controller.Activity.slidingmenu.settings.Filelook;
import com.blink.blinkp2p.Controller.Activity.slidingmenu.settings.FilelookPC;
import com.blink.blinkp2p.Controller.Activity.slidingmenu.settings.QuickStartActivity;
import com.blink.blinkp2p.Controller.Activity.slidingmenu.settings.SkinActivity;

import com.blink.blinkp2p.Moudle.Comment;
import blink.com.blinkcard320.R;

/**
 * Created by Administrator on 2017/3/20.
 */
public class SettingsActivity extends MyBaseActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();
    private LinearLayout ll_down, ll_upload, ll_picturesave;
    private LinearLayout ll_quitstart;
    private LinearLayout ll_skin = null;

    /**
     * Start()
     */
    @Override
    public void init() {
        setTileBar(R.layout.base_top);

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.activity_settings, null);

        // activity的布局
        setContent(view);

        // 标题的文字
        setTitle(getResources().getString(R.string.settings_appsetting));
        // 左边的文字
        setLeftTitle(getResources().getString(R.string.back));
        // 右边的文字隐藏
        setRightTitleVisiable(false);
        // 标题设颜色
        setTopTitleColor(R.color.WhiteSmoke);
        // 左边文字设颜色
        setLeftTitleColor(R.color.WhiteSmoke);
        // 右边文字设颜色
        setRightTitleColor(R.color.WhiteSmoke);
        // 标题栏设颜色   (到时会修改成可设置皮肤)
        setTopColor(R.color.actionbarcolor);

        ll_down = (LinearLayout) findViewById(R.id.activity_ll_down);
        ll_upload = (LinearLayout) findViewById(R.id.activity_ll_upload);
        ll_picturesave = (LinearLayout) findViewById(R.id.activity_ll_picture);
        ll_quitstart = (LinearLayout) findViewById(R.id.ll_quitstart);
        ll_skin = (LinearLayout) findViewById(R.id.ll_skin);

        // 设置监听事件
        ll_down.setOnClickListener(this);
        ll_upload.setOnClickListener(this);
        ll_picturesave.setOnClickListener(this);
        ll_quitstart.setOnClickListener(this);
        ll_skin.setOnClickListener(this);

    }

    Intent intent = null;

    /**
     * 点击后跳到对应的activity
     *
     * @param v
     */
    @Override
    public void Click(View v) {
        switch (v.getId()) {
            case R.id.activity_ll_down:
                intent = new Intent(SettingsActivity.this, Filelook.class);
                intent.putExtra(Comment.FILETYPE, Comment.DOWNFILE);
                startActivity(intent);
                break;
            case R.id.activity_ll_upload:
                intent = new Intent(SettingsActivity.this, FilelookPC.class);
                startActivity(intent);
                break;
            case R.id.activity_ll_picture:
                intent = new Intent(SettingsActivity.this, Filelook.class);
                intent.putExtra(Comment.FILETYPE, Comment.PICTUREFILE);
                startActivity(intent);
                break;
            case R.id.ll_quitstart:
                intent = new Intent(SettingsActivity.this, QuickStartActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_skin:
                intent = new Intent(SettingsActivity.this, SkinActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
