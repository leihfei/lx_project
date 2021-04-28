package com.lnlr.config.druid;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author:leihfei
 * @description 主从数据源配置
 * @date:Create in 10:25 2018/8/29
 * @email:leihfein@gmail.com
 */
@ConfigurationProperties(prefix = "spring.datasource.common")
@Component
@Data
public class CommonDBProperties {

    /**
     * 驱动名称
     */
    private String driverClassName;

    /**
     * 最大活动连接
     */
    private Integer maxActive;

    /**
     * 初始化连接池大小
     */
    private Integer initialSize;

    /**
     * 保持最小连接
     */
    private Integer minIdle;

    /**
     * 最大等待时间
     */
    private Integer maxWait;

    /**
     *
     */
    private Integer maxPoolPreparedStatementPerConnectionSize;

    /**
     * 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
     * 空闲连接回收的时间间隔，与test-while-idle一起使用，设置5分钟
     */
    private Integer timeBetweenEvictionRunsMillis;

    /**
     * 配置一个连接在池中最小生存的时间，单位是毫秒
     * 连接池空闲连接的有效时间 ，设置30分钟
     */
    private Integer minEvictableIdleTimeMillis;

    /**
     * 打开PSCache，并且指定每个连接上PSCache的大小
     */
    private Boolean poolPreparedStatements;

    /**
     *
     */
    private String validationQuery;

    /**
     * 验证连接的有效性
     */
    private Boolean testWhileIdle;

    /**
     * 获取连接时候验证，会影响性能
     */
    private Boolean testOnBorrow;

    /**
     * 在连接归还到连接池时是否测试该连接
     */
    private Boolean testOnReturn;

    /**
     * 通过connectProperties属性来打开mergeSql功能；慢SQL记录
     */
    private String connectionProperties;

    /**
     * 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
     */
    private String filters;

}
