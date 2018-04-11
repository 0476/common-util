package com.ailikes.util.redis.cluster;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * redis3.0集群客户端
 */
public class RedisClusterClient {
	private String addresses; // 中间用逗号分隔例如：127.0.0.1:6379,127.0.0.2:6379
	private Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
	private GenericObjectPoolConfig poolConfig;
	private JedisCluster jc;

	/**
	 * 初始化方法，系统启动的时候调用
	 */
	public void init() {
		String[] host = addresses.split(",");
		for (String item : host) {
			String[] tmp = item.split(":");
			jedisClusterNodes.add(new HostAndPort(tmp[0], Integer
					.parseInt(tmp[1])));
		}
		if(poolConfig == null){
			poolConfig = new GenericObjectPoolConfig();
			poolConfig.setMaxTotal(128);
			poolConfig.setMinIdle(8);
			poolConfig.setMaxIdle(16);
		}
		jc = new JedisCluster(jedisClusterNodes, poolConfig);
	}

	public Long srem(String key, String... members) {
		return jc.srem(key, members);
	}

	public Set<String> smembers(String key) {
		return jc.smembers(key);
	}

	public Long sadd(String key, String... members) {
		return jc.sadd(key, members);
	}

	public String get(String key) {
		return jc.get(key);
	}

	public String set(String key, String value) {
		return jc.set(key, value);
	}

	public String setex(String key, int seconds, String value) {
		return jc.setex(key, seconds, value);
	}

	public Long setnx(String key, String value) {
		return jc.setnx(key, value);
	}

	public String getSet(String key, String value) {
		return jc.getSet(key, value);
	}

	public Long del(String key) {
		return jc.del(key);
	}

	public Long expire(String key, int seconds) {
		return jc.expire(key, seconds);
	}
	
	public Long zadd(String key, String member){
		return jc.zadd(key, 1, member);
	}
	
	public Long zadd(String key, double score, String member){
		return jc.zadd(key, score, member);
	}
	
	public Set<String> zrange(String key, long start, long end){
		return jc.zrange(key, start, end);
	}
	
	public Set<String> zrevrange(String key, long start, long end){
		return jc.zrevrange(key, start, end);
	}

	public String getAddresses() {
		return addresses;
	}

	public void setAddresses(String addresses) {
		this.addresses = addresses;
	}
	
	public GenericObjectPoolConfig getPoolConfig() {
		return poolConfig;
	}

	public void setPoolConfig(GenericObjectPoolConfig poolConfig) {
		this.poolConfig = poolConfig;
	}

}
