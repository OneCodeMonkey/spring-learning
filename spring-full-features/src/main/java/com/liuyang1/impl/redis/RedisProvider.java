package com.liuyang1.impl.redis;

import com.liuyang1.impl.utils.RedisKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author liupoyang
 * @description RedisProvider
 * @date 2021/11/07 20:55
 */
@Slf4j
@Component("redisProvider")
public class RedisProvider {
    JedisPool jedisPool;

    @Resource
    private RedisKeyUtils redisKeyUtils;

    public RedisProvider(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public String set(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.set(key, value);
        } catch (Exception e) {
            log.error("redis error", e);
            return "0";
        } finally {
            jedis.close();
        }
    }

    public String set(String key, String value, SetParams setParams) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.set(key, value, setParams);
        } catch (Exception e) {
            log.error("redis error", e);
            return null;
        } finally {
            jedis.close();
        }
    }

    public String setpx(String key, String value, long expireTimeMillis) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.set(key, value, new SetParams().px(expireTimeMillis));
        } catch (Exception e) {
            log.error("redis error", e);
            return null;
        } finally {
            jedis.close();
        }
    }

    /**
     * @param key
     * @param expireTimeMillis
     * @return
     * @deprecated didi fusion 不支持pexpire命令，参考：http://wiki.intra.xiaojukeji.com/pages/viewpage.action?pageId=137299578
     */
    public Long pexpire(String key, long expireTimeMillis) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.pexpire(key, expireTimeMillis);
        } catch (Exception e) {
            log.error("redis error", e);
            return 0L;
        } finally {
            jedis.close();
        }
    }

    public Long expire(String key, int expireTimeSeconds) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.expire(key, expireTimeSeconds);
        } catch (Exception e) {
            log.error("redis error", e);
            return 0L;
        } finally {
            jedis.close();
        }
    }

    public String get(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.get(key);
        } catch (Exception e) {
            log.error("redis error", e);
            return null;
        } finally {
            jedis.close();
        }
    }

    public Boolean exist(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.exists(key);
        } catch (Exception e) {
            log.error("redis error", e);
            return false;
        } finally {
            jedis.close();
        }
    }

    public Long del(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.del(key);
        } catch (Exception e) {
            log.error("redis error", e);
            return 0L;
        } finally {
            jedis.close();
        }
    }

    public Long incr(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.incr(key);
        } catch (Exception e) {
            log.error("redis error", e);
            return 0L;
        } finally {
            jedis.close();
        }
    }

    public Long ttl(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.ttl(key);
        } catch (Exception e) {
            log.error("redis error", e);
            return 0L;
        } finally {
            jedis.close();
        }
    }

    /**
     * @param oldKey
     * @param newKey
     * @return
     * @deprecated Fusion 不支持 redis 的 rename 命令
     */
    public String rename(String oldKey, String newKey) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.rename(oldKey, newKey);
        } catch (Exception e) {
            log.error("redis error", e);
            return "";
        } finally {
            jedis.close();
        }
    }

    /**
     * 返回key所存储的值类型
     *
     * @param key
     * @return
     * @deprecated Fusion 不支持 redis 的 type 命令
     */
    public String type(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.type(key);
        } catch (Exception e) {
            log.error("redis error", e);
            return "";
        } finally {
            jedis.close();
        }
    }

    /**
     * 监听key的变化
     * Fusion 不支持 watch 命令，建议使用 kedis 或原生redis
     *
     * @param key
     * @return
     */
    public String watch(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.watch(key);
        } catch (Exception e) {
            log.error("redis error", e);
            return "";
        } finally {
            jedis.close();
        }
    }

    public Long incrBy(String key, long step) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.incrBy(key, step);
        } catch (Exception e) {
            log.error("redis error", e);
            return 0L;
        } finally {
            jedis.close();
        }
    }

    public Long sAdd(String key, String... values) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.sadd(key, values);
        } catch (Exception e) {
            log.error("redis error", e);
            return 0L;
        }
    }

    public Long sRem(String key, String... values) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.srem(key, values);
        } catch (Exception e) {
            log.error("redis error", e);
            return 0L;
        }
    }

    public Set<String> sMembers(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.smembers(key);
        } catch (Exception e) {
            log.error("redis error", e);
            return new HashSet<>();
        }
    }

    public Long lPush(String key, String... values) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lpush(key, values);
        } catch (Exception e) {
            log.error("redis error", e);
            return 0L;
        }
    }

    /**
     * @param key
     * @param count
     * @param value
     * @return
     * @deprecated fusion新版本不支持lrem
     */
    public Long lRem(String key, long count, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lrem(key, count, value);
        } catch (Exception e) {
            log.error("redis error", e);
            return 0L;
        }
    }

    /**
     * @param key
     * @param value
     * @return
     * @deprecated fusion新版本不支持lrem
     */
    public Long lRemAll(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lrem(key, 0, value);
        } catch (Exception e) {
            log.error("redis error", e);
            return 0L;
        }
    }

    /**
     * @param key
     * @param value
     * @return
     * @deprecated fusion新版本不支持lrem
     */
    public Long lRemLeftMost(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lrem(key, 1, value);
        } catch (Exception e) {
            log.error("redis error", e);
            return 0L;
        }
    }

    /**
     * @param key
     * @param value
     * @return
     * @deprecated fusion新版本不支持lrem
     */
    public Long lRemRightMost(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lrem(key, -1, value);
        } catch (Exception e) {
            log.error("redis error", e);
            return 0L;
        }
    }

    public Long rPush(String key, String... values) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.rpush(key, values);
        } catch (Exception e) {
            log.error("redis error", e);
            return 0L;
        }
    }

    public List<String> lRange(String key, long start, long stop) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lrange(key, start, stop);
        } catch (Exception e) {
            log.error("redis error", e);
            return null;
        }
    }

    // zset operations
    public Long zAdd(String key, double score, String member) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zadd(key, score, member);
        } catch (Exception e) {
            log.error("redis error", e);
            return null;
        }
    }

    public Long zAdd(String key, HashMap<String, Double> scoreMembers) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zadd(key, scoreMembers);
        } catch (Exception e) {
            log.error("redis error", e);
            return null;
        }
    }

    public Long zCard(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zcard(key);
        } catch (Exception e) {
            log.error("redis error", e);
            return null;
        }
    }

    public Set<String> zRange(String key, long start, long stop) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zrange(key, start, stop);
        } catch (Exception e) {
            log.error("redis error", e);
            return null;
        }
    }

    public Set<String> zRangeByScore(String key, double min, double max) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zrangeByScore(key, min, max);
        } catch (Exception e) {
            log.error("redis error", e);
            return null;
        }
    }

    public Long zRem(String key, String... members) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zrem(key, members);
        } catch (Exception e) {
            log.error("redis error", e);
            return null;
        }
    }

    public Set<String> zRevRangeByScore(String key, double min, double max) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zrevrangeByScore(key, min, max);
        } catch (Exception e) {
            log.error("redis error", e);
            return null;
        }
    }

    public Long zRemRangeByScore(String key, double min, double max) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zremrangeByScore(key, min, max);
        } catch (Exception e) {
            log.error("redis error", e);
            return null;
        }
    }

    public Set<String> zRevRangeByRank(String key, long start, long stop) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zrevrange(key, start, stop);
        } catch (Exception e) {
            log.error("redis error", e);
            return null;
        }
    }

    public Long zRemRangeByRank(String key, long start, long stop) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zremrangeByRank(key, start, stop);
        } catch (Exception e) {
            log.error("redis error", e);
            return null;
        }
    }

    /* hash 命令 start */

    /**
     * hset
     *
     * @param key
     * @param field
     * @param fieldValue
     * @return
     */
    public Long hset(String key, String field, String fieldValue) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hset(key, field, fieldValue);
        } catch (Exception e) {
            log.error("redis error", e);
            return null;
        }
    }

    public Long hsetnx(String key, String field, String fieldValue) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hsetnx(key, field, fieldValue);
        } catch (Exception e) {
            log.error("redis error", e);
            return null;
        }
    }

    public Long hset(String key, Map<String, String> keyValueMapping) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hset(key, keyValueMapping);
        } catch (Exception e) {
            log.error("redis error", e);
            return null;
        }
    }

    public String hmset(String key, Map<String, String> keyValueMapping) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hmset(key, keyValueMapping);
        } catch (Exception e) {
            log.error("redis error", e);
            return null;
        }
    }

    public String hget(String key, String field) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hget(key, field);
        } catch (Exception e) {
            log.error("redis error", e);
            return null;
        }
    }

    public List<String> hmget(String key, String... fields) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hmget(key, fields);
        } catch (Exception e) {
            log.error("redis error", e);
            return null;
        }
    }

    public Long hincrBy(String key, String field, Long step) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hincrBy(key, field, step);
        } catch (Exception e) {
            log.error("redis error", e);
            return null;
        }
    }

    public Boolean hexists(String key, String field) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hexists(key, field);
        } catch (Exception e) {
            log.error("redis error", e);
            return null;
        }
    }

    public Map<String, String> hgetAll(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hgetAll(key);
        } catch (Exception e) {
            log.error("redis error", e);
            return null;
        }
    }

    public Set<String> hkeys(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hkeys(key);
        } catch (Exception e) {
            log.error("redis error", e);
            return null;
        }
    }

    public Long hlen(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hlen(key);
        } catch (Exception e) {
            log.error("redis error", e);
            return null;
        }
    }

    public Long hdel(String key, String... fields) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hdel(key, fields);
        } catch (Exception e) {
            log.error("redis error", e);
            return null;
        }
    }
    /* hash 命令 end */

    public Long eval(String luaScript, int keyCount, String... keys) {
        try (Jedis jedis = jedisPool.getResource()) {
            return (Long) jedis.eval(luaScript, keyCount, keys);
        } catch (Exception e) {
            log.error("redis error", e);
            return -1L;
        }
    }
}
