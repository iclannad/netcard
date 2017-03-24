package smart.blink.com.card.bean;

import com.google.gson.Gson;

/**
 * Created by Ruanjiahui on 2016/12/1.
 */
public class DownLoadStartRsp extends MainObject {
    //文件模块
    private int totalblock = 0;
    //文件大小
    private long filesize = 0;
    //文件名称
    private String filename = null;


    public int getTotalblock() {
        return totalblock;
    }

    public void setTotalblock(int totalblock) {
        this.totalblock = totalblock;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
