package com.blink.blinkp2p.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

/**
 * Created by Administrator on 2017/3/20.
 */
@SuppressLint("NewApi")
public class MGridView extends GridView {

    public MGridView(Context context, AttributeSet attrs, int defStyleAttr,
                     int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

    public MGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public MGridView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public MGridView(Context context) {
        super(context);

    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}
