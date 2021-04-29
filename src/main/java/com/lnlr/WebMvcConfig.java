package com.lnlr;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.lnlr.common.constains.SystemConstants;
import com.lnlr.common.interceptor.HttpInterceptor;
import com.lnlr.common.utils.JsonUtils;
import com.lnlr.common.utils.RedisUtil;
import com.lnlr.pojo.entity.SysDefaultFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 配置类，
 * 增加自定义拦截器和解析器
 * 配置JSON转换
 *
 * @author Created by 39526 on 2017/5/11.
 */
@Configuration
@EnableWebMvc
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 设置FastJson 为 json转换类
     * <p>
     * Configure the {@link HttpMessageConverter}s to use for reading or writing
     * to the body of the request or response. If no converters are added, a
     * default list of converters is registered.
     * <p><strong>Note</strong> that adding converters to the list, turns off
     * default converter registration. To simply add a converter without impacting
     * default registration, consider using the method
     * {@link #extendMessageConverters(List)} instead.
     *
     * @param converters initially an empty list of converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setCharset(Charset.forName("UTF-8"));
        fastJsonConfig.setFeatures();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteEnumUsingName);

        fastConverter.setFastJsonConfig(fastJsonConfig);

        List<MediaType> supported = new ArrayList<>();
        supported.add(MediaType.APPLICATION_JSON_UTF8);
        fastConverter.setSupportedMediaTypes(supported);

        ByteArrayHttpMessageConverter byteArrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
        converters.add(byteArrayHttpMessageConverter);

        converters.add(fastConverter);

    }

    /**
     * 拦截器配置,配置请求过来
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("配置过滤器，并且配置过滤器放过url");
        String url = "/**";
        String redisD = redisUtil.get(SystemConstants.DEFAULT_URL_CATCH);
        if (StringUtils.isEmpty(redisD)) {
            return;
        }
        List<SysDefaultFilter> all = JsonUtils.json2List(redisD, SysDefaultFilter.class);
        List<String> patterns = new ArrayList<>();
        all.forEach(item -> {
            if (!url.equals(item.getUrl())) {
                patterns.add(item.getUrl());
            }
        });
        log.info("过滤器过滤地址：{}" + all);
        registry.addInterceptor(new HttpInterceptor()).addPathPatterns(url)
                .excludePathPatterns(patterns);
    }

}
