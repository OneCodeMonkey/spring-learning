package com.liuyang1.impl.interceptor;

import com.liuyang1.impl.utils.IpUtils;
import com.liuyang1.impl.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author OneCodeMonkey
 */
@Slf4j
@Component
public class IpUrlLimitInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisUtil redisUtils;

    private static final String LOCK_IP_URL_KEY = "lock_ip_";

    private static final String IP_URL_REQ_TIME = "ip_url_times_";

    //访问次数限制
    private static final long LIMIT_TIMES = 3;

    //限制时间 单位：秒
    private static final int IP_LOCK_TIME = 30;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) {
        System.out.println(String.format("request请求地址uri={},ip={}", httpServletRequest.getRequestURI(), IpUtils.getIpAddr(httpServletRequest)));

        if (ipIsLock(IpUtils.getIpAddr(httpServletRequest))) {
            System.out.println(String.format("ip访问被禁止={}", IpUtils.getIpAddr(httpServletRequest)));

            returnJson(httpServletResponse, "当前操作过于频繁，请5分钟后重试");

            return false;
        }

        if (!addRequestTime(IpUtils.getIpAddr(httpServletRequest), httpServletRequest.getRequestURI())) {
            returnJson(httpServletResponse, "当前操作过于频繁，请5分钟后重试");

            return false;
        }

        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    /**
     * @param ip
     * @Description: 判断ip是否被禁用
     */
    private Boolean ipIsLock(String ip) {
        if (redisUtils.hasKey(LOCK_IP_URL_KEY + ip)) {
            return true;
        }

        return false;
    }

    /**
     * @param ip
     * @param uri
     * @Description: 记录请求次数
     */
    private Boolean addRequestTime(String ip, String uri) {
        String key = IP_URL_REQ_TIME + ip + uri;

        if (redisUtils.hasKey(key)) {
            long time = redisUtils.incr(key, 1);

            if (time >= LIMIT_TIMES) {
                redisUtils.set(LOCK_IP_URL_KEY + ip, ip, IP_LOCK_TIME);
                return false;
            }
        } else {
            redisUtils.set(key, (long) 1, 10);
        }

        return true;
    }

    private void returnJson(HttpServletResponse response, String json) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/json; charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {

            writer.print(json);
        } catch (IOException e) {
            System.out.println(String.format("响应错误 ---> {}", e.getMessage(), e));
        }
    }
}
