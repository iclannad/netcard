package blink.com.blinkcard320.Tool.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import blink.com.blinkcard320.Controller.ActivityCode;
import blink.com.blinkcard320.R;
import blink.com.blinkcard320.Tool.Protocol;
import blink.com.blinkcard320.Tool.Utils.Mime;

public class FileListAdapter extends BaseAdapter {

    private static final String TAG = FileListAdapter.class.getSimpleName();
    private List<Pair<String, Integer>> list;
    private Context context;
    private boolean isSelect = false;
    private int[] checkboxSelect;


    public FileListAdapter(List<Pair<String, Integer>> list, Context context) {
        this.list = list;
        this.context = context;
        checkboxSelect = new int[list.size()];
    }


    public int[] getcheboxSelectList() {
        return this.checkboxSelect;
    }

    public void allInSelect() {
        for (int i = 0; i < checkboxSelect.length; i++) {
            if (list.get(i).getB() != Protocol.FL) {
                checkboxSelect[i] = 0;
                continue;
            }
            if (checkboxSelect[i] == 0) {
                checkboxSelect[i] = 1;
            } else {
                checkboxSelect[i] = 0;
            }
        }
        notifyDataSetChanged();
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
        if (isSelect) {
            checkboxSelect = new int[list.size()];
        }
    }

    @Override
    public void notifyDataSetChanged() {
        // TODO Auto-generated method stub
        super.notifyDataSetChanged();
        //	checkboxSelect=new int[list.size()];
    }

    public void setlist(List<Pair<String, Integer>> list) {
        this.list = list;
        this.notifyDataSetChanged();
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
        ViewHolder holder;
        if (convertView != null)
            holder = (ViewHolder) convertView.getTag();
        else {
            convertView = View.inflate(context, R.layout.simple_item, null);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView
                    .findViewById(R.id.image_view_list_item);
            holder.name = (TextView) convertView
                    .findViewById(R.id.text_view_list_item);
            holder.checkbox = (CheckBox) convertView
                    .findViewById(R.id.activity_checkbox);
            convertView.setTag(holder);
        }
        if (isSelect && list.get(position).getB() == Protocol.FL) {
            holder.checkbox.setVisibility(View.VISIBLE);
            if (checkboxSelect[position] == 1) {
                holder.checkbox.setChecked(true);
            } else {
                holder.checkbox.setChecked(false);
            }

            holder.checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO Auto-generated method stub
                    if (isChecked)
                        checkboxSelect[position] = 1;
                    else
                        checkboxSelect[position] = 0;
                }
            });
        } else {
            holder.checkbox.setVisibility(View.GONE);
        }
        Pair<String, Integer> pair = list.get(position);
        if (pair != null) {
            // A中存放在是全路径
            String[] item_names = pair.getA().split("/");
            String item_name = item_names[item_names.length - 1];
            Log.d("run", "item_name=" + item_name + " pair==" + pair.getB());
            // 根据B设置图标
            switch (pair.getB()) {
                // 盘
                case Protocol.PAN:
                    holder.icon.setImageResource(R.mipmap.item_pan);
                    holder.name.setText(item_name);
                    break;
                case Protocol.DIR:
                    // 文件夹
                    holder.icon.setImageResource(R.mipmap.icon_files);
                    holder.name.setText(item_name);
                    break;
                case Protocol.FL:
                    // 文件
                    holder.icon.setImageResource(Mime.getFileIconId(item_name));
                    holder.name.setText(item_name);
                    break;
                default:
                    break;
            }
        }
        return convertView;
    }

    public void setList(List<Pair<String, Integer>> l) {
        this.list = l;
        notifyDataSetChanged();
    }

    public static class Pair<A, B> {
        private A a;
        private B b;

        public Pair() {

        }

        public Pair(A a, B b) {
            this.a = a;
            this.b = b;
        }

        public A getA() {
            return a;
        }

        public B getB() {
            return b;
        }

        public void setA(A a) {
            this.a = a;
        }

        public void setB(B b) {
            this.b = b;
        }

        public String toString() {
            return "(" + a + ", " + b + ")";
        }
    }

    public final class ViewHolder {
        public TextView name;
        public ImageView icon;
        public CheckBox checkbox;
    }


}
