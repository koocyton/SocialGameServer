package com.doopp.gauss.api.service;

import com.alibaba.fastjson.JSONObject;
import com.doopp.gauss.api.entity.UserEntity;

public interface ChatService {

    void roomChat(UserEntity currentUser, String action, JSONObject actionData);

    void roomPersonChat(UserEntity currentUser, String action, JSONObject actionData);

    void personChat(UserEntity currentUser, String action, JSONObject actionData);
}
