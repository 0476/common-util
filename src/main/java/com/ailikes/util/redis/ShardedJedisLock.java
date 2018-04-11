package com.ailikes.util.redis;

/**
 * 
 * 功能描述:
 * 
 * date: 2018年4月11日 下午4:56:48
 * 
 * @author: ailikes
 * @version: 1.0.0
 * @since: 1.0.0
 */
public class ShardedJedisLock {

    // ShardedJedis jedis;
    RedisShardClient redisShardClient;

    /**
     * Lock key path.
     */
    String           lockKey;

    /**
     * Lock expiration in miliseconds.
     */
    int              expireMsecs  = 60 * 1000; // 锁超时，防止线程在入锁以后，无限的执行等待

    /**
     * Acquire timeout in miliseconds.
     */
    int              timeoutMsecs = 10 * 1000; // 锁等待，防止线程饥饿

    boolean          locked       = false;

    /**
     * 
     * @param redisShardClient
     * @param lockKey
     */
    public ShardedJedisLock(RedisShardClient redisShardClient, String lockKey) {
        this.redisShardClient = redisShardClient;
        this.lockKey = lockKey;
    }

    /**
     * 
     * @param redisShardClient
     * @param lockKey
     * @param timeoutMsecs
     */
    public ShardedJedisLock(RedisShardClient redisShardClient, String lockKey, int timeoutMsecs) {
        this(redisShardClient, lockKey);
        this.timeoutMsecs = timeoutMsecs;
    }

    /**
     * 
     * @param redisShardClient
     * @param lockKey
     * @param timeoutMsecs
     * @param expireMsecs
     */
    public ShardedJedisLock(RedisShardClient redisShardClient, String lockKey, int timeoutMsecs, int expireMsecs) {
        this(redisShardClient, lockKey, timeoutMsecs);
        this.expireMsecs = expireMsecs;
    }

    /**
     * 
     * @param lockKey
     */
    public ShardedJedisLock(String lockKey) {
        this(null, lockKey);
    }

    /**
     * 
     * @param lockKey
     * @param timeoutMsecs
     */
    public ShardedJedisLock(String lockKey, int timeoutMsecs) {
        this(null, lockKey, timeoutMsecs);
    }

    /**
     * 
     * @param lockKey
     * @param timeoutMsecs
     * @param expireMsecs
     */
    public ShardedJedisLock(String lockKey, int timeoutMsecs, int expireMsecs) {
        this(null, lockKey, timeoutMsecs, expireMsecs);
    }

    /**
     * 
     * 功能描述: 获取key
     *
     * @return String date: 2018年4月11日 下午4:55:56
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
     * @throws InterruptedException boolean date: 2018年4月11日 下午4:55:40
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public synchronized boolean acquire() throws InterruptedException {
        return acquire(redisShardClient);
    }

    /**
     * 
     * 功能描述: 获取锁
     *
     * @param redisShardClient
     * @return
     * @throws InterruptedException boolean date: 2018年4月11日 下午4:55:15
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public synchronized boolean acquire(RedisShardClient redisShardClient) throws InterruptedException {
        int timeout = timeoutMsecs;
        while (timeout >= 0) {
            long expires = System.currentTimeMillis() + expireMsecs + 1;
            String expiresStr = String.valueOf(expires); // 锁到期时间

            if (redisShardClient.setnx(lockKey, expiresStr) == 1) {
                // lock acquired
                locked = true;
                return true;
            }

            String currentValueStr = redisShardClient.get(lockKey); // redis里的时间
            if (currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
                // 判断是否为空，不为空的情况下，如果被其他线程设置了值，则第二个条件判断是过不去的
                // lock is expired

                String oldValueStr = redisShardClient.getSet(lockKey, expiresStr);
                // 获取上一个锁到期时间，并设置现在的锁到期时间，
                // 只有一个线程才能获取上一个线上的设置时间，因为jedis.getSet是同步的
                if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
                    // 如过这个时候，多个线程恰好都到了这里，但是只有一个线程的设置值和当前值相同，他才有权利获取锁
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
     * 功能描述: Acqurired lock release. void date: 2018年4月11日 下午4:55:06
     * 
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public synchronized void release() {
        release(redisShardClient);
    }

    /**
     * 
     * 功能描述: Acqurired lock release.
     *
     * @param redisShardClient void date: 2018年4月11日 下午4:54:59
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public synchronized void release(RedisShardClient redisShardClient) {
        if (locked) {
            redisShardClient.del(lockKey);
            locked = false;
        }
    }
}
