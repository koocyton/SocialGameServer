package com.doopp.gauss.socket.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.socket.service.ChatService;
import com.doopp.gauss.socket.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    MessageService messageService;

    @Override
    public void roomChat(UserEntity currentUser, String action, JSONObject actionData) {
        int roomId = actionData.getInteger("roomId");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", action);
        jsonObject.put("data", actionData);
        messageService.sendStringToRoom(jsonObject.toJSONString(), roomId);
    }

    @Override
    public void roomPersonChat(UserEntity currentUser, String action, JSONObject actionData) {
        this.personChat(currentUser, action, actionData);
    }

    @Override
    public void personChat(UserEntity currentUser, String action, JSONObject actionData) {
        Long targetUserId = actionData.getLong("targetUserId");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", action);
        jsonObject.put("data", actionData);
        messageService.sendStringToUser(jsonObject.toJSONString(), targetUserId);
    }
}
