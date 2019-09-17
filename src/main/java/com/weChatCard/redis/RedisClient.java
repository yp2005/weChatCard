package com.weChatCard.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * Redis客户端
 *
 * @Author: yupeng
 */

@Component
public class RedisClient {
    private static Logger log = LoggerFactory.getLogger(RedisClient.class);
    @Autowired
    private JedisPool jedisPool;

    /**
     * 设置值
     *
     * @param key
     * @param value
     * @throws Exception
     */
    public void set(String key, String value) throws Exception {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(key)) {
                jedis.del(key);
            }
            jedis.set(key, value);
        } finally {
            //返还到连接池
            jedis.close();
        }
    }

    /**
     * 设置值
     *
     * @param key
     * @param value
     * @param seconds 生存时间设为 seconds (以秒为单位)
     * @throws Exception
     */
    public void set(String key, String value, int seconds) throws Exception {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(key)) {
                jedis.del(key);
            }
            jedis.set(key, value, "NX", "EX", seconds);
        } finally {
            //返还到连接池
            jedis.close();
        }
    }

    /**
     * 获取值
     *
     * @param key
     * @return
     * @throws Exception
     */
    public String get(String key) throws Exception {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.get(key);
        } finally {
            //返还到连接池
            jedis.close();
        }
    }

    /**
     * 删除Key
     *
     * @param key
     * @throws Exception
     */
    public void remove(String key) throws Exception {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(key);
        } finally {
            //返还到连接池
            jedis.close();
        }
    }

    /**
     * 获取所有的Key
     *
     * @return
     * @throws Exception
     */
    public Set<String> getAllKeys() throws Exception {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.keys("*");
        } finally {
            //返还到连接池
            jedis.close();
        }
    }

    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     */
    public boolean exists(String key) throws Exception {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.exists(key);
        } finally {
            //返还到连接池
            jedis.close();
        }
    }

    /**
     * 清空
     *
     * @throws Exception
     */
    public void flushAll() throws Exception {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.flushAll();
        } finally {
            //返还到连接池
            jedis.close();
        }
    }

    /**
     * 获取所有key正则匹配的内存数据
     *
     * @param pattern
     * @return
     * @throws Exception
     */
    public Set<String> keys(String pattern) throws Exception {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.keys(pattern);
        } finally {
            //返还到连接池
            jedis.close();
        }
    }

}