package com.lnlr.common.utils;

import com.google.common.base.Joiner;
import com.lnlr.common.constains.CacheConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;


/**
 * @author:leihfei
 * @description redis 工具类
 * @date:Create in 13:06 2018/9/17
 * @email:leihfein@gmail.com
 */
@Component
public class RedisUtil {

    @Autowired
    private JedisPool jedisPool;

    /**
     * redis默认缓存时间为无分钟
     */
    private Integer defaultTime = 300;

    private Jedis getResource() {
        return jedisPool.getResource();
    }

    /**
     * @param key,
     * @param value
     * @return java.lang.String
     * @author: leihfei
     * @description 缓存数据
     * @date: 21:53 2018/11/9
     * @email: leihfein@gmail.com
     */
    public String set(String key, String value) {
        return set(key, value, defaultTime);
    }

    /**
     * @param key,
     * @param value
     * @return java.lang.String
     * @author: leihfei
     * @description 缓存数据
     * @date: 21:53 2018/11/9
     * @email: leihfein@gmail.com
     */
    public String set(String key, String value, Integer timeout) {
        Jedis jedis = getResource();
        try {
            jedis.set(key, value);
            if (timeout != null) {
                jedis.expire(key, timeout);
            }
            return value;
        } finally {
            jedis.close();
        }
    }

    /**
     * @param key
     * @return java.lang.String
     * @author: leihfei
     * @description 通过key取数据
     * @date: 21:54 2018/11/9
     * @email: leihfein@gmail.com
     */
    public String get(String key) {
        Jedis jedis = getResource();
        try {
            return jedis.get(key);
        } finally {
            jedis.close();
        }
    }

    public byte[] set(byte[] key, byte[] value) {
        Jedis jedis = getResource();
        try {
            jedis.set(key, value);
            return value;
        } finally {
            jedis.close();
        }
    }

    public byte[] set(byte[] key, byte[] value, int expire) {
        Jedis jedis = getResource();
        try {
            jedis.set(key, value);
            jedis.expire(key, expire);
            return value;
        } finally {
            jedis.close();
        }
    }

    public void expire(byte[] key, int i) {
        Jedis jedis = getResource();
        try {
            jedis.expire(key, i);
        } finally {
            jedis.close();
        }
    }

    public byte[] get(byte[] key) {
        Jedis jedis = getResource();
        try {
            return jedis.get(key);
        } finally {
            jedis.close();
        }
    }

    public void del(String key) {
        Jedis jedis = getResource();
        try {
            jedis.del(key);
        } finally {
            jedis.close();
        }
    }


    public void del(byte[] key) {
        Jedis jedis = getResource();
        try {
            jedis.del(key);
        } finally {
            jedis.close();
        }
    }

    public Set<byte[]> keys(String sHIRO_SESSION_PREFIX) {
        Jedis jedis = getResource();
        try {
            return jedis.keys((sHIRO_SESSION_PREFIX + "*").getBytes());
        } finally {
            jedis.close();
        }
    }

    /**
     * @param prefix 前缀
     * @param keys   key值
     * @return java.lang.String
     * @author: leihfei
     * @description 通过key，自动生成缓存前缀
     * @date: 22:00 2018/11/9
     * @email: leihfein@gmail.com
     */
    public String generateCacheKey(CacheConstants prefix, String... keys) {
        String key = prefix.name();
        if (keys != null && keys.length > 0) {
            key += "_" + Joiner.on("_").join(keys);
        }
        return key;
    }
}
