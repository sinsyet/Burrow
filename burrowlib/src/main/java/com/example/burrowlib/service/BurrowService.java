package com.example.burrowlib.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.burrowlib.bean.NatResponse;
import com.example.burrowlib.client.NatClient;
import com.example.burrowlib.fun.base.IRequestObserver;
import com.example.jutil.engine.TaskExecutors;

import java.io.IOException;

public class BurrowService extends Service {
    private static final String TAG = "BurrowService";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        TaskExecutors.exec(r);
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            try {
                com.example.jutil.utils.Log.setDebugLevel(com.example.jutil.utils.Log.E);
                NatClient client = new NatClient();
                client.launch();
                client.registerServer("10.6.0.45", 20001, new IRequestObserver() {
                    @Override
                    public void onResponse(NatResponse resp) {
                        Log.e(TAG, "onResponse: "+resp.toString());
                    }
                });
            } catch (IOException e) {

            }
        }
    };
    @Override
    public void onDestroy() {

    }
}