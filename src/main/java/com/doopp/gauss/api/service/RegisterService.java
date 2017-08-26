package com.doopp.gauss.api.service;

/**
 * 注册的 Service
 *
 * Created by henry on 2017/7/14.
 */
public interface RegisterService {

    boolean registerUser(String account, String password);

    boolean updateUserInfo(String nickname);

    boolean checkRegisterAccount(String account);

    boolean checkRegisterPassword(String password);
}
