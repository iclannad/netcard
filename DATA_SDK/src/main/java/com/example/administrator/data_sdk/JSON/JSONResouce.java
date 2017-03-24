package com.example.administrator.data_sdk.JSON;

/**
 * Created by Administrator on 2016/8/9.
 */
public abstract class JSONResouce {


    /**
     * 这个是json数据转化成类对象（包括父类获取所有的public）
     *
     * @param loadClass
     * @param json
     * @return
     */
    protected abstract Object setJSONToClass(Class loadClass, String json);

    /**
     * 这个是json数据转化成类对象（包括父类获取所有的public）
     *
     * @param loadClass
     * @param object
     * @param json
     * @return
     */
    protected abstract Object setJSONToClass(Class loadClass, Object object, String json);


    /**
     * 这个是json数据转化成类对象（不包括父类）
     *
     * @param loadClass
     * @param json
     * @return
     */
    protected abstract Object setJSONToClassDeclared(Class loadClass, String json);


    /**
     * 这个是json数据转化成类对象（不包括父类）
     *
     * @param loadClass
     * @param json
     * @return
     */
    protected abstract Object setJSONToClassDeclared(Class loadClass, Object object, String json);


}
