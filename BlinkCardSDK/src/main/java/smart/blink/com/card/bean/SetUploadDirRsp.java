package smart.blink.com.card.bean;

import com.google.gson.Gson;

/**
 * Created by Ruanjiahui on 2016/11/30.
 */
public class SetUploadDirRsp extends MainObject{

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
