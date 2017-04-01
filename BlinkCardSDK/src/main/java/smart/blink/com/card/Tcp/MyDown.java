package smart.blink.com.card.Tcp;

import android.util.Log;

import smart.blink.com.card.API.BlinkWeb;
import smart.blink.com.card.BlinkNetCardCall;
import smart.blink.com.card.Tool.FileWriteStream;
import smart.blink.com.card.Tool.SendTools;
import smart.blink.com.card.Tool.TimerTaskCall;
import smart.blink.com.card.bean.DownLoadByServerRsp;
import smart.blink.com.card.bean.MainObject;

/**
 * Created by Administrator on 2017/3/31.
 * <p/>
 * 用MyDown来封装TcpSocket
 */
public class MyDown implements BlinkNetCardCall, TimerTaskCall {

    private static final String TAG = MyDown.class.getSimpleName();
    BlinkNetCardCall call;
    String path;
    String filename;
    int wantblock;
    public static int reqBlockId = 0;


    /**
     * 构造方法
     *
     * @param path
     * @param filename
     * @param wantblock
     * @param call
     */
    public MyDown(String path, String filename, int wantblock, BlinkNetCardCall call) {
        this.path = path;
        this.filename = filename;
        this.wantblock = wantblock;
        this.call = call;
        Log.e(TAG, "MyDown: 当前想下载的所有总块数为：wantblock: " + wantblock);

        startDownLoad();
    }


    private void startDownLoad() {
        Log.e(TAG, "init: 文件的名字是:" + filename);
        byte[] buffer = SendTools.DownloadingOldVersion(reqBlockId, filename);
        new TcpSocket(BlinkWeb.zIP, BlinkWeb.zPORT, buffer, 0, this);
        reqBlockId++;
    }

    @Override
    public void onSuccess(int position, MainObject mainObject) {
        DownLoadByServerRsp downLoadByServerRsp = (DownLoadByServerRsp) mainObject;
        byte[] wdata = downLoadByServerRsp.data;
        if (downLoadByServerRsp.getSuccess() == 0) {
            if (reqBlockId >= wantblock) {
                Log.e(TAG, "onSuccess: " + "下载成功");
                FileWriteStream.writebigblockfileEnd((reqBlockId - 1) % 100, wdata);
                call.onSuccess(0, downLoadByServerRsp);
                reqBlockId = 0;
            } else {
                // 将获取到数据包写入文件中
                FileWriteStream.writebigblockfile((reqBlockId - 1) % 100, wdata);
                // 当下载完成一块时，继续下载下一块
                startDownLoad();
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
