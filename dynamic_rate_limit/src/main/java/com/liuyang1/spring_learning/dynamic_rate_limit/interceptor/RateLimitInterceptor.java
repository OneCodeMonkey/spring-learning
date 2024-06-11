package com.liuyang1.spring_learning.dynamic_rate_limit.interceptor;

import com.liuyang1.spring_learning.dynamic_rate_limit.redis.JedisPoolResource;
import com.liuyang1.spring_learning.dynamic_rate_limit.utils.IpUtils;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author：didi
 * Date: 6/11/24 16:38
 * Desc:
 */
@Slf4j
@Component
public class RateLimitInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private JedisPoolResource jedisPool;

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(8, 8, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(Integer.MAX_VALUE), new ThreadFactory() {
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask-" + threadNumber.getAndIncrement());
        }
    }, new ThreadPoolExecutor.AbortPolicy());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = IpUtils.getIpAddr(request);
        log.info("request address uri={},ip={}", request.getRequestURI(), ip);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String requestUri = request.getRequestURI();
        log.info("url: {}, response code: {}", requestUri, response.getStatus());
        if (response.getStatus() == 200) {
            threadPoolExecutor.submit(() -> {
                int qps = countQps(requestUri);
                boolean checkReactLimit = checkRateLimitRulesByGroovy(qps);
                if (checkReactLimit) {
                    log.error("触发限流规则！！！！！！！！\n触发限流规则！！！！！！！！\n触发限流规则！！！！！！！！");
                }
            });
        }
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    private int countQps(String uri) {

        String luaScript = "local key = KEYS[1]\n" +
                "local expire_key = KEYS[2]\n" +
                "local current_time = redis.call(\"TIME\")[1]\n" +
                "\n" +
                "\n" +
                "local expire_key_result = redis.call(\"GET\", expire_key)\n" +
                "\n" +
                "if type(expire_key_result) == \"boolean\" then\n" +
                "    redis.call(\"SET\", expire_key, current_time)\n" +
                "    redis.call(\"SET\", key, '0')\n" +
                "else\n" +
                "    local expire_time = tonumber(current_time) - tonumber(expire_key_result)\n" +
                " \n" +
                "    if expire_time > 1 or (type(expire_key_result) == \"boolean\") then\n" +
                "        redis.call(\"SET\", expire_key, current_time)\n" +
                "        redis.call(\"SET\", key, '0')\n" +
                "    end\n" +
                "end\n" +
                " \n" +
                "redis.call(\"INCR\", key)\n" +
                "return redis.call(\"GET\", key)";

        try (Jedis jedis = jedisPool.getPoolConnection().getResource()) {
            String uriFormatted = uri.replace("/", "#").trim();
            String countKey = uriFormatted + "#qps";
            String expireKey = uriFormatted + "#expire";

            int qps = Integer.parseInt((String) jedis.eval(luaScript, 2, countKey, expireKey));
            log.info("uri: {}, qps: {}", uri, qps);

            return qps;
        } catch (Exception e) {
            log.error("redis disconnect, please check! e: {}", e.getMessage());
        }

        return 0;
    }

    private boolean checkRateLimitRulesByGroovy(int qps) {
        long startTime = System.currentTimeMillis();
        String filePath = "src/main/resources/scripts/rate-limit.groovy";
        File groovyFile = new File(filePath);
        Binding bindings = new Binding();
        bindings.setProperty("qps", qps);
        GroovyShell shell = new GroovyShell(bindings);
        try {
            Script script = shell.parse(groovyFile);
            Boolean result = (Boolean) script.run();
            log.info("script run cost: {}ms", (System.currentTimeMillis() - startTime));

            return result;
        } catch (Exception e) {
            log.error("checkRateLimitRulesByGroovy error: {}", e.getMessage());
        }

        return false;
    }
}
