package com.example.engine;

import java.util.Timer;
import java.util.TimerTask;

public class Handler {
    private static Timer timer = new Timer();

    public static void post(TimerTask task,long delay){
        if(task == null || delay < 1) throw new IllegalStateException();
        timer.schedule(task,delay);
    }

    public static void post(TimerTask task){
        TaskExecutors.exec(task);
    }

    public static void post(TimerTask task,long delay,long period){
        if(task == null || delay < 1 || period < 1) throw new IllegalStateException();

        timer.schedule(task, delay, period);
    }
}