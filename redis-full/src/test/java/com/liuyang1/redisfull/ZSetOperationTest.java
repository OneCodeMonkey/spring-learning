package com.liuyang1.redisfull;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.resps.Tuple;

import java.util.List;

@Slf4j
public class ZSetOperationTest extends RedisBasicTest {
    @Test
    public void testZSetOperation() {
        Jedis jedis = jedisPool.getResource();

        // 1.zadd, zcard
        jedis.zadd("key1", 1, "elem1");
        jedis.zadd("key1", 2.5, "elem2");
        jedis.zadd("key1", 3, "elem3");
        Assertions.assertEquals(3, jedis.zcard("key1"));

        // 2.zscore
        double score2 = jedis.zscore("key1", "elem2");
        Assertions.assertEquals(2.5, score2);

        // 3.zincrby
        jedis.zincrby("key1", 1, "elem3");
        Assertions.assertEquals(3 + 1, jedis.zscore("key1", "elem3"));

        // 4. zrange：按索引顺序列出指定范围的成员，返回 name
        List<String> members = jedis.zrange("key1", 0, -1);
        Assertions.assertEquals(3, members.size());
        Assertions.assertEquals("elem1", members.get(0));
        Assertions.assertEquals("elem3", members.get(2));
        //
        // Notice：zrange: 是以分数作为排序依据，从小到大排列元素
        jedis.del("key2");
        jedis.zadd("key2", 100, "elem1");
        jedis.zadd("key2", 103.2, "elem2");
        jedis.zadd("key2", 105, "elem3");
        jedis.zadd("key2", 104.1, "elem4");
        List<String> members3 = jedis.zrange("key2", 0, -1);
        Assertions.assertEquals(4, members3.size());
        Assertions.assertEquals("elem1", members3.get(0));
        Assertions.assertEquals("elem4", members3.get(2));
        Assertions.assertEquals("elem3", members3.get(3));
        jedis.del("key2");

        // 5. zrangeByScore
        List<String> members2 = jedis.zrangeByScore("key1", 1, 5);
        Assertions.assertEquals(3, members.size());
        Assertions.assertEquals("elem1", members.get(0));
        Assertions.assertEquals("elem3", members.get(2));

        // 6. zrangeByScoreWithScores
        List<Tuple> membersWithScore = jedis.zrangeByScoreWithScores("key1", 1, 5);
        Assertions.assertEquals(3, members.size());
        Assertions.assertEquals("elem1", membersWithScore.get(0).getElement());
        Assertions.assertEquals(1, membersWithScore.get(0).getScore());
        Assertions.assertEquals("elem3", membersWithScore.get(2).getElement());
        Assertions.assertEquals(4, membersWithScore.get(2).getScore());

        // 7. zrank：获取指定成员的 rank 排名（0-indexed）
        long rank = jedis.zrank("key1", "elem2");
        Assertions.assertEquals(1, rank);
        long rank2 = jedis.zrank("key1", "elem1");
        Assertions.assertEquals(0, rank2);

        // 8. zrevrank: 获取指定成员的 rank 倒排（0-indexed）
        long revRank = jedis.zrevrank("key1", "elem1");
        Assertions.assertEquals(2, revRank);
        long revRank2 = jedis.zrevrank("key1", "elem3");
        Assertions.assertEquals(0, revRank2);

        // 9. zrem
        long removeResult = jedis.zrem("key1", "elem1");
        Assertions.assertEquals(2, jedis.zcard("key1"));
        Assertions.assertEquals(1, removeResult);
        long removeResult2 = jedis.zrem("key1", "elem1");
        // remove 失败，返回 0L
        Assertions.assertEquals(0, removeResult2);

        // 11. zrangeWithScores
        jedis.del("key1");
        jedis.zadd("key1", 100, "elem1");
        jedis.zadd("key1", 103.2, "elem2");
        jedis.zadd("key1", 105, "elem3");
        jedis.zadd("key1", 104.1, "elem4");
        List<Tuple> members4 = jedis.zrangeWithScores("key1", 0, -1);
        Assertions.assertEquals(4, members4.size());
        Assertions.assertEquals("elem2", members4.get(1).getElement());
        Assertions.assertEquals(103.2, members4.get(1).getScore());
        Assertions.assertEquals("elem4", members4.get(2).getElement());
        Assertions.assertEquals(104.1, members4.get(2).getScore());

        // 12. zcount：
        long zcountResult = jedis.zcount("key1", 100.1, 104.2);
        Assertions.assertEquals(2, zcountResult);

        // todo-后续补充 13. 集合类操作：
        // 13.1 zdiff, zdiffstore
        // 13.2 zinter, zinterstore
        // 13.3 zunion, zunionstore
        //
    }

    @AfterAll
    public static void tearDown() {
        Jedis jedis = jedisPool.getResource();
        jedis.del("key1");
        jedis.del("key2");
        jedis.del("key3");
        jedis.del("key4");
        jedis.del("key5");
        jedis.del("key6");
    }
}
