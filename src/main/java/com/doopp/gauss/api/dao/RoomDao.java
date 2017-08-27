package com.doopp.gauss.api.dao;

import com.doopp.gauss.api.entity.RoomEntity;
import com.doopp.gauss.api.entity.UserEntity;

/*
 * 操作 RoomEntity 的 Dao
 */
public interface RoomDao {

    void create(RoomEntity roomEntity);

    void delete(int id);

    boolean adduser(UserEntity userEntity);

    boolean deleteUser(Long userId);

    boolean changeRoomOwner(UserEntity userEntity);

    RoomEntity get();
}
