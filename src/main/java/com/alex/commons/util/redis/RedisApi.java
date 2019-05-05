package com.alex.commons.util.redis;

import com.alex.commons.util.PropertiesUtils;
import com.alex.commons.util.time.TimeConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class RedisApi {

    private static JedisPool pool;

    private static Properties prop = null;

    private static JedisPoolConfig config = null;

    static {
        prop = PropertiesUtils.newProperties("redis.properties");

        config = new JedisPoolConfig();
        config.setMaxTotal(Integer.valueOf(prop.getProperty("MAX_TOTAL")));
        config.setMaxIdle(Integer.valueOf(prop.getProperty("MAX_IDLE")));
        config.setMaxWaitMillis(Integer.valueOf(prop.getProperty("MAX_WAIT_MILLIS")));
        config.setTestOnBorrow(Boolean.valueOf(prop.getProperty("TEST_ON_BORROW")));
        config.setTestOnReturn(Boolean.valueOf(prop.getProperty("TEST_ON_RETURN")));
        config.setTestWhileIdle(Boolean.valueOf(prop.getProperty("TEST_WHILE_IDLE")));

    }

    public static void createJedisPool(String address) {
        pool = new JedisPool(config, address.split(":")[0],
                Integer.valueOf(address.split(":")[1]), 100000);
    }

    public static JedisPool getPool() {

        if (pool == null) {

            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(Integer.valueOf(prop.getProperty("MAX_TOTAL")));
            config.setMaxIdle(Integer.valueOf(prop.getProperty("MAX_IDLE")));
            config.setMaxWaitMillis(Integer.valueOf(prop.getProperty("MAX_WAIT_MILLIS")));
            config.setTestOnBorrow(Boolean.valueOf(prop.getProperty("TEST_ON_BORROW")));
            config.setTestOnReturn(Boolean.valueOf(prop.getProperty("TEST_ON_RETURN")));
            config.setTestWhileIdle(Boolean.valueOf(prop.getProperty("TEST_WHILE_IDLE")));
            pool = new JedisPool(config, prop.getProperty("REDIS_IP"),
                    Integer.valueOf(prop.getProperty("REDIS_PORT")));
        }

        return pool;
    }

    public static void returnResource(Jedis redis) {
        if (redis != null) {
            pool.returnResource(redis);
        }
    }

    static Jedis getJedis() {
        return pool.getResource();
    }

    public static void publish(String channel, String msg) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.publish(channel, msg);
        } catch (Exception e) {
            log.error("publish error", e);
        } finally {
            returnResource(jedis);
        }
    }

    public static void subsribe(String channel, JedisPubSub ps) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.subscribe(ps, channel);
        } catch (Exception e) {
            log.error("subsribe error", e);
        } finally {
            returnResource(jedis);
        }
    }

    public static Long hdel(String key, String key1) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.hdel(key, key1);
        } catch (Exception e) {
            log.error("hdel error", e);
        } finally {
            returnResource(jedis);
        }
        return 0L;
    }

    /**
     * getUser
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        Jedis jedis = null;
        String value = null;
        try {
            jedis = getJedis();
            value = jedis.get(key);
        } catch (Exception e) {
            log.error("getUser error", e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    public static boolean exists(String key) {
        Jedis jedis = null;
        boolean value = false;
        try {
            jedis = getJedis();
            value = jedis.exists(key);
        } catch (Exception e) {
            log.error("exists error", e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * set
     */
    public static String set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.set(key, value);
        } catch (Exception e) {
            log.error("set error", e);
            return null;
        } finally {
            returnResource(jedis);
        }
    }

    public static String set(String key, String value, int expire) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.expire(key, expire);
            return jedis.set(key, value);
        } catch (Exception e) {
            log.error("set error", e);
            return null;
        } finally {
            returnResource(jedis);
        }
    }

    public static Long del(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.del(key);
        } catch (Exception e) {
            log.error("del error", e);
            return 0L;
        } finally {
            returnResource(jedis);
        }
    }


    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    /**
     * 尝试获取分布式锁
     *
     * @param lockKey    锁
     * @param requestId  请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public static boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime) {
        Jedis jedis = getJedis();
        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);

        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;

    }

    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * 释放分布式锁
     *
     * @param lockKey   锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public static boolean releaseDistributedLock(String lockKey, String requestId) {
        Jedis jedis = getJedis();
        String script = "if redis.call('getUser', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));

        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;

    }

    /**
     * @param @param  key
     * @param @param  strings  list.add(object)
     * @param @return 参数
     * @return Long 返回类型
     * @throws
     * @Description 操作list类型数据的
     */

    public static Long lpush(String key, String... strings) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.lpush(key, strings);
        } catch (Exception e) {
            log.error("lpush error", e);
            return 0L;
        } finally {
            returnResource(jedis);
        }
    }

    public static List<String> lrange(String key) {
        Jedis jedis = null;
        String value = null;
        try {
            jedis = getJedis();
            return jedis.lrange(key, 0, -1);
        } catch (Exception e) {
            log.error("lrange error", e);
            return Collections.EMPTY_LIST;
        } finally {
            returnResource(jedis);
        }
    }

    public static String hmset(String key, Map map) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            String hmset = jedis.hmset(key, map);
            jedis.expire(key, TimeConstant.SECONDS_OF_DAY);
            return hmset;
        } catch (Exception e) {
            log.error("hmset error", e);
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    public static List<String> hmget(String key, String... strings) {
        Jedis jedis = null;
        String value = null;
        try {
            jedis = getJedis();
            return jedis.hmget(key, strings);
        } catch (Exception e) {
            log.error("hmget error", e);
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    public static <T> T hgetAll(String key, Class<T> clazz) {
        Map map = hgetAll(key);
        if (map == null || map.isEmpty()) {
            return null;
        }
        try {
            T t = clazz.newInstance();
            BeanUtils.populate(t, map);
            return t;
        } catch (InstantiationException e) {
            log.error("hgetAll error", e);
            return null;
        } catch (IllegalAccessException e) {
            log.error("hgetAll error", e);
        } catch (InvocationTargetException e) {
            log.error("hgetAll error", e);
        }
        return null;
    }

    public static Map hgetAll(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.hgetAll(key);
        } catch (Exception e) {
            log.error("hgetAll error", e);
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    public static boolean setBit(String key, long offset, boolean b) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.setbit(key, offset, b);
        } catch (Exception e) {
            log.error("setBit error", e);
        } finally {
            returnResource(jedis);
        }
        return false;
    }

    public static boolean getbit(String key, long offset) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.getbit(key, offset);
        } catch (Exception e) {
            log.error("getbit error", e);
        } finally {
            returnResource(jedis);
        }
        return false;
    }

    public static Long expire(String key, long time) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.expire(key, (int) time);
        } catch (Exception e) {
            log.error("expire error", e);
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    public static String lpop(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.lpop(key);
        } catch (Exception e) {
            log.error("lpop error", e);
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    public static Long llen(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.llen(key);
        } catch (Exception e) {
            log.error("llen error", e);
        } finally {
            returnResource(jedis);
        }
        return 0L;
    }
}
