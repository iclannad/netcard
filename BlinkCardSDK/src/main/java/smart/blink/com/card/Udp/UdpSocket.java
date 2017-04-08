package smart.blink.com.card.Udp;


import android.graphics.Bitmap;
import android.nfc.tech.IsoDep;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.SimpleTimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.transform.sax.SAXTransformerFactory;

import smart.blink.com.card.API.BlinkLog;
import smart.blink.com.card.API.Protocol;
import smart.blink.com.card.BlinkNetCardCall;
import smart.blink.com.card.Tool.RevicedTools;

/**
 * Created by Ruanjiahui on 2017/1/10.
 */
public class UdpSocket {

    private static final String TAG = UdpSocket.class.getSimpleName();

    private static DatagramSocket socket = null;
    private Thread thread = null;
    private static Thread inThread = null;
    private static byte[] buf = null;
    private static int position = 0;
    private static BlinkNetCardCall call = null;
    private static int count = 0;


    public static ArrayList<byte[]> bufferList = null;
    public static ArrayList<Integer> controlList = null;

    private static Timer timer = null;
    //private  Timer timer = null;
    private Timer wantTimer = null;

    public static boolean isOpen;

    /**
     * 释放资源的接口
     */
    public static void closeUdpSocket() {

        if (inThread != null) {
            inThread = null;
        }
        if (socket != null) {
            socket.close();
            socket = null;
            isOpen = false;
        }

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public UdpSocket(final String ip, final int PORT, final byte[] buffer, final int position, final BlinkNetCardCall call) {
        UdpSocket.position = position;
        UdpSocket.call = call;

        isOpen = true;

        bufferList = new ArrayList<>();
        controlList = new ArrayList<>();
        //　先开启一个接收线程
        if (socket == null) {
            try {
                socket = new DatagramSocket();
                inThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (isOpen) {
                            try {
                                Thread.sleep(0);
                            } catch (InterruptedException e) {
                                BlinkLog.Error(e.toString());
                            }
                            buf = new byte[1296];
                            Reviced(buf);
                        }
                    }
                });
                inThread.start();
            } catch (SocketException e) {
                BlinkLog.Error(e.toString());
            }
        }

//        else if (UdpSocket.position == Protocol.LookFileMsg) {
//            Log.e(TAG, "run: 该问电脑文件目录失败");
//            RevicedTools.LookFileMsgFail(call);
//        }

        timer = null;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // test 发送心跳的逻辑
                if (UdpSocket.position == Protocol.Heart) {

                } else if (UdpSocket.position == Protocol.SetUploadDir) {

                }  else {
                    Log.e(TAG, "run: 已经连接不到服务器了");
                    RevicedTools.failEventHandlerByUdp(position, UdpSocket.call);
                }

                CloseTime();
            }
        }, 3000);

        //　然后再开启一个发送线程
        thread = null;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Send(ip, PORT, buffer);
            }
        });
        thread.start();
    }


    private void Send(String ip, int PORT, byte[] buffer) {
        // 发送心跳包
        BlinkLog.Print("发送数据包: " + ip + ":" + PORT + "----" + Arrays.toString(buffer));
        DatagramPacket out = null;
        try {
            out = new DatagramPacket(buffer, 0, buffer.length, InetAddress.getByName(ip), PORT);
            socket.send(out);
        } catch (UnknownHostException e) {
            BlinkLog.Error(e.toString());
        } catch (IOException e) {
            BlinkLog.Error(e.toString());
        }
    }

    private int length = 0;
    public static boolean isReceived = false;

    private void Reviced(final byte[] buffer) {
        DatagramPacket in = null;
        in = new DatagramPacket(buffer, 0, buffer.length);
        length = 0;
        try {
            socket.receive(in);
            length = in.getLength();

            BlinkLog.Print("接收服务器返回的数据: " + Arrays.toString(buffer));
            setData(buffer, length);

        } catch (IOException e) {
            BlinkLog.Error(e.toString());
        }
    }

    // 这个方法是在主线程中调用的
    private Handler handler = new Handler() {

        @Override
        public void dispatchMessage(Message msg) {
            new RevicedTools(position, (byte[]) msg.obj, msg.what, call);
            thread = null;
            buf = null;
        }
    };


    private void setData(byte[] buffer, int length) {
        synchronized (this) {
            if (buffer[0] == 5) {
                if (buffer[4] == 3) {
                    CloseTime();
                    Message message = new Message();
                    message.obj = new byte[]{5};
                    message.what = 0;
                    handler.sendMessage(message);
                } else {
                    bufferList.add(buffer);
                    controlList.add((int) buffer[4]);
                }
            } else {
                CloseTime();
                //处理返回的结果
                Message message = new Message();
                message.obj = buffer;
                message.what = length;
                handler.sendMessage(message);
            }
        }
    }


    private void CloseTime() {
        BlinkLog.Error("************" + timer);
        // 自己添加的同步代码，如果不行的话就可以去掉
        synchronized (this) {
            if (timer != null) {
                BlinkLog.Error("定时器关闭");
                timer.cancel();
                timer = null;
            }
        }

    }
}
