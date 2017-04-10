package com.blink.blinkp2p.Tool.Thread;

/**
 * Created by Administrator on 2016/9/3.
 */
public interface HandlerImpl {

    /**
     * 处理线程返回的更新界面
     *
     * @param position
     * @param object
     */
    public void myHandler(int position, Object object);

    /**
     * 错误的操作
     * @param position
     */
    public void myError(int position, int error);
}
