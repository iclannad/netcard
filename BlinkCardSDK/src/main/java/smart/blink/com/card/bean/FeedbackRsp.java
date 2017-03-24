package smart.blink.com.card.bean;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/3/21.
 */
public class FeedbackRsp extends MainObject {
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
