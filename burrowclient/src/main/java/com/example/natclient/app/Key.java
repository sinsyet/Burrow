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
        int INVALID_TOKEN           = 202;
    }

    interface Msg{
        // 通道已关闭
        String CLOSED_CHANNEL       = "closed channel";

        // 无效的token
        String INVALID_TOKEN        = "invalid token";
    }

}
