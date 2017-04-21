package com.blink.blinkp2p.Controller.Fragment;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Files.FileColumns;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.blink.blinkp2p.Controller.Activity.FilePreviewActivity;
import com.blink.blinkp2p.Controller.ActivityCode;
import com.blink.blinkp2p.R;
import com.blink.blinkp2p.Tool.Adapter.LGAdapter;
import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.blink.blinkp2p.Moudle.Item;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FragmentFilePhone extends Fragment implements AdapterView.OnItemClickListener {

    private final static int DOC = 10, ZIP = 11, APK = 12;
    private final static int IMAGELOADOK = 1, VIDEOLOADOK = 2, MP3LOADOK = 3;
    public final static int All_data = 2;
    private static final String TAG = FragmentFilePhone.class.getSimpleName();
    private View mview;
    //    private TextView textview_imagenum, mTextView_phonevideo, mTextView_mp3,
//            mtextView_docnum, mtextvTextView_zipnum, mtextTextView_apknum;
    private String s_imagenum, s_phonevideo, s_mp3, s_docnum, s_zipnum, s_apknum;
    //    private LinearLayout[] mlinearlayout;
//    private OnFragmentToActivity onFragmentToActivity;
    private LinearLayout[] mlinearlyout;
    private ArrayList<String> imagelist, videolist, mp3list, ziplist, apklist, textlist;
    private Context context = null;
    private Handler mHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case IMAGELOADOK:
                    str[0] = setNumber(context.getResources().getString(R.string.file_phone_image), imagelist.size());
                    Map<String, Object> map = new HashMap<>();
                    map.put("text", str[0]);
                    map.put("image", image[0]);
                    list.set(0, map);
                    break;
                case VIDEOLOADOK:
                    str[1] = setNumber(context.getResources().getString(R.string.file_pc_video), videolist.size());
                    Map<String, Object> map1 = new HashMap<>();
                    map1.put("text", str[1]);
                    map1.put("image", image[1]);
                    list.set(1, map1);
                    break;
                case MP3LOADOK:
                    str[4] = setNumber(context.getResources().getString(R.string.file_pc_music), mp3list.size());
                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("text", str[4]);
                    map2.put("image", image[4]);
                    list.set(4, map2);
                    break;
                case ZIP:
                    str[3] = setNumber(context.getResources().getString(R.string.file_phone_Compression_package), ziplist.size());
                    Map<String, Object> map3 = new HashMap<>();
                    map3.put("text", str[3]);
                    map3.put("image", image[3]);
                    list.set(3, map3);
                    break;
                case APK:
                    str[5] = setNumber(context.getResources().getString(R.string.file_phone_Installation_package), apklist.size());
                    Map<String, Object> map4 = new HashMap<>();
                    map4.put("text", str[5]);
                    map4.put("image", image[5]);
                    list.set(5, map4);
                    break;
                case DOC:
                    str[2] = setNumber(context.getResources().getString(R.string.file_pc_docume), textlist.size());
                    Map<String, Object> map5 = new HashMap<>();
                    map5.put("text", str[2]);
                    map5.put("image", image[2]);
                    list.set(2, map5);
                    break;
                default:
                    break;
            }
            list.clear();
            for (int i = 0; i < str.length; i++)
                list.add(getItem(image[i], str[i]));


            if (adapter == null)
                adapter = new LGAdapter(context, list, "GridView");
            else {
                adapter.RefreshData(list);
            }

        }
    };

//    public FragmentFilePhone(OnFragmentToActivity onfragmenttoactivity) {
//        // TODO Auto-generated constructor stub
//        this.onFragmentToActivity = onfragmenttoactivity;
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

    private GridView phoneGridview;
    private ArrayList<Object> list = null;
    private LGAdapter adapter = null;

    // android:background="#87CEEB"
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_phone2_file, null);

        list = new ArrayList<>();
        context = getActivity();
        str = new String[]{setNumber(getResources().getString(R.string.file_phone_image), 0), setNumber(getResources().getString(R.string.file_pc_video), 0)
                , setNumber(getResources().getString(R.string.file_pc_docume), 0), setNumber(getResources().getString(R.string.file_phone_Compression_package), 0)
                , setNumber(getResources().getString(R.string.file_pc_music), 0), setNumber(getResources().getString(R.string.file_phone_Installation_package), 0)
                , getResources().getString(R.string.file_phone_myphone), "", ""};

        phoneGridview = (GridView) view.findViewById(R.id.phone_gridview);

        for (int i = 0; i < str.length; i++) {
            list.add(getItem(image[i], str[i]));
        }

        adapter = new LGAdapter(context, list, "GridView");
        phoneGridview.setAdapter(adapter);

        //获取手机的文件数目
        startthread();
        phoneGridview.setOnItemClickListener(this);

        return view;
    }

    private Object getItem(int Drawable, String title) {
        Item item = new Item();

        if (!isAdded()) {
            return item;
        }

        if (Drawable != 0)
            item.setGridImage(getResources().getDrawable(Drawable));
        item.setGridText(title);
        item.setHeight(BaseActivity.width / 3);
        item.setGridImageSize((int) getResources().getDimension(R.dimen.searchLogo));

        return item;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void startthread() {
        imagelist = new ArrayList<>();
        getItems(imagelist, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGELOADOK);
        videolist = new ArrayList<>();
        getItems(videolist, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, VIDEOLOADOK);
        mp3list = new ArrayList<>();
        getItems(mp3list, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, MP3LOADOK);
        ziplist = new ArrayList<>();
        getItems(ziplist, MediaStore.Files.getContentUri("external"), ZIP, ZIP);
        apklist = new ArrayList<>();
        getItems(apklist, MediaStore.Files.getContentUri("external"), APK, APK);
        textlist = new ArrayList<>();
        getItems(textlist, MediaStore.Files.getContentUri("external"), DOC, DOC);

    }

    private int[] image = {R.mipmap.icon_picturefile, R.mipmap.icon_videofile, R.mipmap.icon_document,
            R.mipmap.icon_zipfile, R.mipmap.icon_musicfile, R.mipmap.icon_install, R.mipmap.icon_files, 0, 0};
    private String[] str = null;

    private String setNumber(String msg, int number) {
        return msg + "(" + number + ")";
    }
//
//    private void findview() {
//        list = new ArrayList<>();
//        phone_gridview = (MGridView) mview.findViewById(R.id.phone_gridview);
//
//
//        for (int i = 0; i < str.length; i++) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("text", str[i]);
//            map.put("image", image[i]);
//            list.add(map);
//        }
//
//        mdevicegridadapter = new DeviceGridViewAdapter(getActivity(), list);
//        phone_gridview.setAdapter(mdevicegridadapter);
//        phone_gridview.setOnItemClickListener(this);
//
//
////        textview_imagenum = (TextView) mview.findViewById(R.id.fragment_textview_imagenum);
////        mTextView_phonevideo = (TextView) mview.findViewById(R.id.fragment_textview_phonefile);
////        mTextView_mp3 = (TextView) mview.findViewById(R.id.fragment_textview_music);
////        mtextvTextView_zipnum = (TextView) mview.findViewById(R.id.fragment_textview_zipnum);
////        mtextTextView_apknum = (TextView) mview.findViewById(R.id.fragment_Installation_package);
////        mtextView_docnum = (TextView) mview.findViewById(R.id.fragment_textview_recory);
////        s_imagenum = textview_imagenum.getText().toString();
////        s_phonevideo = mTextView_phonevideo.getText().toString();
////        s_mp3 = mTextView_mp3.getText().toString();
////        s_zipnum = mtextvTextView_zipnum.getText().toString();
////        s_docnum = mtextView_docnum.getText().toString();
////        s_apknum = mtextTextView_apknum.getText().toString();
//    }


//    private void initview() {
//        mlinearlayout = new LinearLayout[7];
//        mlinearlayout[0] = (LinearLayout) mview.findViewById(R.id.imageButton1);
//        mlinearlayout[1] = (LinearLayout) mview.findViewById(R.id.imageButton2);
//        mlinearlayout[2] = (LinearLayout) mview.findViewById(R.id.imageButton3);
//        mlinearlayout[3] = (LinearLayout) mview.findViewById(R.id.imageButton4);
//        mlinearlayout[4] = (LinearLayout) mview.findViewById(R.id.activity_ll_music);
//        mlinearlayout[5] = (LinearLayout) mview.findViewById(R.id.activity_ll_desk);
//        mlinearlayout[6] = (LinearLayout) mview.findViewById(R.id.fragment_phone_all);
//        for (int i = 0; i < 6; i++) {
//            mlinearlayout[i].setOnClickListener(new mOnViewClick(i));
//        }
//        mlinearlayout[6].setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent(getActivity(), FilePreviewActivity.class);
//                intent.putExtra(Protocol.FILE_TYPE, Protocol.FILE_PHONE);
//                startActivity(intent);
//            }
//        });
//    }

    /**
     * 使用内容提供者访问，得到数据
     *
     * @param list
     * @param mUri
     * @param handmsg
     */
    private void getItems(final ArrayList<String> list, final Uri mUri, final int handmsg) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                ContentResolver mContentResolver = getActivity().getContentResolver();
                Cursor mCursor = mContentResolver.query(
                        mUri,
                        null,
                        null,
                        null,
                        null);

                if (mCursor == null) {
                    return;
                }

                while (mCursor.moveToNext()) {
                    String path = mCursor.getString(1);
                    Log.d("run", "path===" + path);
                    list.add(path);
                }
                mHandler.sendEmptyMessage(handmsg);
                mCursor.close();
            }
        }).start();
    }


    private void getItems(final ArrayList<String> list, final Uri mUri, final int handmsg, final int type) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                //Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = getActivity().getContentResolver();
                String selection = null;
                switch (type) {
                    case ZIP:
                        selection = "(" + FileColumns.MIME_TYPE + " == '" + "application/zip" + "')";
                        break;
                    case APK:
                        selection = FileColumns.DATA + " LIKE '%.apk'";
                        break;
                    case DOC:
                        selection = buildDocSelection();
                        break;
                    default:
                        break;
                }
                Cursor mCursor = mContentResolver.query(
                        mUri,
                        null,
                        selection,
                        null,
                        null);

                if (mCursor == null) {
                    return;
                }

                while (mCursor.moveToNext()) {

                    String path = mCursor.getString(1);
                    Log.d("run", "path===" + path);
                    list.add(path);
                }
                /**
                 * 之前他写的代码差了这条代码
                 */
                mHandler.sendEmptyMessage(handmsg);
                mCursor.close();
            }
        }).start();
    }

    private String buildDocSelection() {
        StringBuilder selection = new StringBuilder();
        Iterator<String> iter = sDocMimeTypesSet.iterator();
        while (iter.hasNext()) {
            selection.append("(" + FileColumns.MIME_TYPE + "=='" + iter.next() + "') OR ");
        }
        return selection.substring(0, selection.lastIndexOf(")") + 1);
    }

    public static HashSet<String> sDocMimeTypesSet = new HashSet<String>() {
        {
            add("text/plain");
            add("text/plain");
            add("application/pdf");
            add("application/msword");
            add("application/vnd.ms-excel");
            add("application/vnd.ms-excel");
        }
    };


    /**
     * 从onItemClick打印出的信息可以看出，数据是正确的
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        MainActivity.position = 6;
        Intent intent = new Intent(getActivity(), FilePreviewActivity.class);
        intent.putExtra("type", position);
        switch (position) {
            case 0:
                intent.putStringArrayListExtra("list", imagelist);
                startActivity(intent);
                break;
            case 1:
                intent.putStringArrayListExtra("list", videolist);
                startActivity(intent);
                break;
            case 2:
                intent.putStringArrayListExtra("list", textlist);
                startActivity(intent);
                break;
            case 3:
                intent.putStringArrayListExtra("list", ziplist);
                startActivity(intent);
                break;
            case 4:
                intent.putStringArrayListExtra("list", mp3list);
                startActivity(intent);
                break;
            case 5:
                intent.putStringArrayListExtra("list", apklist);
                startActivity(intent);
                break;
            case 6:
                intent.putExtra("FILETYPE", ActivityCode.PhoneFile);
                startActivity(intent);
                break;
        }
    }
}
