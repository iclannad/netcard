package com.example.administrator.data_sdk.JSON;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Administrator on 2016/8/9.
 */
public class JObjectMain {

    /**
     * 专门处理Object对象的方法
     *
     * @param loadClass
     * @param json
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws JSONException
     */
    public Object AnalysisObject(Class loadClass, JSONObject json) throws IllegalAccessException, InstantiationException, JSONException {
        //获取该类的所有属性
        Field[] fields = loadClass.getDeclaredFields();

        ArrayList<String> colums = getJSONKey(json);
        //将cursor用游标的方式取出来
        Object object = loadClass.newInstance();
        //循环的方法把数据库的列名称给取出来
        for (String columName : colums) {
            for (int i = 0; i < fields.length; i++) {
                //获取类里面的所有属性
                Field field = fields[i];
                //设置可以访问
                field.setAccessible(true);
                //如果数据库的字段名和类的属性名称一样就说明是同一个变量
                if (field.getName().equals(columName)) {
                    //将数据库的数据设置给类的属性
                    if ("String".equals(getClassType(field.getGenericType()))) {
                        field.set(object, json.getString(columName));
                    }
                    if ("Integer".equals(getClassType(field.getGenericType()))) {
                        field.set(object, json.getInt(columName));
                    }
                    break;
                }
            }
        }
        return object;
    }


    /**
     * 专门处理Object对象的方法
     *
     * @param loadClass
     * @param json
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws JSONException
     */
    public Object AnalysisObject(Class loadClass, Object objects, JSONObject json) throws IllegalAccessException, InstantiationException, JSONException {
        //获取该类的所有属性
        Field[] fields = loadClass.getDeclaredFields();

        ArrayList<String> colums = getJSONKey(json);
        //将cursor用游标的方式取出来
        Object object = objects;
        //循环的方法把数据库的列名称给取出来
        for (String columName : colums) {
            for (int i = 0; i < fields.length; i++) {
                //获取类里面的所有属性
                Field field = fields[i];
                //设置可以访问
                field.setAccessible(true);
                //如果数据库的字段名和类的属性名称一样就说明是同一个变量
                if (field.getName().equals(columName)) {
                    //将数据库的数据设置给类的属性
                    if ("String".equals(getClassType(field.getGenericType()))) {
                        field.set(object, json.getString(columName));
                    }
                    if ("Integer".equals(getClassType(field.getGenericType()))) {
                        field.set(object, json.getInt(columName));
                    }
                    break;
                }
            }
        }
        return object;
    }


    private String getClassType(Type type) {
        if (type.toString().equals("class java.lang.String")) {
            return "String";
        }
        if (type.toString().equals("class java.lang.Integer")) {
            return "Integer";
        }
        if (type.toString().equals("class java.lang.Double")) {
            return "Double";
        }
        return null;
    }


    public void AnalysisObjects(Class loadClass, String json) {

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
