package com.liuyang1.impl.utils;

/**
 * Author：OneCodeMonkey
 * Date: 5/14/24 20:09
 * Desc: Number 工具类
 */
public class NumberUtils {
    /**
     * double 类型数据：保留 bit 位小数点
     *
     * @param num
     * @param bit
     * @return
     */
    public static double roundNbits(double num, int bit) {
        bit = Math.min(9, Math.max(0, bit));
        double exp = 1;
        switch (bit) {
            case 1:
                exp = 10.0;
                break;
            case 2:
                exp = 100.0;
                break;
            case 3:
                exp = 1000.0;
                break;
            case 4:
                exp = 10000.0;
                break;
            case 5:
                exp = 100000.0;
                break;
            case 6:
                exp = 1000000.0;
                break;
            case 7:
                exp = 10000000.0;
                break;
            case 8:
                exp = 100000000.0;
                break;
            case 9:
                exp = 1000000000.0;
                break;
            default:
        }

        return Math.round(num * exp) / exp;
    }

    /**
     * float 类型数据：保留 bit 位小数点
     *
     * @param num
     * @param bit
     * @return
     */
    public static float roundNbits(float num, int bit) {
        bit = Math.min(9, Math.max(0, bit));
        float exp = 1;
        switch (bit) {
            case 1:
                exp = 10.0F;
                break;
            case 2:
                exp = 100.0F;
                break;
            case 3:
                exp = 1000.0F;
                break;
            case 4:
                exp = 10000.0F;
                break;
            case 5:
                exp = 100000.0F;
                break;
            case 6:
                exp = 1000000.0F;
                break;
            case 7:
                exp = 10000000.0F;
                break;
            case 8:
                exp = 100000000.0F;
                break;
            case 9:
                exp = 1000000000.0F;
                break;
            default:
        }

        return Math.round(num * exp) / exp;
    }
}
