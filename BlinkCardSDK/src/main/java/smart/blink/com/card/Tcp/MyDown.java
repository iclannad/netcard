package smart.blink.com.card.Tcp;

import android.util.Log;
import android.view.LayoutInflater;

import java.util.Timer;

import smart.blink.com.card.API.BlinkWeb;
import smart.blink.com.card.BlinkNetCardCall;
import smart.blink.com.card.Tool.FileWriteStream;
import smart.blink.com.card.Tool.MyTimerTask;
import smart.blink.com.card.Tool.SendTools;
import smart.blink.com.card.Tool.TimerTaskCall;
import smart.blink.com.card.bean.DownLoadingRsp;
import smart.blink.com.card.bean.MainObject;

/**
 * Created by Administrator on 2017/3/31.
 * <p/>
 * 用MyDown来封装TcpSocket
 */
public class MyDown implements BlinkNetCardCall, TimerTaskCall {

    private static final String TAG = MyDown.class.getSimpleName();
    BlinkNetCardCall call;
    Timer timer;

    String path;
    String filename;
    int wantblock;
    public static int reqBlockId = 0;
    public static int lastReqBlockId = 0;

    String transport_filename = "";

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

        // 获取下载文件的名字
        String[] strings = filename.split("\\\\");
        int length = strings.length - 1;
        this.transport_filename = strings[length];
        Log.e(TAG, "MyDown: transport_filename===" + transport_filename);

        timer = new Timer();
        timer.schedule(new MyTimerTask(this), 0, 5000);

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
        DownLoadingRsp downLoadingRsp = (DownLoadingRsp) mainObject;
        byte[] wdata = downLoadingRsp.data;
        if (downLoadingRsp.getSuccess() == 0) {
            if (reqBlockId >= wantblock) {
                // 最后一次下载成功
                downLoadingRsp.setBlockId(reqBlockId);
                downLoadingRsp.setTotolSize(wantblock);
                String speed = (double) (reqBlockId - lastReqBlockId) / 5 + "k/s";
                lastReqBlockId = 0;
                downLoadingRsp.setFilename(transport_filename);
                transport_filename = "";
                downLoadingRsp.setEnd(true);
                downLoadingRsp.setSpeed(speed);

                Log.e(TAG, "onSuccess: " + "下载成功");
                FileWriteStream.writebigblockfileEnd((reqBlockId - 1) % 100, wdata);
                call.onSuccess(0, downLoadingRsp);
                reqBlockId = 0;

                // 关闭定时器
                timer.cancel();
                timer = null;
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
        DownLoadingRsp downLoadingRsp = new DownLoadingRsp();
        downLoadingRsp.setBlockId(reqBlockId);
        downLoadingRsp.setTotolSize(wantblock);
        // 这里速度不能这么算，这个星期要修改
        String speed = (double) (reqBlockId - lastReqBlockId) / 5 + "k/s";
        lastReqBlockId = reqBlockId;
        downLoadingRsp.setFilename(transport_filename);
        downLoadingRsp.setSpeed(speed);
        // 该次回调只会更新ui，不会开启下一个任务
        downLoadingRsp.setEnd(false);

        call.onSuccess(0, downLoadingRsp);
    }
}
