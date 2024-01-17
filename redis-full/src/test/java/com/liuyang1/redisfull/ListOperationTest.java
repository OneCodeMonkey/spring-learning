package com.liuyang1.redisfull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.args.ListPosition;

import java.util.List;

public class ListOperationTest extends RedisBasicTest {
    @Test
    public void testListOperation() {
        Jedis jedis = jedisPool.getResource();

        // 1.lpush & rpush
        jedis.lpush("key1", "listElem1", "listElem2", "listElem3");
        Assertions.assertEquals(3, jedis.llen("key1"));
        jedis.rpush("key2", "listElem1", "listElem2", "listElem3");
        Assertions.assertEquals(3, jedis.llen("key2"));

        // 1.1 lpushX: only when key exist can push
        jedis.lpushx("key3", "listElem1", "listElem2", "listElem3");
        Assertions.assertFalse(jedis.exists("key3"));

        // 2.lpop
        String elem1 = jedis.lpop("key1");
        Assertions.assertEquals("listElem3", elem1);
        String elem2 = jedis.lpop("key2");
        Assertions.assertEquals("listElem1", elem2);

        // 3.rpop
        String elem3 = jedis.rpop("key1");
        Assertions.assertEquals("listElem1", elem3);
        String elem4 = jedis.rpop("key2");
        Assertions.assertEquals("listElem3", elem4);

        // 4.lindex
        jedis.del("key1");
        jedis.lpush("key1", "listElem1", "listElem2", "listElem3");
        // 验证 lindex 的索引是 0-indexed
        String elemIndex2 = jedis.lindex("key1", 2);
        Assertions.assertEquals("listElem1", elemIndex2);

        // 5.lrange
        List<String> listVals = jedis.lrange("key1", 0, -1);
        Assertions.assertEquals(3, listVals.size());
        Assertions.assertEquals("listElem3", listVals.get(0));
        Assertions.assertEquals("listElem1", listVals.get(2));

        // 6.lrem：从列表中移除指定数量的 目标元素
        jedis.del("key1");
        jedis.lpush("key1", "listElem2", "listElem1", "listElem2", "listElem1", "listElem2", "listElem3");
        jedis.lrem("key1", 1, "listElem1");
        Assertions.assertEquals(5, jedis.llen("key1"));
        jedis.lrem("key1", 2, "listElem1");
        Assertions.assertEquals(4, jedis.llen("key1"));
        jedis.lrem("key1", 3, "listElem2");
        Assertions.assertEquals(1, jedis.llen("key1"));

        // 7.lset: 通过索引位置设置 list 中的元素
        // 复杂度：O(n)，因为用的是链表
        jedis.del("key1");
        jedis.lpush("key1", "listElem1", "listElem2", "listElem3");
        Assertions.assertEquals("listElem2", jedis.lindex("key1", 1));
        jedis.lset("key1", 1, "listElem222");
        Assertions.assertEquals("listElem222", jedis.lindex("key1", 1));

        // 8.linsert: 在列表指定 value 元素的前或后，插入元素
        jedis.del("key1");
        jedis.lpush("key1", "listElem1", "listElem2", "listElem3");
        Assertions.assertEquals("listElem3", jedis.lindex("key1", 0));
        jedis.linsert("key1", ListPosition.BEFORE, "listElem2", "listElem2.5");
        Assertions.assertEquals("listElem2.5", jedis.lindex("key1", 1));
        Assertions.assertEquals("listElem2", jedis.lindex("key1", 2));

        // 9.ltrim: 截取list特定片段，并作为 list 的新值保留下来
        jedis.del("key1");
        jedis.lpush("key1", "listElem1", "listElem2", "listElem3", "listElem4", "listElem5");
        jedis.ltrim("key1", 1, 3);
        Assertions.assertEquals(3, jedis.llen("key1"));
        Assertions.assertEquals("listElem4", jedis.lindex("key1", 0));
        Assertions.assertEquals("listElem2", jedis.lindex("key1", 2));

        // todo extra commands:
        // brpop
        // lmove
        // lmpop
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
