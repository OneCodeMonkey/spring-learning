package com.liuyang1.impl.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Redis key 命名统一前缀
 */
@Component
public class RedisKeyUtils {
    @Value("${spring.application.name}")
    private String appName;
    @Value("${spring.profiles.active}")
    private String env;

    // 部署环境
    private final static String ENV_DEV = "dev";
    private final static String ENV_QA = "qa";
    //    private final static String ENV_PRE = "pre";
    private final static String ENV_PROD = "prod";
    // 缓存有过期时间或永久
    // Cache:有过期时间
    public final static String TYPE_CACHE = "Cache";
    // Storage:永久key
    public final static String TYPE_STORAGE = "Storage";
    // 数据结构类型
    public final static String DATA_TYPE_STRING = "string";
    public final static String DATA_TYPE_HASH = "hash";
    public final static String DATA_TYPE_LIST = "list";
    public final static String DATA_TYPE_SET = "set";
    public final static String DATA_TYPE_ZSET = "zset";
    public final static String DATA_TYPE_GEO = "geo";

    private final List<String> envList = Arrays.asList(ENV_DEV, ENV_QA, ENV_PROD);
    private final List<String> typeList = Arrays.asList(TYPE_CACHE, TYPE_STORAGE);
    private final List<String> dataTypeList = Arrays.asList(DATA_TYPE_STRING, DATA_TYPE_HASH, DATA_TYPE_LIST,
            DATA_TYPE_SET, DATA_TYPE_ZSET, DATA_TYPE_GEO);

    /**
     * 获取统一格式的key前缀
     *
     * @param type     类型：Cache 或 Storage
     * @param dataType 数据类型：string，hash，list，set，zset，geo
     * @return prefix 前缀
     */
    public String getPrefix(String type, String dataType) throws Exception {
//        if (StringUtils.isNotBlank(env) && !envList.contains(env)) {
//            throw new BizException(HUBErrorCode.REDIS_KEY_FORMAT_INVALID);
//        }
        if (!typeList.contains(type) || !dataTypeList.contains(dataType)) {
            throw new Exception("error");
        }

        StringBuilder prefix = new StringBuilder();
        if (StringUtils.isNotBlank(appName)) {
            prefix.append(appName).append("#");
        }
        if (StringUtils.isNotBlank(env)) {
            prefix.append(env).append("#");
        }
        prefix.append(type).append("#");
        prefix.append(dataType).append("#");

        return prefix.toString();
    }

    // 填写自定义的 key 前缀，获取key的方法等。
    private final static String APOLLO_CONFIG = "apollo_config#%s";

    /**
     * Apollo配置缓存key前缀
     *
     * @param suffix
     * @return
     */
    public static String getApolloConfigKey(String suffix) {
        return String.format(APOLLO_CONFIG, suffix);
    }
}
