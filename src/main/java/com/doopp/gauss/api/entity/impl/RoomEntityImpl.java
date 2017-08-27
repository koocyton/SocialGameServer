package com.doopp.gauss.api.entity.impl;

import com.doopp.gauss.api.entity.RoomEntity;
import com.doopp.gauss.api.entity.UserEntity;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.ArrayList;

public class RoomEntityImpl implements RoomEntity {

    private int id;

    private String name;

    private ArrayList<UserEntity> userList;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    public RoomEntityImpl() {
        ValueOperations value = redisTemplate.opsForValue();
    }

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

    public ArrayList<UserEntity> getUserList() {
        return this.userList;
    }

    public UserEntity getUser(Long userId) {
        for(UserEntity user : this.userList) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    public boolean addUser(UserEntity newUser) {
        for(UserEntity user : this.userList) {
            if (user.getId().equals(newUser.getId())) {
                return false;
            }
        }
        this.userList.add(newUser);
        return true;
    }

    public boolean removeUser(Long userId) {
        for(int ii=0; ii<this.userList.size(); ii++) {
            if (this.userList.get(ii).getId().equals(userId)) {
                this.userList.remove(ii);
                return true;
            }
        }
        return false;
    }
}
