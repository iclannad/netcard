package com.blink.blinkp2p.Controller;

/**
 * Created by Ruanjiahui on 2017/1/4.
 * <p/>
 * 请求标识
 */
public class ActivityCode {

    /**
     * 获取内外网IP和端口
     */
    public final static int WANT = 0;

    /**
     * 打洞
     */
    public final static int HELLO = 1;

    /**
     * 申请子服务器
     */
    public final static int RelayMsg = 2;

    /**
     * 请求与子服务器连接
     */
    public final static int ConnectPC = 3;

    /**
     * 关机和定时关机
     */
    public final static int Shutdown = 4;
    public final static int setTimeShutdown = 20;

    /**
     * 重启和定时重启
     */
    public final static int Restart = 5;

    /**
     * 锁屏
     */
    public final static int LOOKPC = 6;

    /**
     * 修改登录密码
     */
    public final static int ChangePwd = 7;

    /**
     * 获取上传目录
     */
    public final static int GetUploadDir = 8;


    /**
     * 设置上传目录
     */
    public final static int SetUploadDir = 9;

    /**
     * 心跳包
     */
    public final static int Heart = 10;

    /**
     * 修改PC端的密码
     */
    public final static int ChangePcPwd = 11;

    /**
     * 查看目录
     */
    public final static int LookFileMsg = 12;


    /**
     * 手机文件
     */
    public final static int PhoneFile = 13;

    /**
     * 电脑文件
     */
    public final static int ComputerFile = 14;


    /**
     * 下载开始
     */
    public final static int DownloadStart = 15;


    /**
     * 下载中
     */
    public final static int Downloading = 16;

    /**
     * 提交反馈
     */
    public final static int Feedback = 17;

    /**
     * 上传文件
     */
    public final static int UploadStart = 18;

    /**
     * 上传文件
     */
    public final static int Upload = 19;


    /**
     * 文件
     */
    public final static int FL = 2;

    /**
     * 文件夹
     */
    public final static int DIR = 1;


    /**
     * 盘符
     */
    public final static int PAN = 5;


}
