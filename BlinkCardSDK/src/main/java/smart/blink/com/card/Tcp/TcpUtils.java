package smart.blink.com.card.Tcp;


import java.util.Arrays;

import smart.blink.com.card.API.BlinkLog;
import smart.blink.com.card.API.BlinkWeb;
import smart.blink.com.card.API.Protocol;
import smart.blink.com.card.BlinkNetCardCall;
import smart.blink.com.card.Tool.SendTools;

/**
 * Created by Ruanjiahui on 2016/12/21.
 */
public class TcpUtils {

    public static void ConnectPc(BlinkNetCardCall call) {
        BlinkLog.Print(Arrays.toString(BlinkWeb.uuid));
        new TcpSocket(BlinkWeb.zIP, BlinkWeb.zPORT, SendTools.ConnectPc(BlinkWeb.uuid), Protocol.CONNECT_SERVER, call);
    }

    public static void LOOKPC(BlinkNetCardCall call) {
        new TcpSocket(BlinkWeb.zIP, BlinkWeb.zPORT, SendTools.LOOKPC(), Protocol.LOOKPC, call);
    }

    public static void Shutdown(int seconds, BlinkNetCardCall call) {
        new TcpSocket(BlinkWeb.zIP, BlinkWeb.zPORT, SendTools.Shutdown(String.valueOf(seconds)), Protocol.Shutdown, call);
    }


    public static void Restart(int seconds, BlinkNetCardCall call) {
        new TcpSocket(BlinkWeb.zIP, BlinkWeb.zPORT, SendTools.Restart(String.valueOf(seconds)), Protocol.Restart, call);
    }

    public static void ChangePwd(String id, String oldPassword, String newPassword, BlinkNetCardCall call) {
        new TcpSocket(BlinkWeb.zIP, BlinkWeb.zPORT, SendTools.ChangePwd(id, oldPassword, newPassword), Protocol.ChangePwd, call);
    }

    public static void GetUploadDir(BlinkNetCardCall call) {
        new TcpSocket(BlinkWeb.zIP, BlinkWeb.zPORT, SendTools.GetUploadDir(), Protocol.GetUploadDir, call);
    }


    public static void SetUploadDir(String path, BlinkNetCardCall call) {
        new TcpSocket(BlinkWeb.zIP, BlinkWeb.zPORT, SendTools.SetUploadDir(path), Protocol.SetUploadDir, call);
    }


    public static void Heart(final BlinkNetCardCall call) {
        new TcpSocket(BlinkWeb.zIP, BlinkWeb.zPORT, SendTools.Heart(), Protocol.Heart, call);
    }

    /**
     * @param newPassword
     * @param call
     */
    public static void ChangePcPwd(String oldPassworld, String newPassword, BlinkNetCardCall call) {
        new TcpSocket(BlinkWeb.zIP, BlinkWeb.zPORT, SendTools.ChangePcPwd(oldPassworld, newPassword), Protocol.ChangePcPwd, call);
    }

    public static void ChangePcPwd(String newPassword, BlinkNetCardCall call) {
        new TcpSocket(BlinkWeb.zIP, BlinkWeb.zPORT, SendTools.ChangePcPwd(newPassword), Protocol.ChangePcPwd, call);
    }

    public static void LookFileMsg(String path, BlinkNetCardCall call) {
        new TcpSocket(BlinkWeb.zIP, BlinkWeb.zPORT, SendTools.LookFileMsg(path), Protocol.LookFileMsg, call);
    }

    public static void DownLoadStart(String path, BlinkNetCardCall call) {
        new TcpSocket(BlinkWeb.zIP, BlinkWeb.zPORT, SendTools.DownloadStartBySubServer(path), Protocol.DownloadStart, call);
    }


    //                             下载到手机的路径        电脑里面要下载文件的全路径   要下载的总块数         回调
    public static void DownLoading(String path, final String filename, final int wantblock, final BlinkNetCardCall call) {
        //new Down(path, filename, wantblock, call);
        new MyDown(path, filename, wantblock, call);
    }

    //                    上传文件的路径（文件夹）    文件名       回调
    public static void Uploading(String path, String filename, BlinkNetCardCall call) {
        //new Upload(path, filename, call);
        new MyUpload(path, filename, call);
    }
}
