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
import smart.blink.com.card.Tcp.bean.Operation;
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
    private static Operation setUploadDirOperatioin;
    private static Operation getUploadDirOperatioin;
    private static Operation connectSubServerOperation;
    private static Operation changPcPwdOperation;
    private static Operation pcShutdownOperation;
    private static Operation pcRestartOperation;

    private static Operation lookPcFileOperation;
    private static int lookPcFileFailedCount = 0;
    private static byte[] lookPcFileBuffer;
    private static boolean isReceivedLookPcFileData = true;

    private static Operation lockPcOperation;
//    private static Operation downloadingOperation;
//    private static Operation uploadingOperation;

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

        handler = null;
        isOpen = false;


    }

    public static void closeTcpSocketWithoutHandler() {
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

        //handler = null;
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

        if (position == Protocol.SetUploadDir) {
            // 设置上传目录
            setUploadDirOperatioin = new Operation();
            setUploadDirOperatioin.position = position;
            setUploadDirOperatioin.call = call;
            setUploadDirOperatioin.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (setUploadDirOperatioin.timer != null) {
                        setUploadDirOperatioin.timer.cancel();
                        setUploadDirOperatioin.timer = null;
                        setUploadDirOperatioin.call.onFail(setUploadDirOperatioin.position);
                        setUploadDirOperatioin = null;
                    }
                }
            }, 6000);
        } else if (position == Protocol.GetUploadDir) {
            // 获取上传目录
            getUploadDirOperatioin = new Operation();
            getUploadDirOperatioin.position = position;
            getUploadDirOperatioin.call = call;
            getUploadDirOperatioin.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (getUploadDirOperatioin.timer != null) {
                        getUploadDirOperatioin.timer.cancel();
                        getUploadDirOperatioin.timer = null;
                        getUploadDirOperatioin.call.onFail(getUploadDirOperatioin.position);
                        getUploadDirOperatioin = null;
                    }
                }
            }, 6000);
        } else if (position == Protocol.CONNECT_SERVER) {
            // 连接子服务器
            connectSubServerOperation = new Operation();
            connectSubServerOperation.position = position;
            connectSubServerOperation.call = call;
            connectSubServerOperation.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (connectSubServerOperation.timer != null) {
                        connectSubServerOperation.timer.cancel();
                        connectSubServerOperation.timer = null;
                        connectSubServerOperation.call.onFail(connectSubServerOperation.position);
                        connectSubServerOperation = null;
                    }
                }
            }, 6000);
        } else if (position == Protocol.ChangePcPwd) {
            // 修改密码
            changPcPwdOperation = new Operation();
            changPcPwdOperation.position = position;
            changPcPwdOperation.call = call;
            changPcPwdOperation.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (changPcPwdOperation.timer != null) {
                        changPcPwdOperation.timer.cancel();
                        changPcPwdOperation.timer = null;
                        changPcPwdOperation.call.onFail(changPcPwdOperation.position);
                        changPcPwdOperation = null;
                    }
                }
            }, 6000);
        } else if (position == Protocol.Shutdown) {
            // 立即关机
            pcShutdownOperation = new Operation();
            pcShutdownOperation.position = position;
            pcShutdownOperation.call = call;
            pcShutdownOperation.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (pcShutdownOperation.timer != null) {
                        pcShutdownOperation.timer.cancel();
                        pcShutdownOperation.timer = null;
                        pcShutdownOperation.call.onFail(pcShutdownOperation.position);
                        pcShutdownOperation = null;
                    }
                }
            }, 6000);
        } else if (position == Protocol.Restart) {
            // 立即重启
            pcRestartOperation = new Operation();
            pcRestartOperation.position = position;
            pcRestartOperation.call = call;
            pcRestartOperation.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (pcRestartOperation.timer != null) {
                        pcRestartOperation.timer.cancel();
                        pcRestartOperation.timer = null;
                        pcRestartOperation.call.onFail(pcRestartOperation.position);
                        pcRestartOperation = null;
                    }
                }
            }, 6000);
        } else if (position == Protocol.LookFileMsg) {
            Log.e(TAG, "TcpSocket: Protocol.LookFileMsg");
            // 此处可修改成功请求多次
            lookPcFileFailedCount = 0;
            lookPcFileBuffer = buffer;
            isReceivedLookPcFileData = true;
            //　查看电脑文件
            lookPcFileOperation = new Operation();
            lookPcFileOperation.position = position;
            lookPcFileOperation.call = call;
            lookPcFileOperation.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // 失败次数 ++
                    lookPcFileFailedCount++;
                    Log.e(TAG, "run: lookPcFileFailedCount===" + lookPcFileFailedCount);
                    Log.e(TAG, "run: 请求查看电脑文件次数 ++");
                    isReceivedLookPcFileData = false;

                    if (lookPcFileOperation.timer != null && lookPcFileFailedCount > 2) {
                        lookPcFileFailedCount = 0;
                        lookPcFileBuffer = null;
                        Log.e(TAG, "run: 请求查看电脑文件失败");

                        lookPcFileOperation.timer.cancel();
                        lookPcFileOperation.timer = null;
                        lookPcFileOperation.call.onFail(lookPcFileOperation.position);
                        lookPcFileOperation = null;

                        return;
                    }

                    isReceivedLookPcFileData = true;
                    Send(lookPcFileBuffer);

                }
            }, 6000, 6000);
        } else if (position == Protocol.LOOKPC) {
            //　PC锁屏
            lockPcOperation = new Operation();
            lockPcOperation.position = position;
            lockPcOperation.call = call;
            lockPcOperation.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (lockPcOperation.timer != null) {
                        lockPcOperation.timer.cancel();
                        lockPcOperation.timer = null;
                        lockPcOperation.call.onFail(lockPcOperation.position);
                        lockPcOperation = null;
                    }
                }
            }, 6000);
        } else if (position == Protocol.Downloading) {
            TcpSocket.downloadingcall = call;
        } else if (position == Protocol.Uploading) {
            TcpSocket.uploadingcall = call;
        } else if (position == Protocol.Heart) {
            heartcall = call;
        } else {
            TcpSocket.call = call;
        }


//        if (position == Protocol.Downloading) {
//            TcpSocket.downloadingcall = call;
//        } else if (position == Protocol.Uploading) {
//            TcpSocket.uploadingcall = call;
//        } else if (position == Protocol.Heart) {
//            heartcall = call;
//        } else if (position == Protocol.LookFileMsg) {
//            lookpcfile = call;
//        } else {
//            TcpSocket.call = call;
//        }

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
                        // 正常接收心跳
                        new RevicedTools(-1, buffer, length, heartcall);
                    } else if (buffer[0] == 5) {
                        if (lookPcFileOperation != null && isReceivedLookPcFileData) {
                            new RevicedTools(lookPcFileOperation.position, buffer, length, lookPcFileOperation.call);
                            lookPcFileOperation = null;
                        }

                        // 访问电脑文件
                        //new RevicedTools(position, buffer, length, TcpSocket.lookpcfile);
                    } else if (buffer[0] == 85) {
                        //锁屏
                        if (lockPcOperation != null) {
                            new RevicedTools(lockPcOperation.position, buffer, length, lockPcOperation.call);
                            lockPcOperation = null;
                        }
                    } else if (buffer[0] == 75) {
                        // 重启
                        if (pcRestartOperation != null) {
                            new RevicedTools(pcRestartOperation.position, buffer, length, pcRestartOperation.call);
                            pcRestartOperation = null;
                        }
                    } else if (buffer[0] == 74) {
                        //　立即关机
                        if (pcShutdownOperation != null) {
                            new RevicedTools(pcShutdownOperation.position, buffer, length, pcShutdownOperation.call);
                            pcShutdownOperation = null;
                        }
                    } else if (buffer[0] == 89 || buffer[0] == 40) {
                        //　修改密码成功
                        if (changPcPwdOperation != null) {
                            new RevicedTools(changPcPwdOperation.position, buffer, length, changPcPwdOperation.call);
                            changPcPwdOperation = null;
                        }
                    } else if (buffer[0] == 110) {
                        // 连接子务器成功
                        if (connectSubServerOperation != null) {
                            new RevicedTools(connectSubServerOperation.position, buffer, length, connectSubServerOperation.call);
                            connectSubServerOperation = null;
                        }
                    } else if (buffer[0] == 87) {
                        // 获取上传目录成功
                        if (getUploadDirOperatioin != null) {
                            new RevicedTools(getUploadDirOperatioin.position, buffer, length, getUploadDirOperatioin.call);
                            getUploadDirOperatioin = null;
                        }
                    } else if (buffer[0] == 86) {
                        // 设置上传目录成功
                        if (setUploadDirOperatioin != null) {
                            new RevicedTools(setUploadDirOperatioin.position, buffer, length, setUploadDirOperatioin.call);
                            setUploadDirOperatioin = null;
                        }
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
                        //closeTcpSocket();
                        closeTcpSocketWithoutHandler();
                    }
                }

                // 开启一个定时器
                if (position == Protocol.SetUploadDir) {

                } else if (position == Protocol.GetUploadDir) {

                } else if (position == Protocol.CONNECT_SERVER) {

                } else if (position == Protocol.ChangePcPwd) {

                } else if (position == Protocol.LookFileMsg) {

                } else if (position == Protocol.LOOKPC) {

                } else if (position == Protocol.Heart) {

                } else if (position == Protocol.Restart) {

                } else if (position == Protocol.Shutdown) {

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
                //closeTcpSocket();
                closeTcpSocketWithoutHandler();
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
            //closeTcpSocket();
            closeTcpSocketWithoutHandler();
            Log.e(TAG, "Write: closeTcpSocket()");
//            Log.e(TAG, "Write: 服务器挂了");
        }

        if (buffer[0] == 0) {
            return;
        }
        BlinkLog.Print("received: " + Arrays.toString(buffer));

        if (buffer[0] == 86) {
            // 设置上传目录
            if (setUploadDirOperatioin != null) {
                if (setUploadDirOperatioin.timer != null) {
                    setUploadDirOperatioin.timer.cancel();
                    setUploadDirOperatioin.timer = null;
                }
            }
        } else if (buffer[0] == 87) {
            // 获取上传目录
            if (getUploadDirOperatioin != null) {
                if (getUploadDirOperatioin.timer != null) {
                    getUploadDirOperatioin.timer.cancel();
                    getUploadDirOperatioin.timer = null;
                }
            }
        } else if (buffer[0] == 110) {
            // 连接子务器成功
            if (connectSubServerOperation != null) {
                if (connectSubServerOperation.timer != null) {
                    connectSubServerOperation.timer.cancel();
                    connectSubServerOperation.timer = null;
                }
            }
        } else if (buffer[0] == 89 || buffer[0] == 40) {
            // 修改密码成功
            if (changPcPwdOperation != null) {
                if (changPcPwdOperation.timer != null) {
                    changPcPwdOperation.timer.cancel();
                    changPcPwdOperation.timer = null;
                }
            }
        } else if (buffer[0] == 74) {
            // 关机
            if (pcShutdownOperation != null) {
                if (pcShutdownOperation.timer != null) {
                    pcShutdownOperation.timer.cancel();
                    pcShutdownOperation.timer = null;
                }
            }
        } else if (buffer[0] == 75) {
            // 重启
            if (pcRestartOperation != null) {
                if (pcRestartOperation.timer != null) {
                    pcRestartOperation.timer.cancel();
                    pcRestartOperation.timer = null;
                }
            }
        } else if (buffer[0] == 5) {
            // 查看文件
            if (lookPcFileOperation != null) {
                if (lookPcFileOperation.timer != null) {
                    lookPcFileOperation.timer.cancel();
                    lookPcFileOperation.timer = null;
                }
            }
        } else if (buffer[0] == 7) {
            //　心跳逻辑
        } else if (buffer[0] == 85) {
            //  锁屏
            if (lockPcOperation != null) {
                if (lockPcOperation.timer != null) {
                    lockPcOperation.timer.cancel();
                    lockPcOperation.timer = null;

                    lookPcFileFailedCount = 0;
                    lookPcFileBuffer = null;
                }
            }
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
        } else {
            // 如果接收到数据就把定时器给关掉
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }

//        if (buffer[0] == 7) {
//            // 心跳
//
//        } else if (buffer[0] == 71) {
//            // 下载
//            // 如果接收到数据就把定时器给关掉
//            if (downloadingTimer != null) {
//                downloadingTimer.cancel();
//                downloadingTimer = null;
//            }
//        } else if (buffer[0] == 80) {
//            // 上传
//            if (uploadingTimer != null) {
//                uploadingTimer.cancel();
//                uploadingTimer = null;
//            }
//
//        } else {
//            // 如果接收到数据就把定时器给关掉
//            if (timer != null) {
//                timer.cancel();
//                timer = null;
//                //Log.e(TAG, "Write: 我接收到数据现在要关闭时定时器");
//            }
//        }

//        if (buffer[0] == 7) {
//            // 心跳
//
//        } else if (buffer[0] == 71) {
//            // 下载
//            // 如果接收到数据就把定时器给关掉
//            if (downloadingTimer != null) {
//                downloadingTimer.cancel();
//                downloadingTimer = null;
//            }
//        } else if (buffer[0] == 80) {
//            // 上传
//            if (uploadingTimer != null) {
//                uploadingTimer.cancel();
//                uploadingTimer = null;
//            }
//
//        } else if (buffer[0] == 5) {
//            // 访问电脑目录
//            if (lookpcfileTimer != null) {
//                lookpcfileTimer.cancel();
//                lookpcfileTimer = null;
//            }
//        } else {
//            // 如果接收到数据就把定时器给关掉
//            if (timer != null) {
//                timer.cancel();
//                timer = null;
//                //Log.e(TAG, "Write: 我接收到数据现在要关闭时定时器");
//            }
//        }

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
                //RevicedTools.failLookPcFileByTcp(lookpcfile);
                if (lookPcFileOperation != null) {
                    lookPcFileOperation.call.onFail(lookPcFileOperation.position);
                    lookPcFileOperation = null;
                }
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
                            //RevicedTools.failEventHandlerByUdp(position, call);
                            if (lookPcFileOperation != null) {
                                lookPcFileOperation.call.onFail(lookPcFileOperation.position);
                                lookPcFileOperation = null;
                            }

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

}
