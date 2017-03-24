package smart.blink.com.card.Tool;

import java.util.TimerTask;

/**
 * Created by Ruanjiahui on 2016/12/8.
 */
public class MyTimerTask extends TimerTask {

    private TimerTaskCall call = null;

    public MyTimerTask(TimerTaskCall call) {
        this.call = call;
    }

    /**
     * The task to run should be specified in the implementation of the {@code run()}
     * method.
     */
    @Override
    public void run() {
        call.TimerCall();
    }
}
