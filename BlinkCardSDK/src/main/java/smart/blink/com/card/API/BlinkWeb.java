package smart.blink.com.card.API;

/**
 * Created by Ruanjiahui on 2016/11/29.
 * <p/>
 * SDK 的请求网址类
 */
public class BlinkWeb {


    //内网IP
    public static String nIP = "192.160.25.100";
    //外网IP
    public static String wIP = "";
    //内网端口
    public static int nPORT = 59896;
    //外网端口
    public static int wPORT = 0;
    //子服务器IP
    public static String zIP = "";
    //子服务器端口
    public static int zPORT = 0;
    //子服务器的uuid
    public static byte[] uuid = null;

    //记录当前访问状态
    //0为离线
    //1为内网
    //2为外网
    public static int STATE = 1;

    public final static int UNONLINE = 0;
    public final static int UDP = 1;
    public final static int TCP = 2;

    //SDK里面使用的IP
    public static String tIP = "";
    //SDK里面使用的端口
    public static int tPORT = 0;

    //public static final String SERVER = "blinkwifi.b-link.net.cn";
    public static final String SERVER = "sandbox.b-link.net.cn";
    public static final int PORT = 6060;
}
