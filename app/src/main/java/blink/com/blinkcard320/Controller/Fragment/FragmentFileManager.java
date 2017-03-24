package blink.com.blinkcard320.Controller.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;

import java.util.ArrayList;

import blink.com.blinkcard320.Controller.Activity.FileRecordActivity;
import blink.com.blinkcard320.Controller.Activity.TransSportActivity;
import blink.com.blinkcard320.Controller.ActivityCode;
import blink.com.blinkcard320.Moudle.Item;
import blink.com.blinkcard320.R;
import blink.com.blinkcard320.Tool.Adapter.LGAdapter;

@SuppressLint("NewApi")
public class FragmentFileManager extends Fragment implements AdapterView.OnItemClickListener {

    //    private LinearLayout[] linearlayout = new LinearLayout[4];
//    private TextView[] textview = new TextView[4];
//    private OnFragmentToActivity onfragmenttoactivity;
    public final static int TOPCFILE = 5;
    public final static int TOPHONEFILE = 6;
    private LinearLayout mlinearLyout_filerecord;
    //    private MGridView activity_file_GridView = null;
//    private DeviceGridViewAdapter mdevicegridadapter;
    private LinearLayout activity_file_linear = null;
    private ArrayList<Object> list = null;

    private int[] str = new int[]{R.string.fragment_phonefile
            , R.string.fragment_pcfile
            , R.string.fragment_translist
            , R.string.fragment_filerecord};
    private int image[] = {R.mipmap.icon_phonefile, R.mipmap.icon_mydevices, R.mipmap.icon_trasportlist, R.mipmap.icon_filerecord};


    private FragmentToActivity fragmentToActivity = null;
//    FragmentFileManager(OnFragmentToActivity onfragmenttoactivity) {
//        // TODO Auto-generated constructor stub
//        this.onfragmenttoactivity = onfragmenttoactivity;
//    }

    //    @Override
//    public void onAttach(Activity activity) {
//        // TODO Auto-generated method stub
//        super.onAttach(activity);
//        try {
//            onfragmenttoactivity = (OnFragmentToActivity) activity;
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//
//    }
    private GridView fragment_file_GridView = null;
    private LGAdapter adapter = null;
    private Context context = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_file, null);

        context = getActivity();
        fragmentToActivity = (FragmentToActivity) getActivity();

        list = new ArrayList<>();
        for (int i = 0; i < str.length; i++) {
            list.add(getItem(image[i], str[i]));
        }

        fragment_file_GridView = (GridView) view.findViewById(R.id.fragment_file_GridView);


        adapter = new LGAdapter(context, list, "GridView");
        fragment_file_GridView.setAdapter(adapter);
        fragment_file_GridView.setOnItemClickListener(this);
//        initview();
//        setonclick();
        return view;

    }


    private Object getItem(int Drawable, int title) {
        Item item = new Item();

        item.setGridImage(getResources().getDrawable(Drawable));
        item.setGridText(getResources().getString(title));
        item.setHeight(BaseActivity.width / 3);
        item.setGridImageSize((int) getResources().getDimension(R.dimen.logoHeight));

        return item;
    }


    private void initview() {


//        linearlayout[1] = (LinearLayout) mview.findViewById(R.id.imageButton1);
//        linearlayout[0] = (LinearLayout) mview.findViewById(R.id.imageButton2);
//        linearlayout[2] = (LinearLayout) mview.findViewById(R.id.imageButton3);
//        linearlayout[3] = (LinearLayout) mview.findViewById(R.id.imageButton4);
//        textview[1] = (TextView) mview.findViewById(R.id.fragment_textview_phonefile);
//        textview[0] = (TextView) mview.findViewById(R.id.fragment_textview_pcfile);
//        textview[2] = (TextView) mview.findViewById(R.id.fragment_textview_filereord);
//        textview[3] = (TextView) mview.findViewById(R.id.fragment_textview_recory);
//        mlinearLyout_filerecord = (LinearLayout) mview.findViewById(R.id.imageButton3);
//        activity_file_linear = (LinearLayout) mview.findViewById(R.id.activity_file_linear);
//        activity_file_GridView = (MGridView) mview.findViewById(R.id.activity_file_GridView);

//        list = new ArrayList<>();
//        for (int i = 0; i < str.length; i++) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("text", str[i]);
//            map.put("image", image[i]);
//            list.add(map);
//        }
//        mdevicegridadapter = new DeviceGridViewAdapter(getActivity(), list);
//        activity_file_GridView.setAdapter(mdevicegridadapter);
//        activity_file_GridView.setOnItemClickListener(this);

//        DensityUtil.setLinearSize(activity_file_linear, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.ItemSpace), DensityUtil.getHeight(getActivity()) / 12 + (int) getResources().getDimension(R.dimen.ItemSpaceer), 0, 0);

    }

//    private void setonclick() {
//
//        linearlayout[3].setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//
//                Intent intent = new Intent(getActivity(), TransSportActivity.class);
//                startActivity(intent);
//
//            }
//        });
//
//        linearlayout[0].setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                onfragmenttoactivity.OnFragmentToFragment(TOPCFILE);
//            }
//        });
//
//        linearlayout[1].setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                onfragmenttoactivity.OnFragmentToFragment(TOPHONEFILE);
//            }
//        });
//
//        mlinearLyout_filerecord.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent(getActivity(), FileRecordActivity.class);
//                startActivity(intent);
//            }
//        });
//
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 1:
                fragmentToActivity.toActivity(ActivityCode.ComputerFile);
                break;
            case 0:
                fragmentToActivity.toActivity(ActivityCode.PhoneFile);
                break;
            case 2:
                Intent intent = new Intent(getActivity(), TransSportActivity.class);
                startActivity(intent);
                break;
            case 3:
                Intent intent1 = new Intent(getActivity(), FileRecordActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
