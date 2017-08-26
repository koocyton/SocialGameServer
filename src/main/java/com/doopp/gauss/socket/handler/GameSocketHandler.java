package com.doopp.gauss.socket.handler;

import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.socket.utils.WebSocketHandshakeInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * GameSocketHandler
 *
 * Created by henry on 2017/7/20.
 */
public class GameSocketHandler implements WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(GameSocketHandler.class);

    // private static final ArrayList<WebSocketSession> users = new ArrayList<WebSocketSession>();

    private static final Map<String, WebSocketSession> socketSessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception
    {
        // 获取当前用户
        UserEntity currentUser = (UserEntity) session.getAttributes().get("currentUser");
        String sessionId = Long.toString(currentUser.getId());

        // 需要判断是否存在旧的连接，存在就断开
        WebSocketSession oldSession = socketSessions.get(sessionId);
        if (oldSession!=null && oldSession.isOpen()){
            oldSession.close();
            socketSessions.remove(sessionId);
        }

        // 保存一个 session 回话
        socketSessions.put(sessionId, session);
        logger.info(" >>> socketSessions.put(sessionId, webSocketSession) " + sessionId);
        session.sendMessage(new TextMessage(currentUser.getAccount() + " connected !"));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception
    {
        // 获取当前用户
        UserEntity currentUser = (UserEntity) session.getAttributes().get("currentUser");

        String requestBody = message.getPayload().toString();
        if (requestBody!=null && requestBody.length()>=1) {
            session.sendMessage(new TextMessage(currentUser.getNickname() + " 说 : " + requestBody));
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.info(" >>> handleTransportError");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        UserEntity userEntity = (UserEntity) session.getAttributes().get("currentUser");
        String sessionId = Long.toString(userEntity.getId());
        this.closeSocketConnect(sessionId);
        logger.info(" >>> afterConnectionClosed : sessionId " + sessionId);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public boolean closeSocketConnect (String sessionId) throws Exception {
        WebSocketSession session = socketSessions.get(sessionId);
        if (session.isOpen()) {
            session.close();
        }
        socketSessions.remove(sessionId);
        logger.info(" >>> closeSocketConnect " + sessionId);
        return true;
    }

    /**
     * 给所有在线用户发送消息
     *
     * @param message 发送的信息
     */
    public void sendMessageToAll(TextMessage message) {
        // for (WebSocketSession user : users) {
        for (Map.Entry<String, WebSocketSession> entry : socketSessions.entrySet()) {
            WebSocketSession socketSession = entry.getValue();
            try {
                if (socketSession.isOpen()) {
                    socketSession.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 给所有在线用户发送消息
     *
     * @param sessionId 发送的用户 ID，也是索引
     * @param message 发送的信息
     */
    public void sendMessageToUser(String sessionId, TextMessage message) {
        WebSocketSession socketSession = socketSessions.get(sessionId);
        try {
            if (socketSession!=null && socketSession.isOpen()) {
                socketSession.sendMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
