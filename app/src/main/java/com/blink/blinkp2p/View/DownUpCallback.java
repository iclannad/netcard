package com.blink.blinkp2p.View;

/**
 * Created by Ruanjiahui on 2017/1/9.
 * <p/>
 * <p/>
 * 下载与上传显示界面的回调接口
 */
public interface DownUpCallback {

    /**
     * 回调的接口
     *
     * @param position
     * @param object   下载DownLoadingRsp这个类   上传UploadReq这个类
     */
    public void Call(int position, Object object);

    /**
     * 失败的接口
     *
     * @param position
     */
    public void Fail(int position);
}
