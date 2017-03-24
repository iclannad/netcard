package blink.com.blinkcard320.Controller.Activity;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

import blink.com.blinkcard320.Controller.Activity.base.MyBaseActivity;
import blink.com.blinkcard320.Controller.ActivityCode;
import blink.com.blinkcard320.Moudle.Item;
import blink.com.blinkcard320.Moudle.skin.SkinConfig;
import blink.com.blinkcard320.R;
import blink.com.blinkcard320.Tool.Adapter.DownUpAdapter;
import blink.com.blinkcard320.Tool.Utils.SharedPrefsUtils;
import blink.com.blinkcard320.View.DownUpCallback;
import smart.blink.com.card.bean.DownLoadingRsp;
import smart.blink.com.card.bean.UploadReq;

public class FileRecordActivity extends MyBaseActivity implements DownUpCallback {

    private ListView taskListView;
    private ArrayList<Object> list;
    //	private MsgDAO msgDAO;
    private Button button_clear;
    private DownUpAdapter adapter;
    private Button btnClearData;

    /**
     * Start()
     */
    @Override
    public void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_filerecord, null);
        btnClearData = (Button) view.findViewById(R.id.activity_button_cleardata);

        setTopTitleColor(R.color.White);
        setLeftTitleColor(R.color.White);
        setTitle(getResources().getString(R.string.activity_record));
        setRightTitleVisiable(false);
        setTopColor(R.color.MainColorBlue);

        list = new ArrayList<>();

        taskListView = (ListView) view.findViewById(R.id.taskListView);

        setContent(view);
    }


    @Override
    protected void onResume() {
        super.onResume();

        //读取正在上传下载的列表
//        getDownorUpload();

//        if (adapter == null) {
//            adapter = new DownUpAdapter(context, list);
//            taskListView.setAdapter(adapter);
//        } else
//            adapter.Redata(list);
    }

    @Override
    protected void onStart() {
        super.onStart();
        int skinValue = SharedPrefsUtils.getIntegerPreference(this, SkinConfig.SKIN_CONFIG, SkinConfig.SKIN_DEFAULT_VALUE);
        btnClearData.setBackgroundResource(skinValue);
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


    private void getDownorUpload() {
//        if (DownUtils.count < Comment.list.size()) {
//            for (int i = DownUtils.count; i < Comment.list.size(); i++) {
//                DownorUpload downorUpload = (DownorUpload) Comment.list.get(i);
//                list.add(getItem(getResources().getDrawable(R.mipmap.download), downorUpload.getName(), "0", "0", 0));
//            }
//        }
//
//        if (UploadUtils.count < Comment.Uploadlist.size()) {
//            for (int i = UploadUtils.count; i < Comment.Uploadlist.size(); i++) {
//                DownorUpload downorUpload = (DownorUpload) Comment.Uploadlist.get(i);
//                list.add(getItem(getResources().getDrawable(R.mipmap.upload), downorUpload.getName(), "0", "0", 0));
//            }
//        }
    }


//	private void sendHeartThread() {
//		Handler heartHandler=new HeartHandler(this);
//		SendHeartThread msSendHeartThread = SendHeartThread.GetInstance(
//				InitActivity.mPc_ip, InitActivity.mPc_port,
//				UdpSocket.getState(), heartHandler);
//		if (!msSendHeartThread.isAlive()) {
//			msSendHeartThread.start();
//		}
//	}

//	private void initdata() {
//		msgDAO = new MsgDAO(this);
//		mlist = msgDAO.QueryDataAll2(SQLHelper.TABLE_NAME);
//		if(mlist==null||mlist.size()==0){
//			return;
//		}
//		mstr=new ArrayList<>();
//		for (int i = 0; i < mlist.size(); i++) {
//			String tmp = mlist.get(i).get(SQLHelper.I_TIME).toString()+":"
//					+mlist.get(i).get(SQLHelper.I_SEND).toString()+">"
//					+mlist.get(i).get(SQLHelper.I_MSG).toString()+""
//					+mlist.get(i).get(SQLHelper.I_RECEIVE).toString();
//			mstr.add(tmp);
//		}
//	}


    /**
     * 回调的接口
     *
     * @param position
     * @param object   下载DownLoadingRsp这个类   上传UploadReq这个类
     */
    @Override
    public void Call(int position, Object object) {
        if (position == ActivityCode.Downloading) {
            DownLoadingRsp downLoadingRsp = (DownLoadingRsp) object;

            if (downLoadingRsp.isEnd()) {
                list.remove(0);
            } else {
                DecimalFormat df = new DecimalFormat("0.00");
                String db = df.format((double) downLoadingRsp.getBlockId() / (double) downLoadingRsp.getBlockLength());
                double d = Double.parseDouble(db) * 100;
                int present = (int) d;
                list.set(0, getItem(getResources().getDrawable(R.mipmap.download), downLoadingRsp.getFilename(), downLoadingRsp.getSpeed(), String.valueOf(present), present));
            }
        }
        if (position == ActivityCode.Upload) {
            UploadReq uploadReq = (UploadReq) object;

            if (uploadReq.isEnd()) {
                list.remove(0);
            } else {
                DecimalFormat df = new DecimalFormat("0.00");
                String db = df.format((double) uploadReq.getBlockID() / (double) uploadReq.getBlockLength());
                double d = Double.parseDouble(db) * 100;
                int present = (int) d;
                list.set(0, getItem(getResources().getDrawable(R.mipmap.download), uploadReq.getFilename(), uploadReq.getSpeed(), String.valueOf(present), present));
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


//	private void initview() {
//
//		listview = (ListView) findViewById(R.id.activity_listview_filerecord);
//		if(mstr!=null){
//			adapter=new ArrayAdapter<String>(this,
//					android.R.layout.simple_list_item_1,mstr);
//			listview.setAdapter(adapter);
//		}
//
//		button_clear=(Button) findViewById(R.id.activity_button_cleardata);
//		button_clear.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//			msgDAO.DeleteAllData();
//			if(mlist!=null){
//				mlist.clear();
//				mstr.clear();
//				Toast.makeText(FileRecordActivity.this,getResources().getString(R.string.error_clearsuccess),Toast.LENGTH_SHORT).show();
//			}
//			listview.setAdapter(adapter);
//			}
//		});
//		button_clear.setBackgroundDrawable(getResources().getDrawable(SkinConfig.getInstance().getButtonColor()));
//	}

}
