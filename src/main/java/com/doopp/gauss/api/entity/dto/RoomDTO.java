package com.doopp.gauss.api.entity.dto;

import com.doopp.gauss.api.entity.UserEntity;
import lombok.Data;

import java.util.ArrayList;

/**
 * 房间的实体
 *
 * Created by Henry on 2017/8/26.
 */
@Data
public class RoomDTO {

    // 房间 ID
    private int id;

    // 房间名
    private String name;

    // 房主
    private Long owner;

    // 房间里坐下的人员列表
    private ArrayList<UserEntity> userList = new ArrayList<>();
}
