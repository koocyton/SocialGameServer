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

    public void save(RoomEntity roomEntity) {
        redisHelper.setObject(roomEntity.getId(), roomEntity);
    }

    public RoomEntity fetchByUserId(Long userId) {
        int roomId = (int) redisHelper.getObject(userId);
        return this.fetchByRoomId(roomId);
    }

    public RoomEntity fetchByRoomId(int roomId) {
        return (RoomEntity) redisHelper.getObject(roomId);
    }

    /*
     * 将用户加入房间
     */
    public void addUser(int roomId, UserEntity userEntity) {
        // 取得数据
        RoomEntity roomEntity = this.fetchByRoomId(roomId);
        ArrayList<UserEntity> userList = roomEntity.getUserList();
        // 有多少个人在房间
        int size = userList.size();
        // 如果一个用户也没有
        if (size==0) {
            userList.add(0, userEntity);
        }
        else {
            // 如果有空位，从前面开始插入空位
            for (int ii = 0; ii < size; ii++) {
                if (userList.get(ii) == null) {
                    userList.add(ii, userEntity);
                }
            }
        }
        // 现在有多少个人在房间
        int newSize = userList.size();
        // 把人加在最后
        if (size==newSize) {
            userList.add(size, userEntity);
        }
        roomEntity.setUserList(userList);
        this.save(roomEntity);
        redisHelper.setObject(userEntity.getId(), roomId);
    }

    /*
     * 将用户退出房间
     */
    public void delUser(Long userId) {
        // 取得数据
        RoomEntity roomEntity = this.fetchByUserId(userId);
        ArrayList<UserEntity> userList = roomEntity.getUserList();
        // 有多少个人在房间
        int size = userList.size();
        // 如果没有人在房间，直接返回
        if (size==0) {
            return;
        }
        // 开始遍历座位
        for(int ii=0; ii<size; ii++) {
            // 检查座位是不是这个人的
            if (userList.get(ii)!=null && userList.get(ii).getId().equals(userId)) {
                userList.remove(ii);
                userList.set(ii, null);
            }
        }
        roomEntity.setUserList(userList);
        this.save(roomEntity);
        redisHelper.delObject(userId);
    }
}
