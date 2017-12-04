package com.example.burrowserver;

import com.example.burrowserver.server.UdpNatServer;
import com.example.burrowserver.utils.Log;
import com.example.burrowserver.utils.ResourceUtil;

import java.io.IOException;

public class APP {

    private static UdpNatServer natServer;

    public static void main(String[] args) throws IOException {
        Log.setDebugLevel(Log.E);
        natServer = new UdpNatServer(Integer.parseInt(ResourceUtil.get("port")));
        natServer.launchServer();
    }
}
