package com.blink.blinkp2p.Tool;

public class Protocol {

    public static String UID;
    public static String passwd;
    public static boolean autologin;
    public static boolean isremeber;


    public final static int FILE_PHONE = 1;
    public final static int FILE_PC = 2;
    public final static String FILE_TYPE = "FILETYPE";


    public static final int UDP = 0;
    public static final int TCP = 1;


    private static final int POS_LEFT_TOP = 0;
    private static final int POS_LEFT_BOTTOM = 1;
    private static final int POS_RIGHT_TOP = 2;
    private static final int POS_RIGHT_BOTTOM = 3;
    public final static String ICON_QUITSTART = "QuitStart";
    public final static String ICON_RESTART = "RESTART";
    public final static String ICON_LOCLPC = "LOOKPC";
    public final static String ICON_CHANGEPASSWD = "CHANGEPW";
    public final static String ICON_CAMERA = "CAMERA";
    public final static String ICON_TIMESHUTDOWN = "TIMESHUTDOWN";
    public final static String ICON_SHUTDOWN = "SHUTDOWN";
    public final static String ICON_GETUPDATE = "GETUPDATE";
    public final static String ICON_UPDATE = "UPDATE";


    public static final String SERVER_HOST = "blinkwifi.b-link.net.cn";
    //    	public static final String SERVER_HOST="192.168.16.109";
    //测试
//    public static final String SERVER_HOST = "sandbox.b-link.net.cn";
    //	public static final String TEST_STREAM="";
//	public static final String SERVER_HOST = "192.168.16.100";
//	public static final int SERVER_PORT=7000;
    public static final int SERVER_PORT = 6060;

    //获取外网IP和内网IP的命令
    public static final int WANT = 0;
    public static final int SOMEONE = 1;
    public static final int ANSWER = 2;
    //通过内网或者通过外网进行发送hello进行打洞
    public static final int HELLO = 3;
    public static final int ACK = 4;
    public static final int LOOK = 5;
    public static final int CONNECT = 6;
    public static final int CLIENTHEART = 7;
    public static final int UDP_LOSS_PC = 8;
    public static final int OFFLINE = 8;
    public static final int MSGACK = 18;
    public static final int MOBILE_ASK_SERVER_REPLAY = 108;
    public static final int CONNECT_SUB_SERVER = 109;
    public static final int REPLY_BY_SUB_SERVER = 110;
    public static final int REPLY_REQUEST = 111;

    //��������
    public static final int DOWNLOAD_START = 9;
    public static final int DOWNLOAD_END = 11;
    public static final int DOWNLOAD_END_ACK = 12;
    public static final int DOWNLOADING = 10;
    public static final int DOWNLOADING_BIG = 70;
    public static final int DOWNLOADING_SMALL = 71;
    public static final int DOWNLOADING_BIG_END = 72;
    public static final int DOWNLOADING_FAILED = 73;

    //����ػ�����
    public static final int SHUTDOWN = 13;
    public static final int SHUTDOWN_ACK = 74;
    public static final int RESTART = 25;
    public static final int RESTART_ACK = 75;
    public static final int Lock_ACK = 85;
    public static final int ALTERPW = 31;
    public static final int NOWDIR = 29;
    public static final int NOWDIR_SET = 30;

    public static final int Tcp_Server = -1;
    public static final int LOGIN_SUCCESS = 14;
    public static final int CONNECT_SUBSERVER_SUCCESS = 112;
    public static final int SHUTDOWN_LOSS = 116;
    public static final int RESTART_LOSS = 117;
    public static final int LOCK_LOSS = 1180;
    public static final int ALTERPW_ACK = 89;
    public static final int ALTERPW_ACK_FAIR = 40;
    public static final int ALTERPW_LOSS = 1181;
    public static final int NOWDIR_ASK = 87;
    public static final int NOWDIR_LOSS = -32;
    public static final int NOWDIR_SET_LOSS = -33;
    public static final int NOWDIR_SET_ASK = 86;


    public static final int GETPCINFO = 26;
    public static final int GETPCINFO_ASK = 88;
    public static final int GETPCINFO_LOSS = 127;


    public static final int UPLOAD_BIG = 76;
    public static final int UPLOAD_ARRAY = 77;
    public static final int UPLOAD_ARRAY_ACK = 78;
    public static final int UPLOAD_SMALL = 79;
    public static final int UPLOAD_SMALL_ACK = 80;
    public static final int UPLOAD_BLOCK_END = 81;
    public static final int UPLOAD_STARTING = 125;


    public static final int DIR = 1;
    public static final int FL = 2;
    public static final int END = 3;
    public static final int NOTFOUND = 4;
    public static final int PAN = 5;
    public static final int DOUCUMENT = 6;
    public static final int LOCKPC = 27;

    //�ļ�׷������
    public static final int VideoFile = 125;
    public static final int MUSICFILE = 126;
    public static final int PICTRUEFILE = 127;
    public static final int ZIPFILE = 128;
    public static final int INSTALLFILE = 129;


    //�޸�����
    public static final int CHANGE_PASSWD = 82;


    //handler�������Ϣ
    public static final int DOWNLOAD_START_PROGRESS = 100;
    public static final int WANT_LOST = 101;
    public static final int ERROR_LEN = 200;
    public static final int HELLO_LOST = 102;
    public static final int HOLE_SUCCESS = 103;
    public static final int DISCONNECT = 104;
    public static final int PASSWD_ERROR = 105;
    public static final int PC_OFFLINE = 106;
    public static final int BUSY = 107;
    public static final int UPDATE_LOSS = -35;
    public static final int UPDATE_PROGRESS = 108;
    public static final int MOBILE_TO_LOST = 109;
    public static final int MOBILE_ASK_SERVER_REPLAY_LOSS = 110;
    public static final int CONNECT_SUB_SERVER_LOSS = 111;
    public static final int DOWNLOAD_FAILED = 112;
    public static final int LOOK_SUCCESS = 113;
    public static final int DOWNLOAD_WAIT = 114;
    public static final int LOOK_FALIED = 115;
    public static final int LOOK_PART = 1260;


    public static final int UPLOAD_START = 118;
    public static final int UPLOAD_FAILED = 119;
    public static final int UPLOAD_SUCCESS = 120;
    public static final int UPLOAD_WAIT = 121;

    public static final int CHANGE_PASSWD_LOSS = 122;
    public static final int CHANGE_PASSWD_ORIGINAL_WRONG = 123;
    public static final int CHANGE_PASSWD_SUCCESS = 124;

    public static final int USER_ERROR = 125;
    public static final int UN_KNOWN = -10;


    //��ݿ��С,�����ϴ�ͳһʹ��ͬһ�ײ���
    public static final int BLOCKSIZE = 1024;
    //����ʱ��ÿ������Ŀ���
    public static final int PACK_SIZE = 1300;
    public static final int BLOCK_COUNT = 100;

    //TCP����
    public static final int DOWN_SMALL = 71;

    //������

    public static final int REFRESH_INTERVAL = 500;

    //type
    public static final int PC = 0;
    public static final int MOBILE = 2;
    public static final int PC_FILE = 2;
    public static final int MOBILE_FILE = 3;


    public static boolean testTCP = false;

    //�ϴ�·��
    public static final int SETFILEUP = 30;
    public static final int SETFILEUP_ASK = 86;
    public static final int SETFILEUP_LOSS = -33;
    public static final int HEART_LOSS = -100;

    //Dialog
    public static final int YES = 0;
    public static final int NO = 1;


    public static final int EMPTY = -100;


    public static final int FEEDBACK = 10;
    public static final int FEEDBACK_LOSS = -10;

    public static final int RECONNECT_LOSS = -66;
    public static final int RECONNECT_SUCCESS = 66;

    //图片的标志
    public static final int PICTURE = 10000;
    //视频的标志
    public static final int MOIVE = 10001;
    //文档的标志
    public static final int WORD = 10002;
    //压缩包
    public static final int ZIP = 10003;
    //音乐
    public static final int MUSIC = 10004;
    //安装包
    public static final int APK = 10005;
    //所有文件
    public static final int ALL = 10006;
}
