package com.doopp.gauss.api.service.impl;

import com.doopp.gauss.api.dao.UserDao;
import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.api.helper.RedisSessionHelper;
import com.doopp.gauss.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by henry on 2017/7/11.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    // private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Resource
    private UserDao userDao;

    @Autowired
    private RedisSessionHelper redisSessionHelper;

    @Autowired
    public UserServiceImpl() {
        // this.userDao = DBSession.getMapper(UserDao.class);
    }

    @Override
    public UserEntity getUserByToken(String accessToken){
        return redisSessionHelper.getUserByToken(accessToken);
    }

    @Override
    public UserEntity getUserInfo(Long userId) {
        return userDao.fetchById(userId);
    }

    @Override
    public UserEntity getUserInfo(String account) {
        return userDao.fetchByAccount(account);
    }

    @Override
    public List<UserEntity> getUserFriendList(Long userId) {
        UserEntity userEntity = userDao.fetchById(userId);
        if (userEntity!=null) {
            return userDao.fetchListByIds(userEntity.getFriends(), 0, 30);
        }
        return null;
    }

    @Override
    public boolean applyFriend(UserEntity userEntity, Long userId) {
        return false;
    }

    @Override
    public boolean acceptFriend(UserEntity userEntity, Long userId) {
        userEntity.addFriend(userId);
        userDao.update(userEntity);
        return true;
    }

    @Override
    public boolean rejectFriend(UserEntity userEntity, Long userId) {
        return false;
    }

    @Override
    public boolean cancelFriend(UserEntity userEntity, Long userId) {
        userEntity.delFriend(userId);
        userDao.update(userEntity);
        return true;
    }
}
