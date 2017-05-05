package com.blink.blinkp2p.View;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;

/**
 * Created by Administrator on 2017/5/5.
 */
public class MyPersonalDialog extends android.app.Dialog {
    Context context;
    View view;
    View backView;
    String message;
    TextView messageTextView;
    String title;
    TextView titleTextView;

    ButtonFlat buttonAccept;
    ButtonFlat buttonCancel;

    String buttonCancelText = null;
    private String buttontext = "";
    View.OnClickListener onAcceptButtonClickListener;
    View.OnClickListener onCancelButtonClickListener;

    public MyPersonalDialog(Context context, String title, String message) {
        super(context, android.R.style.Theme_Translucent);
        this.context = context;// init Context
        this.message = message;
        this.title = title;
    }

    public MyPersonalDialog(Context context, String title, String message, String buttontext) {
        super(context, android.R.style.Theme_Translucent);
        this.context = context;// init Context
        this.message = message;
        this.title = title;
        this.buttontext = buttontext;
    }

    public MyPersonalDialog(Context context, String title, String message, String buttontext, String buttonCancelText) {
        super(context, android.R.style.Theme_Translucent);
        this.context = context;// init Context
        this.message = message;
        this.title = title;
        this.buttontext = buttontext;
        this.buttonCancelText = buttonCancelText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(com.gc.materialdesign.R.layout.dialog);
        view = (RelativeLayout) findViewById(com.gc.materialdesign.R.id.contentDialog);
        backView = (RelativeLayout) findViewById(com.gc.materialdesign.R.id.dialog_rootView);


        this.titleTextView = (TextView) findViewById(com.gc.materialdesign.R.id.title);
        setTitle(title);

        this.messageTextView = (TextView) findViewById(com.gc.materialdesign.R.id.message);
        setMessage(message);

        this.buttonAccept = (ButtonFlat) findViewById(com.gc.materialdesign.R.id.button_accept);
        if (buttontext != "") {
            this.buttonAccept.setText(buttontext);
        }
        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onAcceptButtonClickListener != null)
                    onAcceptButtonClickListener.onClick(v);
            }
        });

        if (buttonCancelText != null) {
            this.buttonCancel = (ButtonFlat) findViewById(com.gc.materialdesign.R.id.button_cancel);
            this.buttonCancel.setVisibility(View.VISIBLE);
            this.buttonCancel.setText(buttonCancelText);
            buttonCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                    if (onCancelButtonClickListener != null)
                        onCancelButtonClickListener.onClick(v);
                }
            });
        }
    }

    @Override
    public void show() {

        // TODO 鑷姩鐢熸垚鐨勬柟娉曞瓨鏍�		super.show();
        // set dialog enter animations
        super.show();

        view.startAnimation(
                AnimationUtils.loadAnimation(context,
                        com.gc.materialdesign.R.anim.dialog_main_show_amination)
        );
        backView.startAnimation(
                AnimationUtils.loadAnimation(
                        context,
                        com.gc.materialdesign.R.anim.dialog_root_show_amin));

    }

    // GETERS & SETTERS

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        messageTextView.setText(message);
    }

    public TextView getMessageTextView() {
        return messageTextView;
    }

    public void setMessageTextView(TextView messageTextView) {
        this.messageTextView = messageTextView;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        if (title == null)
            titleTextView.setVisibility(View.GONE);
        else {
            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(title);
        }
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public void setTitleTextView(TextView titleTextView) {
        this.titleTextView = titleTextView;
    }

    public ButtonFlat getButtonAccept() {
        return buttonAccept;
    }

    public void setButtonAccept(ButtonFlat buttonAccept) {
        this.buttonAccept = buttonAccept;
    }

    public ButtonFlat getButtonCancel() {
        return buttonCancel;
    }

    public void setButtonCancel(ButtonFlat buttonCancel) {
        this.buttonCancel = buttonCancel;
    }

    public void setOnAcceptButtonClickListener(
            View.OnClickListener onAcceptButtonClickListener) {
        this.onAcceptButtonClickListener = onAcceptButtonClickListener;
        if (buttonAccept != null)
            buttonAccept.setOnClickListener(onAcceptButtonClickListener);
    }

    public void setOnCancelButtonClickListener(
            View.OnClickListener onCancelButtonClickListener) {
        this.onCancelButtonClickListener = onCancelButtonClickListener;
        if (buttonCancel != null)
            buttonCancel.setOnClickListener(onCancelButtonClickListener);
    }

    @Override
    public void dismiss() {
        Animation anim = AnimationUtils.loadAnimation(context, com.gc.materialdesign.R.anim.dialog_main_hide_amination);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        MyPersonalDialog.super.dismiss();
                    }
                });

            }
        });
        Animation backAnim = AnimationUtils.loadAnimation(context, com.gc.materialdesign.R.anim.dialog_root_hide_amin);

        view.startAnimation(anim);
        backView.startAnimation(backAnim);
    }


}
