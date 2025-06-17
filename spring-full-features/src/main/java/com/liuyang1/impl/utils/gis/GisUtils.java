package com.liuyang1.impl.utils.gis;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 处理经纬度坐标
 */
public class GisUtils {
    /**
     * 经纬度值，保留一定位小数位
     *
     * @param val
     * @param accuracy
     * @return
     */
    public static Float approximate(Float val, int accuracy) {
        if (accuracy < 0) {
            return val;
        }
        BigDecimal bd = new BigDecimal(String.valueOf(val));
        BigDecimal rounded = bd.setScale(accuracy, RoundingMode.HALF_UP); // 四舍五入

        return rounded.floatValue();
    }
}
