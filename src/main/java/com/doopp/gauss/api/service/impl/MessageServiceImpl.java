package com.doopp.gauss.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.doopp.gauss.api.dao.RoomDao;
import com.doopp.gauss.api.entity.RoomEntity;
import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.api.service.RestResponseService;
import com.doopp.gauss.api.service.ChatService;
import com.doopp.gauss.api.service.MessageService;
import com.doopp.gauss.server.websocket.handler.GameSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * Web socket service impl
 *
 * Created by Henry on 2017/7/20.
 */
@Service("messageService")
public class MessageServiceImpl implements MessageService {

    // private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GameSocketHandler gameSocketHandler;

    @Autowired
    private ChatService chatService;

    @Autowired
    private RoomDao roomDao;

    @Override
    public void sendStringToAll(String message){
        gameSocketHandler.sendMessageToAll(new TextMessage(message));
    }

    @Override
    public void sendStringToUser(String message, Long... targetUsersId){
        for (Long targetUserId : targetUsersId) {
            String sessionId = Long.toString(targetUserId);
            gameSocketHandler.sendMessageToUser(sessionId, new TextMessage(message));
        }
    }

    @Override
    public void sendStringToRoom(String message, int roomId) {
        RoomEntity roomEntity = roomDao.fetchByRoomId(roomId);
        ArrayList<UserEntity> userList = roomEntity.getUserList();
        for(UserEntity userEntity : userList) {
            if (userEntity!=null) {
                this.sendStringToUser(message, userEntity.getId());
            }
        }
    }

    @Override
    public void disconnectSocket(Long userId) {
        String sessionId = Long.toString(userId);
        try {
            gameSocketHandler.closeSocketConnect(sessionId);
        }
        catch(Exception e) {
            ;
        }
    }

    /*
     * game-socket 的 dispatch
     */
    @Override
    public void callMessageService(UserEntity currentUser, String action, JSONObject actionData) {
        // 按 action 调用不同 service
        switch(action) {
            // 房间内发送消息
            case "room-chat":
                chatService.roomChat(
                    currentUser,
                    action,
                    actionData
                );
                break;
            // 房间内私聊
            case "room-private-chat" :
                chatService.roomPersonChat(
                    currentUser,
                    action,
                    actionData
                );
                break;
            // P2P 给某某发送消息
            case "person-chat" :
                chatService.personChat(
                    currentUser,
                    action,
                    actionData
                );
                break;
        }
    }
}
