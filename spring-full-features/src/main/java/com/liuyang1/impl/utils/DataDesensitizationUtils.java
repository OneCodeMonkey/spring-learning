package com.liuyang1.impl.utils;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 数据脱敏工具类
 */
@Slf4j
@SuppressWarnings("unchecked")
public class DataDesensitizationUtils {

    // YAML 文件路径
    private static final String YAML_FILE_PATH = "desensitization.yml";

    // 存储解析后的 YAML 数据
    private static Map<String, Object> map;

    static {
        // 创建 Yaml 对象
        Yaml yaml = new Yaml();
        // 通过 getResourceAsStream 获取 YAML 文件的输入流
        try {
            String fileContent = FileUtils.readResourceFile(YAML_FILE_PATH);
            System.out.println(fileContent);
            map = yaml.loadAs(fileContent, Map.class);
            System.out.println(map.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取嵌套的 Map 数据
     *
     * @param keys 嵌套键路径
     * @return 嵌套数据对应的 Map
     */
    private static Map<String, Object> getNestedMap(String... keys) {
        return getNestedMapValues(map, keys);
    }

    /**
     * 递归获取嵌套 Map 数据
     *
     * @param map  嵌套数据源的 Map
     * @param keys 嵌套键路径
     * @return 嵌套数据对应的 Map
     */
    private static Map<String, Object> getNestedMapValues(Map<String, Object> map, String... keys) {
        // 如果键路径为空或者第一个键不在 Map 中，则返回 null
        if (keys.length == 0 || !map.containsKey(keys[0])) {
            return null;
        }

        // 获取第一个键对应的嵌套对象
        Object nestedObject = map.get(keys[0]);

        // 如果键路径长度为 1，说明已经到达最里层的嵌套 Map，直接返回该 Map 对象
        if (keys.length == 1) {
            if (nestedObject instanceof Map) {
                return (Map<String, Object>) nestedObject;
            } else {
                return null;
            }
        } else {
            // 如果嵌套对象是 Map，继续递归查找下一个键的嵌套 Map
            if (nestedObject instanceof Map) {
                return getNestedMapValues((Map<String, Object>) nestedObject, Arrays.copyOfRange(keys, 1, keys.length));
            } else {
                // 嵌套对象既不是 Map 也不是 List，返回 null
                return null;
            }
        }
    }

    /**
     * 对指定实体数据进行脱敏处理
     *
     * @param entity 要进行脱敏处理的实体数据
     * @param servNo 当前交易的服务号，用于记录日志
     * @param path   当前实体数据在整个数据结构中的路径，用于记录日志
     */
    public static void parseData(Object entity, String servNo, String path) {
        if (entity instanceof Map) {
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) entity).entrySet()) {
                String currentPath = path.isEmpty() ? entry.getKey() : path + "," + entry.getKey();
                if (entry.getValue() instanceof Map) {
                    parseData(entry.getValue(), servNo, currentPath);
                } else if (entry.getValue() instanceof List) {
                    for (Object item : (List) entry.getValue()) {
                        if (item instanceof Map) {
                            parseData(item, servNo, currentPath);
                        }
                    }
                } else {
                    String p = servNo + "," + currentPath;
                    String[] keyPaths = p.split(",");

                    Map<String, Object> nestedMap = getNestedMap(keyPaths);

                    if (Objects.nonNull(nestedMap)) {
                        log.info("-----------------交易【{}】，字段【{}】开始脱敏-----------------", servNo, currentPath.replace(",", "->"));
                        log.info("原始值：【{}:{}】", entry.getKey(), entry.getValue());
                        log.info("脱敏规则:{}", nestedMap);
                        String desensitized = desensitizeLogic((String) entry.getValue(), nestedMap);
                        entry.setValue(desensitized);
                        log.info("脱敏值：【{}:{}】", entry.getKey(), entry.getValue());
                        log.info("-----------------交易【{}】，字段【{}】脱敏结束-----------------", servNo, currentPath.replace(",", "->"));
                    }

                }
            }
        }
    }

    /**
     * 脱敏逻辑
     *
     * @param data 源数据
     * @param map  脱敏规则
     * @return 脱敏后的数据
     */
    private static String desensitizeLogic(String data, Map<String, Object> map) {
        if (map.containsKey("rule")) {
            String rule = (String) map.get("rule");
            String sign = "*";
            if (map.containsKey("format")) {
                sign = (String) map.get("format");
            }
            return data.replaceAll(rule, sign);
        }
        return data;
    }
}
