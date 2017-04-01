package smart.blink.com.card.Tool;

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

/**
 * Created by Administrator on 2017/3/31.
 */
public class Util {

    public static int lastNullIndex(byte[] b) {
        int index = 0;
        for(int i = 0; i < b.length - 1; i ++) {
            if(b[i] != 0 && b[i] != -52) {
                index = i;
            }
        }
        return index;
    }

    public static int StringEnd(byte[] b) {
        int index = 0;
        for(int i = 0; i < b.length - 1; i ++) {
            index=i;
            if(b[i]==0) {
                return index;
            }
        }
        return index;
    }

    public static String forWard(String s1, String s2) {
        return s1+s2+"\\";
    }
    public static String forWardPhone(String s1, String s2) {
        return s1+s2+"/";
    }
    public static String backWard(String input) {
        String result = "";
        String s[] = input.split("\\\\");
        for(int i= 0; i< s.length -1; i++) {
            result+= s[i] + "\\";
        }
        if(result.equals(""))
            result = "/";
        return result;
    }
    public static String backWardPhone(String input) {
        String result = "";
        if(input == "/")
            return "/";
        String s[] = input.split("/");
        for(int i= 0; i< s.length -1; i++) {
            result+= s[i] + "/";
        }
        return result;
    }
    public static String ISOtoUTF(String s) throws UnsupportedEncodingException {
        return new String(s.getBytes("8859_1"), "UTF-8");
    }
    public static String GBtoISO(String s) throws UnsupportedEncodingException {
        return new String (s.getBytes("GB2312"), "8859_1");
    }
    public static String getDateStr(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        Formatter ft=new Formatter(Locale.CHINA);
        String s = ft.format("%1$tY��%1$tm��%1$td��%1$tA��%1$tT:%1$tL", cal).toString();
        ft.close();
        return s;
    }
    //��ȡ�ļ�����1.txt
    public static String getTrueName(String filename) {
        String[] s_arr = filename.split("\\\\");
        return s_arr[s_arr.length-1];
    }

    public static boolean IsAdpter(String s){
        Log.d("newpw==","newpwlen="+s.length());
        if(s==null||s.equals("")||s.length()==0||s.length()>12||s.contains(" ")){
            return false;
        }
        return true;
    }

    //��ȡ�����ļ���,��C:\Users\1.txt
    public static String getCmpName(String filename) {
        int index = filename.lastIndexOf(".");
        return filename.substring(0, index);
    }

    //һ���ַ���ȡ���ض�string
    public static String getStringChild(String str,String start,String end){
        String[] temp=str.split(start);
        String[] res=temp[1].split(end);
        Log.d("getstring", res[0]);
        int i=0;
        for(i=0;i<res[0].length();i++){
            if(res[0].charAt(i)!=32)
                break;
        }
        res[0]=res[0].substring(i);
        return res[0];
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
