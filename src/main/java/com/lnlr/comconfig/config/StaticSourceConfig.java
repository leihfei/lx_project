package com.lnlr.comconfig.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description 映射静态资源
 * 注意：
 * 1、spring.resource.static-location失效，原因可能是拦截器配置把处理静态资源的配置覆盖了
 * 2、当本类放在base-server下时本类只能继承WebMvcConfigurerAdapter，替换为WebMvcConfigurationSupport会导致登陆报错double-quote，原因未知
 * @Author: 郭涛
 * @Date: 2018/12/13 14:55
 * @Version 1.0
 */
@Configuration
public class StaticSourceConfig implements WebMvcConfigurer {

    @Value("${file.save.path}")
    private String fileSavePath;

    @Value("${source.mapping}")
    private String sourceMapping;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        try {
            registry.addResourceHandler(sourceMapping + "/**").addResourceLocations("file:" + fileSavePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

