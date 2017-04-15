package com.blink.blinkp2p.Tool.Utils.download;

/**
 * Created by Administrator on 2017/4/14.
 */
public class DownTask {
    public int id;  // 任务序号
    public int status;  // 任务状态 0 表示等待下载，1表示下载中，2表示下载完成
    public int progress;    // 任务下载进度
    public String speed;    // 任务下载速度
    public String name;     // 任务名
    public String path;
    public boolean isError; // 任务是否导常
}
