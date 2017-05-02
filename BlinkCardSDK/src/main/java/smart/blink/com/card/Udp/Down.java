package smart.blink.com.card.Udp;


import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import smart.blink.com.card.API.BlinkLog;
import smart.blink.com.card.API.ErrorNo;
import smart.blink.com.card.API.Protocol;
import smart.blink.com.card.BlinkNetCardCall;
import smart.blink.com.card.Tool.MyRevicedTools;
import smart.blink.com.card.Udp.File.FileWrite;
import smart.blink.com.card.Tool.MyTimerTask;
import smart.blink.com.card.Tool.RevicedTools;
import smart.blink.com.card.Tool.SendTools;
import smart.blink.com.card.Tool.TimerTaskCall;
import smart.blink.com.card.bean.ConnectPcRsp;
import smart.blink.com.card.bean.DownLoadingRsp;
import smart.blink.com.card.bean.MainObject;

/**
 * Created by Ruanjiahui on 2016/12/21.
 * <p/>
 * 下载文件的操作
 */
public class Down implements BlinkNetCardCall, TimerTaskCall {

    private static final String TAG = Down.class.getSimpleName();
    /**
     * /存储当前每个链路的下载数目
     * 可以根据这个来查看每个链路下载了多少数据
     * 进行数据统计返回个界面
     */
    private int[] countArray = null;
    /**
     * 储存每个线程的开关数组
     * 如果对应的链路为true说明该线程正在使用，否则没有使用获取使用结束
     */
    private boolean[] threadArray = null;
    /**
     * 回调本类的接口
     */
    private BlinkNetCardCall blinkcall = null;
    /**
     * 定时器
     * 专门统计一段时间的下载速度
     */
    private Timer timer = null;
    /**
     * 写入字节的对象
     */
    private FileWrite fileWrite = null;
    /**
     * 储存当前下载文件分成多少块
     */
    private int count = 0;
    /**
     * 文件的大小
     */
    private long size = 0;
    /**
     * 储存上一次显示界面的下载数目
     * 方便与最新的下载数目进行比较得出下载速度
     */
    private int[] speedArray = null;
    /**
     * 这个是下载的队列数组，里面储存每个模块下载的情况，如果为1则说明下载完成，如果为0则说明还没有下载
     */
    private int[] downList = null;
    /**
     * 这个回调接口是返回给界面
     */
    private BlinkNetCardCall call = null;
    /**
     * 储存下载数据的对象
     * 里面拥有下载速度，大小等
     */
    private DownLoadingRsp downLoadingRsp = null;

    /**
     * 设置每个模块下载的大小
     * 10240则是10M
     */
    private static final int downSize = 10240;
    /**
     * 最大开启线程数
     */
    private static final int countThread = 5;
    /**
     * 刚开始创建的线程数
     */
    private int length = 0;

    /**
     * 下载的IP
     */
    private String IP = null;

    /**
     * 下载的端口
     */
    private int PORT = 0;

    /**
     * 文件名称
     */
    private String filename = null;

    /**
     * 统计下载完成的数据模块
     */
    private int total = 0;

    /**
     * 储存链路的数组
     */
    private Socket[] socketArray = null;

    AtomicInteger taskFlag = new AtomicInteger(0);

    // 任务的开关
    public static boolean isStart = true;

    public Down(final String IP, final int PORT, long size, final String filename, String path, final int position, final BlinkNetCardCall call) {
        //计算文件大小开启线程数

        if ((size % downSize) == 0) {
            count = (int) (size / downSize);
        } else
            count = (int) (size / downSize) + 1;

        //文件的大小
        this.size = size;
        this.call = call;
        countArray = new int[count + 1];
        threadArray = new boolean[count + 1];
        speedArray = new int[count + 1];
        downList = new int[count + 1];
        socketArray = new Socket[count + 1];
        this.blinkcall = this;
        downLoadingRsp = new DownLoadingRsp();
        fileWrite = new FileWrite(path, filename);

        // 处理文件大小为０的情况
        if (count == 0) {
            Log.e(TAG, "Down: 下载文件的大小为空");
            fileWrite.Close();
            downLoadingRsp.setEnd(true);
            if (this.call != null) {
                this.call.onSuccess(Protocol.Downloading, downLoadingRsp);
            }

            //downLoadingRsp = null;
            return;
        }

        //开启定时
        timer = new Timer();
        // 改成每统计一次下载速度
        timer.schedule(new MyTimerTask(this), 0, 1000);
        this.IP = IP;
        this.PORT = PORT;
        this.filename = filename;

        // 打印块数
        BlinkLog.Print(count);
        Log.e(TAG, "Down: count==" + count);
        length = count;
        if (count > countThread)
            length = countThread;

        // 同时开启5条线程下载
        for (int i = 0; i < length; i++) {
            final int flag = i + 1;
            downList[flag] = 1;
            StartThread(IP, PORT, filename, flag);
        }
    }


    @Override
    public void onSuccess(int position, MainObject mainObject) {
        DownLoadingRsp downLoadingRsp = (DownLoadingRsp) mainObject;
        // 将获得的数据写入到文件中
        fileWrite.Write(downLoadingRsp.getData(), position, countArray[position]);

        if (position == count) {
            if (countArray[position] == size % downSize) {
//                SendClose(position);
                threadArray[position] = false;
                CloseSocket(position);
                WaitStart();
            }
        } else {
            if (countArray[position] == downSize) {
//                SendClose(position);
                threadArray[position] = false;
                CloseSocket(position);
                WaitStart();
            }
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
     * @param position
     */
//    private void SendClose(int position){
//        Socket sockets = socketArray[position];
//        try {
//            DataOutputStream out = new DataOutputStream(sockets.getOutputStream());
//            out.write(SendTools.UploadClose());
//            out.flush();
//        } catch (IOException e) {
//        }
//    }

    /**
     * 启动链路线程下载
     *
     * @param IP       IP
     * @param PORT     端口
     * @param filename 文件名
     * @param flag     当前的链路
     */
    private void StartThread(final String IP, final int PORT, final String filename, final int flag) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                //下载标识从0开始
                int j = 0;
                int k = flag;
                boolean threadBool = true;
                threadArray[k] = threadBool;
                //初始化套接字
                Socket socket = null;
                //初始化接收池
                byte[] buf = null;
                //初始化发送对象
                DataOutputStream out = null;
                //初始化接收对象
                DataInputStream in = null;
                int id = 0;
                //判断是否在可以下载
                while (threadArray[k] && isStart) {
                    try {
                        //如果套接字为空则创建
                        if (socket == null) {
                            socket = new Socket(IP, PORT);
                            buf = new byte[1400];
                            out = new DataOutputStream(socket.getOutputStream());
                            in = new DataInputStream(socket.getInputStream());
                            //把链路添加到数组里面
                            socketArray[k] = socket;
                            // 设置连接时间
                            socket.setSoTimeout(30000);

                        }
                        //如果id为0则说明请求下载
                        //否则就是进入下载东西的过程
                        if (id == 0) {
                            // 发送数据之前先开启一个定时器，限定操作的时间

                            // 0x03 0x00 ./win7.iso 0x00 13 0x00   数据包长度strlen(“./win7.iso”)+5
                            byte[] buffer = SendTools.Downloading(k, filename);
                            BlinkLog.Print(Arrays.toString(buffer));
                            out.write(buffer);
                            out.flush();
                        } else {
                            //　发送数据之前先开启一个定时器，限定操作的时间

                            // 将会发送下载块数据小包数据包
                            byte[] buffer = SendTools.Downloading();

                            Log.e(TAG, "run: 发送数据：buffer[0]" + buffer[0] + "   downLoadingRsp.getFilename()===" + downLoadingRsp.getFilename() + "   flag===" + flag);
                            out.write(buffer);
                            out.flush();
                        }

                        //接收数据
                        int length = in.read(buf);
                        Log.e(TAG, "run: 接收数据：buf[0]==" + buf[0] + "   downLoadingRsp.getFilename()===" + downLoadingRsp.getFilename() + "   flag===" + flag);
                        if (buf[0] == 0) {
                            return;
                        }


                        if (buf[0] == Protocol.DownloadingReviced) {
                            //当服务器接收到请求下载块数据包之后，正常返回允许下载块数据包，异常发送异常数据包：
                            BlinkLog.Print(Arrays.toString(buf));
                            id = 1;
                        }
                        if (buf[0] == Protocol.DownloadingReviced1) {
                            j++;
                            //正常则读取数据发送实际数据
                            countArray[k] = j;
                            //RevicedTools.Downloading(k, countArray[k], buf, blinkcall);
                            // 此处是在增加多任务下载的时候添加的功能
                            new MyRevicedTools(k, countArray[k], buf, blinkcall);
                        }
                        //　重新清空接收的数据
                        buf = new byte[1400];

                    } catch (IOException e) {
                        Log.e(TAG, "run: IOException e" + "   downLoadingRsp.getFilename()===" + downLoadingRsp.getFilename() + "   flag===" + flag);
                        BlinkLog.Error(e.toString());
                        //socket异常断开
//                        if (ErrorNo.SocketError.equals(e.getMessage()) || ErrorNo.ReadError.equals(e.getMessage())) {
//                            Log.e(TAG, "run: 网络异常断开");
//                            CloseTimer();
//                            call.onFail(ErrorNo.ErrorSocket);
//                            return;
//                        }
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }
                        if (fileWrite != null) {
                            fileWrite.Close();
                        }

                        if ("java.net.SocketTimeoutException".equals(e.toString()) && taskFlag.get() == 0) {
                            // 在此处应该释放该任务的所有资源
                            taskFlag.getAndIncrement();
                            for (int i = 0; i < threadArray.length; i++) {
                                threadArray[i] = false;
                            }
                            for (int i = 0; i < downList.length; i++) {
                                downList[i] = 1;
                            }

                            Log.e(TAG, "run: 下载超时   downLoadingRsp.getFilename()===" + downLoadingRsp.getFilename());
                            Down.this.onFail(-1);

                            return;
                        }

                        // 当网络异常时释放所有的资源----------------------------------------
                        threadArray[k] = false;
                        if (out != null) {
                            try {
                                out.close();
                                out = null;
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                        if (in != null) {
                            try {
                                in.close();
                                in = null;
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                        //发生异常时移除链路
                        try {
                            if (socket != null) {
                                socket.close();
                                socket = null;
                            }

                        } catch (IOException e1) {
                            BlinkLog.Error(e.toString());
                        }


                        // 当网络异常时释放所有的资源----------------------------------------
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
                StartThread(IP, PORT, filename, i);
                downList[i] = 1;
                return;
            }
        }

        //如果下载完成模块数等于总模块数说明下载完成
        if (total == count) {
            CloseTimer();
            downLoadingRsp.setEnd(true);
            //最后一次返回数据给界面
            totalSpeed();
            //关闭写入流
            fileWrite.Close();
        }
    }


    /**
     * 关闭定时器
     */
    public void CloseTimer() {
        //如果downList队列里面没有下载模块说明就是下载完成
        //这个时候取消定时器
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onFail(int error) {
        if (call != null) {
            call.onFail(error);
            call = null;
        }

    }

    private int speed = 0;
    private int totalSize = 0;
    private int lateSize = 0;

    /**
     * 定时回调的接口
     */
    @Override
    public void TimerCall() {
        if (isStart == false) {
            if (timer != null) {
                timer.cancel();
                timer = null;

            }
            return;
        }
        totalSpeed();
    }

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
        downLoadingRsp.setSpeed(speed + "K/S");
        downLoadingRsp.setBlockId(totalSize);
        downLoadingRsp.setFilename(fileWrite.getFilename());
        downLoadingRsp.setTotolSize((int) size);

        // 存放在全局变量中
        if (call != null) {
            call.onSuccess(Protocol.Downloading, downLoadingRsp);
        }
    }
}
