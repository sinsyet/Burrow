package com.example.burrowlib.bean;

/**
 *
 * @author YGX
 *
 * 响应对象
 */

public class NatResponse {

    public boolean      success;
    public int          code;
    public String       r;
    public Throwable    t;
    public String       em;         // error msg

    public NatResponse(Builder builder){
        this.code = builder.code;
        this.success = builder.success;
        this.r = builder.r;
        this.t = builder.t;
    }
    public static class Builder{
        private boolean      success;
        private int          code;
        private String       r;
        private Throwable    t;
        private String       em;
        public Builder(){}

        public Builder success(boolean success){
            this.success = success;
            return this;
        }

        public Builder code(int code){
            this.code = code;
            return this;
        }

        public Builder r(String r){
            this.r = r;
            return this;
        }

        public Builder t(Throwable t){
            this.t = t;
            return this;
        }

        public Builder em(String msg){
            this.em = msg;
            return this;
        }

        public NatResponse build(){
            return new NatResponse(this);
        }
    }

    @Override
    public String toString() {
        return "NatResponse{" +
                "success=" + success +
                ", code=" + code +
                ", r='" + r + '\'' +
                ", t=" + t +
                ", em='" + em + '\'' +
                '}';
    }
}
