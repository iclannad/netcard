package com.blink.blinkp2p.View.pop;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import blink.com.blinkcard320.R;

/**
 * Created by Administrator on 2017/4/7.
 */

public class OptionsAdapter extends BaseAdapter {

    private ArrayList<String> list = new ArrayList<String>();
    private Context activity = null;
    private Handler handler;


    public OptionsAdapter(Context context, Handler handler, ArrayList<String> list) {
        this.activity = context;
        this.handler = handler;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.option_item, null);
            holder.textView = (TextView) convertView.findViewById(R.id.item_text);
            holder.imageView = (ImageView) convertView.findViewById(R.id.delImage);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(list.get(position));

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                Bundle data = new Bundle();
                //����ѡ������
                data.putInt("selIndex", position);
                msg.setData(data);
                msg.what = 1;
                handler.sendMessage(msg);
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                Bundle data = new Bundle();
                //����ɾ������
                data.putInt("delIndex", position);
                msg.setData(data);
                msg.what = 2;
                handler.sendMessage(msg);
            }
        });

        return convertView;
    }


}


class ViewHolder {
    TextView textView;
    ImageView imageView;
}





