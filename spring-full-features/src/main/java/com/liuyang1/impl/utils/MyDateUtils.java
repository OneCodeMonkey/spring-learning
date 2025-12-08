package com.liuyang1.impl.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
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

    /**
     * unix timestamp 转标准日期格式
     *
     * @param timestamp
     * @return
     */
    public static String getFormattedDateFromTimestamp(long timestamp) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(timestamp);
            return formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static long convertFormattedDateToTimestamp(String timeStr) {
        long ret = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(timeStr);
            ret = date.getTime() / 1000;    // 毫秒转秒
        } catch (Exception e) {
            System.out.println("MyDateUtils convertFormattedDateToTimestamp, parse error: " + e.getMessage());
        }
        return ret;
    }

    public static long getDateDaysDiff(String date1, String date2) {
        if (date1 == null || date2 == null || !date1.contains("-") || !date2.contains("-")) {
            return -1L;
        }
        String[] arr1 = date1.split("-"), arr2 = date2.split("-");
        int year1 = Integer.parseInt(arr1[0]), month1 = Integer.parseInt(arr1[1]), day1 = Integer.parseInt(arr1[2]);
        int year2 = Integer.parseInt(arr2[0]), month2 = Integer.parseInt(arr2[1]), day2 = Integer.parseInt(arr2[2]);
        LocalDate date11 = LocalDate.of(year1, month1, day1);
        LocalDate date22 = LocalDate.of(year2, month2, day2);

        return Math.abs(ChronoUnit.DAYS.between(date11, date22));
    }

    public static String getDateSomeDaysAfterOrBefore(String dateStr, int days) {
        return getDateSomeDaysAfterOrBefore(dateStr, days, "yyyy-MM-dd");
    }

    public static String getDateSomeDaysAfterOrBefore(String dateStr, int days, String format) {
        if (!format.contains("yyyy-MM-dd")) {
            return "";
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            LocalDate date = LocalDate.parse(dateStr, formatter);
            LocalDate newDate;
            if (days >= 0) {
                newDate = date.plusDays(days);
            } else {
                newDate = date.minusDays(-days);
            }

            return newDate.format(formatter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 获取同比：上个月的相同日期
     *
     * @param date
     * @return
     */
    public static Date getTongbiDate(String date) {
        return getDateFromFormattedDateStr(getTongbiDateStr(date), "yyyy-MM-dd");
    }

    /**
     * 获取同比：上个年的相同月份+日期
     *
     * @param date
     * @return
     */
    public static Date getHuanbiDate(String date) {
        return getDateFromFormattedDateStr(getHuanbiDateStr(date), "yyyy-MM-dd");
    }

    /**
     * 计算同比日期（上个月的同一天）
     * 如果上个月没有这一天，则取上个月的最后一天
     */
    public static String getTongbiDateStr(String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // 获取上个月的同一天
            LocalDate lastMonthSameDay = date.minusMonths(1);

            // 如果上个月的天数小于原日期天数，取上个月的最后一天
            YearMonth lastYearMonth = YearMonth.from(lastMonthSameDay);
            int lastMonthDays = lastYearMonth.lengthOfMonth();

            if (date.getDayOfMonth() > lastMonthDays) {
                return lastYearMonth.atEndOfMonth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } else {
                return lastMonthSameDay.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("日期格式错误，应为 yyyy-MM-dd: " + dateStr, e);
        }
    }

    /**
     * 计算环比日期（上一年同月的同一天）
     * 如果上一年同月没有这一天，则取上一年同月的最后一天
     */
    public static String getHuanbiDateStr(String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // 获取上一年同月的同一天
            LocalDate lastYearSameMonthSameDay = date.minusYears(1);

            // 如果上一年同月的天数小于原日期天数，取上一年同月的最后一天
            YearMonth lastYearMonth = YearMonth.from(lastYearSameMonthSameDay);
            int lastYearMonthDays = lastYearMonth.lengthOfMonth();

            if (date.getDayOfMonth() > lastYearMonthDays) {
                return lastYearMonth.atEndOfMonth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } else {
                return lastYearSameMonthSameDay.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("日期格式错误，应为 yyyy-MM-dd: " + dateStr, e);
        }
    }
}
