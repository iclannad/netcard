package smart.blink.com.card.Tcp;

import android.os.Handler;
import android.os.Message;
import android.util.Log;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;

import smart.blink.com.card.API.ErrorNo;
import smart.blink.com.card.API.Protocol;
import smart.blink.com.card.BlinkNetCardCall;
import smart.blink.com.card.Tcp.File.FileWrite;
import smart.blink.com.card.Tool.MyTimerTask;
import smart.blink.com.card.Tool.RevicedTools;
import smart.blink.com.card.Tool.SendTools;
import smart.blink.com.card.Tool.TimerTaskCall;
import smart.blink.com.card.bean.DownLoadingRsp;
import smart.blink.com.card.bean.MainObject;

/**
 * Created by Ruanjiahui on 2016/12/21.
 * <p/>
 * 下载文件的操作
 */
public class Down implements BlinkNetCardCall, TimerTaskCall {

    private Socket socket = null;
    //是否是下载结束的标识符
    private boolean isDown = true;
    //是否可以下载
    private boolean isDowning = true;
    //下载中的模块数
    private int wanting = 0;

    //输入输出流
    private DataOutputStream out = null;
    private DataInputStream in = null;
    //文件名称
    private String filename = null;
    //接收数据的内存
    private byte[] buf = null;
    //下载模块总数
    private int wantblock = 0;
    //写入文件的对象
    private FileWrite fileWrite = null;
    //每次更新下标
    private int n = 0;
    //超时的标识
    private boolean timeout = false;
    //暂时存储的内存池
    private byte[] memory = null;
    //定时器
    private Timer timer = null;
    //统计下载速度
    private int speed = 0;
    //回调的接口
    private BlinkNetCardCall call = null;
    //下载超时超过10次则自动停止下载
    private int TimeoutTotal = 0;


    public Down(String path, String filename, int wantblock, BlinkNetCardCall call) {
        socket = TcpSocket.getSocket();
        isDown = true;
        isDowning = true;
        this.call = call;
        this.filename = filename;
        downLoadingRsp = null;
        timer = new Timer();
        this.wantblock = wantblock;
        memory = new byte[51200];
        fileWrite = new FileWrite(path, filename);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Downing();
            }
        }).start();
    }

    private void Downing() {
        wanting = 0;
        timer.schedule(new MyTimerTask(this), 0, 5000);
        while (isDown) {
            if (isDowning) {
                isDowning = false;
                timeout = false;

                //如果当前模块等于总数则说明下载完成
                if (wanting == wantblock) {
                    //下载完成告诉服务器
//                    TcpUtils.DownLoadEnd(filename , call);

                    isDown = false;
                    fileWrite.Close();
                    timer.cancel();
                    return;
                }
                //发送数据
                Send();

                //继续可以下载
                isDowning = true;
                //判断是否超时，超时false不超时，true为超时，
                //超时则不继续下载下一个模块，不超时才下载下一个模块
                if (!timeout) {
                    //如果下载成功一个则恢复归0
                    TimeoutTotal = 0;
                    //下载完成一个模块继续下载另一个模块
                    wanting++;
                    n++;
                } else {
                    TimeoutTotal++;
                    if (TimeoutTotal == 10) {
                        isDown = false;
                        isDowning = false;
                        fileWrite.Close();
                        timer.cancel();
                        call.onFail(ErrorNo.ErrorTimeout);
                    }
                }
            }
        }
    }

    private void Send() {
        try {
            if (out == null)
                out = new DataOutputStream(socket.getOutputStream());
            //发送数据
            out.write(SendTools.Downloading(wanting, filename));

            if (in == null)
                in = new DataInputStream(socket.getInputStream());
            buf = new byte[1296];
            int length = in.read(buf);

            //将下载的数据保存到一个内存池里面，等到一定数量之后写入文本
            new RevicedTools(Protocol.Downloading, buf, length, this);

            //清空等待回收内存
            buf = null;

        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e("System.out" , "超时");
            //下载超时则重下
            timeout = true;
            buf = null;
        }
    }

    @Override
    public void onSuccess(int position, MainObject mainObject) {
        DownLoadingRsp downLoadingRsp = (DownLoadingRsp) mainObject;

        //每50个字节数组写一次进文本
        if ((wanting) % 50 == 0 && wanting != 0) {
            n = 0;
            fileWrite.Write(memory);
            memory = null;
        }
        //如果内存池的内存为空则创建新的内存
        if (memory == null)
            memory = new byte[51200];
        //将每块字节流写入缓冲池
        for (int i = 0; i < downLoadingRsp.getData().length; i++) {
            memory[i + n * 1024] = downLoadingRsp.getData()[i];
        }
        //设置还没有下载完成
        if (Down.downLoadingRsp != null)
            Down.downLoadingRsp.setEnd(false);

        //如果等于最后一块则写入
        if (wanting == wantblock - 1) {
            Down.downLoadingRsp.setEnd(true);
            byte[] b = new byte[n * 1024 + downLoadingRsp.getData().length];
            for (int i = 0; i < b.length; i++)
                b[i] = memory[i];
            fileWrite.Write(b);
            CallObject();
        }
    }

    @Override
    public void onFail(int error) {
        Log.e("System.out", "验证码出错");
        Send();
        buf = null;
    }

    private static DownLoadingRsp downLoadingRsp = null;

    /**
     * 定时回调的接口
     */
    @Override
    public void TimerCall() {
        CallObject();
    }

    private void CallObject() {
        String DSpeed = (wanting - speed) / 5 + "K/S";
        if (downLoadingRsp == null)
            downLoadingRsp = new DownLoadingRsp();

        downLoadingRsp.setSpeed(DSpeed);
        downLoadingRsp.setBlockId(wanting);
        downLoadingRsp.setTotolSize(wantblock);


        Message message = new Message();
        handler.sendMessage(message);

        speed = wanting;
    }


    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            call.onSuccess(Protocol.Downloading, downLoadingRsp);
        }
    };
}
