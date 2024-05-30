package com.liuyang1.spring_learning.bloom_filter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Date: 5/31/24 01:33
 */
@SpringBootTest
@Slf4j
public class BloomFilterUsingGuava {
    /*
     参数说明：
     在guava包中的BloomFilter源码中，构造一个BloomFilter对象有四个参数：

     Funnel funnel：数据类型，由Funnels类指定即可
     long expectedInsertions：预期插入的值的数量
     fpp：错误率
     BloomFilter.Strategy：hash算法

     我们通过调整expectedInsertions和误判率p时，位数组BitArray的大小m（numBits）和Hash函数的个数k（numHashFunctions）都会自适应变化
     */

    /**
     * 预计插入的数据
     */
    private static Integer expectedInsertions = 10_000;
    /**
     * 误判率
     */
    private static Double fpp = 0.001;
    /**
     * 布隆过滤器
     */
    private static BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), expectedInsertions, fpp);

    @Test
    public void testBloomFilter() {
        // 插入 1千万数据
        for (int i = 0; i < expectedInsertions; i++) {
            bloomFilter.put(i);
        }

        // 从 10000 开始，用1万数据测试误判率
        int count = 0;
        for (int i = expectedInsertions; i < expectedInsertions * 2; i++) {
            if (bloomFilter.mightContain(i)) {
                count++;
            }
        }
        log.info("一共误判了：{}/{}", count, expectedInsertions);
        log.info("估计布隆过滤器里的元素个数：{}", bloomFilter.approximateElementCount());
        log.info("估计布隆过滤器的失误率：{}", bloomFilter.expectedFpp());
    }
}
