package blink.com.blinkcard320.Controller.Fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

import blink.com.blinkcard320.Controller.Activity.FilePreviewActivity;
import blink.com.blinkcard320.Controller.ActivityCode;
import blink.com.blinkcard320.Moudle.Item;
import blink.com.blinkcard320.R;
import blink.com.blinkcard320.Tool.Adapter.LGAdapter;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FragmentFilePC extends Fragment implements AdapterView.OnItemClickListener {

    private LinearLayout[] mlinearlayout;
//    private OnFragmentToActivity onFragmentToActivity;

    private LGAdapter adapter = null;
    private Context context = null;
    private GridView activity_pc_GridView = null;
    //    private DeviceGridViewAdapter mdevicegridadapter;
    private LinearLayout activity_pc_linear = null;
    private ArrayList<Object> list = null;


//    public FragmentFilePC(OnFragmentToActivity onfragmenttoactivity) {
//        // TODO Auto-generated constructor stub
//        this.onFragmentToActivity = onfragmenttoactivity;
//    }


//    @Override
//    public void onResume() {
//        super.onResume();
//
//        MainActivity.position = 5;
//    }

//    @Override
//    public void onAttach(Activity activity) {
//        // TODO Auto-generated method stub
//        try {
//            onFragmentToActivity = (OnFragmentToActivity) activity;
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//        super.onAttach(activity);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_pc_file, null);

        context = getActivity();

        list = new ArrayList<>();
        str = new String[]{getResources().getString(R.string.file_pc_mypc)
                , getResources().getString(R.string.file_pc_video)
                , getResources().getString(R.string.file_pc_music)
                , getResources().getString(R.string.file_pc_picture)
                , getResources().getString(R.string.file_pc_docume)
                , getResources().getString(R.string.file_pc_desk)};

        activity_pc_GridView = (GridView) view.findViewById(R.id.activity_pc_GridView);


        for (int i = 0; i < str.length; i++)
            list.add(getItem(image[i], str[i]));


        adapter = new LGAdapter(context, list, "GridView");
        activity_pc_GridView.setAdapter(adapter);
        activity_pc_GridView.setOnItemClickListener(this);

        return view;
    }

    private String str[] = null;
    private int image[] = {R.mipmap.icon_mydevices, R.mipmap.icon_videofile, R.mipmap.icon_music, R.mipmap.icon_picturefile,
            R.mipmap.icon_document, R.mipmap.icon_desk};

    //
//    private void initview() {


//		mlinearlayout=new LinearLayout[6];
//		mlinearlayout[0]=(LinearLayout) mview.findViewById(R.id.imageButton2);
//		mlinearlayout[1]=(LinearLayout) mview.findViewById(R.id.imageButton1);
//		mlinearlayout[2]=(LinearLayout) mview.findViewById(R.id.imageButton4);
//		mlinearlayout[3]=(LinearLayout) mview.findViewById(R.id.imageButton3);
//		mlinearlayout[4]=(LinearLayout) mview.findViewById(R.id.activity_ll_music);
//		mlinearlayout[5]=(LinearLayout) mview.findViewById(R.id.activity_ll_desk);
//		initclick();

    private Object getItem(int Drawable, String title) {
        Item item = new Item();

        if (Drawable != 0)
            item.setGridImage(getResources().getDrawable(Drawable));
        item.setGridText(title);
        item.setHeight(BaseActivity.width / 3);
        item.setGridImageSize((int) getResources().getDimension(R.dimen.searchLogo));

        return item;
    }
//        activity_pc_linear = (LinearLayout) mview.findViewById(R.id.activity_pc_linear);

//
//        list = new ArrayList<>();
//        for (int i = 0; i < str.length; i++) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("text", str[i]);
//            map.put("image", image[i]);
//            list.add(map);
//        }
//        mdevicegridadapter = new DeviceGridViewAdapter(getActivity(), list);
//        activity_pc_GridView.setAdapter(mdevicegridadapter);
//        activity_pc_GridView.setOnItemClickListener(this);
//
//    }

//    private void initclick() {
//
//        mlinearlayout[0].setOnClickListener(new View.OnClickListener() {
//
//            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent();
//                intent.putExtra(Protocol.FILE_TYPE, Protocol.FILE_PC);
//                intent.putExtra("path", path[0]);
//                intent.setClass(getActivity(), FilePreviewActivity.class);
//                startActivity(intent);
//            }
//        });
//        mlinearlayout[1].setOnClickListener(new View.OnClickListener() {
//            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent();
//                intent.putExtra(Protocol.FILE_TYPE, Protocol.FILE_PC);
//                intent.putExtra("path", path[1]);
//                intent.setClass(getActivity(), FilePreviewActivity.class);
//                startActivity(intent);
//            }
//        });
//        mlinearlayout[2].setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent();
//                intent.putExtra(Protocol.FILE_TYPE, Protocol.FILE_PC);
//                intent.putExtra("path", path[2]);
//                intent.setClass(getActivity(), FilePreviewActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        mlinearlayout[3].setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent();
//                intent.putExtra(Protocol.FILE_TYPE, Protocol.FILE_PC);
//                intent.putExtra("path", path[3]);
//                intent.setClass(getActivity(), FilePreviewActivity.class);
//                startActivity(intent);
//            }
//        });
//        mlinearlayout[4].setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent();
//                intent.putExtra(Protocol.FILE_TYPE, Protocol.FILE_PC);
//                intent.putExtra("path", path[4]);
//                intent.setClass(getActivity(), FilePreviewActivity.class);
//                startActivity(intent);
//            }
//        });
//        mlinearlayout[5].setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent();
//                intent.putExtra(Protocol.FILE_TYPE, Protocol.FILE_PC);
//                intent.putExtra("path", path[5]);
//                intent.setClass(getActivity(), FilePreviewActivity.class);
//                startActivity(intent);
//            }
//        });
//
//    }


    /**
     * 电脑文件下面的GridView，访问我的电脑，视频，音乐，图片，文档，桌面， 顺序已修正
     */
    //String path[] = {"/", "video\\", "picture\\", "document\\", "music\\", "desktop\\"};
    String path[] = {"/", "video\\", "music\\", "picture\\", "document\\", "desktop\\"};

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////        MainActivity.position = 5;
        Intent intent = new Intent();
        intent.putExtra("FILETYPE", ActivityCode.ComputerFile);
        intent.putExtra("path", path[position]);
        intent.setClass(getActivity(), FilePreviewActivity.class);
        startActivity(intent);
    }
}
