package com.ailikes.util.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * 
 * 功能描述: redis链接管理对象，单例使用，放在初始化对象里
 * 
 * date:   2018年4月11日 下午5:00:03
 * @author: ailikes
 * @version: 1.0.0
 * @since: 1.0.0
 */
public class RedisShardClient {
    private String               addresses;                               // 中间用逗号分隔例如：127.0.0.1:6379,127.0.0.2:6379
    private List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
    ShardedJedisPool             pool;

    /**
     * 初始化方法，系统启动的时候调用
     */
    public void init() {
        String[] host = addresses.split(",");
        for (String item : host) {
            String[] tmp = item.split(":");
            shards.add(new JedisShardInfo(tmp[0], Integer.parseInt(tmp[1])));
        }
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMinIdle(8);
        config.setMaxIdle(100);
        config.setMaxTotal(10000);
        config.setMaxWaitMillis(3000);
        config.setTestOnBorrow(true);
        config.setTestOnCreate(true);
        pool = new ShardedJedisPool(config, shards);
    }

    public void destory() {
        pool.destroy();
    }

    public Long srem(String key,
                     String... members) {
        ShardedJedis resource = null;
        try {
            resource = pool.getResource();
            return resource.srem(key, members);
        } finally {
            pool.returnResource(resource);
        }
    }

    public Set<String> smembers(String key) {
        ShardedJedis resource = null;
        try {
            resource = pool.getResource();
            return resource.smembers(key);
        } finally {
            pool.returnResource(resource);
        }
    }

    public Long sadd(String key,
                     String... members) {
        ShardedJedis resource = null;
        try {
            resource = pool.getResource();
            return resource.sadd(key, members);
        } finally {
            pool.returnResource(resource);
        }
    }

    public String get(String key) {
        ShardedJedis resource = null;
        try {
            resource = pool.getResource();
            return resource.get(key);
        } finally {
            pool.returnResource(resource);
        }
    }

    public String set(String key,
                      String value) {
        ShardedJedis resource = null;
        try {
            resource = pool.getResource();
            return resource.set(key, value);
        } finally {
            pool.returnResource(resource);
        }
    }

    public String setex(String key,
                        int seconds,
                        String value) {
        ShardedJedis resource = null;
        try {
            resource = pool.getResource();
            return resource.setex(key, seconds, value);
        } finally {
            pool.returnResource(resource);
        }
    }

    public Long setnx(String key,
                      String value) {
        ShardedJedis resource = null;
        try {
            resource = pool.getResource();
            return resource.setnx(key, value);
        } finally {
            pool.returnResource(resource);
        }
    }

    public String getSet(String key,
                         String value) {
        ShardedJedis resource = null;
        try {
            resource = pool.getResource();
            return resource.getSet(key, value);
        } finally {
            pool.returnResource(resource);
        }
    }

    public Long del(String key) {
        ShardedJedis resource = null;
        try {
            resource = pool.getResource();
            return resource.del(key);
        } finally {
            pool.returnResource(resource);
        }
    }

    public Long expire(String key,
                       int seconds) {
        ShardedJedis resource = null;
        try {
            resource = pool.getResource();
            return resource.expire(key, seconds);
        } finally {
            pool.returnResource(resource);
        }
    }
    
	public Long zadd(String key, String member){
		ShardedJedis resource = null;
        try {
            resource = pool.getResource();
            return resource.zadd(key, 1, member);
        } finally {
            pool.returnResource(resource);
        }
	}
	
	public Long zadd(String key, double score, String member){
		ShardedJedis resource = null;
		try {
            resource = pool.getResource();
            return resource.zadd(key, score, member);
        } finally {
            pool.returnResource(resource);
        }
	}
	
	public Set<String> zrange(String key, long start, long end){
		ShardedJedis resource = null;
		try {
            resource = pool.getResource();
            return resource.zrange(key, start, end);
        } finally {
            pool.returnResource(resource);
        }
	}
	
	public Set<String> zrevrange(String key, long start, long end){
		ShardedJedis resource = null;
		try {
            resource = pool.getResource();
            return resource.zrevrange(key, start, end);
        } finally {
            pool.returnResource(resource);
        }
	}

    public String getAddresses() {
        return addresses;
    }

    public void setAddresses(String addresses) {
        this.addresses = addresses;
    }

}
