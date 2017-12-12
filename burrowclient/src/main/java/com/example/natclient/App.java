package com.example.natclient;

import com.example.natclient.bean.NatResponse;
import com.example.natclient.fun.base.IFun;
import com.example.natclient.fun.base.IRequestObserver;
import com.example.utils.Log;
import com.example.utils.ResourceUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class App {
    private static final String TAG = "App";
    private static NatClient natClient;

    public static void main(String[] args) throws IOException {
        Log.setDebugLevel(Log.E);
        natClient = new NatClient();
        natClient.launch();
        natClient.registerServer(
                ResourceUtil.get("usn"),
                ResourceUtil.get("serverHost")/*"255.255.255.255"*/,
                Integer.parseInt(ResourceUtil.get("serverPort")),
                resp -> Log.i(TAG,"onResponse: "+resp.toString()));


        while (true){
            String command = input("input command");
            IFun iFun = sFuns.get(command.toLowerCase());
            if (iFun == null) {
                Log.e(TAG,"no this command fun");
            }else{
                iFun.fun();
            }
        }

    }

    private static Scanner sScanner = new Scanner(System.in);
    private static String input(String hint){
        System.out.print(hint+": ");
        return sScanner.nextLine();
    }

    private static HashMap<String,IFun> sFuns = new HashMap<>();
    static {
        sFuns.put("get client", () -> {
                natClient.getClient(resp -> {
                    System.out.println(resp.toString());
                });
            }
        );

        sFuns.put("burrow", () -> {
            String target = input("input target:");
            natClient.burrow(target, resp -> {
               Log.e(TAG,""+resp.toString());
            });
        });

        sFuns.put("register",() -> {
           natClient.registerServer("",
                   input("input host"),
                   Integer.parseInt(input("input port")),
                   resp -> Log.e(TAG,"onResponse: "+resp.toString()));
        });
    }
}
