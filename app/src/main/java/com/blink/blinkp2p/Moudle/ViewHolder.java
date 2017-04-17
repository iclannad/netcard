package com.blink.blinkp2p.Moudle;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blink.blinkp2p.R;


/**
 * Created by Soft on 2016/7/9.
 */
public class ViewHolder {


    public LinearLayout gridLinear;
    public ImageView gridImage;
    public TextView gridText;
    public ImageView gridCenterImage = null;


    public TextView Sorttitle;
    public TextView Sortname;


    public RelativeLayout listRelative;
    public ImageView listImage;
    public TextView listText;
    public TextView listSubText;
    public ImageView listright;
    public TextView listRightText;
    public LinearLayout listSetting = null;
    public TextView listSettingText1 = null;
    public TextView listSettingText2 = null;
    public TextView listRightText1 = null;


    public RelativeLayout homeRelative;
    public ImageView homeImage;
    public TextView homeText;
    public TextView homeSubText;
    public ImageView homeRightImage;
    public TextView homeRight;
    public TextView homeRight1;
    public TextView edit;
    public TextView delete;
    public TextView share;


    public ImageView duImage;
    public LinearLayout itemRightLinear;
    public TextView presentText;
    public TextView speedText;
    public LinearLayout itemCenterLinear;
    public TextView titleText;
    public ProgressBar speedBar;
    public RelativeLayout duLinear;
    public ImageView taskDeleteImage;


    public ViewHolder(View view, String position) {
        //带有侧滑菜单的
        switch (position) {
//            case "SideView":
//                homeRelative = (RelativeLayout) view.findViewById(R.id.homeRelative);
//                homeImage = (ImageView) view.findViewById(R.id.homeImage);
//                homeText = (TextView) view.findViewById(R.id.homeText);
//                homeSubText = (TextView) view.findViewById(R.id.homeSubText);
//                homeRightImage = (ImageView) view.findViewById(R.id.homeRightImage);
//                homeRight = (TextView) view.findViewById(R.id.homeRight);
//                homeRight1 = (TextView) view.findViewById(R.id.homeRight1);
////                edit = (TextView) view.findViewById(R.id.edit);
//                delete = (TextView) view.findViewById(R.id.delete);
////                share = (TextView) view.findViewById(R.id.share);
//                break;
            case "ListView":
                //纯listview布局
                listRelative = (RelativeLayout) view.findViewById(R.id.listRelative);
                listImage = (ImageView) view.findViewById(R.id.listImage);
                listText = (TextView) view.findViewById(R.id.listText);
                listSubText = (TextView) view.findViewById(R.id.listSubText);
                listright = (ImageView) view.findViewById(R.id.listright);
                listRightText = (TextView) view.findViewById(R.id.listRightText);
                listSetting = (LinearLayout) view.findViewById(R.id.listSetting);
                listSettingText1 = (TextView) view.findViewById(R.id.listSettingText1);
                listSettingText2 = (TextView) view.findViewById(R.id.listSettingText2);
                listRightText1 = (TextView) view.findViewById(R.id.listRightText1);
                break;
            case "GridView":
                //纯GirdView
                gridLinear = (LinearLayout) view.findViewById(R.id.gridLinear);
                gridImage = (ImageView) view.findViewById(R.id.gridImage);
                gridText = (TextView) view.findViewById(R.id.gridText);
                gridCenterImage = (ImageView) view.findViewById(R.id.gridCenterImage);
                break;
            case "DUAdapter":
                duImage = (ImageView) view.findViewById(R.id.duImage);
                itemRightLinear = (LinearLayout) view.findViewById(R.id.itemRightLinear);
                presentText = (TextView) view.findViewById(R.id.presentText);
                speedText = (TextView) view.findViewById(R.id.speedText);
                itemCenterLinear = (LinearLayout) view.findViewById(R.id.itemCenterLinear);
                titleText = (TextView) view.findViewById(R.id.titleText);
                speedBar = (ProgressBar) view.findViewById(R.id.speedBar);
                duLinear = (RelativeLayout) view.findViewById(R.id.duLinear);
                taskDeleteImage = (ImageView) view.findViewById(R.id.iv_delete);
                break;

//            case "SortListView":
//                Sorttitle = (TextView) view.findViewById(R.id.title);
//                Sortname = (TextView) view.findViewById(R.id.name);
//                break;
        }

    }
}
