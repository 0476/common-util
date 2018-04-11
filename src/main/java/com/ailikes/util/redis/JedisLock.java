package com.ailikes.util.redis;

import redis.clients.jedis.Jedis;

/**
 * 
 * 功能描述: Redis distributed lock implementation.
 * 
 * date:   2018年4月11日 下午4:47:21
 * @author: ailikes
 * @version: 1.0.0
 * @since: 1.0.0
 */
public class JedisLock {

    Jedis   jedis;
    /**
     * Lock key path.
     */
    String  lockKey;

    /**
     * Lock expiration in miliseconds.
     */
    int     expireMsecs  = 60 * 1000; //锁超时，防止线程在入锁以后，无限的执行等待

    /**
     * Acquire timeout in miliseconds.
     */
    int     timeoutMsecs = 10 * 1000; //锁等待，防止线程饥饿

    boolean locked       = false;

    /**
     * Detailed constructor with default acquire timeout 10000 msecs and lock expiration of 60000 msecs.
     * @param jedis
     * @param lockKey
     */
    public JedisLock(Jedis jedis, String lockKey) {
        this.jedis = jedis;
        this.lockKey = lockKey;
    }

    /**
     * Detailed constructor with default lock expiration of 60000 msecs.
     * @param jedis
     * @param lockKey
     * @param timeoutMsecs
     */
    public JedisLock(Jedis jedis, String lockKey, int timeoutMsecs) {
        this(jedis, lockKey);
        this.timeoutMsecs = timeoutMsecs;
    }

    /**
     * Detailed constructor.
     * @param jedis
     * @param lockKey
     * @param timeoutMsecs
     * @param expireMsecs
     */
    public JedisLock(Jedis jedis, String lockKey, int timeoutMsecs, int expireMsecs) {
        this(jedis, lockKey, timeoutMsecs);
        this.expireMsecs = expireMsecs;
    }

    /**
     * Detailed constructor with default acquire timeout 10000 msecs and lock expiration of 60000 msecs.
     * @param lockKey
     */
    public JedisLock(String lockKey) {
        this(null, lockKey);
    }

    /**
     * Detailed constructor with default lock expiration of 60000 msecs.
     * @param lockKey
     * @param timeoutMsecs
     */
    public JedisLock(String lockKey, int timeoutMsecs) {
        this(null, lockKey, timeoutMsecs);
    }

    /**
     * Detailed constructor.
     * @param lockKey
     * @param timeoutMsecs
     * @param expireMsecs
     */
    public JedisLock(String lockKey, int timeoutMsecs, int expireMsecs) {
        this(null, lockKey, timeoutMsecs, expireMsecs);
    }

    /**
     * 
     * 功能描述: 获取锁
     *
     * @return String
     * date:   2018年4月11日 下午4:48:33
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public String getLockKey() {
        return lockKey;
    }

    /**
     * 
     * 功能描述: Acquire lock.
     *
     * @return
     * @throws InterruptedException boolean
     * date:   2018年4月11日 下午4:48:47
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public synchronized boolean acquire() throws InterruptedException {
        return acquire(jedis);
    }

    /**
     * 
     * 功能描述: Acquire lock.
     *
     * @param jedis
     * @return
     * @throws InterruptedException boolean
     * date:   2018年4月11日 下午4:48:55
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public synchronized boolean acquire(Jedis jedis) throws InterruptedException {
        int timeout = timeoutMsecs;
        while (timeout >= 0) {
            long expires = System.currentTimeMillis() + expireMsecs + 1;
            String expiresStr = String.valueOf(expires); //锁到期时间
            if (jedis.setnx(lockKey, expiresStr) == 1) {
                // lock acquired
                locked = true;
                return true;
            }

            String currentValueStr = jedis.get(lockKey); //redis里的时间
            if (currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
                //判断是否为空，不为空的情况下，如果被其他线程设置了值，则第二个条件判断是过不去的
                // lock is expired

                String oldValueStr = jedis.getSet(lockKey, expiresStr);
                //获取上一个锁到期时间，并设置现在的锁到期时间，
                //只有一个线程才能获取上一个线上的设置时间，因为jedis.getSet是同步的
                if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
                    //如过这个时候，多个线程恰好都到了这里，但是只有一个线程的设置值和当前值相同，他才有权利获取锁
                    // lock acquired
                    locked = true;
                    return true;
                }
            }
            timeout -= 100;
            Thread.sleep(100);
        }
        return false;
    }

    /**
     * 
     * 功能描述: Acqurired lock release.
     * void
     * date:   2018年4月11日 下午4:49:05
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public synchronized void release() {
        release(jedis);
    }

    /**
     * 
     * 功能描述:  Acqurired lock release.
     *
     * @param jedis void
     * date:   2018年4月11日 下午4:49:14
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public synchronized void release(Jedis jedis) {
        if (locked) {
            jedis.del(lockKey);
            locked = false;
        }
    }
}
