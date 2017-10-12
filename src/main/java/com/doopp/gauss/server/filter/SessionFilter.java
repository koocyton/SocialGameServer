package com.doopp.gauss.server.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.api.utils.RedisSessionHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.doopp.gauss.api.service.RestResponseService;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Created by henry on 2017/4/16.
 */
@Service
public class SessionFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    // private final RedisSessionHelper redisSessionHelper = new RedisSessionHelper();

    /*
     * 登录验证过滤器
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
        throws ServletException, IOException {

        // get bean
        ServletContext context = request.getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
        RedisSessionHelper redisSessionHelper = ctx.getBean(RedisSessionHelper.class);

        // 不过滤的uri
        String[] notFilters = new String[] {
            "/api/v1/register",
            "/api/v1/login",
            "/api/v1/fast-login",
            "/api/v1/logout",
            "/mobile-chat-room",
            "/chat-room",
            "/game-socket",
            "/js",
            "/image",
            "/css",
            "/favicon.ico",
            "/webjars",
            "/d/druid"
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

        // 执行过滤 验证通过的会话
        try {
            if (doFilter) {
                // 从 header 里拿到 access token
                String accessToken = request.getHeader("access-token");
                // 如果 token 存在，且长度正确
                if (accessToken!=null && accessToken.length()>=32) {
                    UserEntity userEntity = redisSessionHelper.getUserByToken(accessToken);
                    // 如果能找到用户
                    if (userEntity!=null) {
                        filterChain.doFilter(request, response);
                        return;
                    }
                    // 如果不能找到用户
                    else {
                        RestResponseService.writeErrorResponse(response, "Session failed");
                        return;
                    }
                }
                // 如果 token 不对
                else {
                    RestResponseService.writeErrorResponse(response, "Session failed");
                    return;
                }
            }
            // 不用校验
            filterChain.doFilter(request, response);
        }
        catch(Exception e) {
            e.printStackTrace();
            RestResponseService.writeErrorResponse(response, e.getMessage());
        }
    }
}