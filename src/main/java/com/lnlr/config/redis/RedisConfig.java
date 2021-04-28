package com.lnlr.config.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author:leihfei
 * @description: redis 配置
 * @date:Create in 13:33 2018/8/7
 * @email:leihfein@gmail.com
 */
@Configuration
@Slf4j
public class RedisConfig extends CachingConfigurerSupport {

    /**
     * redis配置实体类
     */
    @Autowired
    private RedisConfigEntity redisConfigEntity;

    @Bean
    public JedisPool redisPoolFactory() {
        log.info("redisPool注入地址：" + redisConfigEntity.getHost() + ":" + redisConfigEntity.getPort());
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(redisConfigEntity.getMaxIdle());
        jedisPoolConfig.setMaxWaitMillis(redisConfigEntity.getMaxWaitMillis());
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);
        //Idle时进行连接扫描
        jedisPoolConfig.setTestWhileIdle(true);
        //表示idle object evitor两次扫描之间要sleep的毫秒数
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(30000);
        //表示idle object evitor每次扫描的最多的对象数
        jedisPoolConfig.setNumTestsPerEvictionRun(10);
        // 表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
        jedisPoolConfig.setMinEvictableIdleTimeMillis(60000);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, redisConfigEntity.getHost(), redisConfigEntity.getPort(),
                redisConfigEntity.getTimeout(), null, redisConfigEntity.getDatabase());
        return jedisPool;
    }
}

