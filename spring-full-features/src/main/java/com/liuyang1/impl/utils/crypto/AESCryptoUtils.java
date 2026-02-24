package com.liuyang1.impl.utils.crypto;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**
 * AES-GCM 算法加密
 */
@Slf4j
public class AESCryptoUtils {
    private static final String ALGORITHM = "AES/GCM/NoPadding";

    // notice: 实际使用中，将密钥设置为本地环境变量，或从远程读取。一定不要放在代码里
    private static final String DEFAULT_KEY = "123456abA";

    private static final int IV_LENGTH = 12;

    private static SecretKeySpec secretKey;

    static {
        initSecretKey(DEFAULT_KEY);
    }

    public static void initSecretKey(String keyText) {
        try {
            byte[] keyBytes = keyText.getBytes(StandardCharsets.UTF_8);
            byte[] finalKeyBytes = new byte[32];
            System.arraycopy(keyBytes, 0, finalKeyBytes, 0, Math.min(32, keyBytes.length));
            secretKey = new SecretKeySpec(finalKeyBytes, "AES");
        } catch (Exception e) {
            log.error("initSecretKey failed, error: {}", e.getMessage());
            throw new RuntimeException("initSecretKey failed!", e);
        }
    }

    /**
     * AES-GCM 算法加密
     *
     * @param plaintext
     * @return
     */
    public static String encrypt(String plaintext) {
        if (plaintext == null || plaintext.isEmpty()) {
            return plaintext;
        }

        try {
            // 生成随机IV
            byte[] iv = new byte[IV_LENGTH];
            new SecureRandom().nextBytes(iv);

            // 加密
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes());

            // 组合IV和密文，Base64编码
            byte[] encryptedData = new byte[iv.length + ciphertext.length];
            System.arraycopy(iv, 0, encryptedData, 0, iv.length);
            System.arraycopy(ciphertext, 0, encryptedData, iv.length, ciphertext.length);

            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            throw new RuntimeException("加密失败，请检查！");
        }
    }

    public static String decrypt(String encryptedText) {
        if (encryptedText == null || encryptedText.isEmpty()) {
            return encryptedText;
        }

        try {
            // Base64解码
            byte[] encryptedData = Base64.getDecoder().decode(encryptedText);

            // 提取IV和密文
            byte[] iv = Arrays.copyOfRange(encryptedData, 0, IV_LENGTH);
            byte[] ciphertext = Arrays.copyOfRange(encryptedData, IV_LENGTH, encryptedData.length);

            // 解密
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));
            byte[] plaintext = cipher.doFinal(ciphertext);

            return new String(plaintext);
        } catch (Exception e) {
            throw new RuntimeException("解密失败，请检查！");
        }
    }

    // 检查是否已经加密，避免重复加密
    public static boolean isEncrypted(String value) {
        return value != null && value.contains(":");
    }

    // test
//    public static void main(String[] args) {
//        String text = "this is my name. how are you today?";
//        String encryptedText = "";
//        try {
//            encryptedText = AESCryptoUtils.encrypt(text);
//        } catch (Exception e) {
//            log.error("error: {}", e.getMessage());
//        }
//        log.info("encrypt: {} -> {}", text, encryptedText);
//    }
}
