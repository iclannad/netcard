package com.blink.blinkp2p.Tool.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.blink.blinkp2p.R;
import com.example.administrator.ui_sdk.DensityUtil;
import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;

import java.util.ArrayList;

import com.blink.blinkp2p.Moudle.Item;
import com.blink.blinkp2p.Moudle.ViewHolder;

/**
 * Created by Ruanjiahui on 2017/1/9.
 */
public class DownUpAdapter extends BaseAdapter {

    private Context context = null;
    private ArrayList<Object> list = null;
    private ViewHolder viewHolder = null;


    public DownUpAdapter(Context context, ArrayList<Object> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = (Item) list.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.duadpter, null);
            viewHolder = new ViewHolder(convertView, "DUAdapter");

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();


        viewHolder.duImage.setImageDrawable(item.getListImage());
        if (item.getListText() != null || !"".equals(item.getListText()))
            viewHolder.titleText.setText(item.getListText());
        viewHolder.speedText.setText(item.getListRightText1());
        viewHolder.presentText.setText(item.getListRightText());
        viewHolder.speedBar.setProgress(item.getProgressBar());
        if (item.getHeight() != 0) {
            DensityUtil.setAbsSize(viewHolder.duLinear, BaseActivity.width, item.getHeight());
        }

        return convertView;
    }


    public void Redata(ArrayList<Object> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }
}
