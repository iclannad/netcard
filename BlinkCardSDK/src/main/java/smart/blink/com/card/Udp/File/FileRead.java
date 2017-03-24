package smart.blink.com.card.Udp.File;


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

    private static long length = 0;
    private static UploadReq uploadReq = null;
    private String filename = null;

    public FileRead(String path) {
        this.path = path;

        File file = new File(path);

        filename = file.getName();
        length = file.length();
        uploadReq = new UploadReq();
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
        } catch (FileNotFoundException e) {
            BlinkLog.Error("打开读取文件失败" + e.toString());
        }
    }

    /**
     * 返回新的文件名称
     *
     * @return
     */
    public String getFilename() {
        return filename;
    }


    public static long getFileSize() {
        return length;
    }

    /**
     * 读取文件数据
     *
     * @param wantblock 每个线程的标识
     * @param flag      第几个线程
     * @return
     */
    public synchronized UploadReq Read(int wantblock, int flag) {
        byte[] buffer = new byte[1024];
        try {
            randomAccessFile.seek((flag - 1) * 1024 * 1024 * 10 + (wantblock - 1) * 1024);//将文件流的位置移动到pos字节处
            int n = randomAccessFile.read(buffer);

            if (n < 1024) {
                byte[] buf = new byte[n];
                for (int i = 0; i < n; i++)
                    buf[i] = buffer[i];
            }

            uploadReq.setData(buffer);
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
