package smart.blink.com.card.Tcp.File;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import smart.blink.com.card.API.BlinkLog;

/**
 * Created by Ruanjiahui on 2016/12/14.
 * <p/>
 * <p/>
 * 写入文本的工具类
 */
public class FileWrite {

    private static FileOutputStream fileOutputStream = null;

    /**
     * 初始化打开文件
     *
     * @param path
     * @param filename
     */
    public FileWrite(String path, String filename) {
        String a[] = filename.split("/");
        File file = new File(path, a[a.length - 1]);

        //判断是否重名，重名则在后面加一
        int n = 1;
        while (file.exists()) {
            String[] b = a[a.length - 1].split("\\.");
            file = new File(path, b[0] + "(" + n + ")" + "." + b[1]);
            n++;
        }
        //创建文件夹
        //如果拥有则不创建，否则会自动创建
        //如果拥有文件就直接删除该文件
        //该方法自动实现上面的操作包括自动备份功能
//        FileTool.JudgeFile(filename, path);
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            BlinkLog.Error("打开文件失败" + e.toString());
        }
    }

    /**
     * 将字节数组写入文件里面
     *
     * @param buffer
     */
    public void Write(final byte[] buffer) {
        try {

            if (fileOutputStream != null) {
                fileOutputStream.write(buffer);
                fileOutputStream.flush();
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
            if (fileOutputStream != null) {
                fileOutputStream.close();
                fileOutputStream = null;
            }
        } catch (IOException e) {
            BlinkLog.Error("关闭流失败" + e.toString());
        }
    }
}
