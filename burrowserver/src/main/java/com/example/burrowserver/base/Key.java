package com.example.burrowserver.base;



public interface Key {

    interface Code{
        int OK                              = 200;
        int REPEAT_REGISTER                 = 201;
        int NO_TAG                          = 202;
        int NO_REMOTE_TAG                   = 203;
        int REMOTE_IS_NATING                = 204;
    }

    interface Msg{
        String REGISTER_REPEAT              = "register repeat";
        String NO_LOCAL_TAG                 = "no this client";
        String NO_REMOTE_TAG                = "NO REMOTE TAG";
        String REMOTE_IS_NATING             = "REMOTE IS NATING";
    }
}
