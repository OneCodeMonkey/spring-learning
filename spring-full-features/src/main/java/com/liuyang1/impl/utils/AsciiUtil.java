package com.liuyang1.impl.utils;

/**
 * Author：jaxliu
 * Date: 3/28/24 17:24
 * Desc: ASCII字符转换，包括全角/半角字符转换（中文部分不受影响）
 */
public class AsciiUtil {
    /**
     * 全角空格 12288
     */
    public static final char SBC_SPACE = 12288;

    /**
     * 半角空格 32
     */
    public static final char DBC_SPACE = 32;

    /**
     * ASCII character 33-126 <-> unicode 65281-65374
     */
    public static final char ASCII_START = 33;

    public static final char ASCII_END = 126;

    public static final char UNICODE_START = 65281;

    public static final char UNICODE_END = 65374;

    /**
     * 全角半角转换间隔
     */
    public static final char DBC_SBC_STEP = 65248;

    /**
     * 全角字符转半角字符
     *
     * @param src
     * @return
     */
    public static char sbc2dbc(char src) {
        if (src == SBC_SPACE) {
            return DBC_SPACE;
        }

        if (src >= UNICODE_START && src <= UNICODE_END) {
            return (char) (src - DBC_SBC_STEP);
        }

        return src;
    }

    /**
     * 全角字符串转半角字符串
     *
     * @param src
     * @return DBC case
     */
    public static String sbc2dbcCase(String src) {
        if (src == null) {
            return null;
        }
        char[] c = src.toCharArray();
        for (int i = 0; i < c.length; i++) {
            c[i] = sbc2dbc(c[i]);
        }
        return new String(c);
    }

    /**
     * 半角字符转全角字符
     *
     * @param src
     * @return
     */
    public static char dbc2sbc(char src) {
        if (src == DBC_SPACE) {
            return SBC_SPACE;
        }
        if (src <= ASCII_END) {
            return (char) (src + DBC_SBC_STEP);
        }
        return src;
    }

    /**
     * 半角字符串转全角字符串
     *
     * @param src
     * @return SBC case string
     */
    public static String dbc2sbcCase(String src) {
        if (src == null) {
            return null;
        }

        char[] c = src.toCharArray();
        for (int i = 0; i < c.length; i++) {
            c[i] = dbc2sbc(c[i]);
        }

        return new String(c);
    }
}
