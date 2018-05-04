package org.papa.canal.server.config;

import org.papa.canal.server.redis.SerializableRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by PaperCut on 2018/2/6.
 */
@Configuration
public class RedisConfiguration {
    @Autowired
    RedisProperties redisProperties;

    @Bean
    public SerializableRedisService jedisService() {
        SerializableRedisService jedisService = new SerializableRedisService();
        jedisService.setJedisPool(jedisPool());
        return jedisService;
    }

    @Bean
    public JedisPool jedisPool() {
        JedisPool jedisPool = new JedisPool(
                jedisPoolConfig(),
                redisProperties.getHost(),
                redisProperties.getPort(),
                redisProperties.getTimeout(),
                redisProperties.getPassword());
        return jedisPool;
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(redisProperties.getMaxIdle());
        config.setMinIdle(redisProperties.getMinIdle());
        config.setMaxWaitMillis(redisProperties.getMaxWaitMillis());
        config.setMaxTotal(redisProperties.getMaxTotal());
        config.setTestOnBorrow(redisProperties.isTestOnBorrow());
        config.setTestOnReturn(redisProperties.isTestOnReturn());
        return config;
    }
}
