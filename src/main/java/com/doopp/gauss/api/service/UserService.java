package com.doopp.gauss.api.service;

import com.doopp.gauss.api.entity.UserEntity;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by henry on 2017/7/11.
 */
public interface UserService {

    // 从 Session Redis 里获取用户
    UserEntity getUserByToken(String accessToken);

    // 获取用户信息
    UserEntity getUserInfo(Long userId);

    // 获取用户信息
    UserEntity getUserInfo(String account);

    // 获取用户好友列表
    List<UserEntity> getUserFriendList(Long userId);

    // 加为好友
    boolean applyFriend(UserEntity userEntity, Long userId);

    // 通过好友关系
    boolean acceptFriend(UserEntity userEntity, Long userId);

    // 通过好友关系
    boolean rejectFriend(UserEntity userEntity, Long userId);

    // 删除好友关系
    boolean cancelFriend(UserEntity userEntity, Long userId);
}
