package smart.blink.com.card.Tcp.File;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import smart.blink.com.card.API.BlinkLog;
import smart.blink.com.card.bean.UploadReq;

/**
 * Created by Ruanjiahui on 2016/12/22.
 */
public class FileRead {

    private static RandomAccessFile randomAccessFile = null;

    private String path = null;

    private long length = 0;

    public FileRead(String path , String filename) {
        this.path = path;

        File file = new File(path , filename);

        length = file.length();
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
        } catch (FileNotFoundException e) {
            BlinkLog.Error("打开读取文件失败" + e.toString());
        }
    }

    public UploadReq Read(UploadReq uploadReq) {
        byte[] buffer = new byte[1024];
        try {
            randomAccessFile.seek(uploadReq.getBlockID() * 1024);//将文件流的位置移动到pos字节处
            int n = randomAccessFile.read(buffer);

            uploadReq.setRead(false);
            if (n != 1024) {
                uploadReq.setRead(true);
                Close();
            }
            uploadReq.setData(buffer);
            uploadReq.setBlockSize((int) (length / 1024) + 1);
            uploadReq.setBlockLength(n);

        } catch (IOException e) {
            BlinkLog.Error("读取文件失败" + e.toString());
        }

        return uploadReq;
    }

    /**
     * 关闭读取文件流
     */
    public void Close() {
        try {
            if (randomAccessFile != null) {
                randomAccessFile.close();
                randomAccessFile = null;
            }
        } catch (IOException e) {
            BlinkLog.Error("关闭读取文件流失败" + e.toString());
        }
    }
}
