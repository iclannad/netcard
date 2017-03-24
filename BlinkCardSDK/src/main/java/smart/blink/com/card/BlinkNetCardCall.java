package smart.blink.com.card;


import smart.blink.com.card.bean.MainObject;

/**
 * Created by Ruanjiahui on 2016/11/29.
 * <p/>
 * 处理回调结果的接口
 */
public interface BlinkNetCardCall {


    public void onSuccess(int position, MainObject mainObject);


    public void onFail(int error);
}
