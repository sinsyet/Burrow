package com.example.burrowserver.utils;


import java.io.UnsupportedEncodingException;
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
        String tag = "";
        for (Object s : params) {
            tag += s.hashCode();
        }

        return tag;
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
