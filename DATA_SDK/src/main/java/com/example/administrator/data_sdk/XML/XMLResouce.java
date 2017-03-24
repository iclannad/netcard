package com.example.administrator.data_sdk.XML;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/17.
 */
public abstract class XMLResouce {

    /**
     * 这个是解析xml文件的抽象方法
     *
     * @param xml
     * @return
     */
    protected abstract ArrayList<Object> parse(InputStream xml, Class loadClass);

    /**
     * 这个是封装xml文件的抽象方法
     *
     * @param list
     * @return
     */
    protected abstract String serialize(ArrayList<Object> list, Class loadClass);


}
