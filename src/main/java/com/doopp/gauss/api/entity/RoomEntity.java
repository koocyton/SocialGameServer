package com.doopp.gauss.api.entity;

import com.doopp.gauss.api.service.impl.RoomServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 房间的实体
 *
 * Created by Henry on 2017/8/26.
 */
public class RoomEntity implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(RoomServiceImpl.class);

    private int id;

    private int seatCount;

    private String name;

    private Long roomOwner;

    private ArrayList<UserEntity> userList = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRoomOwner() {
        return roomOwner;
    }

    public void setRoomOwner(Long roomOwner) {
        this.roomOwner = roomOwner;
    }

    public ArrayList<UserEntity> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<UserEntity> userList) {
        this.userList = userList;
    }

    /*
     * 将用户加入房间
     */
    public boolean addUser(UserEntity userEntity) {
        // 拿到房间的人
        ArrayList<UserEntity> userList = this.getUserList();
        // 坐过的，有几个座位
        int usedSeatCount = userList.size();
        //
        for(int ii=0; ii<usedSeatCount; ii++) {
            if (userList.get(ii)==null) {
                userList.set(ii, userEntity);
                this.setUserList(userList);
                return true;
            }
        }
        // 没找到空位，在最后一个位置插入用户
        if (usedSeatCount<=seatCount) {
            userList.set(usedSeatCount, userEntity);
            return true;
        }
        return false;
    }

    /*
     * 将用户退出房间
     */
    public boolean delUser(Long userId) {
        /*
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
        */
        return true;
    }
}
