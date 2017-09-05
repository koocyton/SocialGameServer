package com.doopp.gauss.api.service;

import com.doopp.gauss.api.entity.RoomEntity;
import com.doopp.gauss.api.entity.UserEntity;

/**
 * 房间的管理
 *
 * Created by Henry on 2017/8/26.
 */
public interface RoomService {

    RoomEntity userCurrentRoom(UserEntity user);

    RoomEntity userCreateRoom(UserEntity user);

    RoomEntity userJoinRoom(UserEntity user, int roomId);

    RoomEntity userJoinFreeRoom(UserEntity user);

    RoomEntity userLeaveRoom(UserEntity user);
}
