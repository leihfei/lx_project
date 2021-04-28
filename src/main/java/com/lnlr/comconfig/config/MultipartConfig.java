package com.lnlr.comconfig.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import javax.servlet.MultipartConfigElement;

/**
 * 功能说明：文件上传配置
 *
 * @author wangwt
 * @date 2019-04-13 11:26
 */
@Configuration
public class MultipartConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大.//KB,MB
        factory.setMaxFileSize(DataSize.of(1024, DataUnit.MEGABYTES));
        /// 设置总上传数据总大小
        factory.setMaxRequestSize(DataSize.of(1024, DataUnit.MEGABYTES));
        return factory.createMultipartConfig();
    }


}
