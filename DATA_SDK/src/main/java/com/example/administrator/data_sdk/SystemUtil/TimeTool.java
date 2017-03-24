package com.example.administrator.data_sdk.SystemUtil;

import android.util.Log;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created by Administrator on 2016/3/15.
 * <p/>
 * 这类处理关于时间的的操作
 */
public class TimeTool {

    /**
     * 获取某年某月有多少天
     *
     * @param year
     * @param month
     */
    public static int getDay(int year, int month) {
        //判断这个月份是不是2月
        if (month == 2) {
            //判断该年是不是闰年
            if (year % 400 == 0 || (year % 4 == 0 && year % 100 == 0))
                return 29;//闰年
            else
                return 28;//平年
            //判断月份如果是8月前的单数月为大月
        } else if (month % 2 != 0) {
            if (month < 8)
                return 31;
            return 30;
        } else {
            if (month >= 8)
                return 31;
            return 30;
        }
    }

    /**
     * 通过两个时间计算出生日
     *
     * @param oldYear
     * @param oldMonth
     * @param oldDay
     * @return
     */
    public static int getDayCount(int oldYear, int oldMonth, int oldDay) {
        int year = oldYear, month = oldMonth, day = oldDay;
        int newyear = getTimeYear();
        int newmonth = getTimeMonth();
        int newday = getTimeDay();

        if (oldMonth >= newmonth) {
            if (oldMonth == newmonth) {
                if (oldDay <= newday)
                    return newyear - oldYear;
                else
                    return newyear - oldYear - 1;
            } else
                return newyear - oldYear;
        } else {
            return newyear - oldYear - 1;
        }
    }

    /**
     * 获取星座
     */
    public static String[][] getConstellation() {
        String[][] constellation = new String[13][32];
        for (int i = 0; i < constellation.length; i++) {
            for (int r = 0; r < constellation[i].length; r++) {
                switch (i) {
                    case 1:
                        if (r < 21)
                            constellation[i][r] = "魔羯座";
                        if (r > 20)
                            constellation[i][r] = "水瓶座";
                        break;
                    case 2:
                        if (r < 19)
                            constellation[i][r] = "水瓶座";
                        if (r > 18)
                            constellation[i][r] = "双鱼座";
                        break;
                    case 3:
                        if (r < 21)
                            constellation[i][r] = "双鱼座";
                        if (r > 20)
                            constellation[i][r] = "白羊座";
                        break;
                    case 4:
                        if (r < 20)
                            constellation[i][r] = "白羊座";
                        if (r > 19)
                            constellation[i][r] = "金牛座";
                        break;
                    case 5:
                        if (r < 21)
                            constellation[i][r] = "金牛座";
                        if (r > 20)
                            constellation[i][r] = "双子座";
                        break;
                    case 6:
                        if (r < 22)
                            constellation[i][r] = "双子座";
                        if (r > 21)
                            constellation[i][r] = "巨蟹座";
                        break;
                    case 7:
                        if (r < 23)
                            constellation[i][r] = "巨蟹座";
                        if (r > 22)
                            constellation[i][r] = "狮子座";
                        break;
                    case 8:
                        if (r < 24)
                            constellation[i][r] = "狮子座";
                        if (r > 23)
                            constellation[i][r] = "处女座";
                        break;
                    case 9:
                        if (r < 24)
                            constellation[i][r] = "处女座";
                        if (r > 23)
                            constellation[i][r] = "天秤座";
                        break;
                    case 10:
                        if (r < 24)
                            constellation[i][r] = "天秤座";
                        if (r > 23)
                            constellation[i][r] = "天蝎座";
                        break;
                    case 11:
                        if (r < 23)
                            constellation[i][r] = "天蝎座";
                        if (r > 22)
                            constellation[i][r] = "射手座";
                        break;
                    case 12:
                        if (r < 19)
                            constellation[i][r] = "射手座";
                        if (r > 18)
                            constellation[i][r] = "魔羯座";
                        break;

                }
            }
        }
        return constellation;
    }


    /**
     * 获取系统时间
     * 格式HH:MM:SS
     *
     * @return
     */
    public static String getSystemTime() {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        return df.format(new Date());
    }


    private static int mHour = 0;
    private static int mMinuts = 0;
    private static int mSecond = 0;

    /**
     * 获取系统的秒数
     *
     * @return
     */
    public static Calendar getTime() {
        long time = System.currentTimeMillis();
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(time);
        return mCalendar;
    }

    /**
     * 获取系统的小时
     *
     * @return
     */
    public static int getTimeHour() {
        DecimalFormat df = new DecimalFormat("00");
        return Integer.parseInt(df.format(getTime().get(Calendar.HOUR)));
    }

    /**
     * 获取系统的分钟
     *
     * @return
     */
    public static String getTimeMinuts() {
        return String.valueOf(getTime().get(Calendar.MINUTE));
    }

    /**
     * 获取系统时间24小时制的时
     *
     * @return
     */
    public static String getTime24Hour() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        //等于0则说明是上午  1为下午
        if (calendar.get(Calendar.AM_PM) == 0)
            return String.valueOf(getTime().get(Calendar.HOUR));
        return String.valueOf(getTime().get(Calendar.HOUR) + 12);
    }

    /**
     * 获取系统年
     *
     * @return
     */
    public static int getTimeYear() {
        return getTime().get(Calendar.YEAR);
    }

    /**
     * 获取系统月
     *
     * @return
     */
    public static int getTimeMonth() {
        return getTime().get(Calendar.MONTH) + 1;
    }

    /**
     * 获取系统日
     *
     * @return
     */
    public static int getTimeDay() {
        return getTime().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取系统的秒数
     *
     * @return
     */
    public static int getTimeSecond() {
        return mHour = getTime().get(Calendar.SECOND);
    }
}
