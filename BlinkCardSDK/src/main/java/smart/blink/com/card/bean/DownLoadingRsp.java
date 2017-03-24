package smart.blink.com.card.bean;

/**
 * Created by Ruanjiahui on 2016/12/1.
 */
public class DownLoadingRsp extends MainObject {


    //文件的总大小
    private int totolSize = 0;
    private int Size = 0;
    //当前下载的ID
    private int blockId = 0;
    //当前下载ID的长度
    private int blockLength = 0;
    //下载文件名称
    private String filename = null;
    //下载的文件数据
    private byte[] data = null;
    //校验码
    private int cksum = 0;
    //判断下载是否完成
    private boolean isEnd = false;
    //当前下载的速度
    private String Speed = null;

    public String getSpeed() {
        return Speed;
    }

    public void setSpeed(String speed) {
        Speed = speed;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public int getTotolSize() {
        return totolSize;
    }

    public void setTotolSize(int totolSize) {
        this.totolSize = totolSize;
    }

    public int getSize() {
        return Size;
    }

    public void setSize(int size) {
        Size = size;
    }

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    public int getBlockLength() {
        return blockLength;
    }

    public void setBlockLength(int blockLength) {
        this.blockLength = blockLength;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getCksum() {
        return cksum;
    }

    public void setCksum(int cksum) {
        this.cksum = cksum;
    }
}
