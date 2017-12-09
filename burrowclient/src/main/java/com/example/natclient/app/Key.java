package com.example.natclient.app;

/**
 *
 * @author YGX
 *
 * 常量
 */

public interface Key {

    interface Code{
        int OK                      = 200;
        int CLOSED_CHANNEL          = 201;
    }

    interface Msg{

        String CLOSED_CHANNEL       = "closed channel";
    }

}
