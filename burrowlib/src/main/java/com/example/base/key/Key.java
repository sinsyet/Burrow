package com.example.base.key;

public interface Key {
    interface T {
        // t: 1 ~ 50, 用于和服务器交互
        int REQ_REGISTER                = 1;
        int REQ_PITPAT                  = 2;
        int REQ_GET_CLIENTS             = 3;
        int REQ_BURROW                  = 4;

        // t:51 ~ 100, 用于服务器通知客户端打洞的消息
        int REQ_51                      = 51;
        int REQ_52                      = 52;

        // t: 101 ~ 150, 主叫和被叫之间通信使用的t
        int REQ_101                     = 101;
        int REQ_102                     = 102;
    }

    interface Code{
        int OK                          = 200;
        int NO_LTAG                     = 201;
        int NO_RTAG                     = 203;
    }

    interface Em{
        String OK                       = "ok";
        String NO_LTAG                  = "no local tag";
        String NO_RTAG                  = "no remote tag";
    }
}
