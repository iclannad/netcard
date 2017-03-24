package smart.blink.com.card.API;

import android.util.Log;

/**
 * Created by Ruanjiahui on 2016/11/29.
 * <p/>
 * 网卡SDK的打印信息
 */
public class BlinkLog {

    public static void Print(Object o) {
        System.out.println(o.toString());
    }


    public static void Error(Object o) {
        Log.e("System.out", o.toString());
    }
}
