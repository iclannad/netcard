package com.example.administrator.data_sdk.XML;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/17.
 */
public class XMLClass extends XMLResouce {


    public ArrayList<Object> getXml(InputStream xml, Class loadClass) {
        return parse(xml, loadClass);
    }

    /**
     * 这个是解析xml文件的抽象方法
     *
     * @param xml
     * @param loadClass
     * @return
     */
    @Override
    protected ArrayList<Object> parse(InputStream xml, Class loadClass) {
        try {
            ArrayList<Object> list = null;
            Object object = null;
            Field[] fields = loadClass.getDeclaredFields();
            String start = null;

            XmlPullParserFactory pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();
//            XmlPullParser parser = Xml.newPullParser(); //由android.util.Xml创建一个XmlPullParser实例
            parser.setInput(xml, "UTF-8");               //设置输入流 并指明编码方式
            int eventType = parser.getEventType();
            Log.e("Ruan", parser.getPrefix() + "---");
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        Log.e("Ruan", eventType + "--" + parser.getName() + "--" + parser.getAttributeValue(null, parser.getName()));
                        start = parser.getName();
                        list = new ArrayList<>();
                        object = loadClass.newInstance();
                        break;
                    case XmlPullParser.START_TAG:
                        for (Field field : fields) {
                            field.setAccessible(true);
                            if (field.getName().equals(parser.getName())) {
                                eventType = parser.next();
//                                Log.e("Ruan" , field.getName() + "--" + parser.getName() + "--" + parser.getText());
//                                field.set(object, parser.getText());
                            }
                        }
                    case XmlPullParser.END_TAG:
//                        if (start.equals(parser.getName())) {
//                            list.add(object);
//                            object = null;
//                        }
                        break;
                }
                eventType = parser.next();
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 这个是封装xml文件的抽象方法
     *
     * @param list
     * @param loadClass
     * @return
     */
    @Override
    protected String serialize(ArrayList<Object> list, Class loadClass) {
        return null;
    }
}
