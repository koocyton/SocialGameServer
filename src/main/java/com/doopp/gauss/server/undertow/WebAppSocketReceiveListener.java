//package com.doopp.gauss.server.undertow;
//
//import com.alibaba.fastjson.JSONObject;
//import com.doopp.gauss.api.dao.UserDao;
//import com.doopp.gauss.api.entity.UserEntity;
//import com.doopp.gauss.api.service.UserService;
//import io.undertow.websockets.core.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class WebAppSocketReceiveListener extends AbstractReceiveListener {
//
//    private static final Map<Long, WebSocketChannel> allWebSocketChannels = new HashMap<>();
//
//    private final Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Autowired
//    private UserService userService;
//
//    @Override
//    protected void onFullTextMessage(WebSocketChannel channel, BufferedTextMessage message)
//    {
//
//        WebSockets.sendText(message.getData(), channel, null);
//    }
//
//    @Override
//    protected void onFullBinaryMessage(WebSocketChannel channel, BufferedBinaryMessage message) throws IOException
//    {
//        super.onFullBinaryMessage(channel, message);
//    }
//
//    @Override
//    protected void onClose(WebSocketChannel webSocketChannel, StreamSourceFrameChannel channel) throws IOException
//    {
//        super.onClose(webSocketChannel, channel);
//    }
//
//    public void newChannelConnect(WebSocketChannel channel) {
//        UserEntity currentUser = (UserEntity) channel.getAttribute("currentUser");
//        Long userId = currentUser.getId();
//        WebSocketChannel oldChannel = allWebSocketChannels.get(userId);
//        if (oldChannel!=null) {
//            this.delChannel(channel);
//        }
//        this.addChannel(channel);
//    }
//
//    private void addChannel(WebSocketChannel channel) {
//        UserEntity currentUser = (UserEntity) channel.getAttribute("currentUser");
//        Long userId = currentUser.getId();
//        allWebSocketChannels.put(userId, channel);
//    }
//
//    private void delChannel(WebSocketChannel channel) {
//        UserEntity currentUser = (UserEntity) channel.getAttribute("currentUser");
//        Long userId = currentUser.getId();
//        WebSocketChannel oldChannel = allWebSocketChannels.get(userId);
//        try {
//            logger.info(" >>> close channel : " + userId );
//            oldChannel.sendClose();
//        }
//        catch(Exception e) {
//            logger.info(" >>> no can close channel : " + userId );
//        }
//        allWebSocketChannels.remove(userId);
//    }
//}
