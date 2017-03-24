package smart.blink.com.card.bean;

import com.google.gson.Gson;

/**
 * Created by Ruanjiahui on 2016/11/29.
 */
public class WantRsp extends MainObject {


    private String nIP = null;
    private int nPORT = 0;
    private String wIP = null;
    private int wPORT = 0;

    /**
     * 0        获取成功
     * 1        密码错误
     * 2        PC不在线
     * 3        连接这台电脑的数量上限
     * 4        用户名错误
     */

    public String getnIP() {
        return nIP;
    }

    public void setnIP(String nIP) {
        this.nIP = nIP;
    }

    public int getnPORT() {
        return nPORT;
    }

    public void setnPORT(int nPORT) {
        this.nPORT = nPORT;
    }

    public String getwIP() {
        return wIP;
    }

    public void setwIP(String wIP) {
        this.wIP = wIP;
    }

    public int getwPORT() {
        return wPORT;
    }

    public void setwPORT(int wPORT) {
        this.wPORT = wPORT;
    }


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
