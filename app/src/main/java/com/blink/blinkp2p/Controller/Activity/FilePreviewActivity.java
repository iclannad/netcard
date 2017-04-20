package com.blink.blinkp2p.Controller.Activity;

import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.blink.blinkp2p.Controller.Activity.base.MyBaseActivity;
import com.blink.blinkp2p.Controller.ActivityCode;
import com.blink.blinkp2p.Controller.NetCardController;
import com.blink.blinkp2p.Moudle.Comment;
import com.blink.blinkp2p.Moudle.DownorUpload;
import com.blink.blinkp2p.R;
import com.blink.blinkp2p.Tool.Adapter.FileListAdapter;
import com.blink.blinkp2p.Tool.System.MyToast;
import com.blink.blinkp2p.Tool.System.Tools;
import com.blink.blinkp2p.Tool.Thread.HandlerImpl;
import com.blink.blinkp2p.Tool.UploadUtils;
import com.blink.blinkp2p.Tool.Utils.Mime;
import com.blink.blinkp2p.Tool.Utils.download.DownTask;
import com.blink.blinkp2p.Tool.Utils.download.MyDownUtils;
import com.blink.blinkp2p.Tool.Utils.download.tcp.MyTcpDownUtils;
import com.blink.blinkp2p.Tool.Utils.download.tcp.MyTcpUploadUtils;
import com.blink.blinkp2p.Tool.Utils.upload.MyUploadUtils;
import com.blink.blinkp2p.Tool.Utils.upload.UploadTask;
import com.blink.blinkp2p.View.FilePathLineayout;
import com.blink.blinkp2p.View.MyPersonalProgressDIalog;
import com.blink.blinkp2p.heart.HeartController;
import com.example.administrator.data_sdk.FileUtil.FileTool;

import java.io.File;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smart.blink.com.card.API.BlinkLog;
import smart.blink.com.card.API.BlinkWeb;
import smart.blink.com.card.bean.LookFileRsp;

public class FilePreviewActivity extends MyBaseActivity implements OnItemClickListener, OnItemLongClickListener, HandlerImpl {

    private static final String TAG = FilePreviewActivity.class.getSimpleName();

    private LinearLayout mlinearlayout;
    public static Object mUploadLock = new Object();
    private FileListAdapter fileListAdapter = null;
    private List<FileListAdapter.Pair<String, Integer>> list;
    //    private List<Pair<String, Integer>> newlist = new ArrayList<>();
    private ArrayList<String> nlist;
    private int type;
    private File positionFile = null;
    //    public static MainHandler mHandler;
    private String mFileDownload = "";
    private String mSelectedPath = "";
    private String TmSelectedPath = "";
    //已经全选
    private boolean isAllChoose = false;
    // tcp/udp

    private List<Map<String, Object>> mListItems;
    private DatagramSocket mDatagramSock;
    private Button mButtonUpload;
//    private MTaskManager thread;
//    protected SendLookThread mThreadSend;

    public String mCurrentPath;
    public String mTCurrentPath;
    private String mheadCurrentPath;

    public static volatile boolean started = false;
    private ArrayList<Map<String, Object>> mFileListPath;
    private RelativeLayout mRelativeLayout_titleclick;
    private Button buttonSure, buttonCancel;

    private boolean setpath = false;
    private FilePathLineayout mFilePathLineayout;
    private boolean sendboolean = false, deleteboolean = false;


    private Button activityButtonCancel;
    private Button activityButtonSend;
    private Button activityButtonDelete;
    private Button activityButtonAllin;
    private ListView activity_listview = null;
    private HandlerImpl handler = null;

    private Handler filepathhandler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    try {
                        mCurrentPath = msg.obj.toString();
                        mSelectedPath = "";
                    } catch (Exception e) {
                        return;
                    }
                    // 隐藏掉选择框
                    setCheckboxCancel();
                    if (type != ActivityCode.ComputerFile) {
                        mFilePathLineayout.pullViewString(mCurrentPath);
                        onclickfiledir(new File(msg.obj.toString()));
                    } else {

                        // 释放心跳线程的资源
                        HeartController.stopHeart();
                        MyPersonalProgressDIalog.getInstance(FilePreviewActivity.this).setContent("正读取文件").showProgressDialog();
//                        if ("/".equals(mCurrentPath))
//                            mCurrentPath = "";
                        mFilePathLineayout.pullViewString(mCurrentPath);
                        // 访问电脑的文件夹
                        NetCardController.LookFileMsg(mCurrentPath, handler);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * Start()
     */
    @Override
    public void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_file_browse, null);

        setTopTitleColor(R.color.White);
        setLeftTitleColor(R.color.White);
        setTitle(getResources().getString(R.string.file_pre));
        setRightTitleVisiable(false);
        setTopColor(R.color.MainColorBlue);


        handler = this;
        type = getIntent().getIntExtra("FILETYPE", -1);
        list = new ArrayList<>();
        mFileListPath = new ArrayList<>();

        activityButtonCancel = (Button) view.findViewById(R.id.activity_button_cancel);
        activityButtonSend = (Button) view.findViewById(R.id.activity_button_send);
        activityButtonDelete = (Button) view.findViewById(R.id.activity_button_delete);
        activityButtonAllin = (Button) view.findViewById(R.id.activity_button_allin);
        activity_listview = (ListView) view.findViewById(R.id.activity_listview);
        mFilePathLineayout = (FilePathLineayout) view.findViewById(R.id.activity_myfilepath);
        mlinearlayout = (LinearLayout) view.findViewById(R.id.activity_ll_downll);


        fileListAdapter = new FileListAdapter(list, this);
        activity_listview.setAdapter(fileListAdapter);
        activity_listview.setOnItemClickListener(this);
        activity_listview.setOnItemLongClickListener(this);
        activityButtonCancel.setOnClickListener(this);
        activityButtonSend.setOnClickListener(this);
        activityButtonDelete.setOnClickListener(this);
        activityButtonAllin.setOnClickListener(this);

        initdata();

        if (type == ActivityCode.ComputerFile) {
            // 释放心跳线程的资源
            HeartController.stopHeart();

            MyPersonalProgressDIalog.getInstance(this).setContent("正读取文件").showProgressDialog();
            //获取电脑路径
            NetCardController.LookFileMsg(mCurrentPath, this);
        } else if (type == ActivityCode.PhoneFile) {
            AllFile();
        } else {
            SingleFile();
        }


        mFilePathLineayout.sethandler(filepathhandler);
        // 可以设置主题
        //mFilePathLineayout.setBackgroundResource(R.color.MainColorRed);
        mFilePathLineayout.addhead(mCurrentPath);

        setContent(view);
    }

    @Override
    public void Click(View v) {
        //取消
        if (v.getId() == R.id.activity_button_cancel) {
            setCheckboxCancel();
            return;
        }
        //全选
        if (v.getId() == R.id.activity_button_allin) {
            fileListAdapter.allInSelect();
            return;
        }
        //删除
        if (v.getId() == R.id.activity_button_delete) {
            if (type == ActivityCode.ComputerFile) {
                MyToast.Toast(context, R.string.computerDelete);
                return;
            }
            int[] delete = fileListAdapter.getcheboxSelectList();
            ArrayList<Integer> deArray = selectArray(delete);

            // 按照原来他的代码多选删除会报错，所以我先删除文件，再删除对应List里面的对象
            for (int i = 0; i < deArray.size(); i++) {
                //删除文件                                           // 文件夹                 文件名
                FileTool.deleteFolderFile(Tools.GetFilePath(list.get(deArray.get(i)).getA(), positionFile), true);
            }
            List<FileListAdapter.Pair<String, Integer>> newList = new ArrayList<>(list);
            for (int i = 0; i < deArray.size(); i++) {
                list.remove(newList.get(deArray.get(i)));
            }

            fileListAdapter.setList(list);
            fileListAdapter.notifyDataSetChanged();

            setCheckboxCancel();
            return;
        }
        //发送或者下载
        if (v.getId() == R.id.activity_button_send) {
            int downCount = Comment.list.size();
            int uploadCount = Comment.Uploadlist.size();

            int[] send = fileListAdapter.getcheboxSelectList();
            ArrayList<Integer> seArray = selectArray(send);

            for (int i = 0; i < seArray.size(); i++) {
                //点击按钮如果是电脑界面则说明是下载操作
                if (type == ActivityCode.ComputerFile) {
                    DownorUpload downorUpload = new DownorUpload();
                    downorUpload.setName(name.get(seArray.get(i)));
                    downorUpload.setFLAG(DownorUpload.DOWN);
                    downorUpload.setPath(mCurrentPath + name.get(seArray.get(i)));
                    Comment.list.add(downorUpload);

                    //-------------------------------------------------------------------
                    // 下面是多任务同时下载的写法，上面是单任务下载的写法
                    DownTask downTask = new DownTask();
                    downTask.id = Comment.downlist.size();  // 当前任务的序号
                    downTask.name = name.get(seArray.get(i));
                    downTask.path = mCurrentPath + name.get(seArray.get(i));
                    downTask.status = 0;
                    downTask.progress = 0;
                    downTask.speed = "0";
                    // 将加入下载任务列表
                    Comment.downlist.add(downTask);
                } else {
                    DownorUpload downorUpload = new DownorUpload();
                    String[] filename = list.get(seArray.get(i)).getA().split("/");
                    downorUpload.setName(filename[filename.length - 1]);

                    Log.e(TAG, "Click: " + filename[filename.length - 1]);

                    downorUpload.setFLAG(DownorUpload.UPLOAD);
                    String[] filepath = list.get(seArray.get(i)).getA().split("/");
                    String path = "";
                    for (int r = 0; r < filepath.length - 1; r++)
                        path += "/" + filepath[r];
                    downorUpload.setPath(path);
                    Comment.Uploadlist.add(downorUpload);

                    //-------------------------------------------------------------------
                    // 下面是多任务同时上传的写法，上面是单任务上传的写法
                    UploadTask uploadTask = new UploadTask();
                    uploadTask.name = filename[filename.length - 1];
                    uploadTask.path = path;
                    uploadTask.id = Comment.uploadlist.size();
                    uploadTask.progress = 0;
                    uploadTask.status = 0;
                    uploadTask.speed = "0";
                    Comment.uploadlist.add(uploadTask);
                }
            }

            if (type == ActivityCode.ComputerFile) {
                Toast.makeText(this, "添加" + (Comment.list.size() - downCount) + "个任务到下载列表", Toast.LENGTH_SHORT).show();
            } else {
                // 打印查看添加了几个任务
                Toast.makeText(this, "添加" + (Comment.Uploadlist.size() - uploadCount) + "任务到上传列表", Toast.LENGTH_SHORT).show();
            }

            // 上传和下载当前文件
            if (type == ActivityCode.ComputerFile) {
                if (BlinkWeb.STATE == BlinkWeb.TCP) {
                    //new DownUtils(this);
                    new MyTcpDownUtils(this);
                } else {
                    // 测试多任务同时下载
                    new MyDownUtils(this);
                }
            } else {
                if (BlinkWeb.STATE == BlinkWeb.TCP) {
                    //new UploadUtils(this);
                    new MyTcpUploadUtils(this);
                } else {
                    // 测试多任务同时上传
                    new MyUploadUtils(this);
                }
            }
            // 隐藏掉选择框
            setCheckboxCancel();
            return;
        }
    }

    /**
     * 将选择的下标储存起来
     *
     * @param str
     * @return
     */
    private ArrayList<Integer> selectArray(int[] str) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < str.length; i++) {
            if (str[i] == 1)
                list.add(i);
        }
        return list;
    }


    /**
     * 初始化数据
     */
    private void initdata() {
        if (type == -2) {
            mCurrentPath = "/";
            mheadCurrentPath = getIntent().getStringExtra("path");
            mCurrentPath = mheadCurrentPath;
        } else {
            if (type == ActivityCode.ComputerFile) {
                mCurrentPath = getIntent().getStringExtra("path");
                mheadCurrentPath = mCurrentPath;
            } else {
                mCurrentPath = "/";
                mheadCurrentPath = "/";
            }
        }
        mTCurrentPath = mCurrentPath;
        setpath = false;
    }


    /**
     * 获取手机的部分文件和文件夹
     */
    private void SingleFile() {
        nlist = getIntent().getStringArrayListExtra("list");
        for (int i = 0; i < nlist.size(); i++) {
            FileListAdapter.Pair<String, Integer> pair = new FileListAdapter.Pair<>();
            pair.setA(nlist.get(i).toString());
            File f = new File(nlist.get(i).toString());
            if (f.isDirectory()) {
                pair.setB(ActivityCode.DIR);
            } else {
                pair.setB(ActivityCode.FL);
            }
            list.add(pair);
        }
        positionFile = null;
    }

    /**
     * 获取手机全部文件和文件夹
     */
    private void AllFile() {
        list.clear();
        nlist = new ArrayList<>();
        File sdcard = Environment.getExternalStorageDirectory();
        positionFile = sdcard;
        nlist = Tools.sortFileList(sdcard.getPath());
        for (int i = 0; i < nlist.size(); i++) {
            FileListAdapter.Pair<String, Integer> pair = new FileListAdapter.Pair<>();
            pair.setA(nlist.get(i).toString());
            File f = new File(nlist.get(i).toString());
            if (f.isDirectory()) {
                pair.setB(ActivityCode.DIR);
            } else {
                pair.setB(ActivityCode.FL);
            }
            list.add(pair);
        }
        DaoTitle();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 隐藏掉四个选择按钮
        setCheckboxCancel();
        if (type == ActivityCode.ComputerFile) {
            //跳转到下一级
            //如果是文件夹的话
            FileListAdapter.Pair<String, Integer> pair = list.get(position);
            if (pair.getB() == ActivityCode.FL) {
                MyToast.Toast(context, R.string.frag_remote_file_long_click);
                return;
            }
            MyPersonalProgressDIalog.getInstance(this).setContent("正读取文件").showProgressDialog();
            if ("/".equals(mCurrentPath))
                mCurrentPath = "";

            // 释放心跳线程的资源
            HeartController.stopHeart();

            NetCardController.LookFileMsg(mCurrentPath + pair.getA() + "\\", this);
            mFilePathLineayout.pushView(pair.getA(), mCurrentPath + pair.getA() + "\\", this);
            mCurrentPath += pair.getA() + "\\";
        } else {
            onclickfile(position);
        }

    }

    /**
     * 添加标题的路径
     */
    private void DaoTitle() {
        Map<String, Object> attr = new HashMap<>();
        attr.put("name", positionFile.getName());
        attr.put("path", positionFile.getPath());
        mFileListPath.add(attr);
    }

    /**
     * 点击文件的操作
     *
     * @param position
     */
    private void onclickfile(int position) {
        String filepath;
        filepath = Tools.GetFilePath(list.get(position).getA(), positionFile);
        File f = new File(filepath);
        if (f.isDirectory()) {

            onclickfiledir(f);
            mFilePathLineayout.pushView(f.getName(), f.getPath(), this);
        } else {
            try {
                Intent intent = Mime.openFileIntent(f);
                startActivity(intent);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }

    /**
     * 点击文件夹的操作
     *
     * @param f
     */
    private void onclickfiledir(File f) {
        // 提示
        MyPersonalProgressDIalog.getInstance(FilePreviewActivity.this).setContent("正读取文件").showProgressDialog();
        Log.d("run", "path===" + f.getPath());
        Map<String, Object> attr = new HashMap<>();
        attr.put("name", f.getName());
        if (f.getPath().equals("/")) {
            attr.put("path", Environment.getExternalStorageDirectory()
                    .getPath());
            f = Environment.getExternalStorageDirectory();
        } else {
            attr.put("path", f.getPath());
        }
        mFileListPath.add(attr);
        positionFile = f;
        list = Tools.GetFilechild(f,
                (ArrayList<FileListAdapter.Pair<String, Integer>>) list);
        if (list == null) {
            list = new ArrayList<>();
            fileListAdapter.setlist(list);
            fileListAdapter.notifyDataSetInvalidated();
        }
        fileListAdapter.notifyDataSetChanged();
        // 关闭对话框
        MyPersonalProgressDIalog.getInstance(this).dissmissProgress();
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

//        isAllChoose = true;
        setCheckbox();
        return true;
    }


    /**
     * 设置弹出复选框
     */
    private void setCheckbox() {
        mlinearlayout.setVisibility(View.VISIBLE);

        if (fileListAdapter != null) {
            fileListAdapter.setSelect(true);
            fileListAdapter.notifyDataSetChanged();
        }
    }

    private ArrayList<String> name = null;
    private ArrayList<Integer> control = null;

    /**
     * 处理线程返回的更新界面
     *
     * @param position
     * @param object
     */
    @Override
    public void myHandler(int position, Object object) {
        if (position == ActivityCode.LookFileMsg) {
            LookFileRsp lookFileRsp = (LookFileRsp) object;
            BlinkLog.Print(lookFileRsp.toString());

            // 重新开启一个心跳线程
            HeartController.startHeart();

            if (lookFileRsp.getSuccess() == 0) {
                name = new ArrayList<>();
                control = new ArrayList<>();
                list.clear();

                name = lookFileRsp.getList();
                control = lookFileRsp.getProtrolList();

                if (control.size() == 1 && control.get(0) == ActivityCode.PAN) {
                    for (int i = 0; i < name.size(); i++) {
                        FileListAdapter.Pair<String, Integer> pair = new FileListAdapter.Pair<>();
                        pair.setA(name.get(i) + ":");
                        pair.setB(ActivityCode.PAN);
                        list.add(pair);
                    }
                } else {
                    int length = control.size() > name.size() ? name.size() : control.size();
                    for (int i = 0; i < length; i++) {
                        FileListAdapter.Pair<String, Integer> pair = new FileListAdapter.Pair<>();
                        pair.setA(name.get(i));
                        if (control.get(i) == ActivityCode.DIR)
                            pair.setB(ActivityCode.DIR);
                        if (control.get(i) == ActivityCode.FL)
                            pair.setB(ActivityCode.FL);
                        list.add(pair);
                    }
                }
                fileListAdapter.setList(list);
                // 关闭对话框
                MyPersonalProgressDIalog.getInstance(this).dissmissProgress();
            } else if (lookFileRsp.getSuccess() == 1) {
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 关闭对话框
                        MyPersonalProgressDIalog.getInstance(FilePreviewActivity.this).dissmissProgress();
                        Toast.makeText(FilePreviewActivity.this, "访问失败，请点返回再次进入", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    }

    /**
     * 错误的操作
     *
     * @param position
     * @param error
     */
    @Override
    public void myError(int position, int error) {
        if (position == ActivityCode.LookFileMsg) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 重新开启一个心跳线程
                    HeartController.startHeart();

                    // 关闭对话框
                    MyPersonalProgressDIalog.getInstance(FilePreviewActivity.this).dissmissProgress();
                    Toast.makeText(FilePreviewActivity.this, "出现异常，请重新访问", Toast.LENGTH_SHORT).show();
                    FilePreviewActivity.this.finish();
                }
            });
        }
    }

    private void setCheckboxCancel() {
        mFilePathLineayout.setVisibility(View.VISIBLE);
        mlinearlayout.setVisibility(View.GONE);
        if (fileListAdapter != null) {
            fileListAdapter.setSelect(false);
            fileListAdapter.notifyDataSetChanged();
        }
    }


}
