package blink.com.blinkcard320.Controller.Activity.slidingmenu.settings;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;

import java.io.File;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import blink.com.blinkcard320.Controller.Activity.base.MyBaseActivity;
import blink.com.blinkcard320.Moudle.Comment;
import blink.com.blinkcard320.Moudle.skin.SkinConfig;
import blink.com.blinkcard320.R;
import blink.com.blinkcard320.Tool.Adapter.FileDAOAdapter;
import blink.com.blinkcard320.Tool.Adapter.FileListAdapter;
import blink.com.blinkcard320.Tool.Protocol;
import blink.com.blinkcard320.Tool.Utils.FileUtils;
import blink.com.blinkcard320.Tool.Utils.SharedPrefsUtils;
import blink.com.blinkcard320.Tool.Utils.UIHelper;
import blink.com.blinkcard320.View.FilePathLineayout;
import blink.com.blinkcard320.Tool.Adapter.FileListAdapter.Pair;

/**
 * Created by Administrator on 2017/3/20.
 */
public class Filelook extends MyBaseActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = Filelook.class.getSimpleName();
    private FileDAOAdapter mFileDAOAdapter;
    // sd卡根目录
    private String currentfile = Environment.getExternalStorageDirectory().getPath();
    private ArrayList<Pair<String, Integer>> list = new ArrayList<>();

    private ArrayList<Map<String, Object>> list_dao = new ArrayList<>();

    private View view;
    private FilePathLineayout mFilePathLineayout;
    private ListView mListView;
    private LinearLayout mLayout_savefile;
    private FileListAdapter fileListAdapter;

    private String savetype;

    private String mSelectedPath = "";

    /**
     * 当点击ListView上面view时会进来这里
     */
    private Handler filepathhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, "handleMessage: " + msg.what);
            switch (msg.what) {
                case 0:
                    try {
                        currentfile = msg.obj.toString();
                        mSelectedPath = "-1";
                    } catch (Exception e) {
                        // TODO: handle exception
                        return;
                    }
                    mFilePathLineayout.pullViewString(currentfile);
                    if (currentfile.equals("/")) {
                        currentfile = Environment.getExternalStorageDirectory()
                                .getPath();
                    }
                    onclickfiledir(new File(currentfile));
                    break;
                case Protocol.LOOK:
                    break;
                default:
                    break;
            }
        }
    };

    private void onclickfiledir(File f) {
        currentfile = f.getPath();
        list = FileUtils.GetFilechild(f,
                (ArrayList<Pair<String, Integer>>) list);
        fileListAdapter.setlist(list);
        // 暂时注释
        fileListAdapter.notifyDataSetInvalidated();
        Map<String, Object> attr = new HashMap<>();
        attr.put("name", f.getName());
        attr.put("path", f.getPath());
        list_dao.add(attr);
        mFileDAOAdapter.notifyDataSetChanged();
    }

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
        setTitle(getResources().getString(R.string.path_set));
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

        /**
         * 判断是从文件下载还是从照片存储位置过来的
         */
        savetype = getIntent().getStringExtra(Comment.FILETYPE);
        if (savetype.equals(Comment.DOWNFILE)) {
            // 从sharedPrefs中读取以前保存的路径
            setTitle(getResources().getString(R.string.setfiledownpath));
            currentfile = SharedPrefsUtils.getStringPreference(Filelook.this, Comment.DOWNFILE);
        } else if (savetype.equals(Comment.PICTUREFILE)) {
            // 从sharedPrefs中读取以前保存的路径
            setTitle(getResources().getString(R.string.setpicpath));
            currentfile = SharedPrefsUtils.getStringPreference(Filelook.this, Comment.PICTUREFILE);
        }

        if (currentfile == null) {
            currentfile = Environment.getExternalStorageDirectory().getPath();
        }

        Log.e(TAG, "init: " + currentfile);

        initView();

    }

    private void initView() {
        mFilePathLineayout = (FilePathLineayout) view.findViewById(R.id.settings_myfilepath);
        mFilePathLineayout.sethandler(filepathhandler);

        mListView = (ListView) view.findViewById(R.id.activity_listview_filelook);
        mListView.setOnItemClickListener(this);

        mLayout_savefile = (LinearLayout) view.findViewById(R.id.activity_save_filepath);
        // 添加皮肤的设置
        int skinValue = SharedPrefsUtils.getIntegerPreference(this, SkinConfig.SKIN_CONFIG, SkinConfig.SKIN_DEFAULT_VALUE);
        mLayout_savefile.setBackgroundResource(skinValue);

        // 将浏览的位置保存sharedPrefs中
        mLayout_savefile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 保存当前的路径到sp
                if (savetype.equals(Comment.DOWNFILE)) {
                    SharedPrefsUtils.setStringPreference(Filelook.this, Comment.DOWNFILE, currentfile);
                } else if (savetype.equals(Comment.PICTUREFILE)) {
                    SharedPrefsUtils.setStringPreference(Filelook.this, Comment.PICTUREFILE, currentfile);
                }
                UIHelper.ToastSetSuccess(Filelook.this, R.string.set_path_success);
            }
        });

        mFileDAOAdapter = new FileDAOAdapter(this, list_dao);

        // 设定适配器
        fileListAdapter = new FileListAdapter(list, this);
        mListView.setAdapter(fileListAdapter);

        phonefile();
    }

    /**
     * 读取手机下面的文件
     */
    private void phonefile() {
        final File f = new File(currentfile);
        list = FileUtils.GetFilechild(f, list);
        fileListAdapter.setList(list);

        updatedirPhone(currentfile);
    }

    /**
     * 更新ListView上面的指示目录的view
     *
     * @param str
     */
    private void updatedirPhone(String str) {
        mFilePathLineayout.addhead("/");
        String sdpth = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        String[] dirs = str.split(sdpth);
        if (dirs.length < 1)
            return;
        dirs[1] = dirs[1].substring(1);
        String[] dir = dirs[1].split("/");
        String path = sdpth + "/";
        for (int i = 0; i < dir.length; i++) {
            path += dir[i];
            mFilePathLineayout.pushView(dir[i], path, this);
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
        Log.e(TAG, "onItemClick: " + "ListView条目点击");
        phoneOnclick(position);
    }

    private void phoneOnclick(int position) {

        String tmp = list.get(position).getA().toString() + "/";
        Log.d("run", "currentfile---" + tmp);
        File f = new File(tmp);
        if (!f.isDirectory()) {
            return;
        }
        mFilePathLineayout.pushView(f.getName(), f.getPath(), this);
        list = FileUtils.GetFilechild(f, list);
        currentfile = tmp;
        updatelist_dat();
        fileListAdapter.notifyDataSetChanged();
        mFileDAOAdapter.notifyDataSetChanged();

    }

    private void updatelist_dat() {
        list_dao.clear();
        Log.d("run", currentfile);
        String[] str;

        str = currentfile.split("/");

        for (int i = 0; i < str.length; i++) {

            Map<String, Object> map = new HashMap<>();
            if (i == 0) {
                map.put("name", this.getResources().getString(R.string.dir));
                map.put("path", Environment.getExternalStorageDirectory()
                        .getPath());
                list_dao.add(map);

            }
            map.put("name", str[i]);
            map.put("path", str[i]);
            list_dao.add(map);
        }
        fileListAdapter.notifyDataSetChanged();
    }
}
