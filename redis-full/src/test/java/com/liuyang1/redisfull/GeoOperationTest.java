package com.liuyang1.redisfull;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.args.GeoUnit;
import redis.clients.jedis.resps.GeoRadiusResponse;

import java.util.List;

@Slf4j
public class GeoOperationTest extends RedisBasicTest {
    @Test
    public void testGeoOperations() {
        Jedis jedis = jedisPool.getResource();

        // 1.getpos
        jedis.geoadd("key1", 116.397128, 39.916527, "Beijing");
        jedis.geoadd("key1", 121.473701, 31.230416, "Shanghai");
        jedis.geoadd("key1", 113.268554, 23.139590, "Guangzhou");
        jedis.geoadd("key1", 114.065127, 22.545786, "Shenzhen");

        List<GeoCoordinate> coordinates = jedis.geopos("key1", "Beijing", "Shanghai");
        Assertions.assertNotNull(coordinates);
        Assertions.assertEquals(2, coordinates.size());
        Assertions.assertTrue(Math.abs(116.397128 - coordinates.get(0).getLongitude()) < 1e-5);
        Assertions.assertTrue(Math.abs(39.916527 - coordinates.get(0).getLatitude()) < 1e-5);

        // 2.geodist
        double distance = jedis.geodist("key1", "Beijing", "Shanghai", GeoUnit.KM);
        log.info("distance: {}", distance);
        Assertions.assertTrue(distance < 1100L);

        // 3.georadius 半径查找
        List<GeoRadiusResponse> nearLocations = jedis.georadius("key1", 116.397128, 39.916527, 1100, GeoUnit.KM);
        Assertions.assertNotNull(nearLocations);
        Assertions.assertEquals(2, nearLocations.size());
        Assertions.assertEquals("Beijing", nearLocations.get(0).getMemberByString());
        Assertions.assertEquals("Shanghai", nearLocations.get(1).getMemberByString());
        // 中心点传地点名
        List<GeoRadiusResponse> nearLocations2 = jedis.georadiusByMember("key1", "Beijing", 1100, GeoUnit.KM);
        Assertions.assertNotNull(nearLocations2);
        Assertions.assertEquals(2, nearLocations2.size());
        Assertions.assertEquals("Beijing", nearLocations2.get(0).getMemberByString());
        Assertions.assertEquals("Shanghai", nearLocations2.get(1).getMemberByString());
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
