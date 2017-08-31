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
        // 获取用户是否登录的 session
        logger.info(" >>> " + request);
        logger.info(" >>> " + this.getSession(request));
        UserEntity currentUser = (UserEntity) this.getSession(request).getAttribute("currentUser");
        logger.info(" >>> WebSocketHandshakeInterceptor.currentUser " + currentUser);
        // 呵呵
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
        logger.info(" >>> request 1 " + request);
        if (request instanceof ServletServerHttpRequest) {
            logger.info(" >>> request 2 " + request);
            ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
            logger.info(" >>> request 3 " + serverRequest);
            logger.info(" >>> request 4 " + serverRequest.getServletRequest());
            return serverRequest.getServletRequest().getSession(true);
        }
        return null;
    }
}
