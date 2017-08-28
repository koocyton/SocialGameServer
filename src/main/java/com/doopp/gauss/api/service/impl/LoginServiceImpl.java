package com.doopp.gauss.api.service.impl;

import com.doopp.gauss.api.dao.UserDao;
import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.api.service.LoginService;
import com.doopp.gauss.api.helper.EncryHelper;
import com.doopp.gauss.api.service.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 *
 * Created by henry on 2017/7/11.
 */
@Service
public class LoginServiceImpl implements LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Resource
    private UserDao userDao;

    @Autowired
    private WebSocketService webSocketService;

    //@Resource
    //private EhCacheCacheManager ehCacheCacheManager;

    //private Cache cache = ehCacheCacheManager.getCache("access-token-cache");

    //private Cache cache;

    //private String accessToken;

    // private HttpServletRequest request;

    @Autowired
    public LoginServiceImpl(CacheManager cacheManager) {
        //this.userDao = DBSession.getMapper(UserDao.class);
        // this.cache   = cacheManager.getCache("access-token-cache");
        // this.request = request;
    }

    @Override
    public boolean checkLoginRequest(String account, String password) {
        UserEntity userEntity = this.userDao.fetchByAccount(account);
        return userEntity!=null && this.hashPassword(userEntity, password).equals(userEntity.getPassword());
    }

    @Override
    public String hashPassword(UserEntity userEntity, String password) {
        String hashPasswordString = userEntity.getAccount() + " " + password + " " + userEntity.getSalt();
        return EncryHelper.md5(hashPasswordString);
    }

    @Override
    public boolean registerLogin(String account, HttpSession httpSession) {
        UserEntity currentUser = userDao.fetchByAccount(account);
        httpSession.setAttribute("currentUser", currentUser);
        logger.info(" >>> " + currentUser);
        // 哈哈，尝试给长连接发一个消息
        webSocketService.sendStringToUser(currentUser.getAccount() + " 重登录，连接被重置", currentUser.getId());
        webSocketService.disconnectSocket(currentUser.getId());
        webSocketService.sendStringToAll(account + " 登录");
        return true;
        /*
        try {
            OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
            String accessToken = oauthIssuerImpl.accessToken();
            cache.put(accessToken, account);
            this.accessToken = accessToken;
        }
        catch (OAuthSystemException e) {
            return false;
        }
        return true;
         */
    }

    @Override
    public boolean unregisterLogin(HttpSession httpSession) {
        // SessionUserEntity currentUser = (SessionUserEntity) userDao.fetchByAccount(account);
        httpSession.removeAttribute("currentUser");
        return true;
        /*
        try {
            String account = (String) cache.get(accessToken).get();
            if (account == null) {
                return false;
            }
            cache.evict(accessToken);
            // 删除成功返回 true
            return cache.get(accessToken) == null;
        }
        catch(Exception e) {
            return false;
        }
        */
    }

    @Override
    public String getAccessToken() {
        return "";//this.accessToken;
    }
}
