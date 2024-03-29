package com.liuyang1.impl.service;

/**
 * Author：OneCodeMonkey
 * Date: 3/28/24 17:31
 * Desc: IP归属地查询服务
 */
public interface Ip2RegionService {
    /**
     * 根据 ip 地址字符串，搜索归属地
     *
     * @param ip
     * @return
     */
    String searchIp(String ip);
}
