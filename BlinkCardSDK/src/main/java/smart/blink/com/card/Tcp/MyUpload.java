package smart.blink.com.card.Tcp;

import android.util.Log;

import java.io.File;

import smart.blink.com.card.API.BlinkWeb;
import smart.blink.com.card.BlinkNetCardCall;
import smart.blink.com.card.Tool.SendTools;
import smart.blink.com.card.Tool.TimerTaskCall;
import smart.blink.com.card.bean.MainObject;
import smart.blink.com.card.bean.UpLoadByServerRsp;

/**
 * Created by Administrator on 2017/4/1.
 */
public class MyUpload implements BlinkNetCardCall, TimerTaskCall {

    private static final String TAG = MyUpload.class.getSimpleName();
    public String path;
    public String filename;
    public BlinkNetCardCall call;
    public long wantblock = 0;
    public static int reqBlockId = 0;
    private final File file;

    public MyUpload(String path, String filename, BlinkNetCardCall call) {
        this.path = path;
        this.filename = filename;
        this.call = call;
        Log.e(TAG, "path===" + path + "   filename===" + filename);

        // 获得当前文件需要上传的总块数
        file = new File(path, filename);
        Log.e(TAG, "file.exists():  " + file.exists());
        Log.e(TAG, "file.length(): " + file.length());

        long block = file.length() / 1024;
        if (file.length() % 1024 != 0) {
            block++;
        }
        Log.e(TAG, "该文件的的上传的总块数是：  " + block);
        wantblock = block;

        startUpload();
    }

    private void startUpload() {
        Log.e(TAG, "startUpload: reqBlockId===" + reqBlockId);
        byte[] buffer = SendTools.UploadingOldVersion(reqBlockId, wantblock, filename, file);
        new TcpSocket(BlinkWeb.zIP, BlinkWeb.zPORT, buffer, 0, this);
        reqBlockId++;
    }

    @Override
    public void onSuccess(int position, MainObject mainObject) {
        UpLoadByServerRsp upLoadByServerRsp = (UpLoadByServerRsp) mainObject;
        if (upLoadByServerRsp.getSuccess() == 0) {
            //上传一块成功
            if (reqBlockId >= wantblock) {
                // 全部上传成功
                Log.e(TAG, "onSuccess: 全部块数上传成功");
                reqBlockId = 0;
                call.onSuccess(0, upLoadByServerRsp);
            } else {
                Log.e(TAG, "onSuccess: 当上传完成一块时，继续上传下一块");
                startUpload();
            }
        }
    }

    @Override
    public void onFail(int error) {

    }

    /**
     * 定时回调的接口
     */
    @Override
    public void TimerCall() {

    }
}
