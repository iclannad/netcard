package blink.com.blinkcard320.Controller.Activity.slidingmenu;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;

import blink.com.blinkcard320.Controller.Activity.base.MyBaseActivity;
import blink.com.blinkcard320.Controller.ActivityCode;
import blink.com.blinkcard320.Controller.NetCardController;
import blink.com.blinkcard320.R;
import blink.com.blinkcard320.Tool.Protocol;
import blink.com.blinkcard320.Tool.System.Tools;
import blink.com.blinkcard320.Tool.Thread.HandlerImpl;
import blink.com.blinkcard320.Tool.Utils.UIHelper;
import smart.blink.com.card.bean.FeedbackRsp;

/**
 * Created by Administrator on 2017/3/20.
 */
public class FeedbackActivity extends MyBaseActivity implements HandlerImpl {

    private static final String TAG = FeedbackActivity.class.getSimpleName();

    private EditText edit_contacts;
    private EditText edit_text;
    private View view;

    /**
     * Start()
     */
    @Override
    public void init() {
        setTileBar(R.layout.base_top);

        LayoutInflater inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.activity_feedback, null);

        initView();

        // activity的布局
        setContent(view);

        // 标题的文字
        setTitle(getResources().getString(R.string.feedback));
        // 左边的文字
        setLeftTitle(getResources().getString(R.string.back));
        // 右边的文字
        setRightTitle(getResources().getString(R.string.submit));
        // 标题设颜色
        setTopTitleColor(R.color.WhiteSmoke);
        // 左边文字设颜色
        setLeftTitleColor(R.color.WhiteSmoke);
        // 右边文字设颜色
        setRightTitleColor(R.color.WhiteSmoke);
        // 标题栏设颜色   (到时会修改成可设置皮肤)
        setTopColor(R.color.actionbarcolor);
    }

    /**
     * 初始化View
     */
    private void initView() {
        edit_contacts = (EditText) view.findViewById(R.id.init_activity_edit_connectway);
        edit_text = (EditText) view.findViewById(R.id.init_activity_edit_text);
    }

    @Override
    public void setRightTextClick(View v) {

        String contacts = "";
        String text = "";

        contacts = edit_contacts.getText().toString();
        text = edit_text.getText().toString();

        if (contacts.length() == 0 || text.length() == 0) {
            UIHelper.ToastMessageNetError(FeedbackActivity.this,
                    FeedbackActivity.this.getResources().getString(R.string.error_feedback_isnull));
            return;
        }

        if (!Tools.isMobile(contacts) && !Tools.isEmail(contacts)) {
            UIHelper.ToastMessageNetError(FeedbackActivity.this,
                    FeedbackActivity.this.getResources().getString(R.string.isPhoneorEmail));
            return;
        }

        if (contacts.getBytes().length >= 64 || text.getBytes().length >= 256) {
            UIHelper.ToastMessageNetError(FeedbackActivity.this,
                    FeedbackActivity.this.getResources().getString(R.string.error_feedback_islen));
            return;
        }

        NetCardController.FEEDBACK(contacts, text, this);

    }

    /**
     * 处理线程返回的更新界面
     *
     * @param position
     * @param object
     */
    @Override
    public void myHandler(int position, Object object) {
        Log.e(TAG, "myHandler: ");
        if (position == ActivityCode.Feedback) {
            FeedbackRsp feedbackRsp = (FeedbackRsp) object;
            if (feedbackRsp.getSuccess() == 0) {
                //Log.e(TAG, "myHandler: " + "提交成功");
                UIHelper.ToastSetSuccess(FeedbackActivity.this,
                        FeedbackActivity.this.getResources().getString(R.string.submit_feedback_success));
            } else {
                //Log.e(TAG, "myHandler: " + "提交失败");
                UIHelper.ToastMessageNetError(FeedbackActivity.this,
                        FeedbackActivity.this.getResources().getString(R.string.submit_feedback_success));
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
        Log.e(TAG, "myError: ");
    }
}
