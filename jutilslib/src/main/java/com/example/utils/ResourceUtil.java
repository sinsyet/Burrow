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
        sBundle.put("serverHost","10.6.0.45");
        sBundle.put("serverPort","20000");
        sBundle.put("usn","ygx");
        sBundle.put("port","20000");

    }
    public static String get(String key){
        return sBundle.get(key);
    }

}
