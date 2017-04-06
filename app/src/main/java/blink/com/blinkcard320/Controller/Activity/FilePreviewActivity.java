package blink.com.blinkcard320.Controller.Activity;

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

import com.example.administrator.data_sdk.FileUtil.FileTool;
import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;

import java.io.File;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import blink.com.blinkcard320.Controller.Activity.base.MyBaseActivity;
import blink.com.blinkcard320.Controller.ActivityCode;
import blink.com.blinkcard320.Controller.NetCardController;
import blink.com.blinkcard320.Moudle.Comment;
import blink.com.blinkcard320.Moudle.DownorUpload;
import blink.com.blinkcard320.R;
import blink.com.blinkcard320.Tool.Adapter.FileListAdapter;
import blink.com.blinkcard320.Tool.DownUtils;
import blink.com.blinkcard320.Tool.System.MyToast;
import blink.com.blinkcard320.Tool.System.Tools;
import blink.com.blinkcard320.Tool.Thread.HandlerImpl;
import blink.com.blinkcard320.Tool.UploadUtils;
import blink.com.blinkcard320.Tool.Utils.Mime;
import blink.com.blinkcard320.View.FilePathLineayout;
import blink.com.blinkcard320.View.MyPersonalProgressDIalog;
import blink.com.blinkcard320.heart.SendHeartThread;
import smart.blink.com.card.API.BlinkLog;
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
    //    public static Queue<ListItem> taskQueue = new LinkedList<ListItem>();
//    public static LinkedList<ListItem> taskQueueList = new LinkedList<ListItem>();
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
                    if (type != ActivityCode.ComputerFile) {
                        mFilePathLineayout.pullViewString(mCurrentPath);
                        onclickfiledir(new File(msg.obj.toString()));
                    } else {
                        // 访部电脑文件的时候，先暂时关闭发送心跳的线程
                        // 释放心跳线程的资源
                        SendHeartThread.isClose = true;
                        synchronized (SendHeartThread.HeartLock) {
                            SendHeartThread.HeartLock.notify();
                        }

                        MyPersonalProgressDIalog.getInstance(FilePreviewActivity.this).setContent("正读取文件").showProgressDialog();
//                        if ("/".equals(mCurrentPath))
//                            mCurrentPath = "";
                        mFilePathLineayout.pullViewString(mCurrentPath);
                        //读取页面
                        NetCardController.LookFileMsg(mCurrentPath, handler);
                    }
                    //else {
//                        MyProgressDIalog md = MyProgressDIalog
//                                .getInstance(FilePreviewActivity.this);
//                        md.init();
//                        md.showProgressDialog();
//                        if (MainActivity.connectionType == Protocol.UDP) {
//                            mThreadSend = new SendLookThread(InitActivity.mPc_ip,
//                                    InitActivity.mPc_port, mDatagramSock,
//                                    mCurrentPath, mHandler);
//                            mThreadSend.start();
//                        } else {
//                            MyProgressDIalog.OpenCountTimeThread(mHandler,
//                                    Protocol.LOOK_FALIED);
//                            new LookAndOthers(Protocol.LOOK, mCurrentPath).start();
//                        }
//                    }
                    break;
//                case Protocol.LOOK:
//                    if (!mSelectedPath.equals("")) {
//                        mCurrentPath = mTCurrentPath;
//                    }
//                    //第一次进入
//                    if (StringUtils.IsInActivity(mCurrentPath)) {
//                        mFilePathLineayout.pullViewString(mCurrentPath);
//                        break;
//                    }
//                    //什么都没选，点击目录进行了返回
//                    if (mSelectedPath.equals("")) {
//                        mFilePathLineayout.pullViewString(mCurrentPath);
//                        break;
//                    }
//                    mFilePathLineayout.pushView(mSelectedPath, mCurrentPath,
//                            FilePreviewActivity.this);
//                    break;
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

//        if (type == ActivityCode.ComputerFile) {
//            PcGetListinit();
//        }
        initdata();

        if (type == ActivityCode.ComputerFile) {
            MyPersonalProgressDIalog.getInstance(this).setContent("正读取文件").showProgressDialog();
            //获取电脑路径
            NetCardController.LookFileMsg(mCurrentPath, this);
//            getPcList();
//            mButton_delete.setVisibility(View.GONE);
//            buttonCancel.setOnClickListener(new OnDownButtonClick());
//            mButton_send.setOnClickListener(new OnDownButtonClick());
//            return;
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
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onCreate(savedInstanceState);
//        initBackToolbar();
//        setContentView(R.layout.activity_file_browse);
//        setTitle(R.string.file_pre);
//        type = getIntent().getIntExtra(Protocol.FILE_TYPE, -1);
//        initdata();
//        findview();
//        viewdata();
//        initview();
//    }

//    @Override
//    public void reconnect() {
    // TODO Auto-generated method stub
//        super.reconnect();
//        initview();
//    }

//    private void sendHeartThread() {
//        Handler heartHandler = new HeartHandler(this);
//        SendHeartThread msSendHeartThread = SendHeartThread.GetInstance(
//                InitActivity.mPc_ip, InitActivity.mPc_port,
//                UdpSocket.getState(), heartHandler);
//        if (!msSendHeartThread.isAlive()) {
//            msSendHeartThread.start();
//        }
//    }

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
                //list.remove(list.get(deArray.get(i)));
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
                    //否则则是上传操作
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
                }
            }

            if (type == ActivityCode.ComputerFile) {
                Log.e(TAG, "Click: 添加了几个下载任务：" + Comment.list.size());
                Toast.makeText(this, "添加" + (Comment.list.size() - downCount) + "个任务到下载列表", Toast.LENGTH_SHORT).show();
            } else {
                // 打印查看添加了几个任务
                Log.e(TAG, "Click: 添加了几个上传任务：" + Comment.Uploadlist.size());
                Toast.makeText(this, "添加" + (Comment.Uploadlist.size() - uploadCount) + "任务到上传列表", Toast.LENGTH_SHORT).show();
            }


            // 暂时注释
            if (type == ActivityCode.ComputerFile) {
                //Log.e(TAG, "Click: " + "选择框被点击了");
                new DownUtils(this);
            } else {
                //Log.e(TAG, "Click: " + "选择框被点击了");
                new UploadUtils(this);
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

//    private void findview() {
//        mlinearlayout = (LinearLayout) findViewById(R.id.activity_ll_downll);
//        mButton_all_in = (Button) findViewById(R.id.activity_button_allin);
//        mButton_delete = (Button) findViewById(R.id.activity_button_delete);
//        mButton_send = (Button) findViewById(R.id.activity_button_send);
//        list = new ArrayList<>();
//        listview = (ListView) findViewById(R.id.activity_listview);
//        buttonCancel = (Button) findViewById(R.id.activity_button_cancel);
//        buttonSure = (Button) findViewById(R.id.activity_button_sure);
//    mFilePathLineayout=(FilePathLineayout)
//
//    findViewById(R.id.activity_myfilepath);
//
//    mFilePathLineayout.setBackgroundResource(SkinConfig.getInstance().
//
//    getColor()
//
//    );
//    }

//    private void viewdata() {
//        mButton_all_in.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                fileListAdapter.allInSelect();
//            }
//        });
//        mButton_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                isAllChoose = false;
//                deleteboolean = !deleteboolean;
//                deletefile();
//            }
//        });
//        mFilePathLineayout.sethandler(filepathhandler);
//        mFileListPath = new ArrayList<>();
//        fileListAdapter = new FileListAdapter(list, this);
//        listview.setAdapter(fileListAdapter);
//
//        mListItems = new ArrayList<Map<String, Object>>();
//
//        mHandler = new MainHandler(this);
//        if (type == Protocol.FILE_PC) {
//            PcGetListinit();
//        }
//
//    }

//    private void initview() {
//        mFilePathLineayout.addhead(mCurrentPath);
//        if (type == Protocol.FILE_PC) {
//            getPcList();
//            mButton_delete.setVisibility(View.GONE);
//            buttonCancel.setOnClickListener(new OnDownButtonClick());
//            mButton_send.setOnClickListener(new OnDownButtonClick());
//            return;
//        } else if (type == Protocol.FILE_PHONE) {
//            AllFile();
//        } else {
//            SingleFile();
//        }
//        if (MainActivity.connectionType == Protocol.TCP) {
//            TcpReceiver tcpre = TcpReceiver.getInstance(mHandler, this,
//                    listviewhandler);
//            if (!tcpre.isAlive()) {
//                Log.d("run", "tcp is start");
//                MyApplication.getInstance().addThread(tcpre);
//                tcpre.start();
//            }
//        }
//        sendHeartThread();
//        mButton_send.setOnClickListener(new OnUploadButtonClick());
//        listview.setOnItemLongClickListener(new OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//                                           int arg2, long arg3) {
//                // TODO Auto-generated method stub
//                isAllChoose = true;
//                setCheckbox();
//                return false;
//            }
//        });
//        buttonCancel.setOnClickListener(new OnUploadButtonClick());
//    }

    //    private void setOnclickButton_send() {
//        if (sendboolean) {
//            deleteboolean = false;
//            mButton_send.setBackgroundColor(getResources().getColor(
//                    R.color.back));
//        } else {
//            mButton_send.setBackgroundResource(R.drawable.circlebuttonclick);
//        }
//        if (deleteboolean) {
//            mButton_delete.setBackgroundColor(getResources().getColor(
//                    R.color.back));
//            sendboolean = false;
//        } else {
//            mButton_delete.setBackgroundResource(R.drawable.circlebuttonclick);
//        }
//    }
//
//    private void setOnclickButton_delete() {
//
//        if (deleteboolean) {
//            mButton_delete.setBackgroundColor(getResources().getColor(
//                    R.color.toolback));
//            sendboolean = false;
//        } else {
//            mButton_delete.setBackgroundResource(R.drawable.circlebuttonclick);
//        }
//        if (sendboolean) {
//            deleteboolean = false;
//            mButton_send.setBackgroundColor(getResources().getColor(
//                    R.color.toolback));
//        } else {
//            mButton_send.setBackgroundResource(R.drawable.circlebuttonclick);
//        }
//    }
//

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

            // 访部电脑文件的时候，先暂时关闭发送心跳的线程
            // 释放心跳线程的资源
            SendHeartThread.isClose = true;
            synchronized (SendHeartThread.HeartLock) {
                SendHeartThread.HeartLock.notify();
            }

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

    //    @Override
//    protected void onDestroy() {
//        // TODO Auto-generated method stub
//        try {
//            TransportManagement.getInstance().getDownload().onDestory();
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//        super.onDestroy();
//    }
//
//    @Override
//    protected void onResume() {
//        // TODO Auto-generated method stub
//        super.onResume();
//
//    }
//
//
//    public static FilePreviewListHandler listviewhandler = null;
//
//    /**
//     * 获取PC端盘符的列表的列表
//     */
//    private void getPcList() {
//        mDatagramSock = UdpSocket.getState();
//        TransportManagement.getInstance()
//                .initDownload(mHandler, listview, this);
//        listviewhandler = new FilePreviewListHandler(list, newlist,
//                fileListAdapter, filepathhandler);
//        mSelectedPath = "";
//        if (MainActivity.connectionType == Protocol.UDP) {
//            MyProgressDIalog md = MyProgressDIalog
//                    .getInstance(FilePreviewActivity.this);
//            md.init();
//            md.showProgressDialog();
//            TransportManagement.getInstance().sendHeadThread(mHandler, this);
//            setListener_udp();
//            LG.i(getClass(), "mCurrentPath---------" + mCurrentPath);
//            mThreadSend = new SendLookThread(InitActivity.mPc_ip,
//                    InitActivity.mPc_port, mDatagramSock, mCurrentPath,
//                    mHandler);
//            mThreadSend.start();
//            RecvThread recv = RecvThread.getInstance(mDatagramSock, mHandler,
//                    this, listviewhandler);
//            if (!recv.isAlive()) {
//                recv.start();
//            }
//        } else {
//            MyProgressDIalog md = MyProgressDIalog
//                    .getInstance(FilePreviewActivity.this);
//            md.init();
//            md.showProgressDialog();
//            MyProgressDIalog
//                    .OpenCountTimeThread(mHandler, Protocol.LOOK_FALIED);
//            new LookAndOthers(Protocol.LOOK, mCurrentPath).start();
//            TcpReceiver tcpre = TcpReceiver.getInstance(mHandler, this,
//                    listviewhandler);
//            if (!tcpre.isAlive()) {
//                Log.d("run", "tcp is start");
//                MyApplication.getInstance().addThread(tcpre);
//                tcpre.start();
//            }
//            setListener_tcp();
//        }
//        sendHeartThread();
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("path", mCurrentPath);
//        map.put("name", getResources().getString(R.string.dir));
//        mFileListPath.add(map);
//    }
//
//    private void setlistener_udp_listview(int arg2) {
//
//        Pair<String, Integer> pair = list.get(arg2);
//        if (pair.getB() == Protocol.DIR || pair.getB() == Protocol.PAN) {
//            mTCurrentPath = mCurrentPath;
//            mSelectedPath = pair.getA();
//            if (pair.getB() == Protocol.PAN) {
//                mTCurrentPath = pair.getA() + "\\";
//            } else {
//                mTCurrentPath = Util.forWard(mTCurrentPath, mSelectedPath);
//            }
//
//			/*
//             * mFilePathLineayout.pushView(mSelectedPath, mCurrentPath,
//			 * FilePreviewActivity.this);
//			 */
//            mThreadSend = new SendLookThread(InitActivity.mPc_ip,
//                    InitActivity.mPc_port, mDatagramSock, mTCurrentPath,
//                    mHandler);
//            mThreadSend.start();
//            MyProgressDIalog md = MyProgressDIalog
//                    .getInstance(FilePreviewActivity.this);
//            md.init();
//            md.showProgressDialog();
//            System.out.println("send dir request " + mCurrentPath);
//        } else {
//            Toast.makeText(
//                    FilePreviewActivity.this,
//                    FilePreviewActivity.this.getResources().getString(
//                            R.string.frag_remote_file_long_click),
//                    Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void downfile_udp(int arg2) {
//        Pair<String, Integer> pair = list.get(arg2);
//        String str = pair.getA();
//        mFileDownload = Util.forWard(mCurrentPath, str);
//        mFileDownload = mFileDownload.substring(0, mFileDownload.length() - 1);
//        if (pair.getB() == Protocol.FL) {
//            if (Environment.getExternalStorageState().equals(
//                    Environment.MEDIA_MOUNTED)) {
//                TransportManagement
//                        .getInstance()
//                        .getDownload()
//                        .addFile(mFileDownload,
//                                Util.getTrueName(mFileDownload),
//                                Protocol.DOWNLOAD_WAIT);
//                thread = MTaskManager.getInstance(mHandler, mDatagramSock);
//                if (!thread.isAlive()) {
//                    thread.start();
//                }
//            } else
//                UIHelper.ToastMessageNetError(FilePreviewActivity.this, FilePreviewActivity.this.getResources().getString(
//                        R.string.frag_remote_file_sd_unmounted));
//        } else {
//            String msg = mFileDownload + getResources().getString(R.string.error_downfile);
//            UIHelper.ToastMessageNetError(FilePreviewActivity.this, msg);
//        }
//    }
//
//    private void setListener_udp() {
//
//        listview.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//                                    long arg3) {
//                if (isAllChoose == true)
//                    return;
//                setlistener_udp_listview(arg2);
//            }
//
//        });
//
//
//        listview.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//            @Override
//            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//                                           int arg2, long arg3) {
//                isAllChoose = true;
//                setCheckbox();
//                return false;
//            }
//        });
//
//    }
//
//    private void setListener_tcp() {
//        listview.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//                                    long arg3) {
//                // ÿ�β鿴ǰ�������ʱ��
//                if (isAllChoose == true)
//                    return;
//                Pair<String, Integer> pair = list.get(arg2);
//
//                if (pair.getB() == Protocol.DIR || pair.getB() == Protocol.PAN) {
//                    mTCurrentPath = mCurrentPath;
//                    mSelectedPath = pair.getA();
//                    Log.d("run", "mpath----------" + mCurrentPath);
//                    if (pair.getB() == Protocol.PAN) {
//                        mTCurrentPath = pair.getA() + "\\";
//                    } else
//                        mTCurrentPath = Util
//                                .forWard(mTCurrentPath, mSelectedPath);
//
//                    new LookAndOthers(Protocol.LOOK, mTCurrentPath).start();
//                    MyProgressDIalog md = MyProgressDIalog
//                            .getInstance(FilePreviewActivity.this);
//                    md.init();
//                    md.showProgressDialog();
//                    MyProgressDIalog.OpenCountTimeThread(mHandler,
//                            Protocol.LOOK_FALIED);
//                    System.out.println("send dir request " + mCurrentPath);
//
//                } else {
//                    Toast.makeText(
//                            FilePreviewActivity.this,
//                            FilePreviewActivity.this.getResources().getString(
//                                    R.string.frag_remote_file_long_click),
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        });
//
//
//        listview.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//            @Override
//            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//                                           int arg2, long arg3) {
//                isAllChoose = true;
//                setCheckbox();
//
//                return false;
//            }
//        });
//    }
//
//    private void downfile_tcp(int arg2) {
//        Pair<String, Integer> pair = list.get(arg2);
//        String str = pair.getA();
//        LG.i(getClass(), "str===" + str + "i===" + arg2);
//        mFileDownload = Util.forWard(mCurrentPath, str);
//        mFileDownload = mFileDownload.substring(0, mFileDownload.length() - 1);
//        if (pair.getB() == Protocol.FL) {
//            TransportManagement
//                    .getInstance()
//                    .getDownload()
//                    .addFile(mFileDownload, Util.getTrueName(mFileDownload),
//                            Protocol.DOWNLOAD_WAIT);
//            thread = MTaskManager.getInstance(mHandler, mDatagramSock);
//            if (!thread.isAlive()) {
//                thread.start();
//            }
//            synchronized (TransportManagement.enqueueLock) {
//                TransportManagement.enqueueLock.notifyAll();
//            }
//        } else {
//            String msg = mFileDownload + getResources().getString(R.string.error_downfile);
//            UIHelper.ToastMessageNetError(FilePreviewActivity.this, msg);
//        }
//    }
//

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
            // 重新开启一个心跳线程
            SendHeartThread sendHeartThread = new SendHeartThread(MainActivity.heartHandler);
            SendHeartThread.isClose = false;
            sendHeartThread.start();

            LookFileRsp lookFileRsp = (LookFileRsp) object;
            BlinkLog.Print(lookFileRsp.toString());

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
                int length = control.size() > name.size() ? control.size() : control.size();
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

    }

    private void setCheckboxCancel() {
        mFilePathLineayout.setVisibility(View.VISIBLE);
        mlinearlayout.setVisibility(View.GONE);
        if (fileListAdapter != null) {
            fileListAdapter.setSelect(false);
            fileListAdapter.notifyDataSetChanged();
        }
    }
//
//    private void setCheckboxSureDown() {
//        mlinearlayout.setVisibility(View.GONE);
//        mFilePathLineayout.setVisibility(View.VISIBLE);
//        fileListAdapter.setSelect(false);
//        fileListAdapter.notifyDataSetChanged();
//        ArrayList<Integer> tmpchebox = new ArrayList<>();
//        int[] tmp = fileListAdapter.getcheboxSelectList();
//        for (int j = 0; j < tmp.length; j++) {
//            if (tmp[j] == 1) {
//                tmpchebox.add(j);
//            }
//        }
//        if (tmpchebox.size() == 0) {
//            UIHelper.ToastMessageNetError(this, R.string.no_choosefile);
//            return;
//        }
//        if (MainActivity.connectionType == Protocol.UDP) {
//            for (int i = 0; i < tmpchebox.size(); i++) {
//                downfile_udp(tmpchebox.get(i));
//            }
//        } else {
//            for (int i = 0; i < tmpchebox.size(); i++) {
//                downfile_tcp(tmpchebox.get(i));
//            }
//        }
//    }
//
//    String tfilename;
//
//    private void deletefile() {
//        ArrayList<Integer> tmpchebox = new ArrayList<>();
//        int[] tmp = fileListAdapter.getcheboxSelectList();
//        for (int j = 0; j < tmp.length; j++) {
//            if (tmp[j] == 1) {
//                tmpchebox.add(j);
//            }
//        }
//        if (tmpchebox.size() == 0) {
//            UIHelper.ToastMessageNetError(this, R.string.no_choosefile);
//            return;
//        }
//
//        final ArrayList<String> filename = new ArrayList<>();
//        Handler deletehandler = new Handler() {
//
//            @Override
//            public void handleMessage(Message msg) {
//                // TODO Auto-generated method stub
//                switch (msg.what) {
//                    case Protocol.YES:
//                        for (int i = 0; i < filename.size(); i++) {
//                            for (int j = 0; j < list.size(); j++) {
//                                if (list.get(j).getA().equals(filename.get(i))) {
//                                    list.remove(j);
//                                    //				nlist.remove(j);
//                                    break;
//                                }
//                            }
//                            mdeleteFile(filename.get(i));
//                        }
//                        mlinearlayout.setVisibility(View.GONE);
//                        mFilePathLineayout.setVisibility(View.VISIBLE);
//                        fileListAdapter.setSelect(false);
//                        fileListAdapter.notifyDataSetChanged();
//                        break;
//                    case Protocol.NO:
//                        break;
//                    default:
//                        break;
//                }
//            }
//        };
//        for (int i = 0; i < tmpchebox.size(); i++) {
//            tfilename = list.get(tmpchebox.get(i)).getA();
//            filename.add(tfilename);
//        }
//        String msg = getResources().getString(R.string.confirm) +
//                getResources().getString(R.string.delete_file);
//        MyProgressDIalog.CreateYesNoDialog(this, R.string.tips,
//                msg,
//                R.string.yes, R.string.no, deletehandler);
//        fileListAdapter.notifyDataSetChanged();
//    }
//
//    private void setCheckboxSureUpload() {
//        mlinearlayout.setVisibility(View.GONE);
//        mFilePathLineayout.setVisibility(View.VISIBLE);
//        // mRelativeLayout_titleclick.setVisibility(View.GONE);
//        fileListAdapter.setSelect(false);
//        fileListAdapter.notifyDataSetChanged();
//        ArrayList<Integer> tmpchebox = new ArrayList<>();
//        int[] tmp = fileListAdapter.getcheboxSelectList();
//        for (int j = 0; j < tmp.length; j++) {
//            if (tmp[j] == 1) {
//                tmpchebox.add(j);
//            }
//        }
//        if (tmpchebox.size() == 0) {
//            UIHelper.ToastMessageNetError(this, R.string.no_choosefile);
//            return;
//        }
//        for (int i = 0; i < tmpchebox.size(); i++) {
//            uploadFile(tmpchebox.get(i));
//        }
//    }
//
//    private void mdeleteFile(String name) {
//        File f = new File(name);
//        Log.d("run", "delete file-----------" + f.getPath());
//        Log.e("Ruan", f.exists() + "--");
//        if (f.exists())
//            if (f.delete()) {
//                fileListAdapter.notifyDataSetChanged();
//                String msg = name + " " + getResources().getString(R.string.activity_delete);
//                UIHelper.ToastSetSuccess(this,
//                        msg);
//            } else {
//                String msg = name + " " + R.string.activity_delete_error;
//                UIHelper.ToastMessageNetError(this, msg);
//            }
//    }
//
//    private void uploadFile(int arg2) {
//        File f = new File(list.get(arg2).getA());
//        mHandler = new MainHandler(this);
//        TransportManagement.getInstance().initUpload(mHandler, this);
//        TransportManagement.getInstance().getUpload().addFile(f);
//        synchronized (TransportManagement.enqueueLock) {
//            TransportManagement.enqueueLock.notifyAll();
//        }
//    }
//
//    class OnUploadButtonClick implements View.OnClickListener {
//
//        @Override
//        public void onClick(View v) {
//            // TODO Auto-genswitch (key) {
//            switch (v.getId()) {
//                case R.id.activity_button_send:
//                    isAllChoose = false;
//                    setCheckboxSureUpload();
//                    break;
//                case R.id.activity_button_cancel:
//                    isAllChoose = false;
//                    setCheckboxCancel();
//                    break;
//                default:
//                    break;
//            }
//        }
//    }
//
//    class OnDownButtonClick implements View.OnClickListener {
//
//        @Override
//        public void onClick(View v) {
//            // TODO Auto-genswitch (key) {
//            switch (v.getId()) {
//                case R.id.activity_button_send:
//                    isAllChoose = false;
//                    setCheckboxSureDown();
//                    break;
//                case R.id.activity_button_cancel:
//                    isAllChoose = false;
//                    setCheckboxCancel();
//                    break;
//                default:
//                    break;
//            }
//        }
//    }

}
