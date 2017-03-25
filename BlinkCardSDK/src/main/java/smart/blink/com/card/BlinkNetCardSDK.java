package smart.blink.com.card;


import android.util.Log;

import smart.blink.com.card.API.BlinkWeb;
import smart.blink.com.card.Tcp.TcpUtils;
import smart.blink.com.card.Udp.UdpUtils;

/**
 * Created by Ruanjiahui on 2016/11/29.
 * <p/>
 * 所有方法的入口
 */
public class BlinkNetCardSDK {


    private static final String TAG = BlinkNetCardSDK.class.getSimpleName();


    /**
     * 与子服务器进行连接
     *
     * @param call
     */
    public static void CONNECT_TO_SUBSERVER(BlinkNetCardCall call) {
        TcpUtils.ConnectPc(call);
    }

    /**
     * 用户反馈
     * my test code
     *
     * @param username    反馈的用户
     * @param usercontent 反馈的内容
     * @param call        回调界面的接口
     */
    public static void FEEDBACK(String username, String usercontent, BlinkNetCardCall call) {
        UdpUtils.FEEDBACK(username, usercontent, call);
    }

    /**
     * 获取外网IP和内网IP的命令
     *
     * @param id       用户ID
     * @param password 用户密码
     * @param call     处理回调接口的方法
     */
    public static void WANT(String id, String password, BlinkNetCardCall call) {
        UdpUtils.WANT(id, password, call);
    }

    /**
     * 发送Hello进行打洞
     *
     * @param call 处理回调接口的方法
     */
    public static void HELLO(BlinkNetCardCall call) {
        UdpUtils.HELLO(call);
    }

    /**
     * 关机和定时关机
     *
     * @param seconds 距离关机的时间(分钟)
     * @param call    处理回调接口的方法
     */
    public static void Shutdown(int seconds, BlinkNetCardCall call) {
        switch (BlinkWeb.STATE) {
            case BlinkWeb.UDP:
                UdpUtils.Shutdown(seconds, call);
                break;
            case BlinkWeb.TCP:
                TcpUtils.Shutdown(seconds, call);
                break;
            case BlinkWeb.UNONLINE:
                break;
        }
    }

    /**
     * 重启和定时重启
     *
     * @param seconds 距离重启的时间（分钟）
     * @param call    处理回调接口的方法
     */
    public static void Restart(int seconds, BlinkNetCardCall call) {
        switch (BlinkWeb.STATE) {
            case BlinkWeb.UDP:
                UdpUtils.Restart(seconds, call);
                break;
            case BlinkWeb.TCP:
                TcpUtils.Restart(seconds, call);
                break;
            case BlinkWeb.UNONLINE:
                break;
        }
    }

    /**
     * 锁屏
     *
     * @param call 处理回调接口的方法
     */
    public static void LOOKPC(BlinkNetCardCall call) {
        switch (BlinkWeb.STATE) {
            case BlinkWeb.UDP:
                UdpUtils.LOOKPC(call);
                break;
            case BlinkWeb.TCP:
                TcpUtils.LOOKPC(call);
                break;
            case BlinkWeb.UNONLINE:
                break;
        }
    }

    /**
     * 修改登录密码
     *
     * @param id          用户的ID
     * @param oldPassword 用户的旧密码
     * @param newPassword 用户的新密码
     * @param call        处理回调接口的方法
     */
    public static void ChangePwd(String id, String oldPassword, String newPassword, BlinkNetCardCall call) {
        // 修改登录密码，只需要与主服务器
        switch (BlinkWeb.STATE) {
            case BlinkWeb.UDP:
                UdpUtils.ChangePwd(id, oldPassword, newPassword, call);
                break;
            case BlinkWeb.TCP:
                Log.e(TAG, "ChangePwd: " + "我是走了tcp的修改用户登录密码");
                //TcpUtils.ChangePwd(id, oldPassword, newPassword, call);
                UdpUtils.ChangePwd(id, oldPassword, newPassword, call);
                break;
            case BlinkWeb.UNONLINE:
                break;
        }
    }

    /**
     * 获取上传目录的路径
     *
     * @param call 处理回调接口的方法
     */
    public static void GetUploadDir(BlinkNetCardCall call) {
        switch (BlinkWeb.STATE) {
            case BlinkWeb.UDP:
                UdpUtils.GetUploadDir(call);
                break;
            case BlinkWeb.TCP:
                TcpUtils.GetUploadDir(call);
                break;
            case BlinkWeb.UNONLINE:
                break;
        }
    }

    /**
     * 设置上传目录的路径
     *
     * @param path 修改的路径
     * @param call 处理回调接口的方法
     */
    public static void SetUploadDir(String path, BlinkNetCardCall call) {
        switch (BlinkWeb.STATE) {
            case BlinkWeb.UDP:
                UdpUtils.SetUploadDir(path, call);
                break;
            case BlinkWeb.TCP:
                TcpUtils.SetUploadDir(path, call);
                break;
            case BlinkWeb.UNONLINE:
                break;
        }
    }

    /**
     * 心跳包
     *
     * @param call 处理回调接口的方法
     */
    public static void Heart(BlinkNetCardCall call) {
        switch (BlinkWeb.STATE) {
            case BlinkWeb.UDP:
                UdpUtils.Heart(call);
                break;
            case BlinkWeb.TCP:
                Log.e(TAG, "Heart: 我走到了发送心跳TCP的方法中");
                TcpUtils.Heart(call);
                break;
            case BlinkWeb.UNONLINE:
                break;
        }
    }

    /**
     * 修改和设置PC的密码
     *
     * @param newPassword 新密码
     * @param call        处理回调接口的方法
     */
    public static void ChangePcPwd(String newPassword, BlinkNetCardCall call) {
        switch (BlinkWeb.STATE) {
            case BlinkWeb.UDP:
                UdpUtils.ChangePcPwd(newPassword, call);
                break;
            case BlinkWeb.TCP:
                TcpUtils.ChangePcPwd(newPassword, call);
                break;
            case BlinkWeb.UNONLINE:
                break;
        }
    }

    /**
     * 修改和设置PC的密码 自已封装
     *
     * @param newPassword 新密码
     * @param call        处理回调接口的方法
     */
    public static void ChangePcPwd(String oldPassworld, String newPassword, BlinkNetCardCall call) {
        switch (BlinkWeb.STATE) {
            case BlinkWeb.UDP:
                UdpUtils.ChangePcPwd(oldPassworld, newPassword, call);
                break;
            case BlinkWeb.TCP:
                //TcpUtils.ChangePcPwd(newPassword, call);
                TcpUtils.ChangePcPwd(oldPassworld, newPassword, call);
                break;
            case BlinkWeb.UNONLINE:
                break;
        }
    }

    /**
     * 查看文件信息
     *
     * @param path 路径
     * @param call 处理回调接口的方法
     */
    public static void LookFileMsg(String path, BlinkNetCardCall call) {
        switch (BlinkWeb.STATE) {
            case BlinkWeb.UDP:
                UdpUtils.LookFileMsg(path, call);
                break;
            case BlinkWeb.TCP:
                Log.e(TAG, "LookFileMsg: 我走的是tcp方式查看电脑的文件目录");
                TcpUtils.LookFileMsg(path, call);
                break;
            case BlinkWeb.UNONLINE:
                break;
        }
    }

    /**
     * 申请子服务器
     *
     * @param call 处理回调接口的方法
     */
    public static void RelayMsg(BlinkNetCardCall call) {
        UdpUtils.RelayMsg(call);
    }

    /**
     * 子服务器与PC建立连接
     *
     * @param call
     */
    public static void ConnectPc(BlinkNetCardCall call) {
        TcpUtils.ConnectPc(call);
    }

    /**
     * 下载开始
     *
     * @param path 下载路径
     * @param call 处理回调返回的结果
     */
    public static void DownloadStart(String path, BlinkNetCardCall call) {
        switch (BlinkWeb.STATE) {
            case BlinkWeb.UDP:
                UdpUtils.DownLoadStart(path, call);
                break;
            case BlinkWeb.TCP:
                TcpUtils.DownLoadStart(path, call);
                break;
            case BlinkWeb.UNONLINE:
                break;
        }
    }

    /**
     * 下载文件
     *
     * @param path      路径
     * @param filename  文件
     * @param wantblock 下载的总数
     * @param call      处理回调的接口
     */
    public static void DownLoading(String path, String filename, long wantblock, BlinkNetCardCall call) {
        switch (BlinkWeb.STATE) {
            case BlinkWeb.UDP:
                UdpUtils.DownLoading(path, filename, wantblock, call);
                break;
            case BlinkWeb.TCP:
                TcpUtils.DownLoading(path, filename, (int) wantblock, call);
                break;
            case BlinkWeb.UNONLINE:
                break;
        }
    }


    /**
     * 请求上传
     *
     * @param filename 上传文件的名称
     * @param call
     */
    public static void UploadStart(String filename, BlinkNetCardCall call) {
        switch (BlinkWeb.STATE) {
            case BlinkWeb.UDP:
                UdpUtils.UploadStart(filename, call);
                break;
            case BlinkWeb.TCP:
                break;
            case BlinkWeb.UNONLINE:
                break;
        }
    }

    /**
     * 上传文件
     *
     * @param path     文件的路径
     * @param filename 文件的名称
     * @param call     处理回调接口的方法
     */
    public static void Upload(String path, String filename, BlinkNetCardCall call) {
        switch (BlinkWeb.STATE) {
            case BlinkWeb.UDP:
                UdpUtils.Uploading(path, filename, call);
                break;
            case BlinkWeb.TCP:
                TcpUtils.Uploading(path, filename, call);
                break;
            case BlinkWeb.UNONLINE:
                break;
        }
    }


}
