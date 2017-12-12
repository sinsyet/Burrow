package com.example.utils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ygx
 *
 * 资源
 */

public class ResourceUtil {
    // private static ResourceBundle sBundle = ResourceBundle.getBundle("config", Locale.CHINA);
    private static Map<String,String> sBundle = new HashMap<>();
    static {
        sBundle.put("serverHost","127.0.0.1");
        sBundle.put("serverPort","20001");
        sBundle.put("usn","ygx");
        sBundle.put("port","20001");

    }
    public static String get(String key){
        return sBundle.get(key);
    }

}
