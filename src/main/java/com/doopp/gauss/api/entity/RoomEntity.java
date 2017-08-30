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

    private Long roomOwnerId;

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
        // 先摆上空凳子
        for(int ii=0; ii<seatCount; ii++) {
            this.userList.add(null);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRoomOwnerId() {
        return roomOwnerId;
    }

    public void setRoomOwnerId(Long roomOwnerId) {
        this.roomOwnerId = roomOwnerId;
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
        // 1. 按顺序拿一个空座位
        // 2. 要查询是不是重复在这个房间内加入同一个用户
        int mm=-1;
        for(int ii=0; ii<seatCount; ii++) {
            // 如果用户已经在房间里
            if (userList.get(ii)!=null && userList.get(ii).getId().equals(userEntity.getId())) {
                return true;
            }
            // 如果是空房间
            if (userList.get(ii)==null && mm==-1) {
                mm = ii;
            }
        }
        // 如果找到空位
        if (mm!=-1) {
            userList.set(mm, userEntity);
            if (roomOwnerId==null) {
                roomOwnerId = userEntity.getId();
            }
            return true;
        }
        // 位置满了
        return false;
    }

    /*
     * 将用户退出房间
     */
    public boolean delUser(Long userId) {
        // 拿到房间的人
        ArrayList<UserEntity> userList = this.getUserList();
        // 遍历
        for(int ii=0; ii<seatCount; ii++) {
            // 如果不是空的座位，并且 ID 是这个用户就删除这个用户
            if (userList.get(ii)!=null && userList.get(ii).getId().equals(userId)) {
                // 删除用户
                userList.set(ii, null);
                this.setUserList(userList);
                // 判断是不是房主
                if (userId.equals(this.getRoomOwnerId())) {
                    // 如果是房主，先置空房主
                    this.setRoomOwnerId(null);
                    // 重新找一个房主出来
                    for(int nn=0; nn<seatCount; nn++) {
                        if (userList.get(nn)!=null) {
                            this.setRoomOwnerId(userList.get(nn).getId());
                        }
                    }
                }
                // 返回
                return true;
            }
        }
        return false;
    }
}
