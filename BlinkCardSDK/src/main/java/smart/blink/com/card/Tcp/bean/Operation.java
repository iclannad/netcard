package smart.blink.com.card.Tcp.bean;

import java.util.Timer;

import smart.blink.com.card.BlinkNetCardCall;

/**
 * Created by Administrator on 2017/4/25.
 */
public class Operation {
    public int position;
    public BlinkNetCardCall call;
    public Timer timer = new Timer();
    public boolean isFinished;
}
