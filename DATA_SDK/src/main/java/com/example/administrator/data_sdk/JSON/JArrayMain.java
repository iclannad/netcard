package com.example.administrator.data_sdk.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Administrator on 2016/8/9.
 */
public class JArrayMain {

    /**
     * 专门处理Object对象的方法
     *
     * @param loadClass
     * @param array
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws JSONException
     */
//    public Object AnalysisArray(Class loadClass, JSONArray array) throws IllegalAccessException, InstantiationException, JSONException {
        //获取该类的所有属性
//        Field[] fields = loadClass.getDeclaredFields();

//        ArrayList<String> colums = getJSONKey(json);
        //将cursor用游标的方式取出来
//        Object object = loadClass.newInstance();
        //循环的方法把数据库的列名称给取出来
//        for (String columName : colums) {
//            for (int i = 0; i < fields.length; i++) {
//                //获取类里面的所有属性
//                Field field = fields[i];
//                //设置可以访问
//                field.setAccessible(true);
//                //如果数据库的字段名和类的属性名称一样就说明是同一个变量
//                if (field.getName().equals(columName)) {
//                    //将数据库的数据设置给类的属性
//                    field.set(object, json.get(columName));
//                    break;
//                }
//            }
////        }
//        return object;
//    }


    public void AnalysisArrays(Class loadClass, String json) {

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
