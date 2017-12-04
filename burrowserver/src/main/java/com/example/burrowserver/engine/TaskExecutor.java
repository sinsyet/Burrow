package com.example.burrowserver.engine;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskExecutor {
    private static ExecutorService sExecutors = Executors.newCachedThreadPool();
    public static void exec(Runnable r){
        if(r == null) return;
        sExecutors.execute(r);

    }
}
