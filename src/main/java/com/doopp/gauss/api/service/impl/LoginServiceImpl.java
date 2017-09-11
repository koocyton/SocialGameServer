package com.doopp.gauss.api.service.impl;

import com.doopp.gauss.api.dao.UserDao;
import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.api.helper.RedisSessionHelper;
import com.doopp.gauss.api.service.LoginService;
import com.doopp.gauss.api.helper.EncryHelper;
import com.doopp.gauss.api.service.UserService;
import com.doopp.gauss.socket.service.MessageService;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.naming.AuthenticationException;
import javax.servlet.http.HttpSession;

/**
 *
 * Created by henry on 2017/7/11.
 */
@Service("loginService")
public class LoginServiceImpl implements LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Resource
    private UserDao userDao;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisSessionHelper redisSessionHelper;

    //@Resource
    //private EhCacheCacheManager ehCacheCacheManager;

    //private Cache cache = ehCacheCacheManager.getCache("access-token-cache");

    //private Cache cache;

    //private String accessToken;

    // private HttpServletRequest request;

    //@Autowired
    //public LoginServiceImpl(CacheManager cacheManager) {
        //this.userDao = DBSession.getMapper(UserDao.class);
        // this.cache   = cacheManager.getCache("access-token-cache");
        // this.request = request;
    //}

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
    public String registerLogin(String account) { // , HttpSession httpSession) {

        try {
            OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
            String accessToken = oauthIssuerImpl.accessToken();

            UserEntity currentUser = userDao.fetchByAccount(account);
            redisSessionHelper.setUserByToken(accessToken, currentUser);
            // 断开这个用户的长链接
            messageService.disconnectSocket(currentUser.getId());
            messageService.sendStringToUser(currentUser.getAccount() + " 重登录，连接被重置", currentUser.getId());
            return accessToken;
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean unregisterLogin(String accessToken) {

        redisSessionHelper.delUserSessionCache(accessToken);

        // UserEntity currentUser = userService.getUserByToken(accessToken);

        // SessionUserEntity currentUser = (SessionUserEntity) userDao.fetchByAccount(account);
        // httpSession.removeAttribute("currentUser");
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
