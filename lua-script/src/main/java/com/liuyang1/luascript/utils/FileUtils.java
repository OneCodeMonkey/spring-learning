package com.liuyang1.luascript.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

/**
 * 常用文件操作
 */
public class FileUtils {
    public static String readResourceFile(String filePath) {
        ClassPathResource resource = new ClassPathResource(filePath);
        try (InputStream inStream = resource.getInputStream()) {
            byte[] content = inStream.readAllBytes();
            return new String(content);
        } catch (Exception e) {
            System.out.println("FileUtils exception: " + e.getMessage());
        }

        return null;
    }
}
