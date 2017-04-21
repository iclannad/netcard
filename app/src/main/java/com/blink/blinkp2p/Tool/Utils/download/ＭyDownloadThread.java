package com.blink.blinkp2p.Tool.Utils.download;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.blink.blinkp2p.Controller.ActivityCode;
import com.blink.blinkp2p.Controller.NetCardController;
import com.blink.blinkp2p.Moudle.Comment;
import com.blink.blinkp2p.Moudle.DownorUpload;
import com.blink.blinkp2p.R;
import com.blink.blinkp2p.Tool.Dao.MsgDAO;
import com.blink.blinkp2p.Tool.Thread.HandlerImpl;
import com.blink.blinkp2p.Tool.Utils.SharedPrefsUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import smart.blink.com.card.bean.DownLoadStartRsp;
import smart.blink.com.card.bean.DownLoadingRsp;

/**
 * Created by Administrator on 2017/4/13.
 * <p/>
 * 注意：在这里我并没有考虑失败的情况
 */
public class ＭyDownloadThread extends Thread implements HandlerImpl {

    private static final String TAG = ＭyDownloadThread.class.getSimpleName();
    private DownorUpload downorUpload;
    private Context context;
    private ThreadHandlerImpl threadHandler;
    private DownloadingImpl downloading;
    private int position;

    // 构造方法
    public ＭyDownloadThread(int position, DownorUpload downorUpload, Context context, ThreadHandlerImpl threadHandler, DownloadingImpl downloading) {
        this.downorUpload = downorUpload;
        this.context = context;
        this.threadHandler = threadHandler;
        this.position = position;
        this.downloading = downloading;
        Log.e(TAG, "ＭyDownloadThread: 开启下载任务:" + downorUpload.getName());
    }

    @Override
    public void run() {
        NetCardController.DownloadStart(downorUpload.getPath(), this);
    }

    /**
     * 处理线程返回的更新界面
     *
     * @param position
     * @param object
     */
    @Override
    public void myHandler(int position, Object object) {
        if (position == ActivityCode.DownloadStart) {
            DownLoadStartRsp downLoadStartRsp = (DownLoadStartRsp) object;
            // 正在下载
            // 获取下载的路径
            String downFilePath = SharedPrefsUtils.getStringPreference(context, Comment.DOWNFILE);
            String path = null;
            if (downFilePath != null) {
                path = downFilePath;
            } else {
                path = Environment.getExternalStorageDirectory() + "";
            }
            NetCardController.DownLoading(path, downorUpload.getPath(), downLoadStartRsp.getTotalblock(), this);
        }
        if (position == ActivityCode.Downloading) {
            DownLoadingRsp downLoadingRsp = (DownLoadingRsp) object;
            // 如果当前的回调只是更新ui，那么不会开启一个任务
            if (!downLoadingRsp.isEnd()) {
                downloading.downloading(this.position, downLoadingRsp);
                return;
            }

            if (context != null) {
                // 当下载完成的时候，将数据保存在本地数据库中
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                MsgDAO msgdao = new MsgDAO(context);
                msgdao.insertdb(df.format(new Date()),
                        context.getResources().getString(R.string.pc),
                        context.getResources().getString(R.string.send),
                        context.getResources().getString(R.string.phone),
                        null);
                msgdao.close();
            }

            Log.e(TAG, "ＭyDownloadThread: 任务下载完成:" + downorUpload.getName());
            // 下载完成后的回调
            threadHandler.finishTask(this.position);
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
        if (position == ActivityCode.DownloadStart) {

        }

        if (position == ActivityCode.Downloading) {

        }
    }
}
