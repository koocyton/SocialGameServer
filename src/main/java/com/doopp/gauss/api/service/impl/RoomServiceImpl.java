package com.doopp.gauss.api.service.impl;

import com.doopp.gauss.api.dao.RoomDao;
import com.doopp.gauss.api.entity.RoomEntity;
import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.api.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;

/**
 * 房间的管理
 *
 * Created by Henry on 2017/8/26.
 */
@Service
public class RoomServiceImpl implements RoomService {

    // private static final Logger logger = LoggerFactory.getLogger(RoomServiceImpl.class);

    @Resource
    private RoomDao roomDao;

    @Override
    public boolean joinRoom(UserEntity user, int roomId) {
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setId(roomId);
        roomEntity.addUser(user);
        roomDao.create(roomEntity);
        return true;
    }

    @Override
    public boolean leaveRoom(Long userId) {
        RoomEntity roomEntity = roomDao.fetchByUserId(userId);
        roomEntity.delUser(userId);
        roomDao.update(roomEntity);
        return true;
    }
}
