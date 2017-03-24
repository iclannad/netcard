package smart.blink.com.card.API;

/**
 * Created by Ruanjiahui on 2016/11/29.
 * <p/>
 * SDK的错误码
 */
public class ErrorNo {

    //输入的长度太长
    public static final int ErrorlengthLong = 0;
    //IP错误
    public static final int ErrorIP = 1;
    //校验码出错
    public static final int ErrorChecksum = 2;
    //写入失败
    public static final int ErrorWrite = 3;
    //TCP下载超时
    public static final int ErrorTimeout = 4;
    //检验码出错
    public static final int ErrorCheck = 5;
    //socket异常关闭
    public static final int ErrorSocket = 6;
    //socket读操作异常关闭
    public static final int ErrorRead = 7;


    /**
     * Socket 服务器端异常断开
     */
    public static final String SocketError = "Socket closed";

    /**
     * Socket 服务器端异常断开导致客户端还在继续读写
     */
    public static final String ReadError = "recvfrom failed: ECONNRESET (Connection reset by peer)";


}
