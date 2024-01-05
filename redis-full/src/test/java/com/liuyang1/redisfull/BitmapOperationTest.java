package com.liuyang1.redisfull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.args.BitOP;

// todo
public class BitmapOperationTest extends RedisBasicTest {
    @Test
    public void testBitmapOperation() {
        Jedis jedis = jedisPool.getResource();

        // 15. bitop: 对字符串的值进行位操作，指定操作类型 AND，OR，XOR，NOT 等
        // bitop 返回值是判断的 bit 位数，且是二者中 bit 位长度的较大值
        // @see：https://redis.io/commands/bitop/
        // 当两个二进制数不等时，会在短的数后面补0，使两者长度一致后再按位与。注意是向后补0，而不是在前补0！
        jedis.mset("key1", "10000", "key2", "10101");
        // 将结果保存到一个新key中
        long setResult13 = jedis.bitop(BitOP.AND, "key3", "key1", "key2");
        Assertions.assertEquals(5, setResult13);
        Assertions.assertEquals(Integer.toBinaryString(0b10000 & 0b10101), jedis.get("key3"));

        jedis.mset("key1", "1000", "key2", "1011");
        long setResult14 = jedis.bitop(BitOP.AND, "key3", "key1", "key2");
        Assertions.assertEquals(4, setResult14);
        Assertions.assertEquals(Integer.toBinaryString(0b1000 & 0b1011), jedis.get("key3"));

        jedis.mset("key1", "10001", "key2", "1011");
        long setResult15 = jedis.bitop(BitOP.AND, "key3", "key1", "key2");
        Assertions.assertEquals(5, setResult15);
        Assertions.assertEquals("1000\u0000", jedis.get("key3"));

        jedis.mset("key1", "10000", "key2", "10101");
        long setResult16 = jedis.bitop(BitOP.AND, "key3", "key1", "key2");
        Assertions.assertEquals(5, setResult16);
        Assertions.assertEquals(Integer.toBinaryString(0b10000 & 0b10101), jedis.get("key3"));

        jedis.mset("key1", "100011", "key2", "10110");
        long setResult17 = jedis.bitop(BitOP.AND, "key3", "key1", "key2");
        Assertions.assertEquals(6, setResult17);
        Assertions.assertEquals("10000\u0000", jedis.get("key3"));

        // todo bit 操作逻辑有点混乱，没看懂
        // 16. getbit
//        jedis.set("key1", "10001001");  // 100110001001101001101001
//        log.info(Integer.toBinaryString(10001001));
//        for (int i = 0; i < 24; i++) {
//            log.info("{}", jedis.getbit("key1", i));
//        }
//        Assertions.assertTrue(jedis.getbit("key1", 1));
//        Assertions.assertTrue(jedis.getbit("key1", 5));
//        Assertions.assertFalse(jedis.getbit("key1", 8));
//
//        // 17. setbit
//        jedis.set("key1", "10001001");
//        Assertions.assertFalse(jedis.getbit("key1", 3));
//        jedis.setbit("key1", 3, true);
//        Assertions.assertTrue(jedis.getbit("key1", 3));

        // 18. setrangebit
    }

    @AfterAll
    public static void tearDown() {
        Jedis jedis = jedisPool.getResource();
        jedis.del("key1");
        jedis.del("key2");
        jedis.del("key3");
        jedis.del("key4");
        jedis.del("key5");
    }
}
