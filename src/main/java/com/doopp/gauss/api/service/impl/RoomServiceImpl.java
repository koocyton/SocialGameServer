package com.doopp.gauss.api.service.impl;

import com.doopp.gauss.api.dao.RoomDao;
import com.doopp.gauss.api.entity.RoomEntity;
import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.api.service.RoomService;
import com.doopp.gauss.api.service.MessageService;
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
@Service("roomService")
public class RoomServiceImpl implements RoomService {

    private static final Logger logger = LoggerFactory.getLogger(RoomServiceImpl.class);

    @Resource
    private RoomDao roomDao;

    @Autowired
    private MessageService messageService;

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
     * 用户当前所在的房间
     */
    @Override
    public RoomEntity userCurrentRoom(UserEntity user) {
        // 如果用户已经在一个房间里了
        return roomDao.fetchByUserId(user.getId());
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
        RoomEntity roomEntity = this.nextRoom(120);
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
            String message = "<b style=\"color:red;\">" + user.getAccount() + "</b> 信步来房间 ";
            messageService.sendStringToRoom(message, roomEntity.getId());
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
        RoomEntity lastRoomEntity = roomDao.fetchByUserId(user.getId());
        if (lastRoomEntity!=null) {
            return lastRoomEntity;
        }

        // 用户在服务端有 3 次机会，偶遇拿到的空房间刚好坐满并
        for(int ii=0; ii<3; ii++) {
            // 拿到一个空闲的房间
            RoomEntity roomEntity = roomDao.fetchFreeRoom();
            // 加入用户
            if (roomEntity != null) {
                // 如果加入到房间成功
                if (roomEntity.addUser(user)) {
                    String message = "<b style=\"color:red;\">" + user.getAccount() + "</b> 信步来房间 ";
                    messageService.sendStringToRoom(message, roomEntity.getId());
                    roomDao.save(roomEntity);
                    return roomEntity;
                }
                // 加入失败，说明房间已经满员，重新标注房间
                else {
                    roomEntity.setFreeSeat(false);
                    roomDao.save(roomEntity);
                }
            }
        }

        // 实在有问题，就让他创建一个新房间进入
        return this.userCreateRoom(user);
    }

    /*
     * 用户离开房间
     */
    @Override
    public RoomEntity userLeaveRoom(UserEntity user) {
        // 拿到一个空闲的房间
        RoomEntity roomEntity = roomDao.fetchByUserId(user.getId());
        // 删除用户
        if (roomEntity!=null && roomEntity.delUser(user.getId())) {
            // 删除用户 ID 到 room id 的索引
            roomDao.delUserIndex(user.getId());
            // 检查房间还剩多少人，如果人都撒了，就删除这个房间的持久数据
            boolean isEmptyRoom = true;
            ArrayList<UserEntity> userList = roomEntity.getUserList();
            for(UserEntity oneUser : userList) {
                // 如果有人，就不是空房间
                if (oneUser!=null) {
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
                String message = user.getAccount() + " 离开了房间 ";
                messageService.sendStringToRoom(message, roomEntity.getId());
                roomDao.save(roomEntity);
            }
            return roomEntity;
        }
        // 踢出长链接
        // sendMessageService.disconnectSocket(userId);
        // 返回这个房间
        return null;
    }
}
