package blink.com.blinkcard320.Tool.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.example.administrator.ui_sdk.DensityUtil;
import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;

import java.util.ArrayList;

import blink.com.blinkcard320.Moudle.Item;
import blink.com.blinkcard320.Moudle.ViewHolder;
import blink.com.blinkcard320.Moudle.skin.SkinConfig;
import blink.com.blinkcard320.R;
import blink.com.blinkcard320.Tool.Utils.SharedPrefsUtils;

/**
 * Created by Soft on 2016/7/9.
 */
public class LGAdapter extends BaseAdapter {

    private Context context = null;
    private ArrayList<Object> list = null;
    private ViewHolder viewHolder = null;
    private String state = null;

    private int skin_value = SkinConfig.SKIN_DEFAULT_VALUE;

    public LGAdapter(Context context, ArrayList<Object> list, String state) {
        this.context = context;
        this.list = list;
        this.state = state;
        this.skin_value = SharedPrefsUtils.getIntegerPreference(context, SkinConfig.SKIN_CONFIG, SkinConfig.SKIN_DEFAULT_VALUE);
    }

    public void setSkinValue(int value) {
        this.skin_value = value;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return null;
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Item item = (Item) list.get(position);

        if (convertView == null) {

            if ("ListView".equals(state)) {
                convertView = LayoutInflater.from(context).inflate(R.layout.listitem, null);
                viewHolder = new ViewHolder(convertView, state);
            } else if ("GridView".equals(state)) {
                convertView = LayoutInflater.from(context).inflate(R.layout.girditem, null);
                viewHolder = new ViewHolder(convertView, state);
            }

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        if ("ListView".equals(state)) {
            if ("-".equals(item.getListText())) {
                viewHolder.listRelative.setBackgroundColor(context.getResources().getColor(R.color.GreySmoke));
            } else {
                viewHolder.listRelative.setBackgroundResource(R.drawable.itemsector);
                if (item.getSelector() != 0) {
                    //viewHolder.listRelative.setBackgroundResource(item.getSelector());
                    viewHolder.listRelative.setBackgroundResource(skin_value);
                }


                viewHolder.listText.setText(item.getListText());
                viewHolder.listImage.setImageDrawable(item.getListImage());
                viewHolder.listright.setImageDrawable(item.getListright());
                if (item.getListTextColor() != 0){
                    viewHolder.listText.setTextColor(context.getResources().getColor(item.getListTextColor()));
                }
                if (item.getListRightText() != null) {
                    viewHolder.listRightText.setVisibility(View.VISIBLE);
                    viewHolder.listRightText.setText(item.getListRightText());
                } else
                    viewHolder.listRightText.setVisibility(View.GONE);
                if (item.getListRightText1() != null) {
                    viewHolder.listRightText1.setVisibility(View.VISIBLE);
                    viewHolder.listRightText1.setText(item.getListRightText1());
                } else
                    viewHolder.listRightText1.setVisibility(View.GONE);
                if (item.getRightImageSize() != 0)
                    DensityUtil.setRelayoutSize(viewHolder.listright, item.getRightImageSize(), item.getRightImageSize(), 0, 0, 0, DensityUtil.dip2px(context, 10), new int[]{RelativeLayout.CENTER_VERTICAL, RelativeLayout.ALIGN_PARENT_RIGHT});

                if (item.getListSubText() != null) {
                    viewHolder.listSubText.setText(item.getListSubText());
                    viewHolder.listSubText.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.listSubText.setVisibility(View.GONE);
                }
                if (item.getImageSize() != 0) {
                    DensityUtil.setRelayoutSize(viewHolder.listImage, item.getImageSize(), item.getImageSize(), 0, 0, 0, 0, new int[]{RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.CENTER_VERTICAL});
                }

                //显示右边的设置按钮
                if (item.isVisiable()) {
                    viewHolder.listSetting.setVisibility(View.VISIBLE);
                    if (item.isCheck()) {
                        viewHolder.listSetting.setBackground(context.getResources().getDrawable(R.drawable.setting_but_selector));
                        viewHolder.listSettingText1.setBackground(context.getResources().getDrawable(R.drawable.setting_but_open));
                        viewHolder.listSettingText2.setBackground(context.getResources().getDrawable(R.drawable.setting_but_selector));
                    } else {
                        viewHolder.listSetting.setBackground(context.getResources().getDrawable(R.drawable.setting_but_unselector));
                        viewHolder.listSettingText1.setBackground(context.getResources().getDrawable(R.drawable.setting_but_unselector));
                        viewHolder.listSettingText2.setBackground(context.getResources().getDrawable(R.drawable.setting_but_open));
                    }
                } else
                    viewHolder.listSetting.setVisibility(View.GONE);
            }
            if (item.getHeight() != 0)
                DensityUtil.setHeight(viewHolder.listRelative, item.getHeight());
        } else if ("GridView".equals(state)) {
            viewHolder.gridLinear.setBackgroundResource(R.drawable.itemsector);
            viewHolder.gridText.setText(item.getGridText());
            viewHolder.gridImage.setImageDrawable(item.getGridImage());
            if (item.getGridImageSize() != 0)
                DensityUtil.setLinearSize(viewHolder.gridImage, item.getGridImageSize(), item.getGridImageSize());
            if (item.getHeight() != 0)
                DensityUtil.setHeight(viewHolder.gridLinear, item.getHeight());
            if (item.getGridCenterImage() != null)
                DensityUtil.setLinearSize(viewHolder.gridCenterImage, BaseActivity.width / 3, BaseActivity.width / 3);
            viewHolder.gridCenterImage.setBackground(item.getGridCenterImage());

        }
        return convertView;
    }

    /**
     * 外部调用的接口   更新数据
     *
     * @param list
     */
    public void RefreshData(ArrayList<Object> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }
}
