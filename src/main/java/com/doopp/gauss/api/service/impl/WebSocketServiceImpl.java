package com.doopp.gauss.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.api.service.RestResponseService;
import com.doopp.gauss.api.service.WebSocketService;
import com.doopp.gauss.socket.handler.GameSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;

/**
 * Web socket service impl
 *
 * Created by Henry on 2017/7/20.
 */
@Service
public class WebSocketServiceImpl implements WebSocketService {

    // static Logger logger = LoggerFactory.getLogger(WebSocketService.class);

    @Resource
    private GameSocketHandler gameSocketHandler;

    @Autowired
    private RestResponseService restResponseService;

    @Override
    public boolean disconnectSocket(Long userId) {
        String sessionId = Long.toString(userId);
        try {
            return gameSocketHandler.closeSocketConnect(sessionId);
        }
        catch(Exception e) {
            return false;
        }
    }

    @Override
    public void sendStringToUser(String message, Long... usersId){
        for (Long userId : usersId) {
            String sessionId = Long.toString(userId);
            // logger.info(" >>> sendStringToUser by webSocket " + sessionId);
            gameSocketHandler.sendMessageToUser(sessionId, new TextMessage(message));
        }
    }

    @Override
    public void sendStringToAll(String message){
        gameSocketHandler.sendMessageToAll(new TextMessage(message));
    }

    @Override
    public JSONObject dispatch(String action, JSONObject actionData, UserEntity userEntity) {
        return restResponseService.data(userEntity);
    }
}
