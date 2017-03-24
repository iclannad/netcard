package blink.com.blinkcard320.Tool.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import blink.com.blinkcard320.Moudle.skin.SkinBean;
import blink.com.blinkcard320.R;

/**
 * Created by Administrator on 2017/3/20.
 */
public class SkinAdapter extends BaseAdapter {

    private ArrayList<Object> list = null;
    private ViewHolder viewHolder = null;
    private Context context = null;

    public SkinAdapter(ArrayList<Object> list, Context context) {
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
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SkinBean skinBean = (SkinBean) list.get(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.skinadapter, null);
            viewHolder.skin_Adapter_id = (ImageView) convertView.findViewById(R.id.skin_Adapter_id);
            viewHolder.skin_Adapter_Back = (ImageView) convertView.findViewById(R.id.skin_Adapter_Back);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.skin_Adapter_id.setVisibility(View.VISIBLE);
        // 设置条目颜色
        viewHolder.skin_Adapter_Back.setBackgroundResource(skinBean.getSkinBackColor());
        if (skinBean.getSkinIDColor() != 0) {
            viewHolder.skin_Adapter_id.setVisibility(View.VISIBLE);
            viewHolder.skin_Adapter_id.setBackgroundDrawable(context.getResources().getDrawable(skinBean.getSkinIDColor()));
        } else
            viewHolder.skin_Adapter_id.setVisibility(View.GONE);

        return convertView;

    }

    private class ViewHolder {

        private ImageView skin_Adapter_Back = null;
        private ImageView skin_Adapter_id = null;
    }

    public void setChangeData(ArrayList<Object> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }
}
