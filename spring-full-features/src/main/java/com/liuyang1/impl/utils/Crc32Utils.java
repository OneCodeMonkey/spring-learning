package com.liuyang1.impl.utils;

import java.util.zip.CRC32;

/**
 * 计算 crc32() 特征值
 */
public class Crc32Utils {
    public static long crc32(String input) {
        CRC32 crc32 = new CRC32();
        crc32.update(input.getBytes());

        return Math.abs(crc32.getValue());
    }
}
