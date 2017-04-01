package smart.blink.com.card.bean;

/**
 * Created by Administrator on 2017/3/31.
 */
public class DownLoadStartByServerRsp extends MainObject {

    public int totalBlock;

    public long fileSize;

    public String fileName;

    public int getTotalBlock() {
        return totalBlock;
    }

    public void setTotalBlock(int totalBlock) {
        this.totalBlock = totalBlock;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
