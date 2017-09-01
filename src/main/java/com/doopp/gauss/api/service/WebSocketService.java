package com.doopp.gauss.api.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.doopp.gauss.api.entity.UserEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * Web socket service
 *
 * Created by Henry on 2017/7/20.
 */
public interface WebSocketService {

    void sendStringToUser(String message, Long... usersId);

    void sendStringToAll(String message);

    boolean disconnectSocket(Long userId);

    JSONObject dispatch(String action, JSONObject actionData, UserEntity userEntity);
}
