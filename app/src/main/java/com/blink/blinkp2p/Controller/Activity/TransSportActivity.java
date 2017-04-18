package com.blink.blinkp2p.Controller.Activity;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blink.blinkp2p.R;
import com.blink.blinkp2p.Tool.Adapter.DownUpAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.blink.blinkp2p.Controller.Activity.base.MyBaseActivity;
import com.blink.blinkp2p.Controller.ActivityCode;
import com.blink.blinkp2p.Moudle.Comment;
import com.blink.blinkp2p.Moudle.DownorUpload;
import com.blink.blinkp2p.Moudle.Item;
import com.blink.blinkp2p.Moudle.skin.SkinConfig;

import com.blink.blinkp2p.Tool.DownUtils;
import com.blink.blinkp2p.Tool.UploadUtils;
import com.blink.blinkp2p.Tool.Utils.SharedPrefsUtils;
import com.blink.blinkp2p.Tool.Utils.download.DownTask;
import com.blink.blinkp2p.Tool.Utils.download.MyDownUtils;
import com.blink.blinkp2p.Tool.Utils.download.tcp.MyTcpDownUtils;
import com.blink.blinkp2p.Tool.Utils.download.tcp.MyTcpUploadUtils;
import com.blink.blinkp2p.Tool.Utils.upload.MyUploadUtils;
import com.blink.blinkp2p.Tool.Utils.upload.UploadTask;
import com.blink.blinkp2p.View.DownUpCallback;
import com.blink.blinkp2p.application.MyApplication;

import smart.blink.com.card.API.BlinkWeb;
import smart.blink.com.card.Tcp.Down;
import smart.blink.com.card.bean.DownLoadingRsp;
import smart.blink.com.card.bean.UploadReq;

public class TransSportActivity extends MyBaseActivity implements DownUpCallback, TaskDeleteImpl {

    private static final String TAG = TransSportActivity.class.getSimpleName();


    private TextView taskDownText;
    private TextView taskUploadText;
    private ListView taskListView;
    private ArrayList<Object> list;
    private DownUpAdapter adapter;

    /**
     * Start()
     */
    @Override
    public void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.task_manager, null);

        setTopTitleColor(R.color.White);
        setLeftTitleColor(R.color.White);
        setTitle(getResources().getString(R.string.activity_tranlist));
        setRightTitleVisiable(false);
        setTopColor(R.color.MainColorBlue);


        list = new ArrayList<>();

        taskListView = (ListView) view.findViewById(R.id.taskListView);
        taskDownText = (TextView) view.findViewById(R.id.taskDownText);
        taskUploadText = (TextView) view.findViewById(R.id.taskUploadText);


        taskDownText.setOnClickListener(this);
        taskUploadText.setOnClickListener(this);

        // 开启屏幕常亮
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContent(view);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<Object> downloadingTask;
        if (BlinkWeb.STATE == BlinkWeb.UDP) {
            MyDownUtils.setProgress(this);
            downloadingTask = MyDownUtils.getAllDownloadingTask();
        } else {
            MyTcpDownUtils.setProgress(this);
            downloadingTask = MyTcpDownUtils.getAllDownloadingTask();
        }

        if (adapter == null) {
            adapter = new DownUpAdapter(context, downloadingTask, this, Comment.DOWNLOAD);
            taskListView.setAdapter(adapter);
        } else
            adapter.Redata(downloadingTask, Comment.DOWNLOAD);

    }


    @Override
    protected void onStart() {
        super.onStart();
        // 添加皮肤的设置
        int skinValue = SharedPrefsUtils.getIntegerPreference(this, SkinConfig.SKIN_CONFIG, SkinConfig.SKIN_DEFAULT_VALUE);
        taskDownText.setBackgroundResource(skinValue);
        taskUploadText.setBackgroundResource(R.color.WhiteSmoke);
        taskDownText.setTextColor(getResources().getColor(R.color.WhiteSmoke));
        taskUploadText.setTextColor(getResources().getColor(skinValue));
    }


    @Override
    public void Click(View v) {
        super.Click(v);
        int skinValue = SharedPrefsUtils.getIntegerPreference(this, SkinConfig.SKIN_CONFIG, SkinConfig.SKIN_DEFAULT_VALUE);
        // 内网
        if (v.getId() == R.id.taskDownText) {
            taskDownText.setBackgroundResource(skinValue);
            taskUploadText.setBackgroundResource(R.color.WhiteSmoke);
            taskDownText.setTextColor(getResources().getColor(R.color.WhiteSmoke));
            taskUploadText.setTextColor(getResources().getColor(skinValue));

            final ArrayList<Object> downloadingTask;
            if (BlinkWeb.STATE == BlinkWeb.UDP) {
                MyUploadUtils.setProgress(null);
                MyDownUtils.setProgress(this);
                downloadingTask = MyDownUtils.getAllDownloadingTask();
            } else {
                MyTcpUploadUtils.setProgress(null);
                MyTcpDownUtils.setProgress(this);
                downloadingTask = MyTcpDownUtils.getAllDownloadingTask();
            }

            if (adapter == null) {
                adapter = new DownUpAdapter(context, downloadingTask, this, Comment.DOWNLOAD);
                taskListView.setAdapter(adapter);
            } else {
                adapter.Redata(downloadingTask, Comment.DOWNLOAD);
            }

        }
        if (v.getId() == R.id.taskUploadText) {
            taskDownText.setBackgroundResource(R.color.WhiteSmoke);
            taskUploadText.setBackgroundResource(skinValue);
            taskDownText.setTextColor(getResources().getColor(skinValue));
            taskUploadText.setTextColor(getResources().getColor(R.color.WhiteSmoke));

            final ArrayList<Object> allUploadingTask;
            if (BlinkWeb.STATE == BlinkWeb.UDP) {
                MyUploadUtils.setProgress(this);
                MyDownUtils.setProgress(null);
                allUploadingTask = MyUploadUtils.getAllUploadingTask();
            } else {
                MyTcpUploadUtils.setProgress(this);
                MyTcpDownUtils.setProgress(null);
                allUploadingTask = MyTcpUploadUtils.getAllUploadingTask();
            }

            if (adapter == null) {
                adapter = new DownUpAdapter(context, allUploadingTask, this, Comment.UPLOAD);
                taskListView.setAdapter(adapter);
            } else {
                adapter.Redata(allUploadingTask, Comment.UPLOAD);
            }
        }
    }


    /**
     * 回调的接口，更新任务列表
     *
     * @param position
     * @param object   下载DownLoadingRsp这个类   上传UploadReq这个类
     */
    @Override

    public void Call(int position, Object object) {
        if (position == ActivityCode.Downloading) {
            final ArrayList<Object> downloadingTask;
            // UDP内网下载更新界面
            if (BlinkWeb.STATE == BlinkWeb.UDP) {
                downloadingTask = MyDownUtils.getAllDownloadingTask();
            } else {
                downloadingTask = MyTcpDownUtils.getAllDownloadingTask();
            }
            if (adapter == null) {
                adapter = new DownUpAdapter(context, downloadingTask, this, Comment.DOWNLOAD);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        taskListView.setAdapter(adapter);
                    }
                });

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.Redata(downloadingTask, Comment.DOWNLOAD);
                    }
                });
            }
        }

        if (position == ActivityCode.Upload) {
            final ArrayList<Object> allUploadingTask;
            if (BlinkWeb.STATE == BlinkWeb.UDP) {
                allUploadingTask = MyUploadUtils.getAllUploadingTask();
            } else {
                allUploadingTask = MyTcpUploadUtils.getAllUploadingTask();
            }
            if (adapter == null) {
                adapter = new DownUpAdapter(context, allUploadingTask, this, Comment.UPLOAD);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        taskListView.setAdapter(adapter);
                    }
                });

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.Redata(allUploadingTask, Comment.UPLOAD);
                    }
                });
            }
        }

    }

    /**
     * 失败的接口
     *
     * @param position
     */
    @Override
    public void Fail(int position) {

    }

    @Override
    public void delete(int position, int type) {
        Log.e(TAG, "delete: position==" + position);
        if (type == Comment.DOWNLOAD) {
            DownTask downTask = Comment.downlist.get(position);
            // 标记当前的任务已经下载完毕
            if (downTask.status == 1) {
                Toast.makeText(this, "不能删除正在进行的任务。", Toast.LENGTH_SHORT).show();
                return;
            }
            downTask.status = 2;
        }
        if (type == Comment.UPLOAD) {
            UploadTask uploadTask = Comment.uploadlist.get(position);
            if (uploadTask.status == 1) {
                Toast.makeText(this, "不能删除正在进行的任务。", Toast.LENGTH_SHORT).show();
                return;
            }
            uploadTask.status = 2;
        }
    }
}
