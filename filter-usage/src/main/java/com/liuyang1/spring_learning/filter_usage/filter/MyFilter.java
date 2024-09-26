package com.liuyang1.spring_learning.filter_usage.filter;

import com.google.gson.Gson;
import com.liuyang1.spring_learning.filter_usage.utils.IpUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 定义 Filter
 */
@Slf4j
public class MyFilter implements Filter {
    @Override
    public void init(jakarta.servlet.FilterConfig filterConfig) throws jakarta.servlet.ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException {
        String reqIp = IpUtils.getIP();
        log.info("request Addr ip: {}", reqIp);
        if (reqIp.equals("172.24.150.108")) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            Response responseBody = new Response();
            responseBody.setCode(405);
            responseBody.setMessage("invalid request!");
            responseBody.setData(new Object());
            response.getWriter().write(new Gson().toJson(responseBody));
            response.setStatus(405);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    @Data
    static class Response {
        int code;
        String message;
        Object data;
    }
}
