package blink.com.blinkcard320.Tool.Utils;

import android.util.Log;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

import blink.com.blinkcard320.View.ListItem;

/**
 * Created by Administrator on 2017/3/20.
 */
public class Utils {
    //技术支持电话
    public static String Phone = "400-998-5533";

    public static boolean IsAdpter(String s) {
        Log.d("newpw==", "newpwlen=" + s.length());
        if (s == null || s.equals("") || s.length() == 0 || s.length() > 12 || s.contains(" ")) {
            return false;
        }
        return true;
    }

    public static ArrayList<String> sortFileList(String parent) {
        File file = new File(parent+"/");
        String[] files = file.list();
        if(files == null)
            return null;
        if(files.length == 0)
            return null;
        else {
            List<String> dir_list = new ArrayList<String>();
            List<String> file_list = new ArrayList<String>();
            for(int i = 0; i < files.length; i++) {
                File tmp = new File(parent + "/" + files[i]);
                if(tmp.isDirectory())
                    dir_list.add(tmp.getPath());
                else
                    file_list.add(tmp.getPath());
            }
            Collections.sort(dir_list);
            Collections.sort(file_list);
            for (String string : file_list) {
                dir_list.add(string);
            }
            return (ArrayList<String>) dir_list;
        }
    }

}
