package com.liuyang1.redisfull;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.util.List;

@Slf4j
public class StringOperationTest extends RedisBasicTest {
    @Test
    public void testStringOperations() {
        // 1.set
        Jedis jedis = jedisPool.getResource();
        jedis.set("key1", "value1");
        String result = jedis.get("key1");
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result, "value1");

        // 2.mget
        jedis.set("key2", "value2");
        jedis.set("key3", "value3");
        List<String> values = jedis.mget("key1", "key2", "key3");
        Assertions.assertNotNull(values);
        Assertions.assertEquals(values.size(), 3);
        Assertions.assertEquals(values.get(1), "value2");
        Assertions.assertEquals(values.get(2), "value3");

        // 3.mset
        jedis.mset("key2", "value222", "key3", "value333");
        List<String> values2 = jedis.mget("key1", "key2", "key3");
        Assertions.assertNotNull(values);
        Assertions.assertEquals(values2.size(), 3);
        Assertions.assertEquals(values2.get(1), "value222");
        Assertions.assertEquals(values2.get(2), "value333");

        // 4.strlen
        long length = jedis.strlen("key2");
        Assertions.assertEquals(length, "value222".length());

        // 5.append string to a String-key
        jedis.append("key1", "11");
        String result1 = jedis.get("key1");
        Assertions.assertEquals(result1, "value111");

        // 6.get sub string of a String-key
        String subString = jedis.getrange("key1", 1, 5);
        Assertions.assertEquals(subString, "value111".substring(1, 6));

        // 7. getSet: get old value and set new value
        String oldValue = jedis.getSet("key1", "value11111");
        String newValue = jedis.get("key1");
        Assertions.assertEquals(oldValue, "value111");
        Assertions.assertEquals(newValue, "value11111");

        // 8. setex
        String setResult = jedis.setex("key2", 10, "value22222");
        long ttl = jedis.ttl("key2");
        Assertions.assertEquals("OK", setResult);
        Assertions.assertEquals(10, ttl);
        String result2 = jedis.get("key2");
        Assertions.assertEquals(result2, "value22222");

        // 9. psetex
        String setResult2 = jedis.psetex("key2", 1000, "value2222222");
        long pttl = jedis.pttl("key2");
        Assertions.assertEquals(setResult, "OK");
        Assertions.assertTrue(1000 - pttl < 200);
        String result3 = jedis.get("key2");
        Assertions.assertEquals("value2222222", result3);

        // 10. setnx
        jedis.del("key4");
        long setResult3 = jedis.setnx("key4", "value4");
        Assertions.assertEquals(1, setResult3);
        Assertions.assertEquals("value4", jedis.get("key4"));
        // set while key exist
        jedis.set("key3", "value3");
        long setResult4 = jedis.setnx("key3", "value4");
        Assertions.assertEquals(0, setResult4);
        Assertions.assertEquals("value3", jedis.get("key3"));

        // 11. msetnx
        jedis.del("key4");
        jedis.set("key3", "value3");
        long setResult5 = jedis.msetnx("key4", "value4", "key3", "value4");
        Assertions.assertEquals(0, setResult5);
        // only when all key set success, msetnx() return 1L.
        jedis.del("key4");
        jedis.del("key3");
        long setResult6 = jedis.msetnx("key4", "value4", "key3", "value3");
        Assertions.assertEquals(1, setResult6);

        // 12. incr & decr
        jedis.set("key1", "1");
        long setResult7 = jedis.incr("key1");
        Assertions.assertEquals(2, setResult7);
        Assertions.assertEquals("2", jedis.get("key1"));
        long setResult8 = jedis.decr("key1");
        Assertions.assertEquals(1, setResult8);
        Assertions.assertEquals("1", jedis.get("key1"));

        // 13. incrBy & decrBy
        jedis.set("key1", "1");
        long setResult9 = jedis.incrBy("key1", 100);
        Assertions.assertEquals(101, setResult9);
        Assertions.assertEquals("101", jedis.get("key1"));
        long setResult10 = jedis.decrBy("key1", 100);
        Assertions.assertEquals(1, setResult10);
        Assertions.assertEquals("1", jedis.get("key1"));

        // 14. incrByFloat
        jedis.set("key1", "1");
        double setResult11 = jedis.incrByFloat("key1", 0.01);
        Assertions.assertEquals(1.01, setResult11);
        Assertions.assertEquals("1.01", jedis.get("key1"));
        double setResult12 = jedis.incrByFloat("key1", -0.01);
        Assertions.assertEquals(1, setResult12);
        Assertions.assertEquals("1", jedis.get("key1"));
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
