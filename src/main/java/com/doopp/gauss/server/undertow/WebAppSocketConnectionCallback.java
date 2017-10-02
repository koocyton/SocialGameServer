package com.doopp.gauss.server.undertow;

import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.api.helper.RedisSessionHelper;
import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.*;
import io.undertow.websockets.spi.WebSocketHttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class WebAppSocketConnectionCallback implements WebSocketConnectionCallback
{
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WebAppSocketReceiveListener webAppSocketReceiveListener;

    private final RedisSessionHelper redisSessionHelper = new RedisSessionHelper();

    @Override
    public void onConnect(WebSocketHttpExchange exchange, WebSocketChannel channel)
    {
        String accessToken = null;
        String uriQuery = exchange.getQueryString();
        int beginOffset = uriQuery.indexOf("access-token=");
        if (beginOffset!=-1) {
            beginOffset = beginOffset + 13;
            accessToken = uriQuery.substring(beginOffset, beginOffset + 32);
        }

        // 有 token
        if (accessToken!=null) {
            UserEntity currentUser = redisSessionHelper.getUserByToken(accessToken);
            // 存在用户
            if (currentUser!=null) {
                // 在当前连接上存放当前用户信息
                channel.setAttribute("currentUser", currentUser);
                // 这个用户的其他连接应该踢掉
                webAppSocketReceiveListener.unConnectChannel(currentUser.getId());
                // 保存 socket channel 的数组
                webAppSocketReceiveListener.addChannel(channel);
                // 正式连接
                channel.getReceiveSetter().set(webAppSocketReceiveListener);
                channel.resumeReceives();
            }
        }
    }
}
