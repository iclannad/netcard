package blink.com.blinkcard320.Controller.Activity;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

import blink.com.blinkcard320.Controller.Activity.base.MyBaseActivity;
import blink.com.blinkcard320.Controller.ActivityCode;
import blink.com.blinkcard320.Moudle.Item;
import blink.com.blinkcard320.R;
import blink.com.blinkcard320.Tool.Adapter.DownUpAdapter;
import blink.com.blinkcard320.Tool.DownUtils;
import blink.com.blinkcard320.Tool.UploadUtils;
import blink.com.blinkcard320.View.DownUpCallback;
import smart.blink.com.card.bean.DownLoadingRsp;
import smart.blink.com.card.bean.UploadReq;

public class TransSportActivity extends MyBaseActivity implements DownUpCallback {
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

        setContent(view);
    }

    @Override
    protected void onResume() {
        super.onResume();

        DownUtils.setProgress(this);
        UploadUtils.setProgress(this);
        //读取正在上传下载的列表
        getDownorUpload(ActivityCode.Downloading);

        if (adapter == null) {
            adapter = new DownUpAdapter(context, list);
            taskListView.setAdapter(adapter);
        } else
            adapter.Redata(list);
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
//        list.clear();
//        if (code == ActivityCode.Downloading) {
//            if (DownUtils.count <= Comment.list.size()) {
//                for (int i = DownUtils.count - 1; i < Comment.list.size(); i++) {
//                    DownorUpload downorUpload = (DownorUpload) Comment.list.get(i);
//                    list.add(getItem(getResources().getDrawable(R.mipmap.download), downorUpload.getName(), "0", 0 + "%", 0));
//                }
//            }
//        } else {
//            if (UploadUtils.count <= Comment.Uploadlist.size()) {
//                for (int i = UploadUtils.count - 1; i < Comment.Uploadlist.size(); i++) {
//                    DownorUpload downorUpload = (DownorUpload) Comment.Uploadlist.get(i);
//                    list.add(getItem(getResources().getDrawable(R.mipmap.upload), downorUpload.getName(), "0", 0 + "%" , 0));
//                }
//            }
//        }
    }

    //	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		initBackToolbar();
//		setContentView(R.layout.task_manager);
//		setTitle(R.string.activity_tranlist);
//		init();
//	}

//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		TransportManagement.getInstance().registerObserver(this);
//		sendHeartThread();
//	}
//
//	@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//		TransportManagement.getInstance().removeObserver();
//	}

//	private void sendHeartThread() {
//		Handler heartHandler = new HeartHandler(this);
//		SendHeartThread msSendHeartThread = SendHeartThread.GetInstance(
//				InitActivity.mPc_ip, InitActivity.mPc_port,
//				UdpSocket.getState(), heartHandler);
//		if (!msSendHeartThread.isAlive()) {
//			msSendHeartThread.start();
//		}
//	}


    @Override
    public void Click(View v) {
        super.Click(v);

        if (v.getId() == R.id.taskDownText) {
            getDownorUpload(ActivityCode.Downloading);
            adapter.Redata(list);
            return;
        }
        if (v.getId() == R.id.taskUploadText) {
            getDownorUpload(ActivityCode.Upload);
            adapter.Redata(list);
            return;
        }
    }

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
                String db = df.format((double) downLoadingRsp.getBlockId() / (double) downLoadingRsp.getTotolSize());
                double d = Double.parseDouble(db) * 100;
                int present = (int) d;
                list.set(0, getItem(getResources().getDrawable(R.mipmap.download), downLoadingRsp.getFilename(), downLoadingRsp.getSpeed(), present + "%", present));
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
        adapter.Redata(list);
    }

    /**
     * 失败的接口
     *
     * @param position
     */
    @Override
    public void Fail(int position) {

    }

//	private void init() {
//		final String[] taskinfo = {
//				this.getResources().getString(R.string.delete_task),
//				this.getResources().getString(R.string.restart_task),
//				this.getResources().getString(R.string.jump_task) };
//		listView = (ExpandableListView) findViewById(R.id.expandable_list_view);
//		download_manager_clear = (Button) findViewById(R.id.download_manager_clear);
//		lists = TransportManagement.getInstance().getlinklist();
//		adapter = new MyExpandableAdapter(this, lists,new MainHandler(this));
//		listView.setGroupIndicator(null);
//		listView.setAdapter(adapter);
//		/*listView.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//					int arg2, long arg3) {
//				// TODO Auto-generated method stub
//				LG.i(getClass(), "长按listview  arg2===" + arg2);
//				arg2=arg2-1;
//				position = arg2;
//				ArrayList<String> s = new ArrayList<>();
//				if (TransportManagement.getInstance().getlinklist().get(arg2)
//						.getState() == ListItem.RUNNING) {
//					s.add(taskinfo[0]);
//					s.add(taskinfo[2]);
//				}
//				if (TransportManagement.getInstance().getlinklist().get(arg2)
//						.getState() == ListItem.FAILED) {
//					s.add(taskinfo[1]);
//					s.add(taskinfo[0]);
//				}
//				if (TransportManagement.getInstance().getlinklist().get(arg2)
//						.getState() == ListItem.WAITING) {
//					s.add(taskinfo[0]);
//				}
//				final ListDialog dialog = new ListDialog(
//						TransSportActivity.this, s);
//				dialog.setOnLongItemClick(new OnItemClickListener() {
//
//					@Override
//					public void onItemClick(AdapterView<?> arg0, View arg1,
//							int arg2, long arg3) {
//						// TODO Auto-generated method stub
//						LG.i(getClass(), "arg2==" + arg2);
//						ListItem current = TransportManagement.getInstance()
//								.getlinklist().get(position);
//						switch (arg2) {
//						case 0:
//							if (current.getState() == ListItem.FAILED) {
//								Util.removeListItem(current);
//								Util.removeQueueItem(current);
//							} else {
//								TransportManagement.getInstance().getQueue()
//								.add(current);
//						current.setState(ListItem.WAITING);
//							}
//						update(TransportManagement.getInstance().getlinklist());
//							dialog.dismiss();
//							break;
//						case 1:
//							current.setState(ListItem.WAITING);
//							Util.removeQueueItem(current);
//							TransportManagement.getInstance().getQueue().add(current);
//							synchronized (TransportManagement.currentLock) {
//								TransportManagement.currentLock.notifyAll();
//							}
//							update(TransportManagement.getInstance().getlinklist());
//							dialog.dismiss();
//							break;
//						default:
//							break;
//						}
//					}
//				});
//				dialog.show();
//				return false;
//			}
//		})；*/
//	}

//	private void onMyClick(View view) {
//		switch (view.getId()) {
//		case R.id.download_manager_clear:
//			new AlertDialog.Builder(this)
//					.setTitle(this.getResources().getString(R.string.tip))
//					.setMessage(
//							this.getResources().getString(
//									R.string.frag_more_ask_clear))
//					.setNegativeButton(
//							this.getResources().getString(R.string.confirm),
//							new OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int which) {
//									TransportManagement.getInstance()
//											.getlinklist().clear();
//									TransportManagement.getInstance()
//											.getQueue().clear();
//									TransportManagement.getInstance()
//											.getDownload();
//									download_manager_clear
//											.setVisibility(View.GONE);
//									download_manager_edit
//											.setText(TransSportActivity.this
//													.getResources().getString(
//															R.string.edit));
//								}
//							})
//					.setPositiveButton(
//							this.getResources().getString(R.string.cancel),
//							new OnClickListener() {
//
//								public void onClick(DialogInterface dialog,
//										int which) {
//
//								}
//							}).show();
//			break;
//
//		default:
//			break;
//		}
//	}
//
//	@Override
//	public void showdate() {
//		// TODO Auto-generated method stub
//
//	}
//
//	private Handler mHandler = new Handler() {
//
//		public void handleMessage(Message msg) {
//
//			switch (msg.what) {
//			case 100:
//				if (lists != null) {
//					if (adapter != null) {
//						adapter.setLists(lists);
//						adapter.notifyDataSetChanged();
//					} else {
//						adapter = new MyExpandableAdapter(
//								TransSportActivity.this, lists,null);
//					}
//				}
//				break;
//
//			default:
//				break;
//			}
//
//		};
//
//	};
//
//	@Override
//	public void update(LinkedList<ListItem> list) {
//		// TODO Auto-generated method stub
//		lists = list;
//		mHandler.sendEmptyMessage(100);
//	}

}
