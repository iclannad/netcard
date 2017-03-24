package blink.com.blinkcard320.Moudle.skin;

import blink.com.blinkcard320.R;

/**
 * Created by Administrator on 2017/3/20.
 */
public class SkinConfig {

    public static int[] skinArray = new int[]{R.color.MainColorBlue, R.color.MainColorGreen, R.color.MainColorPuplor, R.color.MainColorRed};
    public static int[] skinselectArray = new int[]{R.drawable.adapterblue, R.drawable.adaptergreen, R.drawable.adapterpoplur, R.drawable.adapterred};
    public static int[] skinbutselectArray = new int[]{R.drawable.buttonblue, R.drawable.buttongreen, R.drawable.buttonpoplur, R.drawable.buttonred};
    public static int[] skinScanselectArray = new int[]{R.mipmap.scanblue, R.mipmap.scangreen, R.mipmap.scanpoplur, R.mipmap.scanred};
    public static int[] skinImageArray = new int[]{R.mipmap.addblue, R.mipmap.addgreen, R.mipmap.addpoplure, R.mipmap.addred};

    // 常量，存放在sp中
    public final static int BLUE = 0;
    public final static int GREEN = 1;
    public final static int PURPLE = 2;
    public final static int RED = 3;

    public final static String SKIN_CONFIG = "skin_config";
    public final static int SKIN_DEFAULT_VALUE = R.color.MainColorBlue;

    public final static String SKIN_SELECT_ICON = "skin_select_icon";
    public final static int SKIN_DEFAULT_SKIN_SELECT_ICON_VALUE = BLUE;

}
