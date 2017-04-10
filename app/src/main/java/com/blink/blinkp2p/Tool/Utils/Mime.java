package com.blink.blinkp2p.Tool.Utils;

import android.content.Intent;
import android.net.Uri;


import com.blink.blinkp2p.R;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class Mime {
    /**
     * ����ļ���׺���ö�Ӧ��MIME���͡�
     *
     * @param file
     */
    public static String getMIMEType(File file) {
        String type = "*/*";
        String fName = file.getName();
        //��ȡ��׺��ǰ�ķָ���"."��fName�е�λ�á�
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* ��ȡ�ļ��ĺ�׺�� */
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;
        //��MIME���ļ����͵�ƥ������ҵ���Ӧ��MIME���͡�
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    public static String getMIMEType(String fileName) {
        String type = "*/*";
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex < 0)
            return type;
        String end = fileName.substring(dotIndex, fileName.length()).toLowerCase();
        if (end == "")
            return type;
        for (int i = 0; i < MIME_MapTable.length; i++)
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        return type;
    }

    /**
     * ���ļ�
     *
     * @param file
     */
    public static Intent openFileIntent(File file) {
        //Uri uri = Uri.parse("file://"+file.getAbsolutePath());
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //����intent��Action����
        intent.setAction(Intent.ACTION_VIEW);
        //��ȡ�ļ�file��MIME����
        String type = getMIMEType(file);
        //����intent��data��Type���ԡ�
        intent.setDataAndType(/*uri*/Uri.fromFile(file), type);
        //��ת
        return intent;
    }

    //����һ��MIME�������ļ���׺���ƥ���
    private static final String[][] MIME_MapTable = {
            //{��׺��    MIME����}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".prop", "text/plain"},
            {".rar", "application/x-rar-compressed"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            //{".xml",    "text/xml"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/zip"},
            {"", "*/*"}
    };
    private static Map<String, Integer> Mime_Icon = new HashMap<String, Integer>();

    static {

        Mime_Icon.put(".apk", R.mipmap.ic_launcher2);

        Mime_Icon.put(".jpg", R.mipmap.icon_picturefile);
        Mime_Icon.put(".bmp", R.mipmap.icon_picturefile);
        Mime_Icon.put(".gif", R.mipmap.icon_picturefile);
        Mime_Icon.put(".png", R.mipmap.icon_picturefile);


        Mime_Icon.put(".txt", R.mipmap.icon_document);
        Mime_Icon.put(".log", R.mipmap.icon_document);

        Mime_Icon.put(".mp3", R.mipmap.icon_musicfile);
        Mime_Icon.put(".wav", R.mipmap.icon_musicfile);
        Mime_Icon.put(".wma", R.mipmap.icon_musicfile);

        Mime_Icon.put(".mp4", R.mipmap.icon_videofile);

        Mime_Icon.put(".zip", R.mipmap.icon_zipfile);
        Mime_Icon.put(".rar", R.mipmap.icon_zipfile);
        Mime_Icon.put(".tgz", R.mipmap.icon_zipfile);
        Mime_Icon.put(".gz", R.mipmap.icon_zipfile);

        //	Mime_Icon.put(".ppt", R.drawable.file_ppt);
        //	Mime_Icon.put(".pptx", R.drawable.file_ppt);

        Mime_Icon.put(".doc", R.mipmap.icon_document);
        Mime_Icon.put(".docx", R.mipmap.icon_document);
        Mime_Icon.put(".xlsx", R.mipmap.icon_document);

        Mime_Icon.put(".pdf", R.mipmap.icon_downloading);
        Mime_Icon.put(".pdf", R.mipmap.icon_document);
        Mime_Icon.put(".apk", R.mipmap.icon_install);

        Mime_Icon.put(".cfg", R.mipmap.icon_document);
    }

    public static int getFileIconId(String fileName) {
        int icon = R.mipmap.icon_unkonw; //Ĭ��
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex < 0)
            return icon;
        String end = fileName.substring(dotIndex, fileName.length()).toLowerCase();
        if (end == "")
            return icon;
        else {
            Integer i = Mime_Icon.get(end);
            if (i == null)
                return icon;
            else
                return i;
        }
    }

}
