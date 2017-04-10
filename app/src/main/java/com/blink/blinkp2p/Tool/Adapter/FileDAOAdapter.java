package com.blink.blinkp2p.Tool.Adapter;

/**
 * Created by Administrator on 2017/3/23.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blink.blinkp2p.R;

import java.util.ArrayList;
import java.util.Map;


public class FileDAOAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Map<String, Object>> list;

    public FileDAOAdapter(Context context, ArrayList<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        mLinearLyout_ViewHold listitemview = null;
        if (convertView == null) {
            listitemview = new mLinearLyout_ViewHold();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_filedao_item, null);
            listitemview.textview_name = (TextView) convertView.findViewById(R.id.textview_filename);

            convertView.setTag(listitemview);

        } else {
            listitemview = (mLinearLyout_ViewHold) convertView.getTag();
        }

        listitemview.textview_name.setText(list.get(position).get("name").toString());

        return convertView;
    }

    class mLinearLyout_ViewHold {

        TextView textview_name;

    }

}
