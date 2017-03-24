package blink.com.blinkcard320.Tool.Thread;

/**
 * Created by Administrator on 2016/9/3.
 */
public class MyRunnable implements Runnable {

    private ThreadImpl thread = null;
    private int position = 0;

    public MyRunnable(int position , ThreadImpl thread) {
        this.position = position;
        this.thread = thread;
    }

    /**
     * Starts executing the active part of the class' code. This method is
     * called when a thread is started that has been created with a class which
     * implements {@code Runnable}.
     */
    @Override
    public void run() {
        thread.myRun(position);
    }
}
