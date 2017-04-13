package smart.blink.com.card.Udp.File;


import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import smart.blink.com.card.API.BlinkLog;

/**
 * Created by Ruanjiahui on 2016/12/14.
 * <p/>
 * <p/>
 * 写入文本的工具类
 */
public class FileWrite {

    private static final String TAG = FileWrite.class.getSimpleName();
    //private static RandomAccessFile randomAccessFile = null;
    private RandomAccessFile randomAccessFile = null;
    private String filanme = null;

    /**
     * 初始化打开文件
     *
     * @param path
     * @param filename
     */
    public FileWrite(String path, String filename) {
        String a[] = filename.split("\\\\");
        File file = new File(path, a[a.length - 1]);

        //判断是否重名，重名则在后面加一
        int n = 1;
        while (file.exists()) {
            String[] b = a[a.length - 1].split("\\.");
            String name = "";
            for (int i = 0; i < b.length - 1; i++)
                name += b[i];
            file = new File(path, name + "(" + n + ")" + "." + b[b.length - 1]);
            n++;
        }

        filanme = file.getName();

        //创建文件夹
        //如果拥有则不创建，否则会自动创建
        //如果拥有文件就直接删除该文件
        //该方法自动实现上面的操作包括自动备份功能
        FileTool.JudgeFile(filename, path);
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
        } catch (FileNotFoundException e) {
            BlinkLog.Error("打开写入文件失败" + e.toString());
        }
    }


    /**
     * 获取新的名称
     *
     * @return
     */
    public String getFilename() {
        return filanme;
    }

    /**
     * 将字节数组写入文件里面
     *
     * @param buffer   字节数组
     * @param flag     这个第几个链路的写入
     * @param position 这个是写到当前链路第几个
     */
    public synchronized void Write(final byte[] buffer, int flag, int position) {
        try {
            if (randomAccessFile != null) {
                randomAccessFile.seek((flag - 1) * 1024 * 1024 * 10 + (position - 1) * 1024);//将文件流的位置移动到pos字节处
                randomAccessFile.write(buffer);
            }
        } catch (IOException e) {
            Close();
            BlinkLog.Error("写入文件失败" + e.toString());
        }
    }


    /**
     * 关闭写入流
     */
    public void Close() {
        try {
            if (randomAccessFile != null) {
                randomAccessFile.close();
                randomAccessFile = null;
            }
        } catch (IOException e) {
            BlinkLog.Error("关闭流失败" + e.toString());
        }
    }
}
