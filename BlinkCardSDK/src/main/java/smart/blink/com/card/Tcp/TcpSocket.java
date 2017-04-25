package smart.blink.com.card.Tcp;


import android.graphics.BitmapFactory;
import android.graphics.PixelXorXfermode;
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
    //private DataInputStream in = null;
    private static byte[] buf = null;
    private Thread thread = null;
    private static Thread readThread = null;
    private static int position = 0;
    private static BlinkNetCardCall call = null;

    public static ArrayList<byte[]> bufferList = null;
    public static ArrayList<Integer> controlList = null;
    private static Timer timer;
    private static Timer pcLookTimer;
    private static Timer downloadingTimer;
    private static Timer uploadingTimer;
    private static Timer lookpcfileTimer;


    private static boolean isOpen = false;

    private static BlinkNetCardCall downloadingcall = null;
    private static BlinkNetCardCall uploadingcall = null;
    private static BlinkNetCardCall heartcall = null;
    private static BlinkNetCardCall lookpcfile = null;


    private static Handler handler = null;

    /**
     * 关闭TCP的资源
     */
    public static void closeTcpSocket() {

        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "closeTcpSocket: in error");
            }
            in = null;
        }

        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "closeTcpSocket: socket error");
            }
            socket = null;
        }

        isOpen = false;
    }

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

        if (position == Protocol.Downloading) {
            TcpSocket.downloadingcall = call;
        } else if (position == Protocol.Uploading) {
            TcpSocket.uploadingcall = call;
        } else if (position == Protocol.Heart) {
            heartcall = call;
        } else if (position == Protocol.LookFileMsg) {
            lookpcfile = call;
        } else {
            TcpSocket.call = call;
        }

        // 写在这里是为了放在主线程
        if (handler == null) {
            handler = new Handler() {
                @Override
                public void dispatchMessage(Message msg) {
                    byte[] buffer = (byte[]) msg.obj;
                    int length = msg.what;

                    if (buffer[0] == 71) {
                        // 下载
                        new RevicedTools(position, buffer, length, downloadingcall);
                    } else if (buffer[0] == 80) {
                        // 上传
                        new RevicedTools(position, buffer, length, uploadingcall);
                    } else if (buffer[0] == 7) {
                        // 心跳
                        new RevicedTools(position, buffer, length, heartcall);
                    } else if (buffer[0] == 5) {
                        // 访问电脑文件
                        new RevicedTools(position, buffer, length, TcpSocket.lookpcfile);
                    } else {
                        new RevicedTools(position, buffer, length, TcpSocket.call);
                    }
                }
            };
        }

        bufferList = new ArrayList<>();
        controlList = new ArrayList<>();

        thread = null;

        isOpen = true;

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
                                while (isOpen) {
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
                        closeTcpSocket();
                    }
                }

                // 开启一个定时器
                if (position == Protocol.Heart) {

                } else if (position == Protocol.Downloading) {
                    downloadingTimer = new Timer();
                    downloadingTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // 请求下载失败的处理逻辑
                            RevicedTools.failDownloadingHandlerByTcp(downloadingcall);

                            downloadingTimer.cancel();
                            downloadingTimer = null;
                        }
                    }, 4000);
                } else if (position == Protocol.Uploading) {
                    uploadingTimer = new Timer();
                    uploadingTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // 请求上传失败的逻辑
                            Log.e(TAG, "run: 请求上传失败的逻辑");
                            RevicedTools.failUploadingHandlerByTcp(uploadingcall);

                            uploadingTimer.cancel();
                            uploadingTimer = null;
                        }
                    }, 4000);
                } else if (position == Protocol.LookFileMsg) {
                    lookpcfileTimer = new Timer();
                    lookpcfileTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // 访问电脑文件失败
                            RevicedTools.failLookPcFileByTcp(lookpcfile);
                            lookpcfileTimer.cancel();
                            lookpcfileTimer = null;
                        }
                    }, 6000);
                } else {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (timer != null) {
                                RevicedTools.failEventHandlerByUdp(position, call);
                                timer.cancel();
                                timer = null;
                            }
                        }
                    }, 8000);
                }
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
        if (socket != null) {
            try {
                out = new DataOutputStream(socket.getOutputStream());
                out.write(buffer);
                out.flush();
            } catch (IOException e) {
                BlinkLog.Error(e.toString());
                closeTcpSocket();
                Log.e(TAG, "Send: closeTcpSocket()");
//                Log.e(TAG, "Send: TcpSocket占用的资源");
            }
        }
    }

    private void Write(byte[] buffer) {
        int length = 0;
        try {
            // 如果Tcp服务器一死，这里是不会阻塞的
            if (in != null) {
                length = in.read(buffer);
            }
        } catch (IOException e) {
            BlinkLog.Error(e.toString());
            // 释放资源
            closeTcpSocket();
            Log.e(TAG, "Write: closeTcpSocket()");
//            Log.e(TAG, "Write: 服务器挂了");
        }

        if (buffer[0] == 0) {
            return;
        }
        BlinkLog.Print("received: " + Arrays.toString(buffer));

        if (buffer[0] == 7) {
            // 心跳

        } else if (buffer[0] == 71) {
            // 下载
            // 如果接收到数据就把定时器给关掉
            if (downloadingTimer != null) {
                downloadingTimer.cancel();
                downloadingTimer = null;
            }
        } else if (buffer[0] == 80) {
            // 上传
            if (uploadingTimer != null) {
                uploadingTimer.cancel();
                uploadingTimer = null;
            }

        } else if (buffer[0] == 5) {
            // 访问电脑目录
            if (lookpcfileTimer != null) {
                lookpcfileTimer.cancel();
                lookpcfileTimer = null;
            }
        } else {
            // 如果接收到数据就把定时器给关掉
            if (timer != null) {
                timer.cancel();
                timer = null;
                //Log.e(TAG, "Write: 我接收到数据现在要关闭时定时器");
            }
        }

        /**
         * Tcp访问电脑文件的逻辑
         *
         * 在访问电脑文件的时候有时候会收不到 buffer[4]==3的情况，
         * 如果规定时间没有收到buffer[4]==3那么直接说明访问失败
         */
        if (buffer[0] == 5) {
            if (buffer[4] == 4) {
                // 访问错误
                //RevicedTools.failEventHandlerByUdp(position, call);
                RevicedTools.failLookPcFileByTcp(lookpcfile);
            } else if (buffer[4] == 3) {
                //closeTcpSocket();
                if (pcLookTimer != null) {
                    pcLookTimer.cancel();
                    pcLookTimer = null;
                }

                Message message = new Message();
                message.obj = new byte[]{5};
                message.what = 0;
                handler.sendMessage(message);
                return;
            } else {
                if (pcLookTimer == null) {
                    pcLookTimer = new Timer();
                    pcLookTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            RevicedTools.failEventHandlerByUdp(position, call);

                            if (pcLookTimer != null) {
                                pcLookTimer.cancel();
                                pcLookTimer = null;
                            }
                        }
                    }, 6000);
                }
                // 访问文件的数据添加到集合中
                //bufferList.add(Arrays.copyOfRange(buffer, 0, buffer.length));
                bufferList.add(buffer);
                controlList.add((int) buffer[4]);
            }
        } else {
            // 在主线程用中调用方法
            Message message = Message.obtain();
            message.obj = buffer;
            message.what = length;
            handler.sendMessage(message);
        }
        buf = new byte[1296];

    }

//    // 这个方法是在主线程中调用的
//    private static Handler handler = new Handler() {
//
//        @Override
//        public void dispatchMessage(Message msg) {
//            byte[] buffer = (byte[]) msg.obj;
//            int length = msg.what;
//
//            if (buffer[0] == 71) {
//                // 下载
//                new RevicedTools(position, buffer, length, downloadingcall);
//            } else if (buffer[0] == 80) {
//                // 上传
//                new RevicedTools(position, buffer, length, uploadingcall);
//            } else if (buffer[0] == 7) {
//                // 心跳
//                new RevicedTools(position, buffer, length, heartcall);
//            } else {
//                new RevicedTools(position, buffer, length, call);
//            }
//        }
//    };

}
