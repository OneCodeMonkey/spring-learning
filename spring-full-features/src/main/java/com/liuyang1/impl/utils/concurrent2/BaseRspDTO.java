package com.liuyang1.impl.utils.concurrent2;

/**
 * @author OneCodeMonkey
 */
public class BaseRspDTO<T> {
    /**
     * 区分业务返回的标识
     */
    private String key;

    /**
     * 返回的data
     */
    private T data;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseRspDTO{" +
                "key='" + key + '\'' +
                ", data=" + data +
                '}';
    }
}
