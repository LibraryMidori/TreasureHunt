package com.example.treasurehunt;

import android.os.Handler;

import java.util.Observable;

/**
 * Created by library on 2016/03/14.
 */
public class Timer extends Observable {
    private Handler clock;
    private static Timer _instance = new Timer();
    private Runnable updateTimeElapsed;
    private int timeRemain = 0;

    private Timer() {
        clock = new Handler();

        updateTimeElapsed = new Runnable() {
            @Override
            public void run() {
                long currentMilliseconds = System.currentTimeMillis();
                timeRemain--;

                // add notification
                clock.postAtTime(this, currentMilliseconds);
                // notify to call back after 1 seconds
                // basically to remain in the timer loop
                clock.postDelayed(updateTimeElapsed, 1000);

                if (timeRemain == 0) {
                    notifyObservers(0);
                }

            }
        };
    }

    public static Timer getInstance() {
        return _instance;
    }

    /*
    * Start time time
    *
    * @author: 8B Pham Hung Cuong
    */
    public void startTimer(int timeRemain) {
        this.timeRemain = timeRemain;
        clock.removeCallbacks(updateTimeElapsed);
        clock.postDelayed(updateTimeElapsed, 1000);
    }

    /*
     * Stop the time
	 *
	 * @author 8B Pham Hung Cuong
	 */

    public void stopTimer() {
        // disable call backs
        clock.removeCallbacks(updateTimeElapsed);
    }
}
