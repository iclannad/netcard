package com.blink.blinkp2p.View.pop;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;

import blink.com.blinkcard320.R;
import com.blink.blinkp2p.View.pop.Dao.LoginDAO;

/**
 * Created by Administrator on 2017/4/7.
 */
public class DropDownPopWindows {
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

    public void initPopuWindow(EditText mclearedittext,
                               EditText mEditTextPasswd
    ) {
        isinit = true;
//        if (mclearedittext.getText().length() <= 0)
        // 此处应该模拟从数据库中读取数据
        datas = loginDAO.QueryDataAll();

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
            loginDAO.delete(datas.get(position));
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
