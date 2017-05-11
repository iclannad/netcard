package com.blink.blinkp2p.Controller;


import com.blink.blinkp2p.Tool.Thread.HandlerImpl;

import smart.blink.com.card.BlinkNetCardCall;
import smart.blink.com.card.BlinkNetCardSDK;
import smart.blink.com.card.bean.MainObject;

/**
 * Created by Ruanjiahui on 2017/1/4.
 */
public class NetCardController {

    private static final String TAG = NetCardController.class.getSimpleName();


    /**
     * 与子服务器建立连接
     *
     * @param handler
     */
    public static void CONNECT_TO_SUBSERVER(final HandlerImpl handler) {
        BlinkNetCardSDK.CONNECT_TO_SUBSERVER(new BlinkNetCardCall() {

            @Override
            public void onSuccess(int position, MainObject mainObject) {
                handler.myHandler(ActivityCode.ConnectPC, mainObject);
            }

            @Override
            public void onFail(int error) {
                handler.myError(ActivityCode.ConnectPC,error);
            }
        });
    }

    /**
     * 用户反馈
     * my test code
     *
     * @param username    反馈的用户
     * @param usercontent 反馈的内容
     * @param handler     回调界面的接口
     */
    public static void FEEDBACK(String username, String usercontent, final HandlerImpl handler) {
        BlinkNetCardSDK.FEEDBACK(username, usercontent, new BlinkNetCardCall() {
            @Override
            public void onSuccess(int position, MainObject mainObject) {
                // TODO: 2017/3/21  
                handler.myHandler(ActivityCode.Feedback, mainObject);
            }

            @Override
            public void onFail(int error) {
                handler.myError(ActivityCode.Feedback, error);
            }
        });
    }

    /**
     * 获取内外网IP和端口
     *
     * @param id       账号
     * @param password 密码
     * @param handler  回调界面的接口
     */
    public static void WANT(String id, String password, final HandlerImpl handler) {
        BlinkNetCardSDK.WANT(id, password, new BlinkNetCardCall() {
            @Override
            public void onSuccess(int position, MainObject mainObject) {
                handler.myHandler(ActivityCode.WANT, mainObject);
            }

            @Override
            public void onFail(int error) {
                handler.myError(ActivityCode.WANT, error);
            }
        });
    }

    /**
     * 打洞
     *
     * @param handler
     */
    public static void HELLO(final HandlerImpl handler) {
        BlinkNetCardSDK.HELLO(new BlinkNetCardCall() {
            @Override
            public void onSuccess(int position, MainObject mainObject) {
                handler.myHandler(ActivityCode.HELLO, mainObject);
            }

            @Override
            public void onFail(int error) {
                handler.myError(ActivityCode.HELLO, error);
            }
        });
    }

    /**
     * 申请子服务器
     *
     * @param handler
     */
    public static void RelayMsg(final HandlerImpl handler) {
        BlinkNetCardSDK.RelayMsg(new BlinkNetCardCall() {
            @Override
            public void onSuccess(int position, MainObject mainObject) {
                handler.myHandler(ActivityCode.RelayMsg, mainObject);
            }

            @Override
            public void onFail(int error) {
                handler.myError(ActivityCode.RelayMsg, error);
            }
        });
    }

    /**
     * 请求与子服务器连接
     *
     * @param handler
     */
    public static void ConnectPc(final HandlerImpl handler) {
        BlinkNetCardSDK.ConnectPc(new BlinkNetCardCall() {
            @Override
            public void onSuccess(int position, MainObject mainObject) {
                handler.myHandler(ActivityCode.ConnectPC, mainObject);
            }

            @Override
            public void onFail(int error) {
                handler.myError(ActivityCode.ConnectPC, error);
            }
        });
    }

    /**
     * 关机和定时关机
     *
     * @param seconds 定时的时间（单位分钟）如果为0则立即关机
     * @param handler
     */
    public static void Shutdown(int seconds, final HandlerImpl handler) {
        BlinkNetCardSDK.Shutdown(seconds, new BlinkNetCardCall() {
            @Override
            public void onSuccess(int position, MainObject mainObject) {
                handler.myHandler(ActivityCode.Shutdown, mainObject);
            }

            @Override
            public void onFail(int error) {
                handler.myError(ActivityCode.Shutdown, error);
            }
        });
    }

    public static void setTimeShutdown(int seconds, final HandlerImpl handler) {
        BlinkNetCardSDK.Shutdown(seconds, new BlinkNetCardCall() {
            @Override
            public void onSuccess(int position, MainObject mainObject) {
                handler.myHandler(ActivityCode.setTimeShutdown, mainObject);
            }

            @Override
            public void onFail(int error) {
                handler.myError(ActivityCode.setTimeShutdown, error);
            }
        });
    }

    /**
     * 重启和定时重启
     *
     * @param seconds 重启的时间（单位分钟）如果为0则立即重启
     * @param handler
     */
    public static void Restart(int seconds, final HandlerImpl handler) {
        BlinkNetCardSDK.Restart(seconds, new BlinkNetCardCall() {
            @Override
            public void onSuccess(int position, MainObject mainObject) {
                handler.myHandler(ActivityCode.Restart, mainObject);
            }

            @Override
            public void onFail(int error) {
                handler.myError(ActivityCode.Restart, error);
            }
        });
    }

    /**
     * 锁屏
     *
     * @param handler
     */
    public static void LOOKPC(final HandlerImpl handler) {
        BlinkNetCardSDK.LOOKPC(new BlinkNetCardCall() {
            @Override
            public void onSuccess(int position, MainObject mainObject) {
                handler.myHandler(ActivityCode.LOOKPC, mainObject);
            }

            @Override
            public void onFail(int error) {
                handler.myError(ActivityCode.LOOKPC, error);
            }
        });
    }

    /**
     * 修改登录密码
     *
     * @param id          用户名
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param handler
     */
    public static void ChangePwd(String id, String oldPassword, String newPassword, final HandlerImpl handler) {
        BlinkNetCardSDK.ChangePwd(id, oldPassword, newPassword, new BlinkNetCardCall() {
            @Override
            public void onSuccess(int position, MainObject mainObject) {
                handler.myHandler(ActivityCode.ChangePwd, mainObject);
            }

            @Override
            public void onFail(int error) {
                handler.myError(ActivityCode.ChangePwd, error);
            }
        });
    }

    /**
     * 获取上传目录
     *
     * @param handler
     */
    public static void GetUploadDir(final HandlerImpl handler) {
        BlinkNetCardSDK.GetUploadDir(new BlinkNetCardCall() {
            @Override
            public void onSuccess(int position, MainObject mainObject) {
                handler.myHandler(ActivityCode.GetUploadDir, mainObject);
            }

            @Override
            public void onFail(int error) {
                handler.myError(ActivityCode.GetUploadDir, error);
            }
        });
    }

    /**
     * 设置上传目录
     *
     * @param path    上传路径
     * @param handler
     */
    public static void SetUploadDir(String path, final HandlerImpl handler) {
        BlinkNetCardSDK.SetUploadDir(path, new BlinkNetCardCall() {
            @Override
            public void onSuccess(int position, MainObject mainObject) {
                handler.myHandler(ActivityCode.SetUploadDir, mainObject);
            }

            @Override
            public void onFail(int error) {
                handler.myError(ActivityCode.SetUploadDir, error);
            }
        });
    }

    /**
     * 发送心跳包
     *
     * @param handler
     */
    public static void Heart(final HandlerImpl handler) {
        BlinkNetCardSDK.Heart(new BlinkNetCardCall() {
            @Override
            public void onSuccess(int position, MainObject mainObject) {
                handler.myHandler(ActivityCode.Heart, mainObject);
            }

            @Override
            public void onFail(int error) {
                handler.myError(ActivityCode.Heart, error);
            }
        });
    }

    /**
     * 修改PC端的密码 他写的
     *
     * @param newPassword 新密码
     * @param handler
     */
    public static void ChangePcPwd(String newPassword, final HandlerImpl handler) {
        BlinkNetCardSDK.ChangePcPwd(newPassword, new BlinkNetCardCall() {
            @Override
            public void onSuccess(int position, MainObject mainObject) {
                handler.myHandler(ActivityCode.ChangePcPwd, mainObject);
            }

            @Override
            public void onFail(int error) {
                handler.myError(ActivityCode.ChangePcPwd, error);
            }
        });

    }

    /**
     * 修改PC端的密码     自已封装
     *
     * @param newPassword 新密码
     * @param handler
     */
    public static void ChangePcPwd(String oldPassworld, String newPassword, final HandlerImpl handler) {
        BlinkNetCardSDK.ChangePcPwd(oldPassworld, newPassword, new BlinkNetCardCall() {
            @Override
            public void onSuccess(int position, MainObject mainObject) {
                handler.myHandler(ActivityCode.ChangePcPwd, mainObject);
            }

            @Override
            public void onFail(int error) {
                handler.myError(ActivityCode.ChangePcPwd, error);
            }
        });

    }

    /**
     * 查看文件目录
     *
     * @param path
     * @param handler
     */
    public static void LookFileMsg(String path, final HandlerImpl handler) {
        BlinkNetCardSDK.LookFileMsg(path, new BlinkNetCardCall() {
            @Override
            public void onSuccess(int position, MainObject mainObject) {
                handler.myHandler(ActivityCode.LookFileMsg, mainObject);
            }

            @Override
            public void onFail(int error) {
                handler.myError(ActivityCode.LookFileMsg, error);
            }
        });
    }

    /**
     * 请求下载
     *
     * @param path    下载路径
     * @param handler
     */
    public static void DownloadStart(String path, final HandlerImpl handler) {
        BlinkNetCardSDK.DownloadStart(path, new BlinkNetCardCall() {
            @Override
            public void onSuccess(int position, MainObject mainObject) {
                handler.myHandler(ActivityCode.DownloadStart, mainObject);
            }

            @Override
            public void onFail(int error) {
                handler.myError(ActivityCode.DownloadStart, error);
            }
        });
    }

    /**
     * 下载文件
     *
     * @param path      下载储存路径
     * @param filename  文件名称
     * @param wantblock 模块数
     * @param handler
     */
    public static void DownLoading(String path, String filename, int wantblock, final HandlerImpl handler) {
        BlinkNetCardSDK.DownLoading(path, filename, wantblock, new BlinkNetCardCall() {
            @Override
            public void onSuccess(int position, MainObject mainObject) {
                handler.myHandler(ActivityCode.Downloading, mainObject);
            }

            @Override
            public void onFail(int error) {
                handler.myError(ActivityCode.Downloading, error);
            }
        });
    }

    /**
     * 请求上传
     *
     * @param filename 请求上传文件的名字
     * @param handler
     */
    public static void UploadStart(String filePath,String filename, final HandlerImpl handler) {
        BlinkNetCardSDK.UploadStart(filePath,filename, new BlinkNetCardCall() {
            @Override
            public void onSuccess(int position, MainObject mainObject) {
                handler.myHandler(ActivityCode.UploadStart, mainObject);
            }

            @Override
            public void onFail(int error) {
                handler.myError(ActivityCode.DownloadStart, error);
            }
        });
    }

    /**
     * 上传文件
     *
     * @param path     上传文件的路径
     * @param filename 文件名称
     * @param handler
     */
    public static void Upload(String path, String filename, final HandlerImpl handler) {
        BlinkNetCardSDK.Upload(path, filename, new BlinkNetCardCall() {
            @Override
            public void onSuccess(int position, MainObject mainObject) {
                handler.myHandler(ActivityCode.Upload, mainObject);
            }

            @Override
            public void onFail(int error) {
                handler.myError(ActivityCode.Upload, error);
            }
        });
    }
}
