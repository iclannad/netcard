package blink.com.blinkcard320.Tool;


import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


import blink.com.blinkcard320.Controller.Activity.FilePreviewActivity;
import blink.com.blinkcard320.Controller.ActivityCode;
import blink.com.blinkcard320.Controller.NetCardController;
import blink.com.blinkcard320.Moudle.Comment;
import blink.com.blinkcard320.Moudle.DownorUpload;
import blink.com.blinkcard320.Tool.Thread.HandlerImpl;
import blink.com.blinkcard320.Tool.Utils.SharedPrefsUtils;
import blink.com.blinkcard320.View.DownUpCallback;
import blink.com.blinkcard320.application.MyApplication;
import smart.blink.com.card.bean.DownLoadStartRsp;
import smart.blink.com.card.bean.DownLoadingRsp;


/**
 * Created by Ruanjiahui on 2017/1/6.
 * <p/>
 * 下载处理类
 */
public class DownUtils implements HandlerImpl {

    private static final String TAG = DownUtils.class.getSimpleName();

    private DownorUpload downorUpload = null;
    private static boolean isEnd = true;
    public static int count = 0;
    private static DownUpCallback downUpCallback = null;
    Context context;


    public DownUtils(Context context) {
        this.context = context;
        if (isEnd) {
            StartLoad();
        }
    }

    public static void setProgress(DownUpCallback downUpCallback) {
        DownUtils.downUpCallback = downUpCallback;
    }

    private String downLoadFileNamePath;

    // 同一时段只能下载一个任务
    private void StartLoad() {
        downorUpload = (DownorUpload) Comment.list.get(count);
        downLoadFileNamePath = downorUpload.getPath();
        //NetCardController.DownloadStart(downorUpload.getPath(), this);
        NetCardController.DownloadStart(downLoadFileNamePath, this);
        count++; //先暂时注释
        isEnd = false;
    }


    private DownLoadStartRsp downLoadStartRsp = null;


    /**
     * 处理线程返回的更新界面 （下载）
     *
     * @param position
     * @param object
     */
    @Override
    public void myHandler(int position, Object object) {
        if (position == ActivityCode.DownloadStart) {
            downLoadStartRsp = (DownLoadStartRsp) object;

            // 正在下载
            //NetCardController.DownLoading(Comment.FilePath, downLoadStartRsp.getFilename(), downLoadStartRsp.getTotalblock(), this);
            //NetCardController.DownLoading(Comment.FilePath, downLoadFileNamePath, downLoadStartRsp.getTotalblock(), this);
            // 获取下载的路径
            String downFilePath = SharedPrefsUtils.getStringPreference(context, Comment.DOWNFILE);
            String path = null;
            if (downFilePath != null) {
                path = downFilePath;
            } else {
                path = Environment.getExternalStorageDirectory() + "";
            }
            Log.e(TAG, "myHandler: " + "接收到下载请求，开始下载");
            NetCardController.DownLoading(path, downLoadFileNamePath, downLoadStartRsp.getTotalblock(), this);
        }

        if (position == ActivityCode.Downloading) {
            DownLoadingRsp downLoadingRsp = (DownLoadingRsp) object;
            // 存放在全局变量中
            MyApplication.getInstance().downLoadingRsp = downLoadingRsp;
            // 更新界面调用
            if (downUpCallback != null) {
                // 这条语句在子线程调用
                downUpCallback.Call(position, downLoadingRsp);
            }

            Log.e("Ruan", downLoadingRsp.getSpeed() + "--");
            isEnd = downLoadingRsp.isEnd();
            if (isEnd) {
                //Comment.list.remove(0); // 删除任务列表中的第一个任务
                if (count < Comment.list.size()) {
                    //下载完一个暂停一秒再下下一个
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    StartLoad();
                } else {
                    // 当下载完毕之后将所有的任务都清空
                    Comment.list.clear();
                    count = 0;  // 重新清0
                    final Activity activity = (Activity) this.context;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "任务下载完毕", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

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
        if (downUpCallback != null)
            downUpCallback.Fail(position);
        if (count < Comment.list.size()) {
            //下载完一个暂停一秒再下下一个
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            StartLoad();
        } else
            isEnd = true;
    }
}
