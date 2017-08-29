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

    private ArrayList<UserEntity> userList = new ArrayList<UserEntity>() {{
        add(null);
    }};

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
        // 拿到房间的人
        ArrayList<UserEntity> userList = this.getUserList();
        // 坐过的，有几个座位
        int usedSeatCount = userList.size();
        // 如果是空的座位，就坐下
        for(int ii=0; ii<usedSeatCount; ii++) {
            if (userList.get(ii)==null) {
                userList.set(ii, userEntity);
                this.setUserList(userList);

                // 如果没有房主，设定他为房主
                if (this.getRoomOwnerId()==null) {
                    this.setRoomOwnerId(userEntity.getId());
                }
                return true;
            }
        }
        // 没找到空位，在最后加一个凳子坐下
        if (usedSeatCount<=seatCount) {
            userList.set(usedSeatCount, userEntity);
            this.setUserList(userList);

            // 如果没有房主，设定他为房主
            if (this.getRoomOwnerId()==null) {
                this.setRoomOwnerId(userEntity.getId());
            }
            return true;
        }
        return false;
    }

    /*
     * 将用户退出房间
     */
    public boolean delUser(Long userId) {
        // 拿到房间的人
        ArrayList<UserEntity> userList = this.getUserList();
        // 坐过的，有几个座位
        int usedSeatCount = userList.size();
        // 遍历
        for(int ii=0; ii<usedSeatCount; ii++) {
            // 如果不是空的座位，并且 ID 是这个用户就删除这个用户
            if (userList.get(ii)!=null && userList.get(ii).getId().equals(userId)) {
                // 删除用户
                userList.set(ii, null);
                this.setUserList(userList);
                // 判断是不是房主
                if (userId.equals(this.getRoomOwnerId())) {
                    for(int nn=0; nn<usedSeatCount; nn++) {
                        if (userList.get(ii)!=null) {
                            this.setRoomOwnerId(userList.get(ii).getId());
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }
}
