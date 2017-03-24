package smart.blink.com.card.Tcp;

import android.os.Handler;
import android.os.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;

import smart.blink.com.card.API.ErrorNo;
import smart.blink.com.card.API.Protocol;
import smart.blink.com.card.BlinkNetCardCall;
import smart.blink.com.card.Tcp.File.FileRead;
import smart.blink.com.card.Tool.MyTimerTask;
import smart.blink.com.card.Tool.SendTools;
import smart.blink.com.card.Tool.TimerTaskCall;
import smart.blink.com.card.bean.UploadReq;

/**
 * Created by Ruanjiahui on 2016/12/22.
 * <p/>
 * 上传操作类
 */
public class Upload implements TimerTaskCall {

    private Socket socket = null;
    private static UploadReq uploadReq = null;

    private DataInputStream in = null;
    private DataOutputStream out = null;
    //判断是否结束上传
    private boolean isUpload = true;
    //判断是否可以上传
    private boolean isUploading = true;

    //当前上传的模块数
    private int wanting = 0;
    //回调给主界面的方法
    private BlinkNetCardCall call = null;

    //读本地文本的对象
    private FileRead fileRead = null;
    //接收内存池
    private byte[] buf = null;
    //判断是否超时
    private boolean timeout = false;
    //超时次数
    private int TimeoutTotal = 0;
    //上传速度
    private int speed = 0;
    //定时器
    private Timer timer = null;

    public Upload(String path, String filename , BlinkNetCardCall call) {
        this.call = call;
        socket = TcpSocket.getSocket();
        uploadReq = null;
        buf = new byte[264];
        fileRead = new FileRead(path , filename);
        timer = new Timer();


        new Thread(new Runnable() {
            @Override
            public void run() {
                Uploading();
            }
        }).start();
    }

    private void Uploading() {
        wanting = 0;
        timer.schedule(new MyTimerTask(this), 0, 5000);
        while (isUpload) {
            if (isUploading) {
                if (uploadReq == null)
                    uploadReq = new UploadReq();

                isUploading = false;
                uploadReq.setBlockID(wanting);

                //如果false说明没有读完，true已经读完说明已经上传完成
                if (!uploadReq.isRead()) {
                    Send();
                } else {
                    isUpload = false;
                    timer.cancel();
                }

                if (!timeout)
                    wanting++;
                isUploading = true;
            }
        }

        buf = null;
        fileRead.Close();
    }


    private void Send() {
        try {
            if (out == null)
                out = new DataOutputStream(socket.getOutputStream());


            //将要上传的数据转化成字节数组
            byte[] b = SendTools.Uploading(fileRead.Read(uploadReq));
            out.write(b);


            if (in == null)
                in = new DataInputStream(socket.getInputStream());
            in.read(buf);
            //处理上传是否成功的方法

        } catch (IOException e) {
//                            e.printStackTrace();
            timeout = true;
            TimeoutTotal++;
            //超时超过10则停止上传
            //回调超时的接口
            if (TimeoutTotal == 10) {
                isUpload = false;
                fileRead.Close();
                call.onFail(ErrorNo.ErrorTimeout);
            }
        }
    }

    /**
     * 定时回调的接口
     */
    @Override
    public void TimerCall() {
        //计算上传的速度
        String total = (wanting - speed) / 5 + "K/S";
        uploadReq.setSpeed(total);

        Message message = new Message();
        handler.sendMessage(message);

        speed = wanting;
    }

    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            call.onSuccess(Protocol.Uploading, uploadReq);
        }
    };
}
