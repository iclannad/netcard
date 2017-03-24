package blink.com.blinkcard320.Tool;


import android.util.Log;


import blink.com.blinkcard320.Controller.ActivityCode;
import blink.com.blinkcard320.Controller.NetCardController;
import blink.com.blinkcard320.Moudle.Comment;
import blink.com.blinkcard320.Moudle.DownorUpload;
import blink.com.blinkcard320.Tool.Thread.HandlerImpl;
import blink.com.blinkcard320.View.DownUpCallback;
import smart.blink.com.card.bean.DownLoadStartRsp;
import smart.blink.com.card.bean.DownLoadingRsp;


/**
 * Created by Ruanjiahui on 2017/1/6.
 * <p/>
 * 下载处理类
 */
public class DownUtils implements HandlerImpl {

    private DownorUpload downorUpload = null;
    private static boolean isEnd = true;
    public static int count = 0;
    private static DownUpCallback downUpCallback = null;

    public DownUtils() {
        if (isEnd) {
            StartLoad();
        }
    }


    public static void setProgress(DownUpCallback downUpCallback) {
        DownUtils.downUpCallback = downUpCallback;
    }


    private void StartLoad() {
        downorUpload = (DownorUpload) Comment.list.get(count);
        NetCardController.DownloadStart(downorUpload.getPath(), this);
        count++;
        isEnd = false;
    }


    private DownLoadStartRsp downLoadStartRsp = null;

    /**
     * 处理线程返回的更新界面
     *
     * @param position
     * @param object
     */
    @Override
    public void myHandler(int position, Object object) {
        if (position == ActivityCode.DownloadStart) {
            downLoadStartRsp = (DownLoadStartRsp) object;
            //正在下载
            NetCardController.DownLoading(Comment.FilePath, downLoadStartRsp.getFilename(), downLoadStartRsp.getTotalblock(), this);
        }

        if (position == ActivityCode.Downloading) {
            DownLoadingRsp downLoadingRsp = (DownLoadingRsp) object;
            if (downUpCallback != null)
                downUpCallback.Call(position, downLoadingRsp);
            Log.e("Ruan" , downLoadingRsp.getSpeed() + "--");
            isEnd = downLoadingRsp.isEnd();
            if (isEnd) {
                if (count < Comment.list.size()) {
                    //下载完一个暂停一秒再下下一个
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    StartLoad();
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
