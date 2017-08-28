package com.doopp.gauss.api.dao;

import com.doopp.gauss.api.entity.RoomEntity;
import com.doopp.gauss.api.entity.UserEntity;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RoomDao {

    private static final Logger logger = LoggerFactory.getLogger(RoomDao.class);

    @Resource
    private ShardedJedisPool shardedJedisPool;

    private final JdkSerializationRedisSerializer jsrs = new JdkSerializationRedisSerializer();

    public void create(RoomEntity roomEntity) {
        this.setCacheObject(roomEntity.getId(), roomEntity);
    }

    public void update(RoomEntity roomEntity) {
        this.setCacheObject(roomEntity.getId(), roomEntity);
    }

    public RoomEntity fetchByRoomId(int roomId) {
        return this.getCacheObject(roomId);
    }

    public RoomEntity fetchByUserId(Long userId) {
        int roomId = this.getCacheObject(userId);
        return this.getCacheObject(roomId);
    }

    private RoomEntity getCacheObject(int roomId) {
        String cacheKey = String.valueOf(roomId);
        return getCacheObject(cacheKey);
    }

    private RoomEntity getCacheObject(Long userId) {
        String cacheKey = String.valueOf(userId);
        return getCacheObject(cacheKey);
    }

    private void setCacheObject(int roomId, RoomEntity roomEntity) {
        byte[] room = jsrs.serialize(roomEntity);
        byte[] key = String.valueOf(roomId).getBytes();
        ShardedJedis roomJedis = shardedJedisPool.getResource();
        roomJedis.set(key, room);
        roomJedis.close();
    }

    private RoomEntity getCacheObject(String cacheKey) {
        byte[] key = cacheKey.getBytes();
        ShardedJedis roomJedis = shardedJedisPool.getResource();
        byte[] room = roomJedis.get(key);
        roomJedis.close();
        return (RoomEntity) jsrs.deserialize(room);
    }
}
