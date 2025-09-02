package com.liuyang1.impl.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: huangxinhe
 * @Date: 2021/11/8 11:58
 * @Description:
 */
public class GsonUtils {
    //    static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    public static Gson gson = new GsonBuilder().create();

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    /**
     * Json转List<Object> 工具类
     *
     * @param tag
     * @param <T>
     * @return
     */
    public static <T> List<T> getDataList(String tag) {
        List<T> datalist = new ArrayList<T>();
        if (null == tag) {
            return datalist;
        }

        datalist = gson.fromJson(tag, new TypeToken<List<T>>() {
        }.getType());

        return datalist;
    }

    public static <T> T fromJson(String jsonStr, Class<T> clazz) {
        return gson.fromJson(jsonStr, clazz);
    }

    public static <T> T fromJson(String jsonStr, TypeToken typeOfT) {
        return gson.fromJson(jsonStr, typeOfT.getType());
    }


    /**
     * 对json字符串进行格式化
     *
     * @param content        要格式化的json字符串
     * @param indent         是否进行缩进(false时压缩为一行)
     * @param colonWithSpace key冒号后是否加入空格
     * @return
     */
    public static String toFormat(String content, boolean indent, boolean colonWithSpace) {
        if (content == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int count = 0;
        boolean inString = false;
        String tab = "\t";
        for (int i = 0; i < content.length(); i++) {
            char ch = content.charAt(i);
            switch (ch) {
                case '{':
                case '[':
                    sb.append(ch);
                    if (!inString) {
                        if (indent) {
                            sb.append("\n");
                            count++;
                            for (int j = 0; j < count; j++) {
                                sb.append(tab);
                            }
                        }

                    }
                    break;
                case '\uFEFF': //非法字符
                    if (inString) {
                        sb.append(ch);
                    }
                    break;
                case '}':
                case ']':
                    if (!inString) {
                        if (indent) {
                            count--;
                            sb.append("\n");
                            for (int j = 0; j < count; j++) {
                                sb.append(tab);
                            }
                        }

                        sb.append(ch);
                    } else {
                        sb.append(ch);
                    }
                    break;
                case ',':
                    sb.append(ch);
                    if (!inString) {
                        if (indent) {
                            sb.append("\n");
                            for (int j = 0; j < count; j++) {
                                sb.append(tab);
                            }
                        } else {
                            if (colonWithSpace) {
                                sb.append(' ');
                            }
                        }
                    }
                    break;
                case ':':
                    sb.append(ch);

                    if (!inString) {
                        if (colonWithSpace) {  //key名称冒号后加空格
                            sb.append(' ');
                        }
                    }
                    break;
                case ' ':
                case '\n':
                case '\t':
                    if (inString) {
                        sb.append(ch);
                    }
                    break;
                case '"':
                    if (i > 0 && content.charAt(i - 1) != '\\') {
                        inString = !inString;
                    }
                    sb.append(ch);
                    break;
                default:
                    sb.append(ch);
                    break;
            }
        }
        return sb.toString();
    }
}
