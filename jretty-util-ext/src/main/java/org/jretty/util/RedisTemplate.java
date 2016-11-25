package org.jretty.util;

import java.util.HashSet;
import java.util.Set;

import org.jretty.log.LogFactory;
import org.jretty.log.Logger;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

public class RedisTemplate {

    private static Logger logger = LogFactory.getLogger(RedisTemplate.class);

    private JedisPoolConfig jedisPoolConfig;

    private String redisServers;

    private JedisCluster jc;

    private Set<HostAndPort> jedisClusterNodes;

    public void init() {
        if (redisServers == null) {
            throw new IllegalStateException(
                    "\n IOVPCV Redis Pool didn't find any Redis servers from config file, init Redis Pool ERROR!!!! \n");
        }
    
        logger.info(" IOVPCV Redis Pool found below servers: " + redisServers);
        jedisClusterNodes = initHostAndPorts(redisServers);
        jc = new JedisCluster(jedisClusterNodes, jedisPoolConfig);
        
        testConnection();
    }
    
    private void testConnection() {
        this.jc.exists("foo");
    }
    
    public void destroy() {
        if (jc != null) {
            try {
                this.jc.close();
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    private Set<HostAndPort> initHostAndPorts(String servers) {
        String[] host_Port_s = servers.split("[,]");
        Set<HostAndPort> hostAndPorts = new HashSet<HostAndPort>();
        for (String singleHP : host_Port_s) {
            if (singleHP.contains(":")) {
                String[] singleHPS = singleHP.split("[:]");
                String host = singleHPS[0];
                Integer port = Integer.parseInt(singleHPS[1]);
                hostAndPorts.add(new HostAndPort(host, port));
            } else {
                logger.error(" Found a illeagal Host:port String ", singleHP);
            }
        }
        return hostAndPorts;
    }

    public Set<HostAndPort> getJedisClusterNodes() {
        return jedisClusterNodes;
    }

    public JedisCluster getJedisCluster() {
        if (this.jc == null) {
            init();
        }
        return this.jc;
    }

    protected String getRedisServers() {
        return redisServers;
    }

    public void setRedisServers(String redisServers) {
        this.redisServers = redisServers;
    }

    /**
     * 模糊查询 用 * 星号匹配 返回： 匹配出来的key
     *
     * @param pattern
     */
    public Set<String> keys(String pattern) {
        long start = System.currentTimeMillis();
        Set<HostAndPort> allHosts = getJedisClusterNodes();
        Set<String> allKeysSet = new HashSet<String>();
        if (allHosts != null) {
            for (HostAndPort hp : allHosts) {
                Jedis jedis = new Jedis(hp.getHost(), hp.getPort());
                allKeysSet.addAll(jedis.keys(pattern));
                jedis.close();
            }
        }
        long end = System.currentTimeMillis();
        logger.debug("Redis keys by string global search cost time = " + (end - start) + " ms, "
                + "return result size= " + allKeysSet.size());
        return allKeysSet;
    }

    /**
     * 根据序列化之后的key 做模糊查询
     * 
     * @param patternByte
     * @return 返回匹配的结果的key
     */
    public Set<byte[]> keys(byte[] patternByte) {
        long start = System.currentTimeMillis();
        Set<HostAndPort> allHosts = getJedisClusterNodes();
        Set<byte[]> allKeysSet = new HashSet<byte[]>();
        if (allHosts != null) {
            for (HostAndPort hp : allHosts) {
                Jedis jedis = new Jedis(hp.getHost(), hp.getPort());
                allKeysSet.addAll(jedis.keys(patternByte));
                jedis.close();
            }
        }
        long end = System.currentTimeMillis();
        logger.debug("Redis keys by byte[] global search cost time = " + (end - start) + " ms, "
                + "return result size= " + allKeysSet.size());
        return allKeysSet;
    }

    protected JedisPoolConfig getJedisPoolConfig() {
        return jedisPoolConfig;
    }

    public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
        this.jedisPoolConfig = jedisPoolConfig;
    }

}