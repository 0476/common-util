package com.ailikes.util.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

/**
 * 
 * 〈Redis工具类〉<br>
 * 
 * @author chentianyu
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class RedisShardUtil {

    private static ShardedJedis jedis;

    /**
     * 
     * 功能描述: 保存Map到redis中<br>
     *
     * @param ssoPrincipal
     * @param authTimeout 对象存活时间 s
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
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
     * 功能描述: 按照Key值存入List<br>
     *
     * @autor wangkaining
     * @param listKey
     * @param listValue
     * @param authTimeout
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
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
     * 功能描述: 保存set<br>
     *
     * @autor wangkaining
     * @param setKey
     * @param setValue
     * @param authTimeout
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
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
     * 功能描述: 保存Map到redis中<br>
     *
     * @param ssoPrincipal
     * @param authTimeout 对象存活时间 s
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
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
     * @author 徐大伟
     * @date 2014年12月24日下午10:15:24
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
     * 功能描述: 按照key得打并删除<br>
     *
     * @autor wangkaining
     * @param key
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
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
     * 功能描述: 读取redis并修改保存时间<br>
     *
     * @autor wangkaining
     * @param key 主键
     * @param seconds  设置保存时间  注意传入此时间要大于0
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
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
     * @author 徐大伟
     * @date 2014年12月24日下午10:15:24
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
     * 功能描述: 按照key读取List<br>
     *
     * @autor wangkaining
     * @param key
     * @return
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
     * 功能描述: 取出set<br>
     *
     * @autor wangkaining
     * @param key
     * @return
     */
    public static Set<String> getSet(String key) {
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo("127.0.0.1", 6379));
        jedis = new ShardedJedis(shards);
        Set<String> list=jedis.smembers(key);
        jedis.disconnect();
        return list;
    }
    
    
    public static void expireByKey(String key,int seconds){
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo("127.0.0.1", 6379));
        jedis = new ShardedJedis(shards);
        jedis.expire(key, seconds);
        jedis.disconnect();
    }
    /**
	 * 
	 * 功能描述: 根据yncsid删除SsoPrincipal<br>
	 *
	 * @param yncsid
	 * @param ssoPrincipal
	 * @param authTimeout
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public static void deleteSsoPrincipal(String yncsid){
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo("127.0.0.1", 6379));
		jedis = new ShardedJedis(shards);
		jedis.del(yncsid);
		jedis.disconnect();
	}
	
	
	
	
	
	
	public static void main(String [] args){
	    RedisShardUtil.saveList("IP", "127.0.0.1", 100);
	    RedisShardUtil.saveList("IP", "127.0.0.2", 100);
	    RedisShardUtil.saveList("IP", "127.0.0.3", 100);
	    
	    List<String> ipList = RedisShardUtil.getList("IP");
	    for(String ip : ipList){
	        System.out.println("IP:"+ip);
	    }
	    
	    RedisShardUtil.saveSet("IPSet", "127.0.0.1", 100);
        RedisShardUtil.saveSet("IPSet", "127.0.0.1", 100);
        RedisShardUtil.saveSet("IPSet", "127.0.0.3", 100);
	    
        Set<String> ipSet = RedisShardUtil.getSet("IPSet");
        for(String ip : ipSet){
            System.out.println("IPSet:"+ip);
        }
	}
}
