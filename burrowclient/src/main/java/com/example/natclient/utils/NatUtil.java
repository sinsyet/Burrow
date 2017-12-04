package com.example.natclient.utils;


import java.nio.ByteBuffer;

import sun.misc.BASE64Encoder;

public class NatUtil {

    public static String getMsgByByteBuffer(ByteBuffer buf, boolean reset) {
        byte[] array = buf.array();
        int offset = buf.arrayOffset();
        int length = buf.position();
        if (reset) buf.clear();
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


}
