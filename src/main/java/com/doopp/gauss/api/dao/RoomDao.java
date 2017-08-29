package com.doopp.gauss.api.dao;

import com.doopp.gauss.api.entity.RoomEntity;
import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.api.helper.RedisHelper;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

@Component
public class RoomDao {

    private static final Logger logger = LoggerFactory.getLogger(RoomDao.class);

    @Resource
    private RedisHelper redisHelper;

    // 房间信息保存到 Redis , key 的前缀 room_{roomId}
    private static String roomPrefix = "roomId_room_";

    // 用户 ID 做 key ，保存 user id 和 room id 的索引
    private static String userPrefix = "userId_roomId_";

    // 保存房间的实体
    public void save(RoomEntity roomEntity) {
        ArrayList<UserEntity> userList = roomEntity.getUserList();
        for(UserEntity userEntity : userList) {
            redisHelper.setObject(userPrefix + userEntity.getId(), roomEntity.getId());
        }
        redisHelper.setObject(roomPrefix + roomEntity.getId(), roomEntity);
    }

    // 查询用户在哪个房间
    public RoomEntity fetchByUserId(Long userId) {
        int roomId = (int) redisHelper.getObject(userPrefix + userId);
        return this.fetchByRoomId(roomId);
    }

    // 按房间 id 查询房间
    public RoomEntity fetchByRoomId(int roomId) {
        return (RoomEntity) redisHelper.getObject(roomPrefix + roomId);
    }

    // 查询一个空闲的房间
    public RoomEntity fetchFreeRoom() {
        int roomId = (int) redisHelper.getObject("userId_roomId_*");
        return (RoomEntity) redisHelper.getObject(roomPrefix + roomId);
    }
}
