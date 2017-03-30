package smart.blink.com.card.Udp;


import android.util.Log;

import smart.blink.com.card.API.BlinkWeb;
import smart.blink.com.card.API.Protocol;
import smart.blink.com.card.BlinkNetCardCall;
import smart.blink.com.card.Tool.ReqDownUp;
import smart.blink.com.card.Tool.SendTools;

/**
 * Created by Ruanjiahui on 2016/11/29.
 */
public class UdpUtils {

    private static final String TAG = UdpUtils.class.getSimpleName();

    /**
     * my test code
     *
     * @param username
     * @param usercontent
     * @param call
     */
    public static void FEEDBACK(String username, String usercontent, BlinkNetCardCall call) {
        new UdpSocket(BlinkWeb.SERVER, BlinkWeb.PORT, SendTools.FEEDBACK(username, usercontent), Protocol.FEEDBACK, call);
    }


    public static void WANT(String id, String password, BlinkNetCardCall call) {
        new UdpSocket(BlinkWeb.SERVER, BlinkWeb.PORT, SendTools.WANT(id, password), Protocol.WANT, call);
    }


    public static void HELLO(BlinkNetCardCall call) {
        new UdpSocket(BlinkWeb.nIP, BlinkWeb.nPORT, SendTools.HELLO(), Protocol.HELLO, call);
    }


    public static void Shutdown(int seconds, BlinkNetCardCall call) {
        new UdpSocket(BlinkWeb.nIP, BlinkWeb.nPORT, SendTools.Shutdown(String.valueOf(seconds)), Protocol.Shutdown, call);
    }


    public static void Restart(int seconds, BlinkNetCardCall call) {
        new UdpSocket(BlinkWeb.nIP, BlinkWeb.nPORT, SendTools.Restart(String.valueOf(seconds)), Protocol.Restart, call);
    }


    public static void LOOKPC(BlinkNetCardCall call) {
        new UdpSocket(BlinkWeb.nIP, BlinkWeb.nPORT, SendTools.LOOKPC(), Protocol.LOOKPC, call);
    }


    public static void ChangePwd(String id, String oldPassword, String newPassword, BlinkNetCardCall call) {
        new UdpSocket(BlinkWeb.SERVER, BlinkWeb.PORT, SendTools.ChangePwd(id, oldPassword, newPassword), Protocol.ChangePwd, call);
    }

    public static void GetUploadDir(BlinkNetCardCall call) {
        new UdpSocket(BlinkWeb.nIP, BlinkWeb.nPORT, SendTools.GetUploadDir(), Protocol.GetUploadDir, call);
    }


    public static void SetUploadDir(String path, BlinkNetCardCall call) {
        new UdpSocket(BlinkWeb.nIP, BlinkWeb.nPORT, SendTools.SetUploadDir(path), Protocol.SetUploadDir, call);
    }


    public static void Heart(final BlinkNetCardCall call) {
        new UdpSocket(BlinkWeb.nIP, BlinkWeb.nPORT, SendTools.Heart(), Protocol.Heart, call);
    }


    public static void ChangePcPwd(String newPassword, BlinkNetCardCall call) {
        new UdpSocket(BlinkWeb.nIP, BlinkWeb.nPORT, SendTools.ChangePcPwd(newPassword), Protocol.ChangePcPwd, call);
    }

    /**
     * 自己封装
     *
     * @param newPassword
     * @param call
     */
    public static void ChangePcPwd(String oldPassworld, String newPassword, BlinkNetCardCall call) {
        new UdpSocket(BlinkWeb.nIP, BlinkWeb.nPORT, SendTools.ChangePcPwd(oldPassworld, newPassword), Protocol.ChangePcPwd, call);
    }

    public static void LookFileMsg(String path, BlinkNetCardCall call) {
        new UdpSocket(BlinkWeb.nIP, BlinkWeb.nPORT, SendTools.LookFileMsg(path), Protocol.LookFileMsg, call);
    }

    public static void RelayMsg(BlinkNetCardCall call) {
        new UdpSocket(BlinkWeb.SERVER, BlinkWeb.PORT, SendTools.RelayMsg(), Protocol.RelayMsg, call);
    }


    public static void DownLoadStart(String path, BlinkNetCardCall call) {
        new ReqDownUp(BlinkWeb.nIP, 8000, SendTools.DownloadStart(path), Protocol.DownloadStart, call);
    }

    // 测试而已，会删除
    public static void TCPDownLoadStart(String path, BlinkNetCardCall call) {
        new ReqDownUp(BlinkWeb.zIP, BlinkWeb.zPORT, SendTools.DownloadStart(path), Protocol.DownloadStart, call);
    }

    public static void DownLoading(String path, String filename, long wantblock, BlinkNetCardCall call) {
        new Down(BlinkWeb.nIP, 8000, wantblock, filename, path, Protocol.Downloading, call);
    }


    public static void UploadStart(String filePath, String filename, BlinkNetCardCall call) {
        new ReqDownUp(BlinkWeb.nIP, 8000, SendTools.UploadStart(filePath, filename), Protocol.UploadStart, call);
    }

    public static void Uploading(String path, String filename, BlinkNetCardCall call) {
        new Upload(BlinkWeb.nIP, 8000, filename, path, Protocol.Uploading, call);
    }
}
