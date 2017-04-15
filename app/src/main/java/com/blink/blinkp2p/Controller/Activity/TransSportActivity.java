package com.blink.blinkp2p.Controller.Activity;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

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
import com.blink.blinkp2p.Tool.Utils.download.MyDownUtils;
import com.blink.blinkp2p.Tool.Utils.upload.MyUploadUtils;
import com.blink.blinkp2p.View.DownUpCallback;
import com.blink.blinkp2p.application.MyApplication;

import smart.blink.com.card.API.BlinkWeb;
import smart.blink.com.card.bean.DownLoadingRsp;
import smart.blink.com.card.bean.UploadReq;

public class TransSportActivity extends MyBaseActivity implements DownUpCallback {

    private static final String TAG = TransSportActivity.class.getSimpleName();

//	private MyExpandableAdapter adapter;
//	private LinkedList<ListItem> lists;
//	private ExpandableListView listView;
//	private Button download_manager_clear;
//	private Button download_manager_edit;
//	private Handler handler = new Handler();
//	public static ViewHolder current_view;

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
        if (BlinkWeb.STATE == BlinkWeb.UDP) {
            MyDownUtils.setProgress(this);

            ArrayList<Object> downloadingTask = MyDownUtils.getAllDownloadingTask();
            if (adapter == null) {
                adapter = new DownUpAdapter(context, downloadingTask);
                taskListView.setAdapter(adapter);
            } else
                adapter.Redata(downloadingTask);
        } else {
            DownUtils.setProgress(this);
            UploadUtils.setProgress(this);
            // 默认是打开下载的界面
            //读取正在上传下载的列表
            getDownorUpload(ActivityCode.Downloading);

            if (adapter == null) {
                adapter = new DownUpAdapter(context, list);
                taskListView.setAdapter(adapter);
            } else
                adapter.Redata(list);
        }
    }


    private Object getItem(Drawable drawable, String title, String speed, String present, int progress) {
        Item item = new Item();
        item.setListImage(drawable);
        item.setListText(title);
        item.setListRightText(present);
        item.setListRightText1(speed);
        item.setProgressBar(progress);
        item.setHeight((int) getResources().getDimension(R.dimen.itemHeight));

        return item;
    }

    /**
     * 获取上传或者下载的列表
     */
    private void getDownorUpload(int code) {
        list.clear();
        if (code == ActivityCode.Downloading) {
            // 如果当前没有任务的话就直接返回
            if (Comment.list.size() == 0) {
                return;
            }
            if (DownUtils.count <= Comment.list.size()) {
                for (int i = DownUtils.count - 1; i < Comment.list.size(); i++) {
                    DownorUpload downorUpload = (DownorUpload) Comment.list.get(i);
                    list.add(getItem(getResources().getDrawable(R.mipmap.download), downorUpload.getName(), "0", 0 + "%", 0));
                }
            }
            // 设置第一个任务的进度
            updateLoadDownProgress();
        } else {
            if (UploadUtils.count <= Comment.Uploadlist.size()) {
                // 如果当前没有任务的话就直接返回
                if (Comment.Uploadlist.size() == 0) {
                    return;
                }
                for (int i = UploadUtils.count - 1; i < Comment.Uploadlist.size(); i++) {
                    // 数组越界
                    DownorUpload downorUpload = (DownorUpload) Comment.Uploadlist.get(i);
                    list.add(getItem(getResources().getDrawable(R.mipmap.upload), downorUpload.getName(), "0", 0 + "%", 0));
                }
            }
        }
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
        if (BlinkWeb.STATE == BlinkWeb.UDP) {
            if (v.getId() == R.id.taskDownText) {
                //taskDownText.setBackgroundResource(R.color.MainColorBlue);
                taskDownText.setBackgroundResource(skinValue);
                taskUploadText.setBackgroundResource(R.color.WhiteSmoke);
                taskDownText.setTextColor(getResources().getColor(R.color.WhiteSmoke));
                //taskUploadText.setTextColor(getResources().getColor(R.color.MainColorBlue));
                taskUploadText.setTextColor(getResources().getColor(skinValue));

                MyUploadUtils.setProgress(null);
                MyDownUtils.setProgress(this);

                // 更新当前的下载列表
                final ArrayList<Object> downloadingTask = MyDownUtils.getAllDownloadingTask();
                if (adapter == null) {
                    adapter = new DownUpAdapter(context, downloadingTask);
                    taskListView.setAdapter(adapter);
                } else {
                    adapter.Redata(downloadingTask);
                }

            }
            if (v.getId() == R.id.taskUploadText) {
                taskDownText.setBackgroundResource(R.color.WhiteSmoke);
                //taskUploadText.setBackgroundResource(R.color.MainColorBlue);
                taskUploadText.setBackgroundResource(skinValue);
                //taskDownText.setTextColor(getResources().getColor(R.color.MainColorBlue));
                taskDownText.setTextColor(getResources().getColor(skinValue));
                taskUploadText.setTextColor(getResources().getColor(R.color.WhiteSmoke));

                MyUploadUtils.setProgress(this);
                MyDownUtils.setProgress(null);

                final ArrayList<Object> allUploadingTask = MyUploadUtils.getAllUploadingTask();
                if (adapter == null) {
                    adapter = new DownUpAdapter(context, allUploadingTask);
                    taskListView.setAdapter(adapter);
                } else {
                    adapter.Redata(allUploadingTask);
                }
            }
        } else {
            if (v.getId() == R.id.taskDownText) {
                getDownorUpload(ActivityCode.Downloading);
                adapter.Redata(list);
                //taskDownText.setBackgroundResource(R.color.MainColorBlue);
                taskDownText.setBackgroundResource(skinValue);
                taskUploadText.setBackgroundResource(R.color.WhiteSmoke);
                taskDownText.setTextColor(getResources().getColor(R.color.WhiteSmoke));
                //taskUploadText.setTextColor(getResources().getColor(R.color.MainColorBlue));
                taskUploadText.setTextColor(getResources().getColor(skinValue));

                DownUtils.setProgress(this);
                UploadUtils.setProgress(null);

                updateLoadDownProgress();
                return;
            }
            if (v.getId() == R.id.taskUploadText) {
                getDownorUpload(ActivityCode.Upload);
                adapter.Redata(list);
                taskDownText.setBackgroundResource(R.color.WhiteSmoke);
                //taskUploadText.setBackgroundResource(R.color.MainColorBlue);
                taskUploadText.setBackgroundResource(skinValue);
                //taskDownText.setTextColor(getResources().getColor(R.color.MainColorBlue));
                taskDownText.setTextColor(getResources().getColor(skinValue));
                taskUploadText.setTextColor(getResources().getColor(R.color.WhiteSmoke));

                DownUtils.setProgress(null);
                UploadUtils.setProgress(this);

                updateUpLoadProgress();
                return;
            }
        }

    }

    /**
     * 更新第一个上传的进度条
     */
    private void updateUpLoadProgress() {
        UploadReq uploadReq = MyApplication.getInstance().uploadReq;

        if (MyApplication.getInstance().uploadReq == null) {
            Log.e(TAG, "updateUpLoadProgress: MyApplication.uploadReq == null");
            return;
        } else {
            Log.e(TAG, "updateUpLoadProgress: MyApplication.uploadReq != null");
        }

        DecimalFormat df = new DecimalFormat("0.00");
        String db = df.format((double) uploadReq.getBlockID() / (double) uploadReq.getBlockSize());
        double d = Double.parseDouble(db) * 100;
        int present = (int) d;
        // 选择上传的图片
        list.set(0, getItem(getResources().getDrawable(R.mipmap.upload), uploadReq.getFilename(), uploadReq.getSpeed(), String.valueOf(present) + "%", present));
    }

    /**
     * 更新第一个下载的进度条
     */
    private void updateLoadDownProgress() {

        DownLoadingRsp downLoadingRsp = MyApplication.getInstance().downLoadingRsp;
        if (downLoadingRsp == null) {
            return;
        }

        DecimalFormat df = new DecimalFormat("0.00");
        String db = df.format((double) downLoadingRsp.getBlockId() / (double) downLoadingRsp.getTotolSize());
        double d = Double.parseDouble(db) * 100;
        int present = (int) d;
        list.set(0, getItem(getResources().getDrawable(R.mipmap.download), downLoadingRsp.getFilename(), downLoadingRsp.getSpeed(), present + "%", present));
    }


    /**
     * 回调的接口，更新任务列表
     *
     * @param position
     * @param object   下载DownLoadingRsp这个类   上传UploadReq这个类
     */
    @Override

    public void Call(int position, Object object) {
        if (BlinkWeb.STATE == BlinkWeb.UDP) {
            if (position == ActivityCode.Downloading) {
                // UDP内网下载更新界面
                final ArrayList<Object> downloadingTask = MyDownUtils.getAllDownloadingTask();
                if (adapter == null) {
                    adapter = new DownUpAdapter(context, downloadingTask);
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
                            adapter.Redata(downloadingTask);
                        }
                    });
                }
            }

            // 内网上传的处理逻辑
            if (position == ActivityCode.Upload) {
                final ArrayList<Object> allUploadingTask = MyUploadUtils.getAllUploadingTask();
                if (adapter == null) {
                    adapter = new DownUpAdapter(context, allUploadingTask);
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
                            adapter.Redata(allUploadingTask);
                        }
                    });
                }
            }

        } else {
            if (position == ActivityCode.Downloading && list.size() != 0) {
                // 重新获取当前的列表
                getDownorUpload(ActivityCode.Downloading);
                DownLoadingRsp downLoadingRsp = (DownLoadingRsp) object;
                // 判断是否到了结尾
                if (downLoadingRsp.isEnd()) {
                    list.remove(0);
                } else {
                    DecimalFormat df = new DecimalFormat("0.00");
                    String db = df.format((double) downLoadingRsp.getBlockId() / (double) downLoadingRsp.getTotolSize());
                    double d = Double.parseDouble(db) * 100;
                    int present = (int) d;
                    list.set(0, getItem(getResources().getDrawable(R.mipmap.download), downLoadingRsp.getFilename(), downLoadingRsp.getSpeed(), present + "%", present));
                }
            }
            if (position == ActivityCode.Upload && list.size() != 0) {
                // 重新获取当前的列表
                getDownorUpload(ActivityCode.Upload);

                UploadReq uploadReq = (UploadReq) object;
                if (uploadReq.isEnd()) {
                    // 删除第一个下载完成的任务，更新界面
                    list.remove(0);
                } else {
                    DecimalFormat df = new DecimalFormat("0.00");
                    String db = df.format((double) uploadReq.getBlockID() / (double) uploadReq.getBlockSize());
                    double d = Double.parseDouble(db) * 100;
                    int present = (int) d;
                    list.set(0, getItem(getResources().getDrawable(R.mipmap.upload), uploadReq.getFilename(), uploadReq.getSpeed(), String.valueOf(present) + "%", present));
                }

            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 重新刷新界面
                    adapter.Redata(list);
                }
            });
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

}
