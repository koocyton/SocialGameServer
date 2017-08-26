package com.doopp.gauss.api.service;

import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.socket.handler.GameSocketHandler;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 *
 * Created by henry on 2017/7/11.
 */
public interface LoginService {

    // 判断账号密码
    boolean checkLoginRequest(String account, String password);

    // 混淆密码
    String hashPassword(UserEntity userEntity, String password);

    // 注册登录
    boolean registerLogin(String account, HttpSession httpSession);

    // 注销登录
    boolean unregisterLogin(HttpSession httpSession);

    // 获取 access token，应该和 registerLogin 一起使用
    String getAccessToken();

    // 从 cookie 里获取账号
    static String getAccountBySession(HttpServletRequest request) throws IOException {
        // Logger logger = LoggerFactory.getLogger(LoginService.class);
        try {
            String accessToken = request.getHeader("access-token");
            // String accessToken = CookieHelper.getCookieByName(request, "access-token").getValue();
            return getAccountByKey(accessToken);
        }
        catch (Exception e) {
            return null;
        }
    }

    // 从 KEY 里获取账号
    static String getAccountByKey(String key) {
        Logger logger = LoggerFactory.getLogger(GameSocketHandler.class);
        try {
            CacheManager cacheManager = CacheManager.create(new ClassPathResource("config/ehcache/ehcache.xml").getInputStream());
            Cache cache = cacheManager.getCache("access-token-cache");
            logger.info(" >>> cache : " + cache);
            return (String) cache.get(key).getObjectValue();
        }
        catch (Exception e) {
            return null;
        }
    }
}
