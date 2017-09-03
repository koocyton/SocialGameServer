package com.doopp.gauss.api.dao;

import com.doopp.gauss.api.entity.RoomEntity;
import com.doopp.gauss.api.entity.UserEntity;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Repository
public class RoomDao {

    private static final Logger logger = LoggerFactory.getLogger(RoomDao.class);

    @Resource
    private ShardedJedisPool roomJedis;

    @Resource
    private ShardedJedisPool roomIndexJedis;

    private final JdkSerializationRedisSerializer jsrs = new JdkSerializationRedisSerializer();

    // 房间信息保存到 Redis , key 的前缀 room_{roomId}
    private final String roomPrefix = "roomId_room_";

    // 用户 ID 做 key ，保存 user id 和 room id 的索引
    private final String userPrefix = "userId_roomId_";

    // 保存房间的实体
    public void save(RoomEntity roomEntity) {
        /* 删除旧的 用户ID 对 房间ID 的索引 */
        /*
        // 取出这个房间
        RoomEntity oRoomEntity = this.getRoom(roomEntity.getId());
        // 如果之前房间存在
        if (oRoomEntity!=null) {
            ArrayList<UserEntity> oUserList = oRoomEntity.getUserList();
            // 如果之前不是空房间，将 user id => room id 的索引删除
            for (UserEntity oUserEntity : oUserList) {
                if (oUserEntity != null) {
                    this.delUserIndex(oUserEntity.getId());
                }
            }
        }
        */

        /* 建立新的 用户ID 对房间ID 的索引 */
        // 保存用户到房间的索引
        ArrayList<UserEntity> userList = roomEntity.getUserList();
        for(UserEntity userEntity : userList) {
            if (userEntity!=null) {
                this.setUserIndex(userEntity.getId(), roomEntity.getId());
            }
        }

        /* 保存房间 */
        this.setRoom(roomEntity.getId(), roomEntity);

        /* 设定空闲房间的索引 */
        // 如果有空座，设定空闲房间索引到此房间
        if (roomEntity.isFreeSeat()) {
            this.setFreeRoomIndex(roomEntity.getId());
        }
        // 满员就删除指向这个房间的空闲房间索引
        else {
            this.delFreeRoomIndex(roomEntity.getId());
        }
    }

    // 删除房间
    // 将房间号回收
    public void delete(int roomId) {
        // 删除房间
        this.delRoom(roomId);
        // free room 的索引也要删除
        this.delFreeRoomIndex(roomId);
    }

    // 查询用户在哪个房间
    public RoomEntity fetchByUserId(Long userId) {
        // 获取房间
        RoomEntity roomEntity = this.getRoomByUserId(userId);
        // 判断一下
        if (roomEntity!=null) {
            // 如果房间里能找到这个用户
            for (UserEntity userEntity : roomEntity.getUserList()) {
                if (userEntity!=null && userEntity.getId().equals(userId)) {
                    // 返回房间
                    return roomEntity;
                }
            }
        }
        // 如果找不到这个用户，有错误的数据，就要删除索引
        this.delUserIndex(userId);
        return null;
    }

    // 按房间 id 查询房间
    public RoomEntity fetchByRoomId(int roomId) {
        // 判断空值
        RoomEntity roomEntity = getRoom(roomId);
        if (roomEntity==null) {
            return null;
        }
        return roomEntity;
    }

    // 查询一个空闲的房间
    public RoomEntity fetchFreeRoom() {
        return this.getFirstFreeRoom();
    }


    /*
     * 数据的读写
     */

    // set room info
    private void setRoom(int roomId, RoomEntity roomEntity) {
        byte[] byteRoomKey = (roomPrefix + roomId).getBytes();
        byte[] byteRoom = jsrs.serialize(roomEntity);
        ShardedJedis shardedJedis = roomJedis.getResource();
        shardedJedis.set(byteRoomKey, byteRoom);
        shardedJedis.close();
    }

    // get room info
    private RoomEntity getRoom(int roomId) {
        ShardedJedis shardedJedis = roomJedis.getResource();
        byte[] byteRoomKey = (roomPrefix + roomId).getBytes();
        byte[] byteRoom = shardedJedis.get(byteRoomKey);
        shardedJedis.close();
        Object roomObject = jsrs.deserialize(byteRoom);
        if (roomObject!=null) {
            return (RoomEntity) roomObject;
        }
        return null;
    }

    // del room info
    private void delRoom(int... roomsId) {
        ShardedJedis shardedJedis = roomJedis.getResource();
        for (int roomId : roomsId) {
            byte[] byteRoomKey = (roomPrefix + roomId).getBytes();
            shardedJedis.del(byteRoomKey);
        }
        shardedJedis.close();
    }

    // set room index
    private void setUserIndex(Long userId, int roomId) {
        ShardedJedis shardedJedis = roomIndexJedis.getResource();
        shardedJedis.set(userPrefix + userId, "" + roomId);
        shardedJedis.close();
    }

    // get room index
    private RoomEntity getRoomByUserId(Long userId) {
        ShardedJedis shardedJedis = roomIndexJedis.getResource();
        String sRoomId = shardedJedis.get(userPrefix + userId);
        shardedJedis.close();
        if (sRoomId==null) {
            return null;
        }
        return this.getRoom(Integer.parseInt(sRoomId));
    }

    // del room index
    public void delUserIndex(Long userId) {
        ShardedJedis shardedJedis = roomIndexJedis.getResource();
        shardedJedis.del(userPrefix + userId);
        shardedJedis.close();
    }



    // 设置空闲房间
    private void setFreeRoomIndex(int roomId) {
        ShardedJedis shardedJedis = roomIndexJedis.getResource();
        shardedJedis.hset(roomPrefix + "*", "" + roomId, "");
        shardedJedis.close();
    }

    // 随机取得一个空闲房间
    private RoomEntity getRandomFreeRoom() {
        List<String> roomsId = getFreeRoomsId();
        String roomId = roomsId.remove(new Random().nextInt(roomsId.size()));
        return getRoom(Integer.parseInt(roomId));
    }

    // 取得最后一个空闲房间
    private RoomEntity getLastFreeRoom() {
        List<String> roomsId = getFreeRoomsId();
        int size = roomsId.size();
        if (size==0) {
            return null;
        }
        return getRoom(Integer.parseInt(roomsId.get(size-1)));
    }

    // 取得第一个空闲房间
    private RoomEntity getFirstFreeRoom() {
        List<String> roomsId = getFreeRoomsId();
        int size = roomsId.size();
        if (size==0) {
            return null;
        }
        return getRoom(Integer.parseInt(roomsId.get(0)));
    }

    // 取得空闲房间的 id 列表
    private List<String> getFreeRoomsId() {
        ShardedJedis shardedJedis = roomIndexJedis.getResource();
        Set<String> keys = shardedJedis.hkeys(roomPrefix + "*");
        shardedJedis.close();
        return new ArrayList<>(keys);
    }

    // 删除空闲的房间的索引
    private void delFreeRoomIndex(int roomId) {
        ShardedJedis shardedJedis = roomIndexJedis.getResource();
        shardedJedis.hdel(roomPrefix + "*", "" + roomId);
        shardedJedis.close();
    }
}
