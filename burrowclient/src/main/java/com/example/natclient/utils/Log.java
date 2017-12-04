package com.example.natclient.utils;


public class Log {
    public static final int NONE    = 0;
    public static final int I       = 1;
    public static final int E       = 2;
    private static int sDebugLevel  = I;

    public static void setDebugLevel(int level){
        if(level>=E)            sDebugLevel = E;
        else if(level <= NONE)  sDebugLevel = NONE;
        else                    sDebugLevel = level;
    }
    public static void e(String TAG,String msg,Throwable t){
        if(sDebugLevel >= E) {
            System.err.println(TAG + ": " + msg + "\n "
                    + (t == null ? "" : t.getMessage()));
            if(t != null)
            t.printStackTrace();
        }
    }

    public static void e(String TAG,String msg){
        if(sDebugLevel >= E){
            System.err.println(TAG + ": " + msg + "\n ");
        }
    }

    public static void i(String TAG,String msg){
        if(sDebugLevel >= I){
            System.out.println(TAG + ": " + msg);
        }
    }
}
