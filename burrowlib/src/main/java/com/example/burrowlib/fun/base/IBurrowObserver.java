package com.example.burrowlib.fun.base;

public interface IBurrowObserver {

    void onStart();

    void onProgress(int progress);

    void onSuccess();

    void onFailture(Throwable t,String msg);
}