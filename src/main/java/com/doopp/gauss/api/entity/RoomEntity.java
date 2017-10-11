package com.doopp.gauss.api.entity;

import com.doopp.gauss.api.service.impl.RoomServiceImpl;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 房间的实体
 *
 * Created by Henry on 2017/8/26.
 */
@Data
public class RoomEntity implements Serializable {

    // 日志
    // private public final Logger logger = LoggerFactory.getLogger(RoomServiceImpl.class);

    // 房间 ID
    private int id;

    // 房间有多少个座位，即最多坐多少人
    private int seatCount;

    // 房间名
    private String name;

    // 房主
    private Long roomOwnerId;

    // 房间里坐下的人员列表
    private ArrayList<UserEntity> userList = new ArrayList<>();

    // 是否有空座
    private boolean freeSeat = true;

    // 房间是否可以围观
    private boolean canWatch = true;

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
        // 先摆上空凳子
        for(int ii=0; ii<seatCount; ii++) {
            this.userList.add(null);
        }
    }

    /*
     * 将用户加入房间
     */
    public boolean addUser(UserEntity userEntity) {
        // 1. 按顺序拿一个空座位
        // 2. 要查询是不是重复在这个房间内加入同一个用户
        int mm=-1;
        for(int ii=0; ii<seatCount; ii++) {
            // 如果用户已经在房间里
            if (userList.get(ii)!=null && userList.get(ii).getId().equals(userEntity.getId())) {
                return true;
            }
            // 如果有空房间
            if (userList.get(ii)==null && mm==-1) {
                mm = ii;
            }
        }
        // 如果找到空位
        if (mm!=-1) {
            userList.set(mm, userEntity);
            // 只会在第一个人加入时出现，加入前，没有房主
            if (roomOwnerId==null) {
                roomOwnerId = userEntity.getId();
            }
            // 如果坐在了最后一个位置，说明房间满了
            if (mm>=seatCount-1) {
                // 位置满了
                setFreeSeat(false);
            }
            return true;
        }
        // 位置满了
        setFreeSeat(false);
        return false;
    }

    /*
     * 将用户退出房间
     */
    public boolean delUser(Long userId) {
        // 拿到房间的人
        // ArrayList<UserEntity> userList = this.getUserList();
        // 遍历
        for(int ii=0; ii<seatCount; ii++) {
            // 如果不是空的座位，并且 ID 是这个用户就删除这个用户
            if (userList.get(ii)!=null && userList.get(ii).getId().equals(userId)) {
                // 删除用户
                userList.set(ii, null);
                // this.setUserList(userList);
                // 判断是不是房主
                if (userId.equals(this.getRoomOwnerId())) {
                    // 如果是房主，先置空房主
                    this.setRoomOwnerId(null);
                    // 重新找一个房主出来
                    for(int nn=0; nn<seatCount; nn++) {
                        if (userList.get(nn)!=null) {
                            this.setRoomOwnerId(userList.get(nn).getId());
                        }
                    }
                }
                // 退出成功，应该设定为空房间
                setFreeSeat(true);
                // 返回
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "RoomEntity {" +
            "id=" + id +
            ", seatCount=" + this.seatCount +
            ", name='" + this.name + '\'' +
            ", roomOwnerId='" + this.roomOwnerId + '\'' +
            ", userList=" + this.userList +
            ", freeSeat=" + this.freeSeat +
            ", canWatch=" + this.canWatch +
            '}';
    }
}
