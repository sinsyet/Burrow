package com.example.burrowlib.repository;


import com.example.base.channel.abs.AbsUDPChannelHandler;

import java.nio.channels.SelectableChannel;
import java.util.HashMap;
import java.util.Map;

public class ChannelRepository {

    private static Map<SelectableChannel,AbsUDPChannelHandler> sChannelMaps
            = new HashMap<>();

    public static void register(SelectableChannel channel,
                                AbsUDPChannelHandler handler){
        if(channel == null || handler == null)
            throw new IllegalStateException();

        sChannelMaps.put(channel,handler);
    }

    public static AbsUDPChannelHandler getChannelHandler(SelectableChannel channel){
        if(channel == null) return null;

        return sChannelMaps.get(channel);
    }
}