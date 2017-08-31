package com.doopp.gauss.api.dao;

import com.doopp.gauss.api.entity.RoomEntity;
import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.api.helper.RedisHelper;

import javax.annotation.Resource;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
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
            if (userEntity!=null) {
                redisHelper.setString(userPrefix + userEntity.getId(), "" + roomEntity.getId());
            }
        }
        // 保存房间
        redisHelper.setObject(roomPrefix + roomEntity.getId(), roomEntity);
        // 如果有空座，就解锁房间
        if (roomEntity.isFreeSeat()) {
            unlockRoom(roomEntity.getId());
        }
        // 满员就锁定房间
        else {
            lockRoom(roomEntity.getId());
        }
    }

    // 删除房间
    // 将房间号回收
    public void delete(String roomId) {
        redisHelper.delObject(roomPrefix + roomId);
        // 需要回收房间号
    }

    // 删除用户的索引
    public void delUserIdIndex(Long userId) {
        redisHelper.delString(userPrefix + userId);
    }

    // 查询用户在哪个房间
    public RoomEntity fetchByUserId(Long userId) {
        // 取值，判断空值
        String roomId = redisHelper.getString(userPrefix + userId);
        if (roomId==null) {
            return null;
        }
        // 转化为 room id
        // int roomId = (int) roomIdObject;
        //logger.info(" >>> fetchByUserId " + userId + " roomId " + roomId);
        // 获取房间
        RoomEntity roomEntity = this.fetchByRoomId(roomId);
        // 判断一下
        if (roomEntity!=null) {
            //logger.info(" >>> roomEntity.getUserList() " + roomEntity.getUserList());
            // 如果房间里能找到这个用户
            for (UserEntity userEntity : roomEntity.getUserList()) {
                //logger.info(" >>> userEntity " + userEntity);
                if (userEntity!=null && userEntity.getId().equals(userId)) {
                    // 返回房间
                    return roomEntity;
                }
            }
        }
        // 如果找不到这个用户，有错误的数据，就要删除索引
        this.delUserIdIndex(userId);
        return null;
    }

    // 按房间 id 查询房间
    public RoomEntity fetchByRoomId(String roomId) {
        // 判断空值
        Object roomEntityObject = redisHelper.getObject(roomPrefix + roomId);
        if (roomEntityObject==null) {
            return null;
        }
        return (RoomEntity) roomEntityObject;
    }

    // 查询一个空闲的房间
    public RoomEntity fetchFreeRoom() {
        List<String> freeRoomsId = this.getUnlockRooms();
        // logger.info(" >>> freeRoomsId : \n " + freeRoomsId);
        for(String roomId : freeRoomsId) {
            RoomEntity freeRoom = null;
            try {
                freeRoom = this.fetchByRoomId(roomId);
            }
            catch(NumberFormatException e) {
                redisHelper.hdel(roomPrefix, roomId);
            }
            //(RoomEntity) redisHelper.getObject("" + roomId);
            // logger.info(" >>> freeRoom : " + roomId + " \n " + freeRoom);
            if (freeRoom != null) {
                return freeRoom;
            }
            else {
                redisHelper.hdel(roomPrefix, roomId);
                // this.lockRoom(Integer.parseInt(roomId));
            }
        }
        return null;
    }

    // 检索所有的空闲的房间
    private List<String> getUnlockRooms() {
        return redisHelper.hkeys(roomPrefix);
    }

    // 在房间满员，或房间进入游戏状态后，房间不排在 free 名单中
    private void lockRoom(int roomId) {
        redisHelper.hdel(roomPrefix, "" + roomId);
    }

    // 非游戏状态，而且没有满员，房间解锁，可以进入用户
    private void unlockRoom(int roomId) {
        redisHelper.hset(roomPrefix, "" + roomId, roomPrefix + roomId);
    }
}
