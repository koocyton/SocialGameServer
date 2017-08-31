package com.doopp.gauss.api.utils;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.api.service.RestResponseService;
import org.springframework.web.filter.OncePerRequestFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Created by henry on 2017/4/16.
 */
public class SessionFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(SessionFilter.class);

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

        logger.info(" >>>>> request.getRequestURI() : " + uri);

        // 是否过滤
        boolean doFilter = true;

        // 如果uri中包含不过滤的uri，则不进行过滤
        for (String notFilter : notFilters) {
            if (uri.contains(notFilter) || uri.equals("/api")) {
                doFilter = false;
                break;
            }
        }

        logger.info(" >>>>> while filter ");

        // 执行过滤 验证通过的会话
        try {
            if (doFilter) {
                UserEntity currentUser = (UserEntity)request.getSession().getAttribute("currentUser");
                if (currentUser==null) {
                    RestResponseService.writeErrorResponse(response, "Session failed");
                    return;
                }
            }
            filterChain.doFilter(request, response);
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