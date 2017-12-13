package com.example.utils;


import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;

import sun.misc.BASE64Encoder;

public class NatUtil {

    public static String getMsgByByteBuffer(ByteBuffer buf, boolean reset) {
        byte[] array = buf.array();
        int offset = buf.arrayOffset();
        int length = buf.position();
        if (reset) /*buf.reset();*/
        buf.clear();
        return new String(array, offset, length);
    }

    public static String generateTag(Object... params) {
        if (params.length == 0) throw new IllegalStateException("params length can't be 0");
        int index = 0;
        String tag = "";
        for (Object s : params) {
            tag += Math.abs(s.hashCode() >> ++ index);
        }

        return tag;
    }

    public static void close(Closeable...cls){
        for(Closeable c:cls){
            if(c != null){
                try {
                    c.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    private static BASE64Encoder base64Encoder = new BASE64Encoder();
    public static String base64Encode(Object...msg){
        StringBuilder sBuf = new StringBuilder();
        for(Object obj:msg){
            if(obj == null) continue;
            sBuf.append(obj.toString());
        }
        try {
            return base64Encoder.encode(sBuf.toString().getBytes("UTF-8"));
        } catch (Exception ignored) {

        }
        return "";
    }

}
