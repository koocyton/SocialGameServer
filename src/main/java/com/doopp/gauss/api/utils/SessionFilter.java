package com.doopp.gauss.api.utils;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.doopp.gauss.api.entity.RoomEntity;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.api.service.RestResponseService;
import org.springframework.web.filter.OncePerRequestFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedisPool;

/*
 * Created by henry on 2017/4/16.
 */
public class SessionFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(SessionFilter.class);

    @Resource
    private ShardedJedisPool accessTokenJedis;

    private final JdkSerializationRedisSerializer jsrs = new JdkSerializationRedisSerializer();

    /*
     * 登录验证过滤器
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
        throws ServletException, IOException {



        // 不过滤的uri
        String[] notFilters = new String[] {
            "/api/v1/register",
            "/api/v1/login",
            "/api/v1/logout",
            "/api/v1/web/socket",
            "/chat-socket",
            "/game-socket",
            "/room-socket",
            "/js",
            "/image",
            "/css",
            "/favicon.ico"
        };


        // 请求的uri
        String uri = request.getRequestURI();

        logger.info(" >>> request.getRequestURI() : " + uri);

        // 是否过滤
        boolean doFilter = true;

        // 如果uri中包含不过滤的uri，则不进行过滤
        for (String notFilter : notFilters) {
            if (uri.contains(notFilter) || uri.equals("/api")) {
                doFilter = false;
                break;
            }
        }

        logger.info(" >>>>> Session Id " + request.getSession().getId());

        // 执行过滤 验证通过的会话
        try {
            if (doFilter) {
                byte[] accessToken = request.getHeader("access-token").getBytes();
                ShardedJedis shardedJedis = accessTokenJedis.getResource();
                byte[] byteUser = shardedJedis.get(accessToken);
                shardedJedis.close();
                Object userObject = jsrs.deserialize(byteUser);
                if (userObject == null) {
                    RestResponseService.writeErrorResponse(response, "Session failed");
                }
                filterChain.doFilter(request, response);
            }
        }
        catch(Exception e) {
            /*StringBuilder errorInfo = new StringBuilder();
            errorInfo.append("\n /*");
            for(StackTraceElement stackTraceElement : e.getStackTrace()) {
                errorInfo.append("\n  * " + stackTraceElement.toString());
            }
            errorInfo.append("\n  * / ");
            logger.info(errorInfo.toString());*/
            e.printStackTrace();
            RestResponseService.writeErrorResponse(response, e.getMessage());
        }
    }
}