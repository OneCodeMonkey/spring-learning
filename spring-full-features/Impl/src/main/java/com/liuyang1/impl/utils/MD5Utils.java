package com.liuyang1.impl.utils;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Author：OneCodeMonkey
 * Date: 5/14/24 20:09
 * Desc: MD5 工具类
 */
@Slf4j
public class MD5Utils {
    public static String getMD5Hash(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(text.getBytes(StandardCharsets.UTF_8));

            // 将字节转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = String.format("%02x", b);
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("MD5Utils getMD5Hash error: {}", e.getMessage());
        }

        return null;
    }
}
