package com.blink.blinkp2p.Controller.Activity.slidingmenu;

import android.view.LayoutInflater;
import android.view.View;

import com.blink.blinkp2p.R;
import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;
import com.example.administrator.ui_sdk.MyWebClient;
import com.example.administrator.ui_sdk.View.MyWebView;


/**
 * Created by Administrator on 2017/3/20.
 */
public class ShopActivity extends BaseActivity {

    private MyWebView mWebView;
    public final static String test = "http://www.lbmall.com.cn";
    String Blink_shop = "https://blink.tmall.com/";

    /**
     * Start()
     */
    @Override
    public void init() {

        setTileBar(0);

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.activity_shop, null);

        // activity的布局
        setContent(view);

        mWebView = (MyWebView) findViewById(R.id.activity_webview_shop);

        mWebView.setWebView(Blink_shop, new MyWebClient() {
            @Override
            public void Loading() {

            }

            @Override
            public void Ending() {

            }
        });
    }
}
