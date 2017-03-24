package com.example.administrator.data_sdk.JSON;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Administrator on 2016/8/9.
 */
public class JSONClass extends JSONResouce {
    /**
     * 这个是json数据转化成类对象（包括父类获取所有的public）
     *
     * @param loadClass
     * @param json
     * @return
     */
    @Override
    protected Object setJSONToClass(Class loadClass, String json) {
        return null;
    }

    /**
     * 这个是json数据转化成类对象（包括父类获取所有的public）
     *
     * @param loadClass
     * @param object
     * @param json
     * @return
     */
    @Override
    protected Object setJSONToClass(Class loadClass, Object object, String json) {
        return null;
    }

    /**
     * 这个是json数据转化成类对象（不包括父类）
     *
     * @param loadClass
     * @param json
     * @return
     */
    @Override
    protected Object setJSONToClassDeclared(Class loadClass, String json) {
        if (json != null) {
            //新建链表对象存在数据
            try {
                //判断当前json数据是不是数组对象还是纯json对象
                if (!json.startsWith("[")) {
                    JSONObject jsonObject = new JSONObject(json);
                    //交给专门处理object对象的类进行操作
                    return new JObjectMain().AnalysisObject(loadClass, jsonObject);
                } else {
//                    JSONArray jsonArray = new JSONArray(json);

                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 这个是json数据转化成类对象（不包括父类）
     *
     * @param loadClass
     * @param object
     * @param json      @return
     */
    @Override
    protected Object setJSONToClassDeclared(Class loadClass, Object object, String json) {
        if (json != null) {
            //新建链表对象存在数据
            try {
                //判断当前json数据是不是数组对象还是纯json对象
                if (!json.startsWith("[")) {
                    JSONObject jsonObject = new JSONObject(json);
                    //交给专门处理object对象的类进行操作
                    return new JObjectMain().AnalysisObject(loadClass, object , jsonObject);
                } else {
//                    JSONArray jsonArray = new JSONArray(json);

                }
            } catch (IllegalAccessException e) {
                return null;
            } catch (InstantiationException e) {
                return null;
            } catch (JSONException e) {
                return null;
            }
        }
        return null;
    }


    private Context context = null;

    /**
     * 提供外部使用的接口(获取所有public 属性的值)
     *
     * @param context
     * @param loadClass
     * @param json
     */
    public Object setJSONToClass(Context context, Class loadClass, String json) {
        this.context = context;
        return setJSONToClass(loadClass, json);
    }


    /**
     * 提供外部使用的接口
     *
     * @param context
     * @param loadClass
     * @param json
     */
    public Object setJSONToClassDeclared(Context context, Class loadClass, String json) {
        this.context = context;
        return setJSONToClassDeclared(loadClass, json);
    }


    /**
     * 提供外部使用的接口
     *
     * @param context
     * @param loadClass
     * @param json
     */
    public Object setJSONToClassDeclared(Context context, Object object , Class loadClass, String json) {
        this.context = context;
        return setJSONToClassDeclared(loadClass, object , json);
    }

    /**
     * 获取json数据包的key值
     *
     * @param json
     * @return
     */
    private ArrayList<String> getJSONKey(JSONObject json) {
        Iterator<String> iterator = json.keys();
        ArrayList<String> colums = new ArrayList<>();
        while (iterator.hasNext()) {
            colums.add(iterator.next() + "");
        }
        return colums;
    }
}
