package smart.blink.com.card.bean;

import com.google.gson.Gson;

/**
 * Created by Ruanjiahui on 2016/11/29.
 */
public class HelloRsp extends MainObject {
    /**
     * 返回0成功
     */

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
