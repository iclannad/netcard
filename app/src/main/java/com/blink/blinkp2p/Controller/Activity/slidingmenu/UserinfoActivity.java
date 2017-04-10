package com.blink.blinkp2p.Controller.Activity.slidingmenu;

import android.view.LayoutInflater;
import android.view.View;

import com.blink.blinkp2p.Controller.Activity.base.MyBaseActivity;
import com.example.administrator.ui_sdk.MyWebClient;
import com.example.administrator.ui_sdk.View.MyWebView;

import blink.com.blinkcard320.R;

/**
 * Created by Administrator on 2017/3/20.
 */
public class UserinfoActivity extends MyBaseActivity {

    private MyWebView userinfoWebView = null;

    /**
     * Start()
     */
    @Override
    public void init() {
        setTileBar(R.layout.base_top);

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.activity_userinfo, null);

        // activity的布局
        setContent(view);

        // 标题的文字
        setTitle(getResources().getString(R.string.userinfo));
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

        userinfoWebView = (MyWebView) findViewById(R.id.userinfoWebView);
        userinfoWebView.setWebView("http://app.b-link.net.cn/wifi/help.html", new MyWebClient() {
            @Override
            public void Loading() {

            }

            @Override
            public void Ending() {

            }
        });

    }
}
