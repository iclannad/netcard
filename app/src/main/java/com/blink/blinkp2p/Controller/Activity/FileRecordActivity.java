package com.blink.blinkp2p.Controller.Activity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.blink.blinkp2p.Controller.Activity.base.MyBaseActivity;
import com.blink.blinkp2p.Moudle.skin.SkinConfig;
import com.blink.blinkp2p.Tool.Dao.MsgDAO;
import com.blink.blinkp2p.Tool.Dao.SQLHelper;
import com.blink.blinkp2p.Tool.Utils.SharedPrefsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import blink.com.blinkcard320.R;

public class FileRecordActivity extends MyBaseActivity {

    private static final String TAG = FileRecordActivity.class.getSimpleName();
    private ListView listview;

    private List<String> mstr;
    private ArrayList<Map<Integer, Object>> mlist;
    private MsgDAO msgDAO;

    private Button btnClearData;
    private BaseAdapter adapter;
    private View view;

    /**
     * Start()
     */
    @Override
    public void init() {
        view = LayoutInflater.from(context).inflate(R.layout.activity_filerecord, null);
        listview = (ListView) view.findViewById(R.id.activity_listview_filerecord);
        if (view == null) {
            Log.e(TAG, "init: view为空");
        }
        if (listview == null) {
            Log.e(TAG, "init: listview为空");
        }
        btnClearData = (Button) view.findViewById(R.id.activity_button_cleardata);

        setTopTitleColor(R.color.White);
        setLeftTitleColor(R.color.White);
        setTitle(getResources().getString(R.string.activity_record));
        setRightTitleVisiable(false);
        setTopColor(R.color.MainColorBlue);

        initdata();
        initview();


    }

    private void initview() {
        if (mstr != null) {
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, mstr);
            listview.setAdapter(adapter);
            Log.e(TAG, "initview: 从数据库中获得数据并显示到界面");
        } else {
            Log.e(TAG, "initview: 没有从数据库中获得数据");
        }
        btnClearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: 清除文件传输中的全部数据");
                msgDAO.DeleteAllData();
                if (mlist != null) {
                    mlist.clear();
                    mstr.clear();
                    Toast.makeText(FileRecordActivity.this, getResources().getString(R.string.error_clearsuccess), Toast.LENGTH_SHORT).show();
                }
                if (adapter != null) {
                    listview.setAdapter(adapter);
                }

            }
        });

        setContent(view);
    }

    private void initdata() {
        msgDAO = new MsgDAO(this);
        mlist = msgDAO.QueryDataAll2(SQLHelper.TABLE_NAME);
        if (mlist == null || mlist.size() == 0) {
            return;
        }
        mstr = new ArrayList<>();
        for (int i = 0; i < mlist.size(); i++) {
            String tmp = mlist.get(i).get(SQLHelper.I_TIME).toString() + ":"
                    + mlist.get(i).get(SQLHelper.I_SEND).toString() + ">"
                    + mlist.get(i).get(SQLHelper.I_MSG).toString() + ""
                    + mlist.get(i).get(SQLHelper.I_RECEIVE).toString();
            mstr.add(tmp);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();
        int skinValue = SharedPrefsUtils.getIntegerPreference(this, SkinConfig.SKIN_CONFIG, SkinConfig.SKIN_DEFAULT_VALUE);
        btnClearData.setBackgroundResource(skinValue);
    }

}
