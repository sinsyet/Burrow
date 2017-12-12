package com.example.burrowserver;

import com.example.burrowserver.server.NatServer;
import com.example.burrowserver.server.ServerHandler;
import com.example.utils.Log;
import com.example.utils.ResourceUtil;
import com.sun.org.apache.regexp.internal.RE;

import java.io.IOException;

public class APP{

    public static void main(String[] args) throws IOException {
        Log.setDebugLevel(Log.E);
        NatServer natServer = new NatServer(
                Integer.parseInt(ResourceUtil.get("port")),
                null);
        new ServerHandler(natServer).start();
    }
}
