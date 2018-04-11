package com.ailikes.util.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

/**
 * 
 * 功能描述: Redis工具类
 * 
 * date:   2018年4月11日 下午4:51:09
 * @author: ailikes
 * @version: 1.0.0
 * @since: 1.0.0
 */
public class RedisShardUtil {

    private static ShardedJedis jedis;

    /**
     * 
     * 功能描述: 保存Map到redis中
     *
     * @param yncsid
     * @param map
     * @param authTimeout void
     * date:   2018年4月11日 下午4:51:19
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public static void saveMap(String yncsid, Map<String, String> map, int authTimeout) {
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo("127.0.0.1", 6379));
        jedis = new ShardedJedis(shards);
        jedis.hmset(yncsid, map);
        jedis.expire(yncsid, authTimeout);
        jedis.disconnect();
    }
    /**
     * 
     * 功能描述: 按照Key值存入List
     *
     * @param listKey
     * @param listValue
     * @param authTimeout void
     * date:   2018年4月11日 下午4:51:29
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public static void saveList(String listKey, String listValue, int authTimeout) {
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo("127.0.0.1", 6379));
        jedis = new ShardedJedis(shards);
        jedis.lpush(listKey, listValue);
        jedis.expire(listKey, authTimeout);
        jedis.disconnect();
    }
    
    /**
     * 
     * 功能描述: 保存set
     *
     * @param setKey
     * @param setValue
     * @param authTimeout void
     * date:   2018年4月11日 下午4:51:39
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public static void saveSet(String setKey, String setValue, int authTimeout) {
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo("127.0.0.1", 6379));
        jedis = new ShardedJedis(shards);
        jedis.sadd(setKey, setValue);
        jedis.expire(setKey, authTimeout);
        jedis.disconnect();
    }
    
    /**
     * 
     * 功能描述: 保存Map到redis中
     *
     * @param key
     * @param value
     * @param authTimeout void
     * date:   2018年4月11日 下午4:51:48
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public static void saveData(String key, String value, int authTimeout) {
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo("127.0.0.1", 6379));
        jedis = new ShardedJedis(shards);
        jedis.set(key, value);
        jedis.expire(key, authTimeout);
        jedis.disconnect();
    }

    /**
     * 
     * 功能描述: 读取redis数据
     *
     * @param key
     * @return String
     * date:   2018年4月11日 下午4:51:57
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public static String getData(String key) {
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo("127.0.0.1", 6379));
        jedis = new ShardedJedis(shards);
        String json=jedis.get(key);
        jedis.disconnect();
        return json;
    }
    /**
     * 
     * 功能描述:按照key获取数据并删除 
     *
     * @param key
     * @return String
     * date:   2018年4月11日 下午4:52:05
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public static String getDelData(String key) {
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo("127.0.0.1", 6379));
        jedis = new ShardedJedis(shards);
        String json=jedis.getSet(key, "");
        if (null != json && !"".equals(json)) {
            jedis.del(key);
        }
        jedis.disconnect();
        return json;
    }
    /**
     * 
     * 功能描述: 读取redis并修改保存时间
     *
     * @param key
     * @param seconds
     * @return String
     * date:   2018年4月11日 下午4:52:25
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public static String getData(String key,int seconds) {
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo("127.0.0.1", 6379));
        jedis = new ShardedJedis(shards);
        String json=jedis.get(key);
        jedis.expire(key, seconds);
        jedis.disconnect();
        return json;
    }
    /**
     * 
     * 功能描述: 读取redis数据
     *
     * @param key
     * @return Map
     * date:   2018年4月11日 下午4:52:34
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public static Map<String,String> getMap(String key) {
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo("127.0.0.1", 6379));
        jedis = new ShardedJedis(shards);
        Map<String,String> map=jedis.hgetAll(key);
        jedis.disconnect();
        return map;
    }
    
    /**
     * 
     * 功能描述: 按照key读取List
     *
     * @param key
     * @return List
     * date:   2018年4月11日 下午4:52:42
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public static List<String> getList(String key) {
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo("127.0.0.1", 6379));
        jedis = new ShardedJedis(shards);
        List<String> list=jedis.lrange(key, 0, -1);
        jedis.disconnect();
        return list;
    }
    
    /**
     * 
     * 功能描述: 取出set
     *
     * @param key
     * @return Set
     * date:   2018年4月11日 下午4:52:54
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public static Set<String> getSet(String key) {
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo("127.0.0.1", 6379));
        jedis = new ShardedJedis(shards);
        Set<String> list=jedis.smembers(key);
        jedis.disconnect();
        return list;
    }
    
    /**
     * 
     * 功能描述: 按照KEY设置缓存时间
     *
     * @param key
     * @param seconds void
     * date:   2018年4月11日 下午4:53:30
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public static void expireByKey(String key,int seconds){
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo("127.0.0.1", 6379));
        jedis = new ShardedJedis(shards);
        jedis.expire(key, seconds);
        jedis.disconnect();
    }
	
}
