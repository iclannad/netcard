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

import javax.crypto.spec.OAEPParameterSpec;
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
    private static int lookPcFileFailedCount = 0;
    private static byte[] lookPcFileBuffer;
    private static boolean isReceivedLookPcFileData = true;

    private static Operation lockPcOperation;
    private static Operation feedbackOperation;
    private static Operation wantOperation;
    private static Operation helloOperation;
    private static Operation relaymsgOperation;


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

        if (position == Protocol.RelayMsg) {
            // 向主服务器申请子服务器
            relaymsgOperation = new Operation();
            relaymsgOperation.position = position;
            relaymsgOperation.call = call;
            relaymsgOperation.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (relaymsgOperation.timer != null) {
                        relaymsgOperation.timer.cancel();
                        relaymsgOperation.timer = null;
                        relaymsgOperation.call.onFail(relaymsgOperation.position);
                        relaymsgOperation = null;
                    }
                }
            }, 8000);
        } else if (position == Protocol.HELLO) {
            // 修改用户密码
            helloOperation = new Operation();
            helloOperation.position = position;
            helloOperation.call = call;
            helloOperation.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (helloOperation.timer != null) {
                        helloOperation.timer.cancel();
                        helloOperation.timer = null;
                        helloOperation.call.onFail(helloOperation.position);
                        helloOperation = null;
                    }
                }
            }, 8000);
        } else if (position == Protocol.WANT) {
            // 修改用户密码
            wantOperation = new Operation();
            wantOperation.position = position;
            wantOperation.call = call;
            wantOperation.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (wantOperation.timer != null) {
                        wantOperation.timer.cancel();
                        wantOperation.timer = null;
                        wantOperation.call.onFail(wantOperation.position);
                        wantOperation = null;
                    }
                }
            }, 8000);
        } else if (position == Protocol.FEEDBACK) {
            // 修改用户密码
            feedbackOperation = new Operation();
            feedbackOperation.position = position;
            feedbackOperation.call = call;
            feedbackOperation.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (feedbackOperation.timer != null) {
                        feedbackOperation.timer.cancel();
                        feedbackOperation.timer = null;
                        feedbackOperation.call.onFail(feedbackOperation.position);
                        feedbackOperation = null;
                    }
                }
            }, 8000);
        } else if (position == Protocol.ChangePwd) {
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
            lookPcFileFailedCount = 0;
            lookPcFileBuffer = buffer;
            isReceivedLookPcFileData = true;

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
                        Log.e(TAG, "run: lookPcFileOperation.call.onFail(lookPcFileOperation.position);");
                        lookPcFileOperation.call.onFail(lookPcFileOperation.position);
                        lookPcFileOperation = null;
                        return;
                    }

                    // 再次请求访问电脑文件
                    thread = null;
                    thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (lookPcFileBuffer != null) {
                                Send(ip, PORT, lookPcFileBuffer);
                            }
                        }
                    });
                    isReceivedLookPcFileData = true;
                    thread.start();

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
                        if (heartcall == null) {
                            return;
                        }
                        new RevicedTools(-1, buffer, length, heartcall);
                    } else if (buffer[0] == 5) {
                        // 查看电脑文件
                        if (lookPcFileOperation != null && isReceivedLookPcFileData) {
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
                    } else if (buffer[0] == 10) {
                        // 提交反馈成功
                        if (feedbackOperation != null) {
                            new RevicedTools(feedbackOperation.position, buffer, length, feedbackOperation.call);
                            feedbackOperation = null;
                        }
                    } else if (buffer[0] == 2) {
                        // 请求want
                        if (wantOperation != null) {
                            new RevicedTools(wantOperation.position, buffer, length, wantOperation.call);
                            wantOperation = null;
                        }
                    } else if (buffer[0] == 4) {
                        // hello打洞
                        if (helloOperation != null) {
                            new RevicedTools(helloOperation.position, buffer, length, helloOperation.call);
                            helloOperation = null;
                        }
                    } else if (buffer[0] == 110) {
                        // 申请子服务器
                        if (relaymsgOperation != null) {
                            new RevicedTools(relaymsgOperation.position, buffer, length, relaymsgOperation.call);
                            relaymsgOperation = null;
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
        if (position == Protocol.RelayMsg) {

        } else if (position == Protocol.HELLO) {

        } else if (position == Protocol.WANT) {

        } else if (position == Protocol.FEEDBACK) {

        } else if (position == Protocol.ChangePwd) {

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
            if (buffer[0] == 110) {
                // 申请子服务器
                if (relaymsgOperation != null) {
                    if (relaymsgOperation.timer != null) {
                        relaymsgOperation.timer.cancel();
                        relaymsgOperation.timer = null;
                    }
                }
            } else if (buffer[0] == 4) {
                // hello打洞
                if (helloOperation != null) {
                    if (helloOperation.timer != null) {
                        helloOperation.timer.cancel();
                        helloOperation.timer = null;
                    }
                }
            } else if (buffer[0] == 2) {
                // want请求
                if (wantOperation != null) {
                    if (wantOperation.timer != null) {
                        wantOperation.timer.cancel();
                        wantOperation.timer = null;
                    }
                }
            } else if (buffer[0] == 10) {
                // 提交反馈
                if (feedbackOperation != null) {
                    if (feedbackOperation.timer != null) {
                        feedbackOperation.timer.cancel();
                        feedbackOperation.timer = null;
                    }
                }
            } else if (buffer[0] == 82) {
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

                        lookPcFileFailedCount = 0;
                        lookPcFileBuffer = null;
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

                if (handler != null) {
                    handler.sendMessage(message);
                }

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
