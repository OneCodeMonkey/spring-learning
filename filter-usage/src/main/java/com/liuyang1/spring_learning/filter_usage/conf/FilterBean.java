package com.liuyang1.spring_learning.filter_usage.conf;

import com.liuyang1.spring_learning.filter_usage.filter.MyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 注册 Filter
 */
@Configuration
public class FilterBean {
    @Bean
    public FilterRegistrationBean<MyFilter> filterRegistrationBean() {
        FilterRegistrationBean<MyFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new MyFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setBeanName("IpFilter");

        // 还可以设置其他属性，如 order，asyncSupported, matchAfter 等
        //

        return registrationBean;
    }
}
