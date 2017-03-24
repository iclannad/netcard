package blink.com.blinkcard320.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import blink.com.blinkcard320.R;

/**
 * Created by Runajiahui on 15-11-13.
 */
@SuppressLint("NewApi")
public class FilePathLineayout extends LinearLayout {

	private ArrayList<Map<String, Object>> list;

	private ArrayList<Map<String, Object>> showlist;

	public static final String myname = "name";
	public static final String mypath = "path";
	public static final String myView = "view";
	public static final String myPathView = "mypathview";
	private Handler mhandler;
	private Context context;
	private boolean isfull = false;

	public FilePathLineayout(Context context) {
		super(context);
		init(context);
	}

	public FilePathLineayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public FilePathLineayout(Context context, AttributeSet attrs,
							 int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public FilePathLineayout(Context context, AttributeSet attrs,
							 int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		list = new ArrayList<>();
		showlist = new ArrayList<>();
		
	}
	public void addhead(String path){
		pushView(context.getResources().getString(R.string.dir),path, context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public void pushView(String name, String path, Context context) {
		final TextView tv = new TextView(context);
		tv.setTag(list.size());
		if (list.size() == 0 || name.equals("/")
				|| name.equals(context.getResources().getString(R.string.dir))) {
			tv.setText(R.string.dir);
		} else {
			tv.setText(name + " ");
		}
		LayoutParams layoutParams = new LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(10, 10, 10, 10);// 4个参数按顺序分别是左上右下
		tv.setLayoutParams(layoutParams);
		tv.setTextSize(15);
		tv.setTextColor(Color.WHITE);
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				NoteActivity((int) tv.getTag());
			}
		});
		listAddView(tv, name, path);
		checkStringlen(context);
	}

	private void listAddView(TextView tv, String name, String path) {
		ImageView iv = new ImageView(context);
		iv.setLayoutParams(new LayoutParams((int)context.getResources().getDimension(R.dimen.marginBig),
				(int)context.getResources().getDimension(R.dimen.marginBig)));
		iv.setBackgroundResource(R.mipmap.split);
		Map<String, Object> map = new HashMap<>();
//		iv.setPadding(0 , (int)context.getResources().getDimension(R.dimen.margin) , 0 , (int)context.getResources().getDimension(R.dimen.margin));
		map.put(myView, tv);
		map.put(myname, name);
		map.put(mypath, path);
		map.put(myPathView, iv);
		list.add(map);
		showlist.add(map);
		addView(tv);
		addView(iv);
	}

	private void checkStringlen(final Context context) {
		int childlen = 0;
		for (int i = 0; i < list.size(); i++) {
			View v = (View) list.get(i).get(myView);
			childlen += v.getWidth();
		}

		if (childlen > getWidth() / 2) {
			isfull = true;
			subTextView();
		}

		if (isfull == true && childlen <= getWidth() / 2) {
			isfull = false;
			showAllpath();
		}
		Log.d("run", "len=" + childlen);
		return;
	}

	private void showAllpath() {
		removeAllViews();
		ArrayList<Map<String, Object>> tmplist = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			tmplist.add(list.get(i));
		}
		list.clear();
		for (int i = 0; i < tmplist.size(); i++) {
			pushView(tmplist.get(i).get(myname).toString(),
					tmplist.get(i).get(mypath).toString(), context);
		}
	}

	private void subTextView() {
		removeAllViews();
		final TextView tvfirst = new TextView(context);
		tvfirst.setTag(0);
		tvfirst.setText(context.getResources().getString(R.string.dir));
		tvfirst.setTextSize(15);
		tvfirst.setTextColor(Color.WHITE);
		tvfirst.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				list.clear();
				removeAllViews();
				isfull = false;
				pushView("/", "/", context);
				NoteActivity(0);
			}
		});
		addView(tvfirst);
		ImageView iv = new ImageView(context);
		iv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT));
		iv.setLayoutParams(new LayoutParams((int)context.getResources().getDimension(R.dimen.marginBig),
				(int)context.getResources().getDimension(R.dimen.marginBig)));
		iv.setBackgroundResource(R.mipmap.split);
		addView(iv);
		TextView tvback = new TextView(context);
		tvback.setText("  . . .  ");
		tvback.setTextSize(15);
		tvback.setTextColor(Color.WHITE);
		tvback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pullView(list.size() - 2);
				checkStringlen(context);
			}
		});
		ImageView iv3 = new ImageView(context);
		iv3.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT));
		iv3.setBackgroundResource(R.mipmap.split);

		addView(tvback);
		addView(iv3);
		if (list.size() - 1 <= 0) {
			return;
		}
		addView((View) list.get(list.size() - 1).get(myView));
		ImageView iv2 = new ImageView(context);
		iv2.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT));
		iv2.setBackgroundResource(R.mipmap.split);
		addView(iv2);
	}

	public void NoteActivity(int position) {

		Message msg = new Message();
		msg.what = 0;
		try {
			msg.obj = list.get(position).get(mypath).toString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		mhandler.sendMessage(msg);

	}

	public void pullViewString(String path) {
		for (int i = list.size() - 1; i >= 0; i--) {
			View v = (View) list.get(i).get(myView);
			if (!list.get(i).get(mypath).equals(path)) {
				View pathview = (View) list.get(i).get(myPathView);
				removeView(v);
				removeView(pathview);
				list.remove(i);
			} else {
				invalidate();
				return;
			}
		}
		invalidate();
	}

	public void pullView(int position) {
		Log.d("run", "postion=======" + position);
		for (int i = list.size() - 1; i >= 0; i--) {
			View v = (View) list.get(i).get(myView);
			if ((int) (v.getTag()) != position) {
				removeView(v);
				int iposition = getChildCount() - 1;
				removeViewAt(iposition);
				list.remove(i);
			} else {
				invalidate();
				NoteActivity(i);
				checkStringlen(context);
				return;
			}
		}
		invalidate();
		NoteActivity(0);
	}

	public void sethandler(Handler mhandler) {
		this.mhandler = mhandler;
	}

}
