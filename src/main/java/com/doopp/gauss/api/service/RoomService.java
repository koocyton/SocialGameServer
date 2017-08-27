package com.doopp.gauss.api.service;

import com.doopp.gauss.api.entity.UserEntity;

/**
 * 房间的管理
 *
 * Created by Henry on 2017/8/26.
 */
public interface RoomService {

    boolean joinRoom(UserEntity user, int roomId);
}
