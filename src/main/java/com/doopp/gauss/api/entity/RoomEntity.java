package com.doopp.gauss.api.entity;

import com.doopp.gauss.api.service.impl.RoomServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * 房间的实体
 *
 * Created by Henry on 2017/8/26.
 */
public class RoomEntity {

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

    public boolean addUser(UserEntity userEntity) {
        //logger.info(" >>> " + userEntity);
        //logger.info(" >>> " + userList);
        logger.info(" >>> " + userList.size());
        for(int ii=0; ii<userList.size(); ii++) {
            logger.info(" >>> " + userList);
            logger.info(" >>> " + userList.get(ii));
            if (userList.get(ii).equals(null)) {
                userList.set(ii, userEntity);
                return true;
            }
        }
        return false;
    }

    public boolean deleteUser(Long userId) {
        for(int ii=0; ii<userList.size(); ii++) {
            if (userList.get(ii).getId().equals(userId)) {
                userList.remove(ii);
                return true;
            }
        }
        return false;
    }
}
