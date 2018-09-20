package com.mygdx.game;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jackwa on 3/30/18.
 */
public class CountDown {

        static int interval;
        static Timer timer;
        private int delay;
        private int period;

        public CountDown(int secs) {

            delay = 1000;
            period = 1000;
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

        private static final int setInterval() {
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

