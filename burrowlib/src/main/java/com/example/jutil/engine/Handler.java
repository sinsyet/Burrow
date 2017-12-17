package com.example.jutil.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Handler {
    private static Timer timer = new Timer();

    @Deprecated
    public static void post2(TimerTask task,long delay){
        if(task == null || delay < 1) throw new IllegalStateException();
        timer.schedule(task,delay);
    }

    private static Map<Long,TimerTask> sTaskSwitch = new HashMap<>();
    public static void post(TimerTask task,long delay){
        if(task == null || delay < 1) throw new IllegalStateException();
        long taskToken = System.currentTimeMillis();
        timer.schedule(task,delay);
        sTaskSwitch.put(taskToken,task);
    }

    public static void removeAllCallback(long token){
        TimerTask task = sTaskSwitch.get(token);
        if(task != null) task.cancel();
    }

    public static void post(TimerTask task){
        TaskExecutors.exec(task);
    }

    public static void post(TimerTask task,long delay,long period){
        if(task == null || delay < 1 || period < 1) throw new IllegalStateException();

        timer.schedule(task, delay, period);
    }

}