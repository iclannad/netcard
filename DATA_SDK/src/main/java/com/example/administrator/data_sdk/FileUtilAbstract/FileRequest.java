package com.example.administrator.data_sdk.FileUtilAbstract;

import java.io.InputStream;

/**
 * Created by Administrator on 2016/3/15.
 * <p/>
 * 这个类专门处理 字节流的实现方法的抽象类
 */
public abstract class FileRequest {


    /**
     * 这个是写字节流的抽象方法
     *
     * @param inputStream 文本的字节流
     * @param path        文本的路径
     * @param filename    文本的名称
     */
    public abstract void WriteByte(InputStream inputStream, String path, String filename);


    /**
     * 这个是写字节流的抽象方法
     *
     * @param buffer   文本的字节流
     * @param path     文本的路径
     * @param filename 文本的名称
     */
    public abstract void WriteByte(byte[] buffer, String path, String filename);


    /**
     * 这个是读字节流的抽象方法
     *
     * @param path     文件的路径
     * @param filename 文件的名称
     */
    public abstract String ReadByte(String path, String filename);
}
