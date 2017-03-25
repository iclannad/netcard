package smart.blink.com.card.API;

/**
 * Created by Ruanjiahui on 2016/11/29.
 */
public class Protocol {

    // 内网进行打洞失败
    public static final int HELLO_FAILED = 3;
    // 发反馈信息
    public static final int FEEDBACK = 10;

    //获取外网IP和内网IP的命令
    public static final int WANT = 0;
    //通过内网或者通过外网进行发送hello进行打洞
    public static final int HELLO = 3;
    //发心跳包
    public static final int Heart = 7;
    //定时关机的操作
    public static final int Shutdown = 13;
    //重启的操作
    public static final int Restart = 25;
    //发送锁屏
    public static final int LOOKPC = 27;
    //获取上传路径
    public static final int GetUploadDir = 29;
    //设置上传路径
    public static final int SetUploadDir = 30;
    //修改PC登录密码
    public static final int ChangePcPwd = 31;
    //修改登录密码
    public static final int ChangePwd = 82;
    //查看文件
    public static final int LookFileMsg = 5;
    //文件夹
    public static final int controlDIR = 1;
    //文件
    public static final int controlFL = 2;
    //输入完毕
    public static final int controlEND = 3;
    //文件夹未找到
    public static final int controlNOTFOUND = 4;
    //盘符
    public static final int controlPAN = 5;
    //发送请求下载指令
    public static final int DownloadStart = 0x02;
    //发送下载设置参数指令
    public static final int Downloading = 0x04;
    //发送下载指令
    public static final int Downloading1 = 0x15;
    //发送下载完成指令
    public static final int DownloadEnd = 12;
    //发送上传设置参数指令
    public static final int Uploading = 0x03;
    //发送上传指令
    public static final int Uploading1 = 0x12;
    //发送上传完成指令
    public static final int UploadEnd = 0x09;
    //开始发送上传的指令
    public static final int UploadStart = 0x01;
    //申请子服务器
    public static final int RelayMsg = 108;
    //子服务器申请连接PC
    public static final int CONNECT_SERVER = 109;
    //PC链路
    public static final int pcCmdSock = 1;
    //手机链路
    public static final int mobileCmdSock = 2;


    //want命令的接收标识
    public static final int WANTReviced = 2;
    //hello打洞返回的数据
    public static final int HELLOReviced = 4;
    //锁屏返回结果
    public static final int LOOKPCReviced = 85;
    //发心跳包
    public static final int HeartReviced = 7;
    //重启返回结果
    public static final int RestartReviced = 75;
    //关机返回的结果
    public static final int ShutdownReviced = 74;
    //修改登录密码返回的结果
    public static final int ChangePwdReviced = 82;
    //获取上传路径返回的结果
    public static final int GetUploadDirReviced = 87;
    //设置上传路径返回的结果
    public static final int SetUploadDirReviced = 86;

    //修改PC登录密码返回的结果
    public static final int ChangePcPwdReviced = 89;
    //修改PC登录密码失败的结果
    public static final int ChangePcPwdFair = 40;

    //查看文件返回的结果
    public static final int LookFileMsgReviced = 5;
    //发送下载指令返回的结果
    public static final int DownloadStartReviced = 0x06;
    //发送下载中指令返回的结果
    public static final int DownloadingReviced = 0x14;
    //发送下载中指令返回结果
    public static final int DownloadingReviced1 = 0x16;
    //发送上传指令设置参数返回的结果
    public static final int UploadingReviced = 0x11;
    //发送上传指令返回的结果
    public static final int UploadingReviced1 = 0x13;
    //发送上传完成指令返回的结果
    public static final int UploadEndReviced = 81;
    //申请子服务器或者子服务器连接PC的返回的结果
    public static final int RelayMsgReviced = 110;
    //开始发送上传的指令
    public static final int UploadStartReviced = 0x07;
    //拒接上传
    public static final int ReUploadStart = 0x17;
    //异常上传
    public static final int ErrorUploadStart = 0x05;
    //重新上传
    public static final int RestartUpload = 0x25;
}
