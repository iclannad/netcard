package com.blink.blinkp2p.View;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.ui_sdk.DensityUtil;
import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;

import java.lang.reflect.Field;

import blink.com.blinkcard320.R;

/**
 * Created by Administrator on 2017/3/20.
 */
public class MyDialog extends Dialog implements View.OnClickListener {

    private Context context = null;
    private String[] number = null;


    public MyDialog(Context context, int themeResId) {
        super(context, themeResId);

        this.context = context;
        init();
    }

    public MyDialog(Context context) {
        this(context, 0);
    }

    private RelativeLayout shareRelayout;
    private TextView shareText;
    private TextView shareCancal = null;
    private TextView shareEnter = null;
    private EditText shareEdit = null;
    private EditText shareEdit1 = null;


    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.mydialog, null);

        number = new String[2];

        shareRelayout = (RelativeLayout) view.findViewById(R.id.shareRelayout);
        shareText = (TextView) view.findViewById(R.id.shareText);
        shareCancal = (TextView) view.findViewById(R.id.shareCancal);
        shareEnter = (TextView) view.findViewById(R.id.shareEnter);
        shareEdit = (EditText) view.findViewById(R.id.shareEdit);
        shareEdit1 = (EditText) view.findViewById(R.id.shareEdit1);

        DensityUtil.setRelWidth(shareRelayout, BaseActivity.width / 3 * 2);

        this.setContentView(view);

        shareRelayout.setOnClickListener(this);
        shareCancal.setOnClickListener(this);
        shareEnter.setOnClickListener(this);
    }

    /**
     * 设置对话框是否消失
     *
     * @param isShow
     */
    public void DialogShow(boolean isShow) {
        Field field = null;
        try {
            setCancelText(context.getResources().getString(R.string.InstallLeftButExits));
            field = this.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true); //设置mShowing值，欺骗android系统
            field.set(this, isShow); //需要关闭的时候将这个参数设置为true 他就会自动关闭了
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public void setEnterText(String msg) {
        shareEnter.setText(msg);
    }

    public void setCancelText(String msg) {
        shareCancal.setText(msg);
    }

    /**
     * 获取对话框编辑框的文字
     *
     * @return
     */
    public String getEditText() {
        return shareEdit.getText().toString();
    }

    public void setThemeColor(int color){
        shareCancal.setTextColor(context.getResources().getColor(color));
        shareEnter.setTextColor(context.getResources().getColor(color));
    }

    /**
     * 获取对话框编辑框的文字
     *
     * @return
     */
    public String getEditText1() {
        return shareEdit1.getText().toString();
    }

    /**
     * 设置编辑框的内容
     *
     * @param result
     */
    public void setEditText(String result) {
        if (result != null && result.length() > 0 && !"".equals(result))
            shareEdit.setText(result);
    }

    /**
     * 设置编辑框的备份
     *
     * @param result
     */
    public void setEditText1(String result) {
        if (result != null && result.length() > 0 && !"".equals(result)) {
            shareEdit1.setText(result);
            shareEdit1.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置编辑框的hint内容
     *
     * @param result
     */
    public void setEditHint(String result) {
        if (result != null && result.length() > 0 && !"".equals(result))
            shareEdit.setHint(result);
    }

    public void setEditVisiable(boolean isVisiable) {
        if (isVisiable)
            shareEdit.setVisibility(View.VISIBLE);
        else
            shareEdit.setVisibility(View.GONE);
    }

    public void setEditVisiable1(boolean isVisiable) {
        if (isVisiable)
            shareEdit1.setVisibility(View.VISIBLE);
        else
            shareEdit1.setVisibility(View.GONE);
    }

    /**
     * 设置只显示一个按钮
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setNormalButton() {
        shareCancal.setVisibility(View.GONE);
        shareEnter.setBackground(context.getResources().getDrawable(R.drawable.button_enter_normal));
    }

    /**
     * 设置编辑框的hint内容
     *
     * @param result
     */
    public void setEditHint1(String result) {
        if (result != null && result.length() > 0 && !"".equals(result)) {
            shareEdit1.setHint(result);
            shareEdit1.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 将数据传输对话框进行显示
     *
     * @param result
     */
    public void setShareText(String result) {
        shareText.setVisibility(View.VISIBLE);
        shareText.setText(result);
        shareEdit.setVisibility(View.GONE);
    }


    public void setEditType(int type) {
        shareEdit.setInputType(type);
    }


    public void setEditMax(int Max) {
        shareEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Max)});
    }


    public void setEditMax1(int Max) {
        shareEdit1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Max)});
    }

    public void setEditdigits(String digits) {
        shareEdit.setKeyListener(DigitsKeyListener.getInstance(digits));
    }

    public void setEditdigits1(String digits) {
        shareEdit1.setKeyListener(DigitsKeyListener.getInstance(digits));
    }


    public EditText getShareEdit() {
        return shareEdit;
    }

    public EditText getShareEdit1() {
        return shareEdit1;
    }

    public void setEditType1(int type) {
        shareEdit1.setInputType(type);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shareCancal:
                if (dialogClick == null)
                    this.dismiss();
                else
                    dialogClick.Canel(position);
                break;
            case R.id.shareEnter:
                if (dialogClick != null)
                    dialogClick.Enter(position);
                break;
        }
    }

    private DialogClick dialogClick = null;
    private int position = 0;

    /**
     * 对话框点击事件 的外部接口
     *
     * @param dialogClick
     */
    public void DialogClick(DialogClick dialogClick, int position) {
        this.dialogClick = dialogClick;
        this.position = position;
    }

    /**
     * 获取两个数字选择器的接口
     *
     * @return
     */
    public String[] getNumber() {
        return number;
    }

}
