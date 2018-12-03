package com.mygdx.game;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jackwa on 3/30/18.
 */
public class CountDown {

        private static int interval;
        private static Timer timer;
        private final int delay;
        private final int period;

    /**
     * Constructor.
     *
     * @param secs the time allotted for the game.
     */
    public CountDown(int secs) {
        delay = Constants.COUNT_DOWN_DELAY;
        period = Constants.CONT_DOWN_PERIOD;
        timer = new Timer();
        interval = secs;
    }

    public void countDown() {
        System.out.println(interval);
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                System.out.println(setInterval());
            }
        }, delay, period);
    }

    private static int setInterval() {
        if (interval == 1) {
            timer.cancel();
            System.out.println("Time's up son!");
        }
        return --interval;
    }

    public int getInterval() {
        return interval;
    }

    public void cancel() {
        timer.cancel();
    }
}

