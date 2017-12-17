package com.example.jutil.eventbus.bean;

/**
 *
 * @author ygx
 *
 * 事件
 */

public class Event {
    public Class to;
    public Object obj;

    private Event(Builder builder){
        this.to = builder.to;
        this.obj = builder.obj;
    }

    public static class Builder{
        Class to;
        Object obj;
        public Builder(){}

        public Builder to(Class to){
            this.to = to;
            return this;
        }

        public Builder obj(Object obj){
            this.obj = obj;
            return this;
        }

        public Event build(){
            return new Event(this);
        }
    }
}
