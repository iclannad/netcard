package com.blink.blinkp2p.Controller.Activity.slidingmenu.settings;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.blink.blinkp2p.Controller.Activity.base.MyBaseActivity;
import com.blink.blinkp2p.Moudle.Comment;
import com.blink.blinkp2p.R;
import com.blink.blinkp2p.Tool.Adapter.DeviceGridViewAdapter;
import com.blink.blinkp2p.Tool.Utils.SharedPrefsUtils;
import com.blink.blinkp2p.Tool.Utils.UIHelper;
import com.blink.blinkp2p.View.MGridView;

/**
 * Created by Administrator on 2017/3/20.
 */
public class QuickStartActivity extends MyBaseActivity implements AdapterView.OnItemClickListener {

    private MGridView mgridview;
    private DeviceGridViewAdapter mdevicegridadapter;

    private int[] image = {R.mipmap.icon_restart, R.mipmap.icon_timeshowdown,
            R.mipmap.icon_shutdown, R.mipmap.icon_lockdevices,
            R.mipmap.icon_changepw, R.mipmap.icon_update,
            R.mipmap.icon_videofile, 0, 0};

    private String[] str;
    private ArrayList<Map<String, Object>> list = new ArrayList<>();

    /**
     * Start()
     */
    @Override
    public void init() {
        initData();
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        setTileBar(R.layout.base_top);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.xc_arcmenu_view_demo, null);

        // activity的布局
        setContent(view);

        // 标题的文字
        setTitle(getResources().getString(R.string.quit_start));
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

        mgridview = (MGridView) findViewById(R.id.gridview);

        list = new ArrayList<>();
        for (int i = 0; i < str.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("text", str[i]);
            map.put("image", image[i]);
            list.add(map);

        }

        mdevicegridadapter = new DeviceGridViewAdapter(this, list);
        mgridview.setAdapter(mdevicegridadapter);
        mgridview.setOnItemClickListener(this);
    }

    /**
     * 初始化Data
     */
    private void initData() {
        str = new String[]{
                getResources().getString(R.string.instruction_reboot),
                getResources().getString(R.string.instruction_wshutdown),
                getResources().getString(R.string.instruction_shutdown),
                getResources().getString(R.string.instruction_lockingdevice),
                getResources().getString(R.string.instruction_AlterPassword),
                getResources().getString(R.string.instruction_getupdate),
                getResources().getString(R.string.instruction_Camera), "", ""};
    }


    /**
     * GridView onclick
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String tip = QuickStartActivity.this.getResources().getString(R.string.quitstart);

        switch (position) {
            case 0:
                //SharedPrefsUtils.setIntegerPreference(context, Comment.QUICK_START, Comment.REBOOT);
                SharedPrefsUtils.setStringPreference(context, Comment.ICON_QUITSTART, Comment.ICON_RESTART);
                tip += QuickStartActivity.this.getResources().getString(R.string.instruction_reboot);
                UIHelper.ToastSetSuccess(QuickStartActivity.this, tip);
                break;
            case 1:
                //SharedPrefsUtils.setIntegerPreference(context, Comment.QUICK_START, Comment.SET_TIME_SHUTDOWN);
                SharedPrefsUtils.setStringPreference(context, Comment.ICON_QUITSTART, Comment.ICON_TIMESHUTDOWN);
                tip += QuickStartActivity.this.getResources().getString(R.string.instruction_wshutdown);
                UIHelper.ToastSetSuccess(QuickStartActivity.this, tip);
                break;
            case 2:
                //SharedPrefsUtils.setIntegerPreference(context, Comment.QUICK_START, Comment.SHUTDOWN);
                SharedPrefsUtils.setStringPreference(context, Comment.ICON_QUITSTART, Comment.ICON_SHUTDOWN);
                tip += QuickStartActivity.this.getResources().getString(R.string.instruction_shutdown);
                UIHelper.ToastSetSuccess(QuickStartActivity.this, tip);
                break;
            case 3:
                //SharedPrefsUtils.setIntegerPreference(context, Comment.QUICK_START, Comment.LOCK_PC);
                SharedPrefsUtils.setStringPreference(context, Comment.ICON_QUITSTART, Comment.ICON_LOCLPC);
                tip += QuickStartActivity.this.getResources().getString(R.string.lock_pc);
                UIHelper.ToastSetSuccess(QuickStartActivity.this, tip);
                break;
            case 4:
                //SharedPrefsUtils.setIntegerPreference(context, Comment.QUICK_START, Comment.ALTER_PWD);
                SharedPrefsUtils.setStringPreference(context, Comment.ICON_QUITSTART, Comment.ICON_CHANGEPASSWD);
                tip += QuickStartActivity.this.getResources().getString(R.string.instruction_AlterPassword);
                UIHelper.ToastSetSuccess(QuickStartActivity.this, tip);
                break;
            case 5:
                //SharedPrefsUtils.setIntegerPreference(context, Comment.QUICK_START, Comment.GET_UPDATE);
                SharedPrefsUtils.setStringPreference(context, Comment.ICON_QUITSTART, Comment.ICON_GETUPDATE);
                tip += QuickStartActivity.this.getResources().getString(R.string.update);
                UIHelper.ToastSetSuccess(QuickStartActivity.this, tip);
                break;
            case 6:
                //SharedPrefsUtils.setIntegerPreference(context, Comment.QUICK_START, Comment.CAMERA);
                SharedPrefsUtils.setStringPreference(context, Comment.ICON_QUITSTART, Comment.ICON_CAMERA);
                tip += QuickStartActivity.this.getResources().getString(R.string.Camera);
                UIHelper.ToastSetSuccess(QuickStartActivity.this, tip);
                break;
            default:
//                SharedPrefsUtils.setIntegerPreference(context, Comment.QUICK_START, Comment.DEFAULT);
//                tip = QuickStartActivity.this.getResources().getString(R.string.reset_quick_start);
//                UIHelper.ToastSetSuccess(QuickStartActivity.this, tip);
                break;
        }
    }
}
