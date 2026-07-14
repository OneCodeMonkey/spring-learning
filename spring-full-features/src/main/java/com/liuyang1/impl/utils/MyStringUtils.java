package com.liuyang1.impl.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyStringUtils {
    /**
     * URL 参数做 encode()
     *
     * @param param
     * @return
     */
    public static String urlencode(String param) {
        try {
            return URLEncoder.encode(param, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return param;
    }

    /**
     * 手机号加密混淆处理：如：13577889990 -> 135****9990，010-98801119 -> 010-****1119
     *
     * @param phoneNumber
     * @return
     */
    public static String telephoneConfuse(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return null;
        }

        // 匹配手机号：1开头 + 10位数字（共11位）
        // 匹配座机号：0开头 + 2~4位区号 + 可选连字符 + 7~8位号码（总长度需为11位）
        String regex = "\\b(1\\d{10}|0\\d{2,4}-?\\d{7,8})\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String match = matcher.group();          // 匹配到的完整字符串
            String digits = match.replace("-", "");  // 去除连字符

            // 只处理长度为 11 的数字串
            if (digits.length() != 11) {
                matcher.appendReplacement(sb, Matcher.quoteReplacement(match));
                continue;
            }

            char first = digits.charAt(0);
            if (first != '1' && first != '0') {
                matcher.appendReplacement(sb, Matcher.quoteReplacement(match));
                continue;
            }

            // 保留前 3 位和后 4 位，中间 4 位（第 4～7 位）替换为 ****
            String maskedDigits = digits.substring(0, 3) + "****" + digits.substring(7);

            // 如果原字符串含有连字符，则恢复其位置
            int hyphenIdx = match.indexOf('-');
            String replacement;
            if (hyphenIdx != -1) {
                replacement = maskedDigits.substring(0, hyphenIdx) + "-" + maskedDigits.substring(hyphenIdx);
            } else {
                replacement = maskedDigits;
            }

            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    // -------------------------------------------------------------------------
    // emoji 相关：识别文本中的 emoji（含大部分 4 字节 UTF-8 表情符号），
    // 转换为形如 "[EMOJI_NAME]" 的文字描述，避免写入 utf8/varchar 等
    // 不支持 4 字节字符的存储介质时报 "Incorrect string value" 错误。
    // -------------------------------------------------------------------------
    private static final int[][] EMOJI_RANGES = {
            {0x2600, 0x27BF},     // 杂项符号 & 装饰符号（☀ ✨ ❤ 等）
            {0x2B00, 0x2BFF},     // 杂项符号和箭头（⭐ ⬛ 等）
            {0x1F1E6, 0x1F1FF},   // 区域指示符（国旗）
            {0x1F300, 0x1F5FF},   // 杂项符号与图形
            {0x1F600, 0x1F64F},   // 表情符号
            {0x1F680, 0x1F6FF},   // 交通与地图符号
            {0x1F700, 0x1F77F},   // 炼金符号
            {0x1F780, 0x1F7FF},   // 几何图形扩展
            {0x1F800, 0x1F8FF},   // 补充箭头-C
            {0x1F900, 0x1F9FF},   // 补充符号与图形
            {0x1FA00, 0x1FA6F},   // 棋类符号
            {0x1FA70, 0x1FAFF},   // 符号与图形扩展-A
    };

    /**
     * 识别并转换大段文本中的 emoji 图标为对应的文字表示，
     * 例如 "今天天气🍊不错" -> "今天天气[TANGERINE]不错"。
     * 未命中 Unicode 官方名称的 emoji，会转换为 "[EMOJI_十六进制码点]"。
     *
     * @param text 原始文本
     * @return emoji 已替换为文字表示的文本；text 为 null 或空时原样返回
     */
    public static String removeEmoji(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder sb = new StringBuilder(text.length());
        int length = text.length();
        for (int i = 0; i < length; ) {
            int codePoint = text.codePointAt(i);
            int charCount = Character.charCount(codePoint);

            if (isEmojiModifier(codePoint)) {
                // 零宽连接符/变体选择符/肤色修饰符/keycap 组合符：附着在相邻 emoji 上，直接丢弃
            } else if (isEmoji(codePoint)) {
                sb.append('[').append(emojiName(codePoint)).append(']');
            } else {
                sb.appendCodePoint(codePoint);
            }
            i += charCount;
        }
        return sb.toString();
    }

    private static boolean isEmoji(int codePoint) {
        for (int[] range : EMOJI_RANGES) {
            if (codePoint >= range[0] && codePoint <= range[1]) {
                return true;
            }
        }
        return false;
    }

    private static boolean isEmojiModifier(int codePoint) {
        return codePoint == 0x200D // 零宽连接符 ZWJ，用于拼接组合 emoji
                || codePoint == 0x20E3 // combining enclosing keycap，如 1️⃣ 中的方框
                || (codePoint >= 0xFE00 && codePoint <= 0xFE0F) // 变体选择符（emoji/文字样式切换）
                || (codePoint >= 0x1F3FB && codePoint <= 0x1F3FF); // 肤色修饰符
    }

    private static String emojiName(int codePoint) {
        try {
            String name = Character.getName(codePoint);
            if (name != null && !name.isEmpty()) {
                return name;
            }
        } catch (Exception ignored) {
        }
        return "EMOJI_" + Integer.toHexString(codePoint).toUpperCase();
    }
}
