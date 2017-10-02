package com.doopp.gauss.server.undertow;

import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.api.helper.RedisSessionHelper;
import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.*;
import io.undertow.websockets.spi.WebSocketHttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebAppSocketConnectionCallback implements WebSocketConnectionCallback
{
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private WebAppSocketReceiveListener webAppSocketReceiveListener;// = new WebAppSocketReceiveListener();

    private RedisSessionHelper redisSessionHelper = new RedisSessionHelper();

    @Override
    public void onConnect(WebSocketHttpExchange exchange, WebSocketChannel channel)  {

        logger.info(" >>> " + webAppSocketReceiveListener);

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
                // 新的 channel
                // 1. 就要将 channel map 里此用户的旧连接断掉，
                // 2. channel map 里此用户的连接从 map 里删除，
                // 3. 并将此连接添加在 channel map 里
                webAppSocketReceiveListener.newChannelConnect(channel);
                // 正式连接
                channel.getReceiveSetter().set(webAppSocketReceiveListener);
                channel.resumeReceives();
            }
        }
    }

    public void setWebAppSocketReceiveListener(WebAppSocketReceiveListener webAppSocketReceiveListener) {
        this.webAppSocketReceiveListener = webAppSocketReceiveListener;
    }
}
