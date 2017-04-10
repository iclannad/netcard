package com.blink.blinkp2p.Controller.Activity;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.blink.blinkp2p.Controller.Activity.LeadFragment.LeadFragment1;
import com.blink.blinkp2p.Controller.Activity.LeadFragment.LeadFragment2;
import com.blink.blinkp2p.Controller.Activity.LeadFragment.LeadFragment3;
import com.blink.blinkp2p.R;
import com.example.administrator.data_sdk.ImageUtil.ImageTransformation;
import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;
import com.example.administrator.ui_sdk.Other.MyViewPagerItemAdapter;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017/4/5.
 */
public class LeadActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private ViewPager lead_Viewpager = null;
    private ArrayList<Fragment> list = null;
    private MyViewPagerItemAdapter adapter = null;

    private ImageView leadImg1, leadImg2, leadImg3 = null;

    /**
     * Start()
     */
    @Override
    public void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.lead_main, null);

        setTileBar(0);

        list = new ArrayList<>();
        list.add(new LeadFragment1());
        list.add(new LeadFragment2());
        list.add(new LeadFragment3());

        lead_Viewpager = (ViewPager) view.findViewById(R.id.lead_Viewpager);
        leadImg1 = (ImageView) view.findViewById(R.id.leadImg1);
        leadImg2 = (ImageView) view.findViewById(R.id.leadImg2);
        leadImg3 = (ImageView) view.findViewById(R.id.leadImg3);


        adapter = new MyViewPagerItemAdapter(getSupportFragmentManager(), list);
        lead_Viewpager.setAdapter(adapter);


        setContent(view);


        lead_Viewpager.setCurrentItem(0);
        Selector(0);


        lead_Viewpager.addOnPageChangeListener(this);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    @Override
    public void onPageSelected(int position) {
        Selector(position);
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private void Selector(int position) {
        switch (position) {
            case 0:
                leadImg1.setImageDrawable(getBackDrawable(R.mipmap.dot1));
                leadImg2.setImageDrawable(getBackDrawable(R.mipmap.dot));
                leadImg3.setImageDrawable(getBackDrawable(R.mipmap.dot));
                break;
            case 1:
                leadImg1.setImageDrawable(getBackDrawable(R.mipmap.dot));
                leadImg2.setImageDrawable(getBackDrawable(R.mipmap.dot1));
                leadImg3.setImageDrawable(getBackDrawable(R.mipmap.dot));
                break;
            case 2:
                leadImg1.setImageDrawable(getBackDrawable(R.mipmap.dot));
                leadImg2.setImageDrawable(getBackDrawable(R.mipmap.dot));
                leadImg3.setImageDrawable(getBackDrawable(R.mipmap.dot1));
                break;
        }
    }

    private Drawable getBackDrawable(int resid) {
        return ImageTransformation.Resouce2Drawable(context, resid);
    }

}
