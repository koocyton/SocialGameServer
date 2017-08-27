package com.doopp.gauss.api.entity;

import java.util.ArrayList;

/**
 * 房间的实体
 *
 * Created by Henry on 2017/8/26.
 */
public class RoomEntity {

    int getId();

    void setId(int id);

    String getName();

    void setName(String name);

    ArrayList<UserEntity> getUserList();

    UserEntity getUser(Long userId);

    boolean addUser(UserEntity newUser);

    boolean removeUser(Long userId);
}
