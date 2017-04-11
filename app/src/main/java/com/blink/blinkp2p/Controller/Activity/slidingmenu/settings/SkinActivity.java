package com.blink.blinkp2p.Controller.Activity.slidingmenu.settings;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.blink.blinkp2p.Moudle.skin.SkinBean;
import com.blink.blinkp2p.Moudle.skin.SkinConfig;
import com.blink.blinkp2p.R;
import com.blink.blinkp2p.Tool.Adapter.SkinAdapter;
import com.blink.blinkp2p.Tool.System.Tools;
import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;

import java.util.ArrayList;


import com.blink.blinkp2p.Tool.Utils.SharedPrefsUtils;

/**
 * Created by Administrator on 2017/3/20.
 */
public class SkinActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private GridView skin_gridView = null;
    private SkinAdapter adapter = null;
    private ArrayList<Object> list = null;

    /**
     * Start()
     */
    @Override
    public void init() {
        setTileBar(R.layout.base_top);

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.skin_main, null);

        // activity的布局
        setContent(view);

        // 标题的文字
        setTitle(getResources().getString(R.string.setting_skin));
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
        //setTopColor(R.color.actionbarcolor);

        list = new ArrayList<>();
        // 标题栏设颜色
        initSkinConfig();


        skin_gridView = (GridView) view.findViewById(R.id.skin_gridView);
        //setListView(0);
        adapter = new SkinAdapter(list, this);
        skin_gridView.setAdapter(adapter);
        skin_gridView.setOnItemClickListener(this);

    }

    /**
     * 初始化皮肤设置
     */
    public void initSkinConfig() {
        int skinValue = SharedPrefsUtils.getIntegerPreference(this, SkinConfig.SKIN_CONFIG, SkinConfig.SKIN_DEFAULT_VALUE);
        setTopColor(skinValue);
        int position = SharedPrefsUtils.getIntegerPreference(this, SkinConfig.SKIN_SELECT_ICON, SkinConfig.SKIN_DEFAULT_SKIN_SELECT_ICON_VALUE);
        setListView(position);
    }

    /**
     * 给GridView设数据
     *
     * @param position
     */
    private void setListView(int position) {
        list.clear();
        for (int i = 0; i < 4; i++) {
            if (i == position) {
                list.add(getItem(SkinConfig.skinArray[i], R.mipmap.skin));
                continue;
            }
            list.add(getItem(SkinConfig.skinArray[i], 0));
        }
    }

    public Object getItem(int colorBack, int colorID) {
        SkinBean skinBean = new SkinBean();

        skinBean.setSkinBackColor(colorBack);
        skinBean.setSkinIDColor(colorID);

        return skinBean;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 条目点击时显示勾选图片
        setListView(position);
        adapter.setChangeData(list);

        // 设置标题栏颜色
        setTopColor(SkinConfig.skinArray[position]);

        // 把值存放在本地中
        SharedPrefsUtils.setIntegerPreference(this, SkinConfig.SKIN_CONFIG, SkinConfig.skinArray[position]);
        SharedPrefsUtils.setIntegerPreference(this, SkinConfig.SKIN_SELECT_ICON, position);

        // 兼容旧版本
        Tools.WriteSkinConfig(this, position);
    }
}
