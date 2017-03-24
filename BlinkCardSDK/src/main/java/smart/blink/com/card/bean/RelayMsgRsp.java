package smart.blink.com.card.bean;

/**
 * Created by Ruanjiahui on 2016/12/20.
 */
public class RelayMsgRsp extends MainObject{

    private String IP = null;
    private int PORT = 0;
    private byte[] UUID = null;

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public int getPORT() {
        return PORT;
    }

    public void setPORT(int PORT) {
        this.PORT = PORT;
    }

    public byte[] getUUID() {
        return UUID;
    }

    public void setUUID(byte[] UUID) {
        this.UUID = UUID;
    }
}
