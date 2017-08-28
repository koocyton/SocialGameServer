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

    //@Autowired
    //private EhCacheCacheManager springCacheManager;

    // @Resource
    // private ShardedJedisPool shardedJedisPool;

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

    /*@Resource
    private EhCacheCacheManager ehCacheCacheManager;

    private static final Logger logger = LoggerFactory.getLogger(RoomServiceImpl.class);

    private Cache roomsCache;

    @Autowired
    public RoomServiceImpl() {
        roomsCache = ehCacheCacheManager.getCache("room-cache");
    }

    @Override
    public boolean joinRoom(UserEntity user, int roomId) {
        if (roomId==0) {
            RoomEntity room = this.createRoom();
            room.addUser(user);
        }
        RoomEntity room = getRoomInfo(roomId);
        if (room==null) {
            return false;
        }
        roomsCache.put(room.getId(), room);
        return true;
    }

    private RoomEntity createRoom() {
        RoomEntity room = new RoomEntity();
        int roomId = 123;
        room.setId(roomId);
        // room.setCreateAt(System.currentTimeMillis() / 1000);
        return room;
    }

    private RoomEntity getRoomInfo(int roomId) {
        Cache roomCache = ehCacheCacheManager.getCache("room-cache");
        return (RoomEntity) roomCache.get(roomId);
    }
    */
}
