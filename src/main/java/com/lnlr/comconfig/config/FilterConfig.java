package com.lnlr.comconfig.config;

import com.lnlr.comconfig.filter.LoginFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * @author:leihfei
 * @description 过滤器配置文件
 * @date:Create in 11:10 2018/10/26
 * @email:leihfein@gmail.com
 */
@Configuration
public class FilterConfig {

    private FilterRegistrationBean registrationBean(Filter filter, String pattern, int order) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.addUrlPatterns(pattern);
        registration.setOrder(order);
        return registration;
    }

    @Bean
    public FilterRegistrationBean registrationLoginFilterBean() {
        return registrationBean(getLoginFilter(), "/*", 5);
    }


    @Bean
    public LoginFilter getLoginFilter() {
        return new LoginFilter();
    }
}
