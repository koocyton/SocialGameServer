package com.doopp.gauss.socket.utils;

import com.doopp.gauss.api.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 *
 * Created by henry on 2017/7/20.
 */
public class WebSocketHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandshakeInterceptor.class);

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {

        logger.info(" >>> getAttribute(\"currentUser\") " + this.getSession(request).getAttribute("currentUser"));
        logger.info(" >>> getId() " + this.getSession(request).getId());
        // 获取用户是否登录的 session
        UserEntity currentUser = (UserEntity) this.getSession(request).getAttribute("currentUser");
        // 如果不能从 Session 里获取 currentUser ，就不能连接上
        return currentUser!=null && super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception ex) {
        super.afterHandshake(request, response, wsHandler, ex);
    }

    private HttpSession getSession(ServerHttpRequest request) {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
            logger.info(" >>> serverRequest " + serverRequest);
            logger.info(" >>> serverRequest.getServletRequest() " + serverRequest.getServletRequest().getSession());
            return serverRequest.getServletRequest().getSession(true);
        }
        return null;
    }
}
