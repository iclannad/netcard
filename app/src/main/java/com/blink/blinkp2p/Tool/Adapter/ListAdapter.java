package com.blink.blinkp2p.Tool.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blink.blinkp2p.Moudle.Item;
import com.blink.blinkp2p.Moudle.skin.SkinConfig;
import com.blink.blinkp2p.R;
import com.blink.blinkp2p.Tool.Utils.SharedPrefsUtils;
import com.example.administrator.ui_sdk.DensityUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/12.
 */
public class ListAdapter extends BaseAdapter {
    private Context context = null;
    private ArrayList<Object> list = null;

    private int skin_value = SkinConfig.SKIN_DEFAULT_VALUE;

    public void setSkinValue(int value) {
        this.skin_value = value;
    }

    public ListAdapter(Context context, ArrayList<Object> list) {
        this.context = context;
        this.list = list;
        this.skin_value = SharedPrefsUtils.getIntegerPreference(context, SkinConfig.SKIN_CONFIG, SkinConfig.SKIN_DEFAULT_VALUE);
    }

    private ViewHolder viewHolder = null;

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
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.listadapter, null);
            // 设置ListView图片
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.listAdapterImage);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.listAdapterText);
            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.listAdapterLinear);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageView.setImageDrawable(item.getListImage());
        viewHolder.textView.setText(item.getListText());
        viewHolder.linearLayout.setBackgroundResource(skin_value);
        if (item.getHeight() != 0)
            DensityUtil.setAbsSize(viewHolder.linearLayout, AbsListView.LayoutParams.MATCH_PARENT, item.getHeight());

        return convertView;
    }

    class ViewHolder {

        private ImageView imageView = null;
        private TextView textView = null;
        private LinearLayout linearLayout = null;
    }
}
