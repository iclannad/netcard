package com.blink.blinkp2p.Controller.Activity.splash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;

import com.blink.blinkp2p.Tool.System.Tools;
import com.blink.blinkp2p.Tool.Utils.StringUtils;
import com.blink.blinkp2p.View.MyPersonalProgressDIalog;
import com.blink.blinkp2p.View.MyProgressDIalog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.File;

import blink.com.blinkcard320.R;

/**
 * Created by Administrator on 2017/4/10.
 */
public class CheckVersionThread {
    int overtime = 5;
    public static String vurl = "http://app.b-link.net.cn/wifi/Android/version.xml";
    private Handler mhandler;
    private String myversion;
    private String hversion;
    private String download;
    private Context context;

    public CheckVersionThread(Handler mHandler, Context context) {
        // TODO Auto-generated constructor stub
        mhandler = mHandler;
        this.context = context;
        getversion();
        checkversion();
    }

    private void getversion() {
        myversion = Tools.getVersionName(context);
    }

    private void Isversion() {
        String myversion = Tools.getVersionName(context);
        int isupdate = 0;
        int[] mynum = StringUtils.VerStringToInt(myversion);
        int[] sernum = StringUtils.VerStringToInt(hversion);

        for (int i = 0; i < sernum.length; i++) {
            if (sernum[i] > mynum[i]) {
                isupdate = 1;
                break;
            }
            if (sernum[i] < mynum[i]) {
                isupdate = -1;
                break;
            }
        }
        if (isupdate == 0) {
            if (hversion.length() > myversion.length()) {
                isupdate = 1;
            } else {
                isupdate = -1;
            }
        }
        if (isupdate == 1) {
            MyProgressDIalog.CreateNolmalDialog(
                    context, R.string.update, R.string.findversion, mhandler, SplashActivity.VersionUpdate, -1);
        } else {
            mhandler.sendEmptyMessage(SplashActivity.VersionNoUpdate);
        }
    }


    private void checkversion() {
        HttpUtils http = new HttpUtils(overtime * 1000);
        http.send(HttpRequest.HttpMethod.GET, vurl, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                // TODO Auto-generated method stub
                mhandler.sendEmptyMessage(SplashActivity.VersionOvertime);
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                // version
                String[] version = arg0.result
                        .split("<version>");
                String[] v2 = version[1].split("</version>");
                hversion = v2[0];
                // down
                String[] res = arg0.result
                        .split("<apkdownload>");
                String[] res1 = res[1].split("</apkdownload>");
                download = res1[0];
//                LG.i(getClass(), "download==" + download + "version="
//                        + hversion);
                Isversion();
            }
        });

    }

    public void downloadApk() {
//        MyProgressDIalog myProgressDIalog = new MyProgressDIalog(context);
//        myProgressDIalog.init();
//        myProgressDIalog.showProgressDialog();


        File f = new File(Environment.getExternalStorageDirectory()
                .getAbsoluteFile() + "/blinkrouteapk" + hversion + ".apk");
        if (f.exists()) {
            f.delete();
        }
        //LG.i(getClass(), "downpath==" + f.getPath());
        HttpUtils http = new HttpUtils(5 * 1000);
        HttpHandler handler = http.download(download, f.getPath(), true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                new RequestCallBack<File>() {

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onLoading(long total, long current,
                                          boolean isUploading) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        //LG.i(getClass(), "下载成功");
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(
                                Uri.fromFile(responseInfo.result),
                                "application/vnd.android.package-archive");
                        context.startActivity(intent);
                        ((Activity) context).finish();

                        // 关闭对话框
                        MyPersonalProgressDIalog.getInstance(context).dissmissProgress();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        //LG.i(getClass(), "下载失败" + msg);
                        mhandler.sendEmptyMessage(SplashActivity.VersionOvertime);
                    }
                });
    }

}
