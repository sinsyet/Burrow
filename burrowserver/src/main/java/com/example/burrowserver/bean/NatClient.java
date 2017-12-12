package com.example.burrowserver.bean;



public class NatClient {
    public String       host;
    public int          port;
    public long         activeStamp;
    public String       tag;
    public String       usn;
    public boolean      isNating;

    public NatClient(){}

    @Deprecated
    public NatClient(String host,
                     int port,
                     long activeStamp,
                     String tag,
                     String usn,
                     boolean isNating) {
        this.host = host;
        this.port = port;
        this.activeStamp = activeStamp;
        this.tag = tag;
        this.usn = usn;
        this.isNating = isNating;
    }

    public NatClient(String host,
                     int port,
                     long activeStamp,
                     String tag){
        this.host = host;
        this.port = port;
        this.activeStamp = activeStamp;
        this.tag = tag;
    }

    public void updateActiveStamp() {
        this.activeStamp = System.currentTimeMillis();
    }

    @Override public String toString(){
        return this.getClass().getSimpleName() + "@: [" +
                "tag: " + this.tag +
                "host: " + this.host +
                "port: " + this.port +
                "activeStamp: " + this.activeStamp +
                "]";
    }
}
