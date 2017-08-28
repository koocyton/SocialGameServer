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

    public void addUser(UserEntity userEntity) {
        int size = userList.size();
        for(int ii=0; ii<=size; ii++) {
            if (ii==size || userList.get(ii)==null) {
                userList.add(ii, userEntity);
            }
        }
    }

    public void delUser(Long userId) {
        int size = userList.size();
        for(int ii=0; ii<=size; ii++) {
            if (userList.get(ii).getId().equals(userId)) {
                userList.set(ii, null);
            }
        }
    }
}
