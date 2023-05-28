package com.liuyang1.luascript;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Slf4j
public class AESEncryptor {
    /**
     * Cipher加密器初始化需要一个字符串，字符串里提供了三种设置。
     * 一是，加解密算法；二是，加解密模式；三是，是否需要填充。
     * <br/>
     * ECB（电码本模式），CBC（加密块链模式），OFB（输出反馈模式），CFB（加密反馈模式）
     */
    private static final String CIPHER_MODE = "AES/ECB/PKCS5Padding";

    private static final String ALGORITHM = "AES";

    /**
     * 生成密钥
     */
    public static String initAesKey(String seed) throws NoSuchAlgorithmException, DecoderException {
        SecureRandom secureRandom;
        if (StringUtils.isNotBlank(seed)) {
            secureRandom = new SecureRandom(Hex.decodeHex(seed));
        } else {
            secureRandom = new SecureRandom();
        }
        // init key生成器
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM);
        // 要生成多少位，只需要修改这里即可 128, 192 或 256，单位 bit
        kg.init(128, secureRandom);
        // 生成一个Key
        SecretKey secretKey = kg.generateKey();
        // 转变为字节数组
        byte[] encoded = secretKey.getEncoded();
        // 生成密钥字符串
        return Hex.encodeHexString(encoded);
    }

    /**
     * AES加密
     *
     * @param data 待加密的数据
     * @param key  密钥
     * @return 加密后的数据
     */
    public static String encrypt(String data, String key) {
        try {
            Cipher cipher = getCipher(key, Cipher.ENCRYPT_MODE);
            return Hex.encodeHexString(cipher.doFinal(data.getBytes())).toUpperCase();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | DecoderException | IllegalBlockSizeException | BadPaddingException e) {
            log.error("加密异常：", e);
            return null;
        }
    }


    /**
     * AES解密
     *
     * @param data 待解密的数据
     * @param key  密钥
     * @return 解密后的数据
     */
    public static String decrypt(String data, String key) {
        try {
            Cipher cipher = getCipher(key, Cipher.DECRYPT_MODE);
            return new String(cipher.doFinal(Hex.decodeHex(data)));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | DecoderException | IllegalBlockSizeException | BadPaddingException e) {
            log.error("解密异常：", e);
            return null;
        }
    }

    private static Cipher getCipher(String key, Integer mode) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, DecoderException {
        // 创建密匙
        SecretKey secretKey = new SecretKeySpec(Hex.decodeHex(key), ALGORITHM);
        // Cipher 对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(CIPHER_MODE);
        // 用密匙初始化 Cipher 对象
        cipher.init(mode, secretKey);


        return cipher;
    }
}
