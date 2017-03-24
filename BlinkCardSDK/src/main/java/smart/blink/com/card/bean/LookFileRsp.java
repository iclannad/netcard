package smart.blink.com.card.bean;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Ruanjiahui on 2016/12/1.
 */
public class LookFileRsp extends MainObject {


    private ArrayList<String> list = null;
    private ArrayList<Integer> protrolList = null;


    public ArrayList<Integer> getProtrolList() {
        return protrolList;
    }

    public void setProtrolList(ArrayList<Integer> protrolList) {
        this.protrolList = protrolList;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
