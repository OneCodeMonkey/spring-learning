package com.liuyang1.impl.utils.gis;

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
        String formatted = String.format("%." + accuracy + "f", val);
        return Float.parseFloat(formatted);
    }
}
