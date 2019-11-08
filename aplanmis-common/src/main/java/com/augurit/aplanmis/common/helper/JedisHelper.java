package com.augurit.aplanmis.common.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * @author tiantian
 * @date 2018/12/24
 */
@Component
@Slf4j
public class JedisHelper {
    
    private String host;

    private int port;

    public Jedis getJedis(){
        return  new Jedis(host,port);
    }

    public String getHost() {
        return host;
    }

    @Value("${spring.redis.host:119.23.247.255}")
    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    @Value("${spring.redis.port:6379}")
    public void setPort(int port) {
        this.port = port;
    }

    public String setex(final String key, final int seconds, final String value) {
        Jedis jedis = null;
        try{

            jedis = getJedis();

            return jedis.setex(key,seconds,value);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            throw e;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public String set(final String key, final String value) {
        Jedis jedis = null;
        try{
            jedis = getJedis();

            return jedis.set(key,value);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            throw e;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public void del(final String key) {
        Jedis jedis = null;
        try{
            jedis = getJedis();

            jedis.del(key);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            throw e;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public String get(final String key) {
        Jedis jedis = null;
        try{
            jedis = getJedis();
            return jedis.get(key);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            throw e;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }
}
