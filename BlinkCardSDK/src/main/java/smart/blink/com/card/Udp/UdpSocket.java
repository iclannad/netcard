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
import smart.blink.com.card.Tcp.bean.Operation;
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

    private static BlinkNetCardCall heartcall = null;
    private static BlinkNetCardCall lookfilemsgcall = null;


    public static ArrayList<byte[]> bufferList = null;
    public static ArrayList<Integer> controlList = null;

    private static Timer timer = null;
    private static Timer pcLookTimer;

    public static boolean isOpen;

    private static Handler handler = null;


    private static Operation changeUserPwdOperatioin;
    private static Operation setUploadDirOperatioin;
    private static Operation getUploadDirOperatioin;
    private static Operation connectSubServerOperation;
    private static Operation changPcPwdOperation;
    private static Operation pcShutdownOperation;
    private static Operation pcRestartOperation;
    private static Operation lookPcFileOperation;
    private static Operation lockPcOperation;


    /**
     * 释放资源的接口
     */
    public static void closeUdpSocket() {

        if (inThread != null) {
            inThread.interrupt();
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
        if (pcLookTimer != null) {
            pcLookTimer.cancel();
            pcLookTimer = null;
        }
        handler = null;
        call = null;
        heartcall = null;

        bufferList = null;
        controlList = null;

        buf = null;

        isOpen = false;
    }

    public UdpSocket(final String ip, final int PORT, final byte[] buffer, final int position, final BlinkNetCardCall call) {
        UdpSocket.position = position;

        if (position == Protocol.ChangePwd) {
            // 修改用户密码
            changeUserPwdOperatioin = new Operation();
            changeUserPwdOperatioin.position = position;
            changeUserPwdOperatioin.call = call;
            changeUserPwdOperatioin.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (changeUserPwdOperatioin.timer != null) {
                        changeUserPwdOperatioin.timer.cancel();
                        changeUserPwdOperatioin.timer = null;
                        changeUserPwdOperatioin.call.onFail(changeUserPwdOperatioin.position);
                        changeUserPwdOperatioin = null;
                    }
                }
            }, 6000);

        } else if (position == Protocol.SetUploadDir) {
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
            //　查看电脑文件
            lookPcFileOperation = new Operation();
            lookPcFileOperation.position = position;
            lookPcFileOperation.call = call;
            lookPcFileOperation.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (lookPcFileOperation.timer != null) {
                        lookPcFileOperation.timer.cancel();
                        lookPcFileOperation.timer = null;
                        lookPcFileOperation.call.onFail(lookPcFileOperation.position);
                        lookPcFileOperation = null;
                    }
                }
            }, 6000);
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
        } else if (position == Protocol.Heart) {
            heartcall = call;
        } else {
            UdpSocket.call = call;
        }

        if (handler == null) {
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    byte[] buffer = (byte[]) msg.obj;
                    int length = msg.what;

                    if (buffer[0] == 7) {
                        // 正常接收心跳
                        new RevicedTools(-1, buffer, length, heartcall);
                    } else if (buffer[0] == 5) {
                        // 查看电脑文件
                        if (lookPcFileOperation != null) {
                            new RevicedTools(lookPcFileOperation.position, buffer, length, lookPcFileOperation.call);
                            lookPcFileOperation = null;
                        }
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
                    } else if (buffer[0] == 82) {
                        // 修改用户密码成功
                        if (changeUserPwdOperatioin != null) {
                            new RevicedTools(changeUserPwdOperatioin.position, buffer, length, changeUserPwdOperatioin.call);
                            changeUserPwdOperatioin = null;
                        }
                    } else {
                        new RevicedTools(position, buffer, length, UdpSocket.call);
                    }

                    //buf = null;
                }
            };
        }

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

        // 开启一个定时器
        if (position == Protocol.ChangePwd) {

        } else if (position == Protocol.SetUploadDir) {

        } else if (position == Protocol.GetUploadDir) {

        } else if (position == Protocol.ChangePcPwd) {

        } else if (position == Protocol.LookFileMsg) {

        } else if (position == Protocol.LOOKPC) {

        } else if (position == Protocol.Heart) {

        } else if (position == Protocol.Restart) {

        } else if (position == Protocol.Shutdown) {

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
            }, 6000);
        }

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
            if (socket != null) {
                socket.receive(in);
            }

            length = in.getLength();

            BlinkLog.Print("接收服务器返回的数据: " + Arrays.toString(buffer));
            setData(buffer, length);

        } catch (IOException e) {
            BlinkLog.Error(e.toString());
        }
    }

    private void setData(byte[] buffer, int length) {
        synchronized (this) {

            // 如果接收数据成功的话就取消定时器
            if (buffer[0] == 82) {
                // 修改用户密码
                if (changeUserPwdOperatioin != null) {
                    if (changeUserPwdOperatioin.timer != null) {
                        changeUserPwdOperatioin.timer.cancel();
                        changeUserPwdOperatioin.timer = null;
                    }
                }
            } else if (buffer[0] == 86) {
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
                    }
                }
            } else {
                // 如果接收到数据就把定时器给关掉
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
            }


            if (buffer[0] == 5) {
                if (buffer[4] == 4) {
                    // 访问错误
                    if (lookPcFileOperation != null) {
                        lookPcFileOperation.call.onFail(lookPcFileOperation.position);
                        lookPcFileOperation = null;
                    }
                    //RevicedTools.failEventHandlerByUdp(position, UdpSocket.lookfilemsgcall);
                } else if (buffer[4] == 3) {
                    if (pcLookTimer != null) {
                        pcLookTimer.cancel();
                        pcLookTimer = null;
                    }

                    CloseTime();
                    Message message = new Message();
                    message.obj = new byte[]{5};
                    message.what = 0;
                    handler.sendMessage(message);

                    thread = null;
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
                        }, 12000);
                    }

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

                thread = null;
            }
        }
    }


    private void CloseTime() {
        //BlinkLog.Error("************" + timer);
        synchronized (this) {
            if (timer != null) {
                //BlinkLog.Error("定时器关闭");
                timer.cancel();
                timer = null;
            }
        }
    }
}
