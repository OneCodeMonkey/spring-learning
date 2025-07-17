package com.liuyang1.impl.consts;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 【常量类】spring框架相关
 *
 * @author OneCodeMonkey
 */
@Data
@Component
public class SpringApplicationConstants {
    public static final String envDev = "dev", envStable = "stable";

    /**
     * 常量类加载 profile，解决某些需要在 component construct 中取 @Value 为 null 的问题。
     */
    @Value("${spring.profiles.active:}")
    public String env;
}
