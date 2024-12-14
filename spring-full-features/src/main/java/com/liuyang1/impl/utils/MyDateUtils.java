package com.liuyang1.impl.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Author：OneCodeMonkey
 * Date: 5/14/24 20:09
 * Desc: Date 工具类
 */
public class MyDateUtils {
    public static Date getFormattedDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date createdTime = null;
        try {
            createdTime = formatter.parse(formatter.format(new Date()));
        } catch (Exception ignored) {
        }

        return createdTime;
    }

    public static String getCurrentFormattedDate() {
        return getCurrentFormattedDate("yyyy-MM-dd 'at' HH:mm:ss z");
    }

    public static String getCurrentFormattedDate(String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            Date date = new Date(System.currentTimeMillis());
            return formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "";
    }

    public static String getCurrentFormattedDateByMilliSecond() {
        // 毫秒时间戳
        SimpleDateFormat milliSecFormatter = new SimpleDateFormat("yyyy-MM-dd 'T' HH:mm:ss.SSS XXX");

        Date date = new Date(System.currentTimeMillis());

        return milliSecFormatter.format(date);
    }

    /**
     * 获取N天前的零点 Date 对象
     *
     * @param days
     * @return
     */
    public static Date getSomeDayAgo(int days) {
        days = Math.max(0, days);
        // N天前的零点时间
        LocalDateTime someDaysAgo = LocalDateTime.now().minusDays(days).withHour(0).withMinute(0).withSecond(0)
                .withNano(0);

        return Date.from(someDaysAgo.atZone(ZoneId.systemDefault()).toInstant());
    }
}
