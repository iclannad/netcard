package com.example.administrator.data_sdk.FileUtil;

import com.example.administrator.data_sdk.FileUtilAbstract.FileRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/3/15.
 * <p/>
 * 这个类专门处理 字节流的实现方法
 */
public class FileSource extends FileRequest {

    private FileOutputStream fileOutputStream = null;
    //保存字节流的长度
    private int lenght = 0;
    //这个是记录每次记录的字节流的数字
    private int readbyte = 0;
    //这个是储存字节流的字节数组
    private byte[] buffer = null;
    //记录文件的长度
    private int sumbyte = 0;


    /**
     * 这个方法是重写实现字节流的写入
     *
     * @param inputStream 文本的字节流
     * @param path        文本的路径
     * @param filename    文本的名称
     */
    @Override
    public void WriteByte(InputStream inputStream, String path, String filename) {
        File file = new File(path, filename);
        try {
            //这个是获取字节流的长度
            lenght = inputStream.available();
            //这个是实现要输出的文本的字节流
            fileOutputStream = new FileOutputStream(file);
            //实例化对象
            //字节数组的长度等于储存字节流的长度
            buffer = new byte[lenght];

            //这句话的意思是
            //readbyte = inputStream.read(buffer) //这意思是将获取出来的字节流用readbyte保存起来
            //readbyte != -1  //这个的意思是当字节流读到-1的时候说明字节流已经读取完毕
            while ((readbyte = inputStream.read(buffer)) != -1) {
                //每次循环获取相加就是等于文件的大小
                sumbyte += readbyte;
                //将字节流写进文本当中
                fileOutputStream.write(buffer, 0, readbyte);
            }
            //刷新
            fileOutputStream.flush();
            //关闭所有的字节流
            inputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
        }
    }

    /**
     * 这个是写字节流的抽象方法
     *
     * @param buffer   文本的字节流
     * @param path     文本的路径
     * @param filename 文本的名称
     */
    @Override
    public void WriteByte(byte[] buffer, String path, String filename) {
        File file = new File(path, filename);
        try {
            //这个是获取字节流的长度
//            lenght = buffer.length;
            //这个是实现要输出的文本的字节流
            fileOutputStream = new FileOutputStream(file);
            //实例化对象
            //字节数组的长度等于储存字节流的长度
            this.buffer = buffer;

            //这句话的意思是
            //readbyte = inputStream.read(buffer) //这意思是将获取出来的字节流用readbyte保存起来
            //readbyte != -1  //这个的意思是当字节流读到-1的时候说明字节流已经读取完毕
//            while ((readbyte = inputStream.read(buffer)) != -1) {
                //每次循环获取相加就是等于文件的大小
//                sumbyte += readbyte;
                //将字节流写进文本当中
                fileOutputStream.write(buffer, 0, buffer.length);
//            }
            //刷新
            fileOutputStream.flush();
            //关闭所有的字节流
//            inputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
        }
    }

    /**
     * 这个方法是重写实现字节流的读入
     *
     * @param path     文件的路径
     * @param filename 文件的名称
     */
    @Override
    public String ReadByte(String path, String filename) {

        File file = new File(path, filename);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);

            //获取文本字节流的长度
            lenght = fileInputStream.available();
            //实例化字节数组
            //设置字节数组的长度
            buffer = new byte[lenght];
            //将文本的字节流写进字节数组中
            fileInputStream.read(buffer);
            //将字节数组转换成字符串返回
            return new String(buffer);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
