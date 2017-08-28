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

    // private final ShardedJedis roomJedis = shardedJedisPool.getResource();

    public void create(RoomEntity roomEntity) {

        // RoomEntity roomEntity = this.newRoom();


        logger.info(" >>> shardedJedisPool " + shardedJedisPool);
        ShardedJedis roomJedis = shardedJedisPool.getResource();
        // RoomEntity roomEntity = new RoomEntity();
        //roomEntity.setId(999);
        //roomEntity.addUser(userEntity);
        //logger.info(" >>> roomEntity " + roomEntity.getUserList());
        JdkSerializationRedisSerializer jsrs = new JdkSerializationRedisSerializer();
        byte[] room = jsrs.serialize(roomEntity);
        roomJedis.set("key1".getBytes(), room);

        byte[] r2 = roomJedis.get("key1".getBytes());
        logger.info(" >>> r2 " + r2);
        // byte[] r3 = r2.getBytes();
        // logger.info(" >>> r3 " + r3);
        RoomEntity r4 = (RoomEntity) jsrs.deserialize(r2);
        logger.info(" >>> r4 " + r4.getId());
        logger.info(" >>> r4 " + r4.getUserList());
    }

    private RoomEntity newRoom() {
        return new RoomEntity();
    }
}
