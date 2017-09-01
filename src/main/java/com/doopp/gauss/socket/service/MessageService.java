package com.doopp.gauss.socket.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.doopp.gauss.api.entity.UserEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * Web socket service
 *
 * Created by Henry on 2017/7/20.
 */
public interface MessageService {

    // 发送消息给所有人
    // void sendStringToAll(String message);

    // 将消息发送到房间
    void sendStringToRoom(String message, int targetRoomId);

    // 将消息发送给人
    void sendStringToUser(String message, Long... targetUsersId);

    // 关闭连接
    void disconnectSocket(Long userId);

    // 将收到的消息转发给对应的服务
    void callMessageService(UserEntity userEntity, String action, JSONObject actionData);
}
