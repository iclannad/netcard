package com.blink.blinkp2p.Tool.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.blink.blinkp2p.Controller.Activity.TaskDeleteImpl;

import com.blink.blinkp2p.Moudle.Comment;
import com.blink.blinkp2p.R;
import com.example.administrator.ui_sdk.DensityUtil;
import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;

import java.util.ArrayList;

import com.blink.blinkp2p.Moudle.Item;
import com.blink.blinkp2p.Moudle.ViewHolder;

import smart.blink.com.card.API.BlinkWeb;

/**
 * Created by Ruanjiahui on 2017/1/9.
 */
public class DownUpAdapter extends BaseAdapter {

    private static final String TAG = DownUpAdapter.class.getSimpleName();
    private Context context = null;
    private ArrayList<Object> list = null;
    private ViewHolder viewHolder = null;
    TaskDeleteImpl taskDelete = null;
    int type;   // 上传下载类型

    public DownUpAdapter(Context context, ArrayList<Object> list, TaskDeleteImpl taskDelete, int type) {
        this.context = context;
        this.list = list;
        this.taskDelete = taskDelete;
        this.type = type;
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Item item = (Item) list.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.duadpter, null);
            viewHolder = new ViewHolder(convertView, "DUAdapter");

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();


        viewHolder.duImage.setImageDrawable(item.getListImage());
        if (item.getListText() != null || !"".equals(item.getListText()))
            viewHolder.titleText.setText(item.getListText());
        viewHolder.speedText.setText(item.getListRightText1());
        viewHolder.presentText.setText(item.getListRightText());
        viewHolder.speedBar.setProgress(item.getProgressBar());
//        // 任务删除
        viewHolder.taskDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle(
                                context.getResources().getString(
                                        R.string.delete_file))
                        .setMessage(
                                context.getResources().getString(
                                        R.string.delete_currenttask))
                        .setNegativeButton(
                                context.getResources().getString(R.string.yes),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
//                                        if (BlinkWeb.STATE == BlinkWeb.UDP) {
                                            if (taskDelete != null) {
                                                // 此处应该传入要删除任务id
                                                taskDelete.delete(item.id, type);
                                            }
//                                        } else {
//                                            list.remove(position);
//                                            DownUpAdapter.this.notifyDataSetChanged();
//                                            if (type == Comment.DOWNLOAD) {
//                                                Comment.list.remove(position);
//                                            } else {
//                                                Comment.Uploadlist.remove(position);
//                                            }
//                                        }

                                    }
                                })
                        .setPositiveButton(R.string.no,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                }).show();

            }
        });
        if (item.getHeight() != 0) {
            DensityUtil.setAbsSize(viewHolder.duLinear, BaseActivity.width, item.getHeight());
        }

        return convertView;
    }


    public void Redata(ArrayList<Object> list, int type) {
        this.list = list;
        this.notifyDataSetChanged();
        this.type = type;
    }
}
