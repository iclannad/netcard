package smart.blink.com.card.Udp;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Timer;

import smart.blink.com.card.API.BlinkLog;
import smart.blink.com.card.API.Protocol;
import smart.blink.com.card.BlinkNetCardCall;
import smart.blink.com.card.Udp.File.FileRead;
import smart.blink.com.card.Tool.MyTimerTask;
import smart.blink.com.card.Tool.SendTools;
import smart.blink.com.card.Tool.TimerTaskCall;
import smart.blink.com.card.bean.MainObject;
import smart.blink.com.card.bean.UploadReq;

/**
 * Created by Ruanjiahui on 2017/1/12.
 * <p/>
 * 上传处理类
 */
public class Upload implements BlinkNetCardCall, TimerTaskCall {

    private FileRead fileRead = null;
    private int[] countArray = null;
    private boolean[] threadArray = null;
    private Socket[] socketArray = null;
    private int[] downList = null;
    private int[] speedArray = null;

    private long size = 0;

    private int count = 0;
    private final static int downSize = 1024 * 10;
    private final static int countThread = 5;
    private int length = 0;

    private Timer timer = null;

    private int total = 0;
    private String IP = null;
    private int PORT = 0;
    private String filename = null;

    private BlinkNetCardCall call = null;
    private UploadReq uploadReq = null;

    public Upload(final String IP, final int PORT, final String filename, String path, int position, BlinkNetCardCall call) {

        this.IP = IP;
        this.PORT = PORT;
        this.filename = filename;
        this.call = call;

        uploadReq = new UploadReq();
        fileRead = new FileRead(path);
        size = fileRead.getFileSize() / 1024;
        if (fileRead.getFileSize() % 1024 != 0)
            size += 1;

        //计算文件大小开启线程数
        if ((size % downSize) == 0) {
            count = (int) (size / downSize);
        } else
            count = (int) (size / downSize) + 1;

        countArray = new int[count + 1];
        threadArray = new boolean[count + 1];
        socketArray = new Socket[count + 1];
        speedArray = new int[count + 1];
        downList = new int[count + 1];

        BlinkLog.Print(count);
        length = count;
        if (count > countThread)
            length = countThread;


        timer = new Timer();
        timer.schedule(new MyTimerTask(this), 0, 5000);

        for (int i = 1; i <= length; i++) {
            int flag = i;
            downList[i] = 1;
            StartThread(IP, PORT, flag, filename);
        }
    }

    /**
     * 开启新的链路
     *
     * @param IP
     * @param PORT
     * @param flag
     * @param filename
     */
    private void StartThread(final String IP, final int PORT, final int flag, final String filename) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int k = flag;
                int j = 1;
                threadArray[k] = true;
                Socket socket = null;
                DataInputStream in = null;
                DataOutputStream out = null;
                //初始化接收池
                byte[] buf = null;
                //id为0说明现在请求处于请求上传的状态，如果id为1说明现在处于正在上传状态
                int id = 0;
                while (threadArray[k]) {
                    if (socket == null) {
                        try {
                            socket = new Socket(IP, PORT);
                            buf = new byte[2];
                            in = new DataInputStream(socket.getInputStream());
                            out = new DataOutputStream(socket.getOutputStream());
                        } catch (IOException e) {
                            BlinkLog.Error(e.toString());
                        }
                        //设置超时为2秒
                        try {
                            socket.setSoTimeout(5000);
                        } catch (SocketException e) {
                            BlinkLog.Error(e.toString());
                        }
                        socketArray[k] = socket;
                    }
                    byte[] buffer = null;
                    if (id == 0) {
                        buffer = SendTools.Uploading(k, filename);
//                        BlinkLog.Print(Arrays.toString(buffer));
                    } else {
                        UploadReq uploadReq = fileRead.Read(j, k);
                        buffer = SendTools.Uploading(uploadReq);

                    }
                    try {
                        out.write(buffer);
                        out.flush();
                    } catch (IOException e) {
                        BlinkLog.Error(e.toString());
                    }

                    try {
                        in.read(buf);
                        if (buf[0] == Protocol.UploadingReviced) {
//                            BlinkLog.Print(Arrays.toString(buf));
                            //上传请求验证成功，将id转成1
                            id = 1;
                        }
                        if (buf[0] == Protocol.UploadingReviced1) {
                            //上传成功之后才允许上传下一个
                            //将下载完成写入数组里面进行统计
                            countArray[k] = j;
                            j++;
                            onSuccess(k, null);
                        }
                        //重新上传
                        if (buf[0] == Protocol.RestartUpload) {
                            //重新上传不用修改下表，继续传相同的数据
                        }
                    } catch (IOException e) {
                        BlinkLog.Error(e.toString());
                    }

                }
            }
        }).start();
    }


    /**
     * 调用队列的方法
     */
    private void WaitStart() {
        //每次下载完成一个模块数就加一
        total++;
        for (int i = 1; i < downList.length; i++) {
            //判断队列里面还有没有没有下载完成的模块
            if (downList[i] == 0) {
                StartThread(IP, PORT, i, filename);
                downList[i] = 1;
                return;
            }
        }

        //如果下载完成模块数等于总模块数说明下载完成
        if (total == count) {
            //如果downList队列里面没有下载模块说明就是下载完成
            //这个时候取消定时器
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            //最后一次返回数据给界面
            totalSpeed();
            //关闭写入流
            fileRead.Close();
        }
    }

    /**
     * 关闭下载完成的链路
     *
     * @param position
     */
    private void CloseSocket(int position) {
        try {
            if (socketArray[position] != null && socketArray[position].isConnected()) {
                socketArray[position].close();
            }
        } catch (IOException e) {
            BlinkLog.Error(e.toString());
        }
    }

    /**
     * 发送关闭链路
     *
     * @param position
     */
    private void SendClose(int position) {
        Socket sockets = socketArray[position];
        try {
            DataOutputStream out = new DataOutputStream(sockets.getOutputStream());
            out.write(SendTools.UploadClose());
            out.flush();
        } catch (IOException e) {
        }
    }

    @Override
    public void onSuccess(int position, MainObject mainObject) {
        if (position == count) {
            if (countArray[position] == size % downSize) {
                SendClose(position);
                threadArray[position] = false;
                BlinkLog.Print("上传完成");
                CloseSocket(position);
                WaitStart();
            }
        } else {
            if (countArray[position] == downSize) {
                SendClose(position);
                threadArray[position] = false;
                CloseSocket(position);
                BlinkLog.Print("第" + position + "个链路上传完成");
                WaitStart();
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
        totalSpeed();
    }

    private int speed = 0;
    private int totalSize = 0;
    private int lateSize = 0;

    /**
     * 统计下载速度和下载进度
     */
    private void totalSpeed() {
        totalSize = 0;
        lateSize = 0;
        for (int i = 1; i < count + 1; i++) {
            totalSize += countArray[i];
            lateSize += speedArray[i];
            speedArray[i] = countArray[i];
        }
        speed = totalSize - lateSize;
        uploadReq.setSpeed(speed / 5 + "K/S");
        uploadReq.setBlockID(totalSize);
        uploadReq.setBlockSize((int) size);
        uploadReq.setFilename(fileRead.getFilename());
        call.onSuccess(Protocol.Downloading, uploadReq);
    }
}