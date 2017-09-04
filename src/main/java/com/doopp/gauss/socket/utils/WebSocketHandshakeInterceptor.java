package com.doopp.gauss.socket.utils;

import com.doopp.gauss.api.entity.RoomEntity;
import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.api.helper.RedisSessionHelper;
import com.doopp.gauss.socket.handler.GameSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by henry on 2017/7/20.
 */
public class WebSocketHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandshakeInterceptor.class);

    private final RedisSessionHelper redisSessionHelper = new RedisSessionHelper();

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {

        // 默认从 url query 里获取 access token
        String accessToken = null;
        String uriQuery = request.getURI().getQuery();
        int beginOffset = uriQuery.indexOf("access-token=");
        if (beginOffset!=-1) {
            beginOffset = beginOffset + 13;
            accessToken = uriQuery.substring(beginOffset, beginOffset + 32);
        }

        // 从 header 里获取 access token
        /*
        if (accessToken==null) {
            List<String> tokens = request.getHeaders().get("access-token");
            if (tokens!=null) {
                accessToken = tokens.get(0);
            }
        }
        */

        // 有 token
        if (accessToken!=null) {
            UserEntity currentUser = redisSessionHelper.getUserByToken(accessToken);
            // 在会话里加入当前用户信息
            attributes.put("currentUser", currentUser);
            return currentUser!=null && super.beforeHandshake(request, response, wsHandler, attributes);
        }

        // 不能连接
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception ex) {
        super.afterHandshake(request, response, wsHandler, ex);
    }

    /*
    private HttpSession getSession(ServerHttpRequest request) {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
            logger.info(" >>> serverRequest " + serverRequest);
            logger.info(" >>> serverRequest.getServletRequest() " + serverRequest.getServletRequest().getSession());
            return serverRequest.getServletRequest().getSession(true);
        }
        return null;
    }
    */
}
