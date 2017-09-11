package com.doopp.gauss.api.service.impl;

import com.doopp.gauss.api.dao.UserDao;
import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.api.helper.EncryHelper;
import com.doopp.gauss.api.helper.IdWorker;
import com.doopp.gauss.api.service.LoginService;
import com.doopp.gauss.api.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 注册用户实现
 * Created by henry on 2017/7/14.
 */
@Service("registerService")
public class RegisterServiceImpl implements RegisterService {

    @Resource
    private UserDao userDao;

    @Autowired
    private LoginService loginService;

    //@Autowired
    //public RegisterServiceImpl() {
        // this.userDao = DBSession.getMapper(UserDao.class);
    //}

    @Override
    public boolean registerUser(String account, String password) {
        // 用户 ID
        IdWorker idWorker = new IdWorker(1, 1);
        // 当前时间
        int currentTime = (int)(System.currentTimeMillis() / 1000);
        // 密码混淆的值
        String salt = EncryHelper.md5(String.valueOf(currentTime));
        // 性别范围
        // int _gender = (gender>=0 && gender<=4) ? gender : 0;
        System.out.println(" >>> " + salt);

        try {
            // 初始化用户
            UserEntity userEntity = new UserEntity();
            userEntity.setId(idWorker.nextId());
            userEntity.setAccount(account);
            userEntity.setSalt(salt);
            userEntity.setNickname("");
            userEntity.setPortrait("");
            userEntity.setFriends("");
            userEntity.setPassword(loginService.hashPassword(userEntity, password));
            userEntity.setGender(0);
            userEntity.setCreate_at(currentTime);
            // 创建
            userDao.create(userEntity);
        }
        catch(Exception e) {
            // System.out.println(" >>> " + e.getMessage());
            // throw e;
            return false;
        }
        // 注册成功
        return true;
    }

    @Override
    public boolean updateUserInfo(String nickname) {
        return false;
    }

    @Override
    public boolean checkRegisterAccount(String account) {
        String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        Pattern p = Pattern.compile(RULE_EMAIL);
        Matcher m = p.matcher(account);
        return m.matches();
    }

    @Override
    public boolean checkRegisterPassword(String password) {
        return password.length()>=8;
    }
}
