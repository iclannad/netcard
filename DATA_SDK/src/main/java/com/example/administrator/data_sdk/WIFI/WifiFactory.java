package com.example.administrator.data_sdk.WIFI;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by Administrator on 2016/7/18.
 * <p/>
 * wifi工厂类
 */
public class WifiFactory {

    private static WifiManager wifiManager = null;
    private static WifiInfo wifiInfo = null;

    /**
     * 获取wifi管理器
     *
     * @param context
     * @return
     */
    public static synchronized WifiManager getIntance(Context context) {
        if (wifiManager == null)
            wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager;
    }


    /**
     * 获取链接wifi的信息
     *
     * @param context
     * @return
     */
    public static WifiInfo getWifiInfo(Context context) {
        return getIntance(context).getConnectionInfo();
    }

    /**
     * 获取链接wifi的SSID即  wifi名称
     *
     * @param context
     * @return
     */
    public static String getWifiSSID(Context context) {
        return getWifiInfo(context).getSSID().replace("\"", "");
    }

    /**
     * 获取链接的Wifi的IP地址
     *
     * @param context
     * @return
     */
    public static String getWifiIP(Context context) {
        return intToIp(getWifiInfo(context).getIpAddress());
    }

    /**
     * 获取链接wifi的Mac地址
     *
     * @param context
     * @return
     */
    public static String getWifiMac(Context context) {
        return getWifiInfo(context).getMacAddress();
    }


    /**
     * 将整数值进行右移位操作（>>）
     * 右移24位，右移时高位补0，得到的数字即为第一段IP
     * 右移16位，右移时高位补0，得到的数字即为第二段IP
     * 右移8位，右移时高位补0，得到的数字即为第三段IP
     * 最后一段的为第四段IP
     *
     * @param i
     * @return String
     */
    public static String intToIp(int i) {
        return ((i >> 24) & 0xFF) + "." + ((i >> 16) & 0xFF) + "."
                + ((i >> 8) & 0xFF) + "." + (i & 0xFF);
    }
}
