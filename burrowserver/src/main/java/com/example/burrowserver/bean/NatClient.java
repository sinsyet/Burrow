package com.example.burrowserver.bean;



public class NatClient {
    public String       host;
    public int          port;
    public long         activeStamp;
    public String       tag;
    public String       usn;
    public boolean      isNating;

    public NatClient(){}

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
}
