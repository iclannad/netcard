package com.example.administrator.data_sdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/12.
 */
public class CommonIntent {

    public final static String DATA = "data";


    public static void IntentActivity(Context context, Class cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        context.startActivity(intent);
    }

    /**
     * 页面跳珠
     *
     * @param context
     * @param cls
     * @param str
     */
    public static void IntentActivity(Context context, Class cls, String str) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("data", str);
        intent.putExtras(bundle);
        intent.setClass(context, cls);
        context.startActivity(intent);
    }


    /**
     * 传输Serializable对象
     *
     * @param context
     * @param cls
     * @param data
     */
    public static void IntentActivity(Context context, Class cls, Serializable data) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);
        intent.putExtras(bundle);
        intent.setClass(context, cls);
        context.startActivity(intent);
    }


    public static void IntentActivity(Context context, Class cls, String str, String str1) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("data", str);
        bundle.putString("flag", str1);
        intent.putExtras(bundle);
        intent.setClass(context, cls);
        context.startActivity(intent);
    }

    public static void IntentActivity(Context context, Class cls, int str, String str1) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("data", str);
        bundle.putString("FLAG", str1);
        intent.putExtras(bundle);
        intent.setClass(context, cls);
        context.startActivity(intent);
    }



    public static void IntentActivity(Context context, Class cls, String str, String str1 , boolean str2) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("data", str);
        bundle.putString("flag", str1);
        bundle.putBoolean("status" , str2);
        intent.putExtras(bundle);
        intent.setClass(context, cls);
        context.startActivity(intent);
    }

    /**
     * 传输Parcelable对象
     *
     * @param context
     * @param cls
     * @param data
     */
    public static void IntentActivity(Context context, Class cls, Parcelable data) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        intent.putExtras(bundle);
        intent.setClass(context, cls);
        context.startActivity(intent);
    }

    /**
     * 传输Parcelable对象
     *
     * @param context
     * @param cls
     * @param data
     */
    public static void IntentActivity(Context context, Class cls, Parcelable data, Parcelable user) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        bundle.putParcelable("user", user);
        intent.putExtras(bundle);
        intent.setClass(context, cls);
        context.startActivity(intent);
    }


    /**
     * 传输Parcelable对象附带返回值
     *
     * @param activity
     * @param cls
     * @param data
     * @param resultCode
     */
    public static void IntentActivity(Activity activity, Class cls, Parcelable data, int resultCode) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        intent.putExtras(bundle);
        intent.setClass(activity, cls);
        activity.startActivityForResult(intent, resultCode);
    }

    /**
     * 跳转页面
     *
     * @param activity
     * @param cls
     * @param data
     * @param resultCode
     * @param user
     */
    public static void IntentActivity(Activity activity, Class cls, Parcelable data, int resultCode, Parcelable user) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        bundle.putParcelable("user", user);
        intent.putExtras(bundle);
        intent.setClass(activity, cls);
        activity.startActivityForResult(intent, resultCode);
    }


    /**
     * 跳转页面
     *
     * @param activity
     * @param cls
     * @param resultCode
     */
    public static void IntentResActivity(Activity activity, Class cls, int resultCode, int data) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("data" , data);
        intent.putExtras(bundle);
        intent.setClass(activity, cls);
        activity.startActivityForResult(intent, resultCode);
    }


    /**
     * 跳转页面
     *
     * @param activity
     * @param cls
     * @param resultCode
     */
    public static void IntentResActivity(Activity activity, Class cls, int resultCode) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        activity.startActivityForResult(intent, resultCode);
    }


    public static void SetActivity(Activity activity, String[] data, int resultCode) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putStringArray("data", data);
        intent.putExtras(bundle);
        activity.setResult(resultCode, intent);
    }


    public static void SetActivity(Activity activity, Parcelable data, int resultCode) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        intent.putExtras(bundle);
        activity.setResult(resultCode, intent);
    }

    public static void SetActivity(Activity activity, int resultCode) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        activity.setResult(resultCode, intent);
    }


}
