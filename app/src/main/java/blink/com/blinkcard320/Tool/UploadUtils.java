package blink.com.blinkcard320.Tool;


import blink.com.blinkcard320.Controller.ActivityCode;
import blink.com.blinkcard320.Controller.NetCardController;
import blink.com.blinkcard320.Moudle.Comment;
import blink.com.blinkcard320.Moudle.DownorUpload;
import blink.com.blinkcard320.Tool.Thread.HandlerImpl;
import blink.com.blinkcard320.View.DownUpCallback;
import smart.blink.com.card.bean.UploadReq;

/**
 * Created by Ruanjiahui on 2017/1/9.
 * <p/>
 * 上传处理类
 */
public class UploadUtils implements HandlerImpl {

    private DownorUpload downorUpload = null;
    private static boolean isEnd = true;
    public static int count = 0;
    private static DownUpCallback downUpCallback = null;


    public UploadUtils() {
        if (isEnd) {
            StartLoad();
        }
    }

    public static void setProgress(DownUpCallback downUpCallback) {
        UploadUtils.downUpCallback = downUpCallback;
    }

    private void StartLoad() {
        downorUpload = (DownorUpload) Comment.Uploadlist.get(count);
        NetCardController.Upload(downorUpload.getPath(), downorUpload.getName(), this);
        count++;
        isEnd = false;
    }

    /**
     * 处理线程返回的更新界面
     *
     * @param position
     * @param object
     */
    @Override
    public void myHandler(int position, Object object) {
        if (position == ActivityCode.Upload) {
            UploadReq uploadReq = (UploadReq) object;
            if (downUpCallback != null)
                downUpCallback.Call(position, uploadReq);
            isEnd = uploadReq.isEnd();
            if (isEnd) {
                if (count < Comment.Uploadlist.size()) {
                    //上传完一个暂停一秒再上传下一个
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
        if (count < Comment.Uploadlist.size()) {
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
