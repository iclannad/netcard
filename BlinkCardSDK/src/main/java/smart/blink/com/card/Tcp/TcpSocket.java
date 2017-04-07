package smart.blink.com.card.Tcp;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TooManyListenersException;

import smart.blink.com.card.API.BlinkLog;
import smart.blink.com.card.API.Protocol;
import smart.blink.com.card.BlinkNetCardCall;
import smart.blink.com.card.Tool.RevicedTools;

/**
 * Created by Ruanjiahui on 2017/1/10.
 */
public class TcpSocket {

    private static final String TAG = TcpSocket.class.getSimpleName();

    private static Socket socket = null;
    private DataOutputStream out = null;
    private static DataInputStream in = null;
    private static byte[] buf = null;
    private Thread thread = null;
    private static Thread readThread = null;
    private static int position = 0;
    private static BlinkNetCardCall call = null;

    public static ArrayList<byte[]> bufferList = null;
    public static ArrayList<Integer> controlList = null;
    //private static Timer timer;

    /**
     * 封装tcp连接
     *
     * @param ip
     * @param PORT
     * @param buffer
     * @param position
     * @param call
     */
    public TcpSocket(final String ip, final int PORT, final byte[] buffer, final int position, final BlinkNetCardCall call) {
        TcpSocket.position = position;
        TcpSocket.call = call;

        bufferList = new ArrayList<>();
        controlList = new ArrayList<>();

        thread = null;

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (socket == null) {
                    try {
                        Log.e(TAG, "run: " + ip + "---" + PORT);
                        socket = new Socket(ip, PORT);
                        in = new DataInputStream(socket.getInputStream());
                        buf = new byte[1296];
                        readThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 接收数据
                                while (true) {
                                    try {
                                        Thread.sleep(0);
                                    } catch (InterruptedException e) {
                                        BlinkLog.Error(e.toString());
                                    }
                                    Write(buf);
                                }
                            }
                        });
                        readThread.start();
                    } catch (IOException e) {
                        BlinkLog.Error(e.toString());
                    }
                }
                /**
                 * 如果当前是电脑目录的话做特殊处理，如果7s内没有数据返回的就说明当明访问失败
                 */
//                if (position == Protocol.LookFileMsgReviced) {
//                    timer = new Timer();
//                    timer.schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            Log.e(TAG, "run: 访问电脑目录失败");
//                            if (timer != null) {
//                                Log.e(TAG, "send: 取消了定时器");
//                                timer.cancel();
//                                timer = null;
//                            }
//                        }
//                    }, 5000);
//                }
                Send(buffer);
            }
        });
        thread.start();
    }


    public static Socket getSocket() {
        return socket;
    }


    private void Send(byte[] buffer) {
        BlinkLog.Print("send: " + Arrays.toString(buffer));
        try {
            out = new DataOutputStream(socket.getOutputStream());
            out.write(buffer);
            out.flush();
        } catch (IOException e) {
            BlinkLog.Error(e.toString());
        }
    }


    private void Write(byte[] buffer) {
        int length = 0;
        try {
            // 没有数据来的话就会在这里阻塞
            length = in.read(buffer);
            BlinkLog.Print("received: " + Arrays.toString(buffer));

//            // 访问文件时做特殊处理
//            if (position == Protocol.LookFileMsgReviced) {
//                if (timer != null) {
//                    Log.e(TAG, "Write: 取消了定时器");
//                    timer.cancel();
//                    timer = null;
//                }
//            }

        } catch (IOException e) {
            BlinkLog.Error(e.toString());
        }

        /**
         * ＴＣＰ访问电脑文件的逻辑
         */
        if (buffer[0] == 5) {
            if (buffer[4] == 3) {
                Message message = new Message();
                message.obj = new byte[]{5};
                message.what = 0;
                handler.sendMessage(message);
                return;
            } else {
                bufferList.add(Arrays.copyOfRange(buffer, 0, buffer.length));
                controlList.add((int) buffer[4]);
            }
        } else {
            // 在主线程用中调用方法
            Message message = Message.obtain();
            message.obj = buffer;
            message.what = length;
            handler.sendMessage(message);
        }


    }

    // 这个方法是在主线程中调用的
    private Handler handler = new Handler() {

        @Override
        public void dispatchMessage(Message msg) {
            byte[] buffer = (byte[]) msg.obj;
            int length = msg.what;
            //处理返回的结果
            new RevicedTools(position, buffer, length, call);

        }
    };

}
