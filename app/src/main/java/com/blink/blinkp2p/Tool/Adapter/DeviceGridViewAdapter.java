package com.blink.blinkp2p.Tool.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blink.blinkp2p.R;
import com.example.administrator.ui_sdk.DensityUtil;

import java.util.ArrayList;
import java.util.Map;


public class DeviceGridViewAdapter extends BaseAdapter {

    private ArrayList<Map<String, Object>> list;
    private Context context;

    public DeviceGridViewAdapter(Context context, ArrayList<Map<String, Object>> list) {
        this.list = list;
        this.context = context;

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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_gridview_items, null);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.iv_item);
            viewHolder.textview_name = (TextView) convertView.findViewById(R.id.tv_item);
            viewHolder.fragment_itemsRelayout = (RelativeLayout) convertView.findViewById(R.id.fragment_itemsRelayout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if ((int) list.get(position).get("image") != 0) {
            viewHolder.image.setImageResource((int) list.get(position).get("image"));
        }
        if (!"".equals((String) list.get(position).get("text"))) {
            viewHolder.textview_name.setText((String) list.get(position).get("text"));
        }

        DensityUtil.setItemHeight(viewHolder.fragment_itemsRelayout, DensityUtil.getWidth(context) / 3);
        viewHolder.fragment_itemsRelayout.setBackgroundDrawable(context.getResources().getDrawable(R.color.WhiteSmoke));

        return convertView;
    }

    private class ViewHolder {

        ImageView image;
        TextView textview_name;
        RelativeLayout fragment_itemsRelayout;

    }

    /**
     * 更新当前的条目
     *
     * @param list
     */
    public void setChangeData(ArrayList<Map<String, Object>> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }


}