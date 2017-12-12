package com.example.burrowserver.server;

import com.example.base.base.Status;
import com.example.base.fun.Fun1;
import com.example.burrowserver.engine.repository.NatClientRepository;
import com.example.utils.Log;
import com.example.utils.ScannerUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ServerHandler {
    private static final String TAG = "ServerHandler";
    private static final String PRE = "Server Status";
    private static Map<String,Object> sServerStatus =
            new HashMap<>();
    public static void updateStatus(String key,Object value){
        sServerStatus.put(key, value);
    }

    public static Object getStatus(String key){
        return sServerStatus.get(key);
    }

    public static void previewServerStatus(){
        for(Map.Entry<String,Object> entry : sServerStatus.entrySet()){
            String key = entry.getKey();
            Object value = entry.getValue();

            if(value == null)
                Log.i(PRE, key + ": null");
            else if(value.getClass().isArray())
                Log.i(PRE, key + ": "+ Arrays.toString((Object[])value));
            else
                Log.i(PRE, key + ": " + value.toString());
        }
    }
    private NatServer server;
    private Scanner mScanner = new Scanner(System.in);
    private Map<String,Fun1> mFuns = new HashMap<>();
    public ServerHandler(NatServer server){
        this.server = server;
    }

    public void start(){
        while (true){
            String command = ScannerUtil.input("input command");
            Fun1 fun1 = mFuns.get(command);
            if(fun1 != null){
                fun1.run();
            }else {
                Log.i(TAG," no this command function: "+command);
            }
        }
    }

    {
        mFuns.put("launch", new Fun1() {
            @Override
            public void run() {
                if(getStatus(Status.SERVER_LAUNCH) != null){
                    Log.e(TAG,"launch server function: " +
                            "server already launch");
                    return;
                }
                try {
                    server.launch();
                    updateStatus(Status.SERVER_LAUNCH,true);
                    Log.i(TAG,"server launch success");
                } catch (IOException e) {
                    Log.i(TAG,"server launch fail "+e.getMessage());
                }
            }
        });

        mFuns.put("preview status", new Fun1() {
            @Override
            public void run() {
                previewServerStatus();
            }
        });

        mFuns.put("preview clients", new Fun1() {
            @Override
            public void run() {
                NatClientRepository.traverseNatClients();
            }
        });
    }
}