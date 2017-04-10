package com.blink.blinkp2p.Moudle;

/**
 * Created by Ruanjiahui on 2017/1/6.
 * <p/>
 * <p/>
 * 记录上传下载的数据类
 */
public class DownorUpload {

//    private static DownorUpload downorUpload = null;

    /**
     * 下载或者上传的路径
     */
    private String path = null;

    /**
     * 下载或者上传的名称
     */
    private String name = null;

    /**
     * 上传或者下载的文件大小
     */
    private int Length = 0;


    /**
     * 判断当前处于上传还是下载
     */
    private int FLAG = 0;

    /**
     * 下载的标识
     */
    public static int DOWN = 1;


    /**
     * 上传的标识
     */
    public static int UPLOAD = 2;


//    public synchronized static DownorUpload getInstance() {
//        if (downorUpload == null)
//            downorUpload = new DownorUpload();
//        return downorUpload;
//    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return Length;
    }

    public void setLength(int length) {
        Length = length;
    }

    public int getFLAG() {
        return FLAG;
    }

    public void setFLAG(int FLAG) {
        this.FLAG = FLAG;
    }

//    public static DownorUpload getDownorUpload() {
//        return downorUpload;
//    }
//
//    public static void setDownorUpload(DownorUpload downorUpload) {
//        DownorUpload.downorUpload = downorUpload;
//    }
}
