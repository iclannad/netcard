package com.blink.blinkp2p.Controller.Activity.slidingmenu.settings;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.blink.blinkp2p.Controller.Activity.MainActivity;
import com.blink.blinkp2p.Controller.Activity.base.MyBaseActivity;
import com.blink.blinkp2p.Controller.ActivityCode;
import com.blink.blinkp2p.R;
import com.blink.blinkp2p.Tool.Adapter.FileDAOAdapter;
import com.blink.blinkp2p.Tool.Thread.HandlerImpl;

import java.util.ArrayList;
import java.util.Map;

import com.blink.blinkp2p.Controller.NetCardController;
import com.blink.blinkp2p.Moudle.Comment;
import com.blink.blinkp2p.Moudle.skin.SkinConfig;


import com.blink.blinkp2p.Tool.Adapter.FileListAdapter;
import com.blink.blinkp2p.Tool.Utils.SharedPrefsUtils;
import com.blink.blinkp2p.View.FilePathLineayout;
import com.blink.blinkp2p.Tool.Adapter.FileListAdapter.Pair;
import com.blink.blinkp2p.View.MyPersonalProgressDIalog;
import com.blink.blinkp2p.heart.HeartController;
import com.blink.blinkp2p.heart.SendHeartThread;

import smart.blink.com.card.bean.GetUploadDirRsp;
import smart.blink.com.card.bean.LookFileRsp;

/**
 * Created by Administrator on 2017/3/23.
 */
public class FilelookPC extends MyBaseActivity implements AdapterView.OnItemClickListener, HandlerImpl {

    private static final String TAG = FilelookPC.class.getSimpleName();
    private String currentfile = Environment.getExternalStorageDirectory()
            .getPath();

    private FileDAOAdapter mFileDAOAdapter;
    private FileListAdapter mFileListAdapter;
    private ArrayList<Map<String, Object>> list_dao = new ArrayList<>();
    private ArrayList<Pair<String, Integer>> list = new ArrayList<>();

    private View view;
    private FilePathLineayout mFilePathLineayout;
    private ListView mListView;
    private LinearLayout mLayout_savefile;

    /**
     * Start()
     */
    @Override
    public void init() {
        setTileBar(R.layout.base_top);
        LayoutInflater inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.activity_filelook, null);

        // activity的布局
        setContent(view);

        // 标题的文字
        setTitle(getResources().getString(R.string.uploadfilepath));
        // 左边的文字
        setLeftTitle(getResources().getString(R.string.back));
        // 右边的文字隐藏
        setRightTitleVisiable(false);
        // 标题设颜色
        setTopTitleColor(R.color.WhiteSmoke);
        // 左边文字设颜色
        setLeftTitleColor(R.color.WhiteSmoke);

        // 标题栏设颜色   (到时会修改成可设置皮肤)
        setTopColor(R.color.actionbarcolor);

        currentfile = SharedPrefsUtils.getStringPreference(FilelookPC.this, Comment.PICTUREFILE);
        // 暂时先这么处理
        if (currentfile == null) {
            currentfile = Environment.getExternalStorageDirectory().getPath();
        }
        initView();
    }

    /**
     * 当点击ListView上面view时会进来这里
     */
    private Handler filepathhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, "handleMessage: " + msg.what);
            switch (msg.what) {
                case 0:
                    currentfile = msg.obj.toString();
                    Log.e(TAG, "handleMessage: " + currentfile);

//                    if (currentfile.equals("/")) {
//                        Log.e(TAG, "handleMessage: " + "跳转到电脑的根目录，现暂时不处理");
//                        return;
//                    }

                    mFilePathLineayout.pullViewString(currentfile);
                    HeartController.stopHeart();
                    // 弹出提示框
                    MyPersonalProgressDIalog.getInstance(FilelookPC.this).setContent("正读取文件").showProgressDialog();
                    // 请求currentfile目录下面的文件
                    NetCardController.LookFileMsg(currentfile, FilelookPC.this);

                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 初始化View
     */
    private void initView() {
        mFilePathLineayout = (FilePathLineayout) view.findViewById(R.id.settings_myfilepath);
        mFilePathLineayout.sethandler(filepathhandler);

        mListView = (ListView) view.findViewById(R.id.activity_listview_filelook);
        mListView.setOnItemClickListener(this);

        mLayout_savefile = (LinearLayout) view.findViewById(R.id.activity_save_filepath);
        // 添加皮肤的设置
        int skinValue = SharedPrefsUtils.getIntegerPreference(this, SkinConfig.SKIN_CONFIG, SkinConfig.SKIN_DEFAULT_VALUE);
        mLayout_savefile.setBackgroundResource(skinValue);

        mLayout_savefile.setOnClickListener(this);

        mFileDAOAdapter = new FileDAOAdapter(this, list_dao);
        mFileListAdapter = new FileListAdapter(list, this);
        mListView.setAdapter(mFileListAdapter);

        mFilePathLineayout.addhead("/");


        HeartController.stopHeart();
        MyPersonalProgressDIalog.getInstance(this).setContent("正读取文件").showProgressDialog();
        // 从服务器请求上传目录
        NetCardController.GetUploadDir(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.activity_save_filepath) {
            // 可能文档会有错误,请注意
            Log.e(TAG, "onClick: " + "当前的位置是" + currentfile);
            // 弹出提示框
            MyPersonalProgressDIalog.getInstance(this).setContent("设置上传路径...").showProgressDialog();

            HeartController.stopHeart();
            NetCardController.SetUploadDir(currentfile, FilelookPC.this);
        }
    }

    /**
     * ListView条目点击
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e(TAG, "onItemClick: " + "条目被点击");
        Pair<String, Integer> pair = list.get(position);
        if ("/".equals(currentfile)) {
            currentfile = "";
        }
        if (pair.getB() == ActivityCode.FL) {
            return;
        }

        // 弹出提示框
        MyPersonalProgressDIalog.getInstance(this).setContent("正读取文件").showProgressDialog();

        // 关闭心跳
        HeartController.stopHeart();

        // 请求查看电脑的文件
        NetCardController.LookFileMsg(currentfile + pair.getA() + "\\", this);
        mFilePathLineayout.pushView(pair.getA(), currentfile + pair.getA() + "\\", this);
        currentfile += pair.getA() + "\\";
    }


    private ArrayList<String> name = null;
    private ArrayList<Integer> control = null;
    private boolean isFirstTimer = true;

    /**
     * 处理线程返回的更新界面
     *
     * @param position
     * @param object
     */
    @Override
    public void myHandler(int position, Object object) {

        if (position == ActivityCode.GetUploadDir) {
            GetUploadDirRsp uploadDirRsp = (GetUploadDirRsp) object;

            // 获取电脑上传文件的目录
            currentfile = uploadDirRsp.getPath();
            // 获得上传目录之后就可以向服务器请求其子文件和子文件夹
            NetCardController.LookFileMsg(currentfile, this);
        }

        if (position == ActivityCode.LookFileMsg) {
            LookFileRsp lookFileRsp = (LookFileRsp) object;

            // 开启心跳
            HeartController.startHeart();

            if (lookFileRsp.getSuccess() == 0) {
                // 关闭提示框
                MyPersonalProgressDIalog.getInstance(this).dissmissProgress();
                name = new ArrayList<>();
                control = new ArrayList<>();
                list.clear();

                name = lookFileRsp.getList();
                control = lookFileRsp.getProtrolList();

                if (control.size() == 1 && control.get(0) == ActivityCode.PAN) {
                    for (int i = 0; i < name.size(); i++) {
                        Pair<String, Integer> pair = new Pair<>();
                        pair.setA(name.get(i) + ":");
                        pair.setB(ActivityCode.PAN);
                        list.add(pair);
                    }
                } else {
                    // 这条代码有问题
                    //int length = control.size() > name.size() ? control.size() : control.size();
                    int length = control.size() > name.size() ? name.size() : control.size();

                    for (int i = 0; i < length; i++) {
                        Pair<String, Integer> pair = new Pair<>();
                        pair.setA(name.get(i));

                        if (control.get(i) == ActivityCode.DIR)
                            pair.setB(ActivityCode.DIR);
                        if (control.get(i) == ActivityCode.FL)
                            pair.setB(ActivityCode.FL);
                        list.add(pair);
                    }
                }

                // 重新刷新一下界面
                mFileListAdapter.setList(list);
                if (isFirstTimer) {
                    updatedir(currentfile);
                    isFirstTimer = false;
                }
            } else if (lookFileRsp.getSuccess() == 1) {
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 关闭提示框
                        MyPersonalProgressDIalog.getInstance(FilelookPC.this).dissmissProgress();
                        Toast.makeText(FilelookPC.this, "访问失败，请点返回再次进入", Toast.LENGTH_SHORT).show();
                    }
                });

            }


        }

        if (position == ActivityCode.SetUploadDir) {
            HeartController.startHeart();
            Log.e(TAG, "myHandler: " + "设置目录成功");
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 关闭提示框
                    MyPersonalProgressDIalog.getInstance(FilelookPC.this).dissmissProgress();
                    Toast.makeText(FilelookPC.this, "上传路径设置成功", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    /**
     * 该方法仅仅用于更新指示View
     *
     * @param str
     */
    private void updatedir(String str) {
        String[] dir = str.split("/");
        String path = "";
        for (int i = 0; i < dir.length; i++) {
            if (dir[i].length() == 0 || dir[i].equals(" ")) {
                continue;
            }
            path += dir[i] + "\\";
            // test code
            mFilePathLineayout.pushView(dir[i], path, this);
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
        // 获得上传路径失败的逻辑
        if (position == ActivityCode.GetUploadDir) {
            HeartController.startHeart();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MyPersonalProgressDIalog.getInstance(FilelookPC.this).dissmissProgress();
                    Toast.makeText(FilelookPC.this, "访问失败，请点返回再次进入", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // 访问文件目录失败
        if (position == ActivityCode.LookFileMsg) {
            HeartController.startHeart();
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MyPersonalProgressDIalog.getInstance(FilelookPC.this).dissmissProgress();
                    Toast.makeText(FilelookPC.this, "访问失败，请点返回再次进入", Toast.LENGTH_SHORT).show();
                }
            });

        }

        if (position == ActivityCode.SetUploadDir) {
            Log.e(TAG, "myError: 路径设置失败");
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 关闭提示框
                    MyPersonalProgressDIalog.getInstance(FilelookPC.this).dissmissProgress();
                    Toast.makeText(FilelookPC.this, "上传路径设置失败", Toast.LENGTH_SHORT).show();
                }
            });
            HeartController.startHeart();
        }
    }
}
