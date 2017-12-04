package com.example.natclient.engine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author YGX
 *
 * 线程池
 */

public class TaskExecutors {

    private static ExecutorService sExectors = Executors.newCachedThreadPool();

    public static void exec(Runnable r){
        if(r == null) return;

        sExectors.execute(r);
    }
}
