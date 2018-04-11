package com.ailikes.util.redis;

import java.util.Set;

import redis.clients.jedis.ShardedJedis;

public class SentinelShardClient {
	
   private SentinelShardPool redisPool;

    public void destory() {
        redisPool.destroy();
    }

    public Long srem(String key,
                     String... members) {
        ShardedJedis resource = null;
        try {
            resource = redisPool.getResource();
            return resource.srem(key, members);
        } finally {
            redisPool.returnResource(resource);
        }
    }

    public Set<String> smembers(String key) {
        ShardedJedis resource = null;
        try {
            resource = redisPool.getResource();
            return resource.smembers(key);
        } finally {
            redisPool.returnResource(resource);
        }
    }

    public Long sadd(String key,
                     String... members) {
        ShardedJedis resource = null;
        try {
            resource = redisPool.getResource();
            return resource.sadd(key, members);
        } finally {
            redisPool.returnResource(resource);
        }
    }

    public String get(String key) {
        ShardedJedis resource = null;
        try {
            resource = redisPool.getResource();
            return resource.get(key);
        } finally {
            redisPool.returnResource(resource);
        }
    }

    public String set(String key,
                      String value) {
        ShardedJedis resource = null;
        try {
            resource = redisPool.getResource();
            return resource.set(key, value);
        } finally {
            redisPool.returnResource(resource);
        }
    }

    public String setex(String key,
                        int seconds,
                        String value) {
        ShardedJedis resource = null;
        try {
            resource = redisPool.getResource();
            return resource.setex(key, seconds, value);
        } finally {
            redisPool.returnResource(resource);
        }
    }

    public Long setnx(String key,
                      String value) {
        ShardedJedis resource = null;
        try {
            resource = redisPool.getResource();
            return resource.setnx(key, value);
        } finally {
            redisPool.returnResource(resource);
        }
    }

    public String getSet(String key,
                         String value) {
        ShardedJedis resource = null;
        try {
            resource = redisPool.getResource();
            return resource.getSet(key, value);
        } finally {
            redisPool.returnResource(resource);
        }
    }

    public Long del(String key) {
        ShardedJedis resource = null;
        try {
            resource = redisPool.getResource();
            return resource.del(key);
        } finally {
            redisPool.returnResource(resource);
        }
    }

    public Long expire(String key,
                       int seconds) {
        ShardedJedis resource = null;
        try {
            resource = redisPool.getResource();
            return resource.expire(key, seconds);
        } finally {
            redisPool.returnResource(resource);
        }
    }

	public SentinelShardPool getredisPool() {
		return redisPool;
	}

	public void setredisPool(SentinelShardPool redisPool) {
		this.redisPool = redisPool;
	}

}
