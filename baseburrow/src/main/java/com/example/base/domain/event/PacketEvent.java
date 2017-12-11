package com.example.base.domain.event;

/**
 * @author YGX
 *
 * <p>报文事件</p>
 */
public class PacketEvent {
    public String fromHost;
    public int fromPort;
    public String msg;

    private PacketEvent(Builder builder){
        this.fromHost = builder.host;
        this.fromPort = builder.port;
        this.msg = builder.msg;
    }
    public static class Builder{
        private static final int INIT_HOST  = 1;
        private static final int INIT_PORT  = 1 << 1;
        private static final int INIT_MSG   = 1 << 2;
        private int initFlag;
        private String host;
        private int port;
        private String msg;

        public Builder(){
            initFlag = 0;
        }
        public Builder fromHost(String fromHost){
            this.host = fromHost;
            initFlag |= INIT_HOST;
            return this;
        }

        public Builder fromPort(int fromPort){
            if(fromPort < 1 || fromPort > 65535)
                throw new IllegalStateException(
                        "from port can't lower than 1 and " +
                                "can't higher than 65535");
            this.port = fromPort;
            initFlag |= INIT_PORT;
            return this;
        }

        public Builder msg(String msg){
            if(msg == null)
                throw new IllegalStateException(
                        "msg can't be null"
                );
            this.msg = msg;
            initFlag |= INIT_MSG;
            return this;
        }

        public PacketEvent build(){
            if(this.initFlag !=
                    (INIT_MSG | INIT_HOST | INIT_PORT))
            {
                throw new IllegalStateException("" +
                        "please init packet complete first");
            }
            return new PacketEvent(this);
        }
    }
}