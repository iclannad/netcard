package com.blink.blinkp2p.View.pop;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.blink.blinkp2p.Controller.Activity.login.LoginBeanGson;
import com.blink.blinkp2p.Moudle.Comment;
import com.blink.blinkp2p.R;
import com.blink.blinkp2p.Tool.Utils.SharedPrefsUtils;
import com.blink.blinkp2p.View.pop.Dao.LoginDAO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by Administrator on 2017/4/7.
 */
public class DropDownPopWindows {
    private static final String TAG = DropDownPopWindows.class.getSimpleName();

    private ArrayList<String> datas = new ArrayList<String>();

    private OptionsAdapter optionsAdapter = null;
    private Context context;
    private Handler mhandler;
    private int pwidth;
    private boolean isinit = false;
    private ListView listView;
    private int height;

    LoginDAO loginDAO = null;

    private PopupWindow selectPopupWindow = null;

    public DropDownPopWindows(Context context, Handler handler, int pwidth) {
        this.context = context;
        this.mhandler = handler;
        this.pwidth = pwidth;
        this.height = height;
        loginDAO = new LoginDAO(context);
    }

    private ArrayList<String> getHistroyDataFromSharedPrefs() {
        ArrayList<String> list = new ArrayList<String>();
        String userlist = SharedPrefsUtils.getStringPreference(context, Comment.LOGINDATA);
        Log.e(TAG, "getHistroyDataFromSharedPrefs: userlist===" + userlist);
        if (userlist == null) {
            return list;
        }

        Gson g = new Gson();
        Type lt = new TypeToken<List<LoginBeanGson>>() {
        }.getType();
        try {
            // ArrayList
            ArrayList<LoginBeanGson> arraylist = g.fromJson(userlist, lt);
            for (int i = 0; i < arraylist.size(); i++) {
                LoginBeanGson t = arraylist.get(i);

                list.add(t.getUsername());
            }
        } catch (Exception e) {

            LoginBeanGson l = g.fromJson(userlist, LoginBeanGson.class);
            list.add(l.getUsername());
        }
        return list;
    }

    public void initPopuWindow(EditText mclearedittext,
                               EditText mEditTextPasswd
    ) {
        isinit = true;
//        if (mclearedittext.getText().length() <= 0)
        // 此处应该模拟从数据库中读取数据
        //datas = loginDAO.QueryDataAll();
        datas = getHistroyDataFromSharedPrefs();

        View loginwindow = (View) LayoutInflater.from(context).inflate(
                R.layout.options, null);
        listView = (ListView) loginwindow.findViewById(R.id.list);

        optionsAdapter = new OptionsAdapter(context, mhandler, datas);
        listView.setAdapter(optionsAdapter);

        selectPopupWindow = new PopupWindow(loginwindow, pwidth,
                ActionBar.LayoutParams.WRAP_CONTENT, true);

        selectPopupWindow.setOutsideTouchable(true);

        selectPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        // popupWindwShowing();
    }

    public void popupWindwShowing(EditText mclearedittext) {
        selectPopupWindow.showAsDropDown(mclearedittext, 0, (int) context.getResources().getDimension(R.dimen.ItemSpace));
    }

    public void dismiss() {
        selectPopupWindow.dismiss();
    }

    public String getdatas(int position) {
        try {

            return datas.get(position);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    public void removedatasItem(int position) {
        try {
            // 此处应该删除数据库中的数据
            //loginDAO.delete(datas.get(position));

            datas.remove(position);
            optionsAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void AdapterUpdate() {
        optionsAdapter.notifyDataSetChanged();
    }

    public boolean IsInit() {
        return this.isinit;
    }


}
