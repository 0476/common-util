package com.ailikes.util.redis.cluster;

/**
 * 
 * 功能描述: Redis distributed lock implementation.
 * 
 * date:   2018年4月11日 下午4:43:15
 * @author: ailikes
 * @version: 1.0.0
 * @since: 1.0.0
 */
public class ClusterJedisLock {

	RedisClusterClient client;

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
     * Detailed constructor with default acquire timeout 10000 msecs and lock expiration of 60000 msecs.
     * @param client
     * @param lockKey
     */
    public ClusterJedisLock(RedisClusterClient client, String lockKey) {
        this.client = client;
        this.lockKey = lockKey;
    }

    /**
     * Detailed constructor with default lock expiration of 60000 msecs.
     * @param client
     * @param lockKey
     * @param timeoutMsecs
     */
    public ClusterJedisLock(RedisClusterClient client, String lockKey, int timeoutMsecs) {
        this(client, lockKey);
        this.timeoutMsecs = timeoutMsecs;
    }

    public ClusterJedisLock(RedisClusterClient client, String lockKey, int timeoutMsecs, int expireMsecs) {
        this(client, lockKey, timeoutMsecs);
        this.expireMsecs = expireMsecs;
    }

    /**
     * Detailed constructor with default acquire timeout 10000 msecs and lock expiration of 60000 msecs.
     * @param lockKey
     */
    public ClusterJedisLock(String lockKey) {
        this(null, lockKey);
    }

    /**
     * Detailed constructor with default lock expiration of 60000 msecs.
     * @param lockKey
     * @param timeoutMsecs
     */
    public ClusterJedisLock(String lockKey, int timeoutMsecs) {
        this(null, lockKey, timeoutMsecs);
    }

    /**
     * Detailed constructor.
     * @param lockKey
     * @param timeoutMsecs
     * @param expireMsecs
     */
    public ClusterJedisLock(String lockKey, int timeoutMsecs, int expireMsecs) {
        this(null, lockKey, timeoutMsecs, expireMsecs);
    }

    /**
     * 
     * 功能描述: 获取锁
     *
     * @return String
     * date:   2018年4月11日 下午4:45:07
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
     * date:   2018年4月11日 下午4:45:38
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public synchronized boolean acquire() throws InterruptedException {
        return acquire(client);
    }

    /**
     * 
     * 功能描述: Acquire lock.
     *
     * @param client
     * @return
     * @throws InterruptedException boolean
     * date:   2018年4月11日 下午4:45:58
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public synchronized boolean acquire(RedisClusterClient client) throws InterruptedException {
        int timeout = timeoutMsecs;
        while (timeout >= 0) {
            long expires = System.currentTimeMillis() + expireMsecs + 1;
            String expiresStr = String.valueOf(expires); // 锁到期时间

            if (client.setnx(lockKey, expiresStr) == 1) {
                // lock acquired
                locked = true;
                return true;
            }

            String currentValueStr = client.get(lockKey); // redis里的时间
            if (currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
                // 判断是否为空，不为空的情况下，如果被其他线程设置了值，则第二个条件判断是过不去的
                // lock is expired

                String oldValueStr = client.getSet(lockKey, expiresStr);
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
     * 功能描述: Acqurired lock release.
     * void
     * date:   2018年4月11日 下午4:46:08
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public synchronized void release() {
        release(client);
    }

    /**
     * 
     * 功能描述:  Acqurired lock release.
     *
     * @param client void
     * date:   2018年4月11日 下午4:46:18
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public synchronized void release(RedisClusterClient client) {
        if (locked) {
            client.del(lockKey);
            locked = false;
        }
    }
}
