package org.papa.canal.server.redis;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by (Even wu for yoju) on 2018/4/1.
 */
public interface RedisService<T> {
    /** 在redis的基础上增加"Object"操作方法 **/
    T getObject(String key) throws Exception;

    void setObject(String key, T value) throws Exception;

    void setexObject(String key, int seconds, T value) throws Exception;

    void hmsetObject(String key, Map<String, T> hash) throws Exception;

    List<T> hmgetObject(String key, String... fields) throws Exception;

    boolean sismemberObject(String key, T value) throws Exception;

    void saddObject(String key, T value) throws Exception;

    Set<T> smembersObject(String key) throws Exception;

    /*********************************/

    /********* Redis原生方法 ***************/
    byte[] getAsByteArray(String key) throws Exception;

    String get(String key) throws Exception;

    void setAsByteArray(String key, byte[] value) throws Exception;

    void set(String key, String value) throws Exception;

    List<byte[]> hmget(String key, String... fields) throws Exception;

    void hmset(String key, Map<String, byte[]> hash) throws Exception;

    Long hset(String key, String field, String value) throws Exception;

    Long hdel(String key, String field) throws Exception;

    Long del(String... keys) throws Exception;

    void setexAsByteArray(String key, int seconds, byte[] value) throws Exception;

    void setex(String key, int seconds, String value) throws Exception;

    boolean exists(String key) throws Exception;

    boolean sismember(String key, byte[] value) throws Exception;

    void sadd(String key, byte[] value) throws Exception;

    Set<byte[]> smembers(String key) throws Exception;

    Long scard(String key) throws Exception;

    Long incr(String key) throws Exception;

    Long incrBy(String key, long integer) throws Exception;

    Long decr(String key)throws Exception;

    Long decrBy(String key, long integer) throws Exception;

    Set<String> keys(String prefix);

    Long srem(String key, byte[]... value) throws UnsupportedEncodingException;      // srem key member [member ...]
    /*****************************************/
    /**
     * 加锁
     * @param lock 令牌
     * @param expired 时间(毫秒)
     * @return
     */
    boolean acquireLock(String lock, Long expired);

    /**
     * 释放锁
     * @param lock 令牌
     */
    void releaseLock(String lock);
}
