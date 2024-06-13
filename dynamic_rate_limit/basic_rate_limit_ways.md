高并发系统有三大特征：限流、缓存和熔断。
限流是保护后端系统资源，优化服务稳定性的最重要手段之一。

常用算法：
1.固定窗口计数
2.滑动窗口计数
3.漏桶算法
4.令牌桶算法

阅读：令牌桶与漏桶算法的区别，理解：https://blog.csdn.net/weixin_37862824/article/details/124814827
重点在于 令牌桶允许更大的突发流量，而漏桶的重点在于保持出水（处理请求）的速率恒定。


3.使用Redis实现限流
使用 Redis 也可以实现简单的限流，它的常见限流方法有以下几种实现：

基于计数器和过期时间实现的计数器算法：使用一个计数器存储当前请求量（每次使用 incr 方法相加），并设置一个过期时间，计数器在一定时间内自动清零。计数器未到达限流值就可以继续运行，反之则不能继续运行。

基于有序集合（ZSet）实现的滑动窗口算法：将请求都存入到 ZSet 集合中，在分数（score）中存储当前请求时间。然后再使用 ZSet 提供的 range 方法轻易的获取到 2 个时间戳内的所有请求，通过获取的请求数和限流数进行比较并判断，从而实现限流。

基于列表（List）实现的令牌桶算法：在程序中使用定时任务给 Redis 中的 List 添加令牌，程序通过 List 提供的 leftPop 来获取令牌，得到令牌继续执行，否则就是限流不能继续运行。

了解了以上概念后，接下来我们来看具体的实现。

3.1 计数器算法
此方法的实现思路是：使用一个计数器存储当前请求量（每次使用 incr 方法相加），并设置一个过期时间，计数器在一定时间内自动清零，从而实现限流。

它的具体操作步骤如下：

使用 Redis 的计数器保存当前请求的数量。
设置一个过期时间，使得计数器在一定时间内自动清零。
每次收到请求时，检查计数器当前值，如果未达到限流阈值，则增加计数器的值，否则拒绝请求。
具体实现代码如下：

```java
import redis.clients.jedis.Jedis;

public class RedisRateLimiter {
private static final String REDIS_KEY = "request_counter";
private static final int REQUEST_LIMIT = 100; // 限流阈值
private static final int EXPIRE_TIME = 60; // 过期时间（秒）

    public boolean allowRequest() {
        Jedis jedis = new Jedis("localhost");
        
        try {
            Long counter = jedis.incr(REDIS_KEY);
            if (counter == 1) {
                // 第一次设置过期时间
                jedis.expire(REDIS_KEY, EXPIRE_TIME);
            }
            
            if (counter <= REQUEST_LIMIT) {
                return true; // 允许请求通过
            } else {
                return false; // 请求达到限流阈值，拒绝请求
            }
        } finally {
            jedis.close();
        }
    }

    public static void main(String[] args) {
        RedisRateLimiter rateLimiter = new RedisRateLimiter();
        for (int i = 0; i < 110; i++) {
            if (rateLimiter.allowRequest()) {
                System.out.println("Request Allowed");
            } else {
                System.out.println("Request Denied (Rate Limited)");
            }
        }
    }
}
```

在上述代码中，每次请求会通过 allowRequest() 方法判断是否达到限流阈值，如果未达到则允许通过，并递增计数器的值，否则拒绝请求。同时，第一次设置计数器的过期时间，使得计数器在指定的时间内自动清零。

PS：以上是一个简单的示例，实际应用中需要根据具体场景实现更复杂的限流逻辑，并考虑并发情况下的线程安全性等问题。

因为计算器算法有突刺问题，因此我们需要使用升级版的滑动窗口算法或其他限流算法来解决此问题。

3.2 滑动窗口算法
此方法的实现思路是：将请求都存入到 ZSet 集合中，在分数（score）中存储当前请求时间。然后再使用 ZSet 提供的 range 方法轻易的获取到 2 个时间戳内的所有请求，通过获取的请求数和限流数进行比较并判断，从而实现限流。

它的具体操作步骤如下：

使用有序集合（ZSet）来存储每个时间窗口内的请求时间戳，成员（member）表示请求的唯一标识，分数（score）表示请求的时间戳。
每次收到请求时，将请求的时间戳作为成员，当前时间戳作为分数加入到有序集合中。
根据有序集合的时间范围和滑动窗口的设置，判断当前时间窗口内的请求数量是否超过限流阈值。

具体实现代码如下：

```java
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.Set;

public class RedisSlidingWindowRateLimiter {

    private static final String ZSET_KEY = "request_timestamps";
    private static final int WINDOW_SIZE = 60; // 时间窗口大小（单位：秒）
    private static final int REQUEST_LIMIT = 100; // 限流阈值

    public boolean allowRequest() {
        Jedis jedis = new Jedis("localhost");
        long currentTimestamp = System.currentTimeMillis() / 1000;

        // 添加当前请求的时间戳到有序集合
        jedis.zadd(ZSET_KEY, currentTimestamp, String.valueOf(currentTimestamp));

        // 移除过期的请求时间戳，保持时间窗口内的请求
        long start = currentTimestamp - WINDOW_SIZE;
        long end = currentTimestamp;
        jedis.zremrangeByScore(ZSET_KEY, 0, start);

        // 查询当前时间窗口内的请求数量
        Set<Tuple> requestTimestamps = jedis.zrangeByScoreWithScores(ZSET_KEY, start, end);
        long requestCount = requestTimestamps.size();

        jedis.close();

        // 判断请求数量是否超过限流阈值
        return requestCount <= REQUEST_LIMIT;
    }

    public static void main(String[] args) {
        RedisSlidingWindowRateLimiter rateLimiter = new RedisSlidingWindowRateLimiter();

        for (int i = 0; i < 110; i++) {
            if (rateLimiter.allowRequest()) {
                System.out.println("Request Allowed");
            } else {
                System.out.println("Request Denied (Rate Limited)");
            }
        }
    }
}
```


在上述代码中，每次收到请求时，将当前请求的时间戳加入到有序集合中，并移除过期的请求时间戳，然后查询当前时间窗口内的请求数量，判断是否达到限流阈值。这样基于 Redis 的滑动窗口限流算法可以有效控制单位时间内的请求流量，避免系统被过多请求压垮。


3.3 令牌桶算法
此方法的实现思路是：在程序中使用定时任务给 Redis 中的 List 添加令牌，程序通过 List 提供的 leftPop 来获取令牌，得到令牌继续执行，否则就是限流不能继续运行。

① 添加令牌
在 Spring Boot 项目中，通过定时任务给 Redis 中的 List 每秒中添加一个令牌（当然也可以通过修改定时任务的执行时间来控制令牌的发放速度），具体实现代码如下：

```java
@Configuration      // 1.注入到 IoC 中，启动程序时加载
@EnableScheduling   // 2.开启定时任务
public class SaticScheduleTask {
@Autowired
private RedisTemplate redisTemplate;
    // 3.添加定时任务
    @Scheduled(fixedRate = 1000)
    private void configureTasks() {
        redisTemplate.opsForList().rightPush("limit_list",UUID.randomUUID().toString());
    }
    
    // ② 获取令牌
    // 令牌的获取代码如下：
    public boolean allowRequest(){
        Object result = redisTemplate.opsForList().leftPop("limit_list");
        if(result == null){
            return false;
        }
        return true;
    }
}
```


在上述代码中，我们每次访问 allowRequest() 方法时，会尝试从 Redis 中获取一个令牌，如果拿到令牌了，那就说明没超出限制，可以继续执行，反之则不能执行。
