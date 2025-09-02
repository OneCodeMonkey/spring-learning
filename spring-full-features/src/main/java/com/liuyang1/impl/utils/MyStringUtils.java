package com.liuyang1.impl.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class MyStringUtils {
    /**
     * URL 参数做 encode()
     *
     * @param param
     * @return
     */
    public static String urlencode(String param) {
        try {
            return URLEncoder.encode(param, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return param;
    }
}
