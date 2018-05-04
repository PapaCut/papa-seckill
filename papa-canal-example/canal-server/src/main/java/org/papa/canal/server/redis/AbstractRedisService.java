package org.papa.canal.server.redis;

import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by (Even wu for yoju) on 2018/4/1.
 */
public abstract class AbstractRedisService<T> implements RedisService<T>{
    private static final String ENCODING = "UTF-8";
    private JedisPool jedisPool;

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public byte[] getAsByteArray(String key) throws Exception {
        Jedis jedis = jedisPool.getResource();
        boolean isSuccess = true;

        try{
            return jedis.get(key.getBytes(ENCODING));
        } catch (Exception e) {
            isSuccess = false;
            throw e;
        } finally {
            if (jedis != null)
                jedis.close();
            if (!isSuccess)
                return null;
        }
    }

    public String get(String key) throws Exception {
        Jedis jedis = jedisPool.getResource();
        boolean isSuccess = true;

        try{
            return jedis.get(key);
        } catch (Exception e) {
            isSuccess = false;
            throw e;
        } finally {
            if (jedis != null)
                jedis.close();
            if (!isSuccess)
                return null;
        }
    }

    @Override
    public Long hset(String key, String field, String value) throws Exception {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.hset(key, field, value);
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public Long hdel(String key, String field) throws Exception {
        Jedis jedis = jedisPool.getResource();
        boolean isSuccess = true;

        try {
            return jedis.hdel(key, field);
        } catch(Exception e) {
            isSuccess = false;
            throw e;
        } finally {
            if (jedis != null)
                jedis.close();

            if (!isSuccess)
                return null;
        }
    }

    public Long del(String... keys) throws Exception {
        Jedis jedis = jedisPool.getResource();
        boolean isSuccess = true;

        try {
            return jedis.del(keys);
        } catch(Exception e) {
            isSuccess = false;
            throw e;
        } finally {
            if (jedis != null)
                jedis.close();

            if (!isSuccess)
                return null;
        }
    }

    @Override
    public void setAsByteArray(String key, byte[] value) throws Exception {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key.getBytes(ENCODING), value);
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    public void set(String key, String value) throws Exception {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    public List<byte[]> hmget(String key, String... fields) throws Exception {
        Jedis jedis = jedisPool.getResource();
        boolean isSuccess = true;

        try {
            byte[][] bytes = new byte[fields.length][];
            for (int i = 0; i < fields.length; i++) {
                bytes[i] = fields[i].getBytes(ENCODING);
            }

            List<byte[]> list = jedis.hmget(key.getBytes(ENCODING), bytes);
            if(list.size() > 0 && list.get(0) != null)
                return list;
            else
                return new ArrayList<byte[]>();
        } catch (Exception e) {
            isSuccess = false;
            throw e;
        } finally {
            if (jedis != null)
                jedis.close();
            if (!isSuccess)
                return null;
        }
    }

    public void hmset(String key, Map<String, byte[]> hash) throws Exception {
        Jedis jedis = jedisPool.getResource();

        try {
            Map<byte[], byte[]> value = new HashMap<>();

            Set<String> fields = hash.keySet();
            for (String field : fields) {
                value.put(field.getBytes(ENCODING), hash.get(field));
            }

            jedis.hmset(key.getBytes(ENCODING), value);
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public void setexAsByteArray(String key, int seconds, byte[] value) throws Exception {
        Jedis jedis = jedisPool.getResource();

        try {
            jedis.setex(key.getBytes(ENCODING), seconds, value);
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public void setex(String key, int seconds, String value) throws Exception {
        Jedis jedis = jedisPool.getResource();

        try {
            jedis.setex(key, seconds, value);
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public boolean exists(String key) throws Exception {
        Jedis jedis = jedisPool.getResource();

        try {
            return jedis.exists(key.getBytes(ENCODING));
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public boolean sismember(String key, byte[] value) throws Exception {
        Jedis jedis = jedisPool.getResource();

        try {
            return jedis.sismember(key.getBytes(ENCODING), value);
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public void sadd(String key, byte[] value) throws Exception {
        Jedis jedis = jedisPool.getResource();

        try {
            jedis.sadd(key.getBytes(ENCODING), value);
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public Set<byte[]> smembers(String key) throws Exception {
        Jedis jedis = jedisPool.getResource();

        try {
            return jedis.smembers(key.getBytes(ENCODING));
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public Long scard(String key) throws Exception {
        Jedis jedis = jedisPool.getResource();

        try {
            return jedis.scard(key.getBytes(ENCODING));
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public Long incr(String key) throws Exception {
        Jedis jedis = jedisPool.getResource();

        try {
            return jedis.incr(key.getBytes(ENCODING));
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public Long incrBy(String key, long integer) throws Exception {
        Jedis jedis = jedisPool.getResource();

        try {
            return jedis.incrBy(key.getBytes(ENCODING), integer);
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public Long decr(String key) throws Exception {
        Jedis jedis = jedisPool.getResource();

        try {
            if(StringUtils.isNumeric(jedis.get(key)) && Long.parseLong(jedis.get(key)) > 0) {
                return jedis.decr(key.getBytes(ENCODING));
            }else {
                return 0L;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public Long decrBy(String key, long integer) throws Exception {
        Jedis jedis = jedisPool.getResource();

        try {
            return jedis.decrBy(key.getBytes(ENCODING), integer);
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public Set<String> keys(String prefix) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.keys(prefix);
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public Long srem(String key, byte[]... value) throws UnsupportedEncodingException {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.srem(key.getBytes(ENCODING), value);
        } catch (UnsupportedEncodingException e) {
            throw e;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    //加锁
    @Override
    public  boolean acquireLock(String lock,Long expired) {
        Jedis jedis = jedisPool.getResource();
        try {
            // 1. 通过SETNX试图获取一个lock
            boolean success = false;
            long value = System.currentTimeMillis() + expired + 1;
            long acquired = jedis.setnx(lock, String.valueOf(value));
            //SETNX成功，则成功获取一个锁
            if (acquired == 1)
                success = true;
                //SETNX失败，说明锁仍然被其他对象保持，检查其是否已经超时
            else {
                long oldValue = Long.valueOf(jedis.get(lock));

                //超时
                if (oldValue < System.currentTimeMillis()) {
                    String getValue = jedis.getSet(lock, String.valueOf(value));
                    // 获取锁成功
                    if (Long.valueOf(getValue) == oldValue)
                        success = true;
                        // 已被其他进程捷足先登了
                    else
                        success = false;
                }
                //未超时，则直接返回失败
                else
                    success = false;
            }
            return success;
        } catch (Exception e) {
            throw e;
        }finally {
            if (jedis != null)
                jedis.close();
        }
    }

    //释放锁
    @Override
    public  void releaseLock(String lock) {
        Jedis jedis = jedisPool.getResource();
        try {
            long current = System.currentTimeMillis();
            // 避免删除非自己获取得到的锁
            if (current < Long.valueOf(jedis.get(lock)))
                jedis.del(lock);
        } catch (Exception e) {
            throw e;
        }finally {
            if (jedis != null)
                jedis.close();
        }
    }
}
