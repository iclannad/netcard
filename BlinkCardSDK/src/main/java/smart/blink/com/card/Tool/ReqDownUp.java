package smart.blink.com.card.Tool;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import smart.blink.com.card.API.BlinkLog;
import smart.blink.com.card.API.ErrorNo;
import smart.blink.com.card.API.Protocol;
import smart.blink.com.card.BlinkNetCardCall;

/**
 * Created by Ruanjiahui on 2017/1/10.
 */
public class ReqDownUp {

    private static Socket socket = null;
    private static byte[] buf = null;

    private DataOutputStream out = null;
    private static DataInputStream in = null;
    private static BlinkNetCardCall call;
    private static int position = 0;

    private static Thread readThread = null;
    private static Thread thread = null;

    private static byte[] buffer = null;
    private static boolean isError = false;


    public ReqDownUp(final String IP, final int PORT, final byte[] buffer, final int position, final BlinkNetCardCall call) {
        ReqDownUp.call = call;
        ReqDownUp.buffer = buffer;
        ReqDownUp.position = position;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    BlinkLog.Error(e.toString());
                }
                if (socket == null) {
                    try {
                        socket = new Socket(IP, PORT);
                        buf = new byte[1024];
                        in = new DataInputStream(socket.getInputStream());
                        readThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (!isError) {
                                    try {
                                        Thread.sleep(0);
                                    } catch (InterruptedException e) {
                                        BlinkLog.Error(e.toString());
                                    }
                                    Reviced(buf);
                                }
                            }
                        });
                        readThread.start();
                    } catch (IOException e) {
                        BlinkLog.Error(e.toString());
                        if (ErrorNo.SocketError.equals(e.getMessage())) {
                            call.onFail(ErrorNo.ErrorSocket);
                            return;
                        }
                        //发生异常移除链路
                        try {
                            socket.close();
                        } catch (IOException e1) {
                            BlinkLog.Error(e.toString());
                        }
                    }
                }

                Read(buffer);
            }
        });
        thread.start();
    }


    private void Read(byte[] buffer) {
        BlinkLog.Print(Arrays.toString(buffer));
        try {
            out = new DataOutputStream(socket.getOutputStream());
            out.write(buffer);
            out.flush();
        } catch (IOException e) {
            BlinkLog.Error(e.toString());
        }
    }


    private void Reviced(byte[] buffer) {
        int length = 0;
        try {
            length = in.read(buffer);
            BlinkLog.Print(Arrays.toString(buffer));
        } catch (IOException e) {
            BlinkLog.Error(e.toString());
            if (ErrorNo.ReadError.equals(e.getMessage())) {
                isError = true;
                CloseSocket();
                return;
            }
        }
        //处理返回的结果
        //上传的请求
        //下面就是下载的请求
        if (ReqDownUp.buffer[0] == Protocol.UploadStart && !isError)
            RevicedTools.UploadStart(buffer, position, call);
        if (ReqDownUp.buffer[0] == Protocol.DownloadStart && !isError)
            RevicedTools.DownloadStart(buffer, length, position, call);
    }

    /**
     * 关闭Socket
     */
    private void CloseSocket() {
        thread.interrupt();
        readThread.interrupt();
        //终止线程
        call.onFail(ErrorNo.ErrorRead);
    }

}