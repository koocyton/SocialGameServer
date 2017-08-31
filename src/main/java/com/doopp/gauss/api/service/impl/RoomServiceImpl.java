package com.doopp.gauss.api.service.impl;

import com.doopp.gauss.api.dao.RoomDao;
import com.doopp.gauss.api.entity.RoomEntity;
import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.api.service.RoomService;
import com.doopp.gauss.api.service.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * 房间的管理
 *
 * Created by Henry on 2017/8/26.
 */
@Service
public class RoomServiceImpl implements RoomService {

    private static final Logger logger = LoggerFactory.getLogger(RoomServiceImpl.class);

    @Resource
    private RoomDao roomDao;

    @Autowired
    private WebSocketService webSocketService;

    // 新开房间最小编号
    private int minRoomNumber = 1000;

    // 初始化一个房间
    private RoomEntity nextRoom(int seatCount) {
        // 创建房间数据
        RoomEntity roomEntity = new RoomEntity();
        // 座位数，即房间最多多少个人
        roomEntity.setSeatCount(seatCount);
        synchronized (RoomServiceImpl.class) {
            // 设定房间编号
            roomEntity.setId(minRoomNumber++);
        }
        // 设定房主
        return roomEntity;
    }

    /*
     * 用户创建房间
     */
    @Override
    public RoomEntity userCreateRoom(UserEntity user) {
        // 如果用户已经在一个房间里了
        RoomEntity lastRoomEntity = roomDao.fetchByUserId(user.getId());
        if (lastRoomEntity!=null) {
            return lastRoomEntity;
        }
        // 创建房间的座位的数量
        RoomEntity roomEntity = this.nextRoom(12);
        // 添加一个用户
        roomEntity.addUser(user);
        // 将房间保存
        roomDao.save(roomEntity);
        // 返回这个房间
        return roomEntity;
    }

    /*
     * 用户加入指定的房间
     */
    @Override
    public RoomEntity userJoinRoom(UserEntity user, int roomId) {
        // 如果用户已经在一个房间里了
        RoomEntity lastRoomEntity = roomDao.fetchByUserId(user.getId());
        if (lastRoomEntity!=null) {
            return lastRoomEntity;
        }
        // 拿到房间
        RoomEntity roomEntity = roomDao.fetchByRoomId(roomId);
        // 加入用户
        if (roomEntity!=null && roomEntity.addUser(user)) {
            roomDao.save(roomEntity);
            return roomEntity;
        }
        // 返回这个房间
        return null;
    }

    /*
     * 用户随机加入一个空闲的房间
     */
    @Override
    public RoomEntity userJoinFreeRoom(UserEntity user) {
        // 如果用户已经在一个房间里了
        /* RoomEntity lastRoomEntity = roomDao.fetchByUserId(user.getId());
        if (lastRoomEntity!=null) {
            return lastRoomEntity;
        } */
        // 拿到一个空闲的房间
        RoomEntity roomEntity = roomDao.fetchFreeRoom();
        logger.info( " >>> " + roomEntity);
        // 加入用户
        if (roomEntity!=null && roomEntity.addUser(user)) {
            roomDao.save(roomEntity);
            return roomEntity;
        }
        // 返回这个房间
        return null;
    }

    /*
     * 用户离开房间
     */
    @Override
    public RoomEntity userLeaveRoom(Long userId) {
        // 拿到一个空闲的房间
        RoomEntity roomEntity = roomDao.fetchByUserId(userId);
        // 删除用户
        if (roomEntity!=null && roomEntity.delUser(userId)) {
            // 删除用户 ID 到 room id 的索引
            roomDao.delUserIdIndex(userId);
            // 检查房间还剩多少人，如果人都撒了，就删除这个房间的持久数据
            boolean isEmptyRoom = true;
            ArrayList<UserEntity> userList = roomEntity.getUserList();
            for(UserEntity user : userList) {
                // 如果有人，就不是空房间
                if (user!=null) {
                    isEmptyRoom = false;
                    break;
                }
            }
            // 空房间，就删除这个房间的持久数据
            if (isEmptyRoom) {
                roomDao.delete(roomEntity.getId());
            }
            // 删除了一个人后，将房间数据，持久化
            else {
                roomDao.save(roomEntity);
            }
            return roomEntity;
        }
        // 踢出长链接
        // webSocketService.disconnectSocket(userId);
        // 返回这个房间
        return null;
    }
}
