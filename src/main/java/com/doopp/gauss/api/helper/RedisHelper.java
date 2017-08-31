package com.doopp.gauss.api.helper;

import com.doopp.gauss.api.entity.RoomEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class RedisHelper {

    private static final Logger logger = LoggerFactory.getLogger(RedisHelper.class);

    @Resource
    private ShardedJedisPool shardedJedisPool;

    private final JdkSerializationRedisSerializer jsrs = new JdkSerializationRedisSerializer();

    /*
     * String
     */
    public void setString(String key, String value) {
        ShardedJedis roomJedis = shardedJedisPool.getResource();
        roomJedis.set(key, value);
        roomJedis.close();
    }

    public String getString(String key) {
        ShardedJedis roomJedis = shardedJedisPool.getResource();
        String value = roomJedis.get(key);
        roomJedis.close();
        return value;
    }

    public void delString(String key) {
        ShardedJedis roomJedis = shardedJedisPool.getResource();
        roomJedis.del(key);
        roomJedis.close();
    }

    public void setObject(String key, Object object) {
        byte[] byteKey = key.getBytes();
        byte[] byteObject = jsrs.serialize(object);
        ShardedJedis roomJedis = shardedJedisPool.getResource();
        roomJedis.set(byteKey, byteObject);
        roomJedis.close();
    }

    public Object getObject(String key) {
        byte[] byteKey = key.getBytes();
        ShardedJedis roomJedis = shardedJedisPool.getResource();
        byte[] byteRoom = roomJedis.get(byteKey);
        roomJedis.close();
        return jsrs.deserialize(byteRoom);
    }

    public void delObject(String key) {
        byte[] byteKey = key.getBytes();
        ShardedJedis roomJedis = shardedJedisPool.getResource();
        roomJedis.del(byteKey);
        roomJedis.close();
    }

    public void hset(String key, String field, String value) {
        ShardedJedis roomJedis = shardedJedisPool.getResource();
        roomJedis.hset(key, field, value);
        roomJedis.close();
    }

    public void hdel(String key, String field) {
        ShardedJedis roomJedis = shardedJedisPool.getResource();
        roomJedis.hdel(key, field);
        roomJedis.close();
    }

    public List<String> hkeys(String key) {
        ShardedJedis roomJedis = shardedJedisPool.getResource();
        Set<String> keys = roomJedis.hkeys(key);
        roomJedis.close();
        return new ArrayList<>(keys);
    }

    public List<String> hvals(String key) {
        ShardedJedis roomJedis = shardedJedisPool.getResource();
        List<String> values = roomJedis.hvals(key);
        roomJedis.close();
        return values;
    }

    private static byte[] int2Bytes(int num) {
        byte[] byteNum = new byte[4];
        for (int ix = 0; ix < 4; ++ix) {
            int offset = 32 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }

    private static byte[] long2Bytes(long num) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }
}
