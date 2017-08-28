package com.doopp.gauss.api.dao;

import com.doopp.gauss.api.entity.RoomEntity;
import com.doopp.gauss.api.entity.UserEntity;

/*
 * 操作 RoomEntity 的 Dao
 */
public interface RoomDao {

    void create(RoomEntity roomEntity);

    void delete(int roomId);

    void update(RoomEntity roomEntity);

    Long count();

    RoomEntity fetchById(long id);
}
