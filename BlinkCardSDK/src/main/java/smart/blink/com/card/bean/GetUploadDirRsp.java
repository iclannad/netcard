package smart.blink.com.card.bean;

import com.google.gson.Gson;

/**
 * Created by Ruanjiahui on 2016/11/30.
 */
public class GetUploadDirRsp extends MainObject{

    private String path = null;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
