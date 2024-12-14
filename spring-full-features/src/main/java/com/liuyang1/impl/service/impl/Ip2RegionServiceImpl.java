package com.liuyang1.impl.service.impl;

import com.liuyang1.impl.service.Ip2RegionService;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Author：OneCodeMonkey
 * Date: 3/28/24 17:33
 * Desc:
 */
@Service
@Slf4j
public class Ip2RegionServiceImpl implements Ip2RegionService {
    @Autowired
    private ResourceLoader resourceLoader;

    private Searcher searcher;

    @PostConstruct
    public void init() throws IOException {
        // 创建 searcher 对象
        // 最好把 ip2region.xdb 拷贝到 部署路径的一个文件夹下，通过绝对路径读取，或加载到内存中查询，提高查询速度
        String dbPath = "{directory_path}/ip2region.xdb";
        log.info("dbPath: {}", dbPath);
        try {
            searcher = Searcher.newWithFileOnly(dbPath);
        } catch (IOException e) {
            log.error("failed to create searcher with `{}`: {}", dbPath, e.getMessage());
        }
    }

    @Override
    public String searchIp(String ip) {
        // 2、查询
        try {
            long sTime = System.nanoTime();
            String region = searcher.search(ip);
            long cost = TimeUnit.NANOSECONDS.toMicros((long) (System.nanoTime() - sTime));
            log.info("{region: {}, ip: {}, ioCount: {}, took: {} μs}", region, ip, searcher.getIOCount(), cost);
            return region;
        } catch (Exception e) {
            log.info("failed to search({}): {}", ip, e);
        }

        // 3、关闭资源
        // searcher.close();

        return null;
    }
}
