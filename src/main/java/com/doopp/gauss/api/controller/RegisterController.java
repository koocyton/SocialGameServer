package com.doopp.gauss.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.doopp.gauss.api.service.LoginService;
import com.doopp.gauss.api.service.RegisterService;
import com.doopp.gauss.api.service.RestResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 注册新用户
 * Created by henry on 2017/7/14.
 */
@Controller
@RequestMapping(value = "api/v1/")
public class RegisterController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private RestResponseService restResponse;

    /*
     * 提交登录
     */
    @ResponseBody
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public JSONObject register(HttpServletResponse response, HttpSession httpSession,
                               @RequestParam("account") String account,
                               @RequestParam("password") String password)
    {
        if (!registerService.checkRegisterAccount(account)) {
            return restResponse.error(response, 500, "account must is an email");
        }
        if (!registerService.checkRegisterPassword(password)) {
            return restResponse.error(response, 500, "The password must be eight bits long");
        }
        // 注册一个用户
        if (!registerService.registerUser(account, password)) {
            return restResponse.error(response, 500, "can not register account : " + account);
        }
        // 注册一个登录用户，生成 access token ，并缓存这个 key 对应的值 (account)
        if (!loginService.registerLogin(account, httpSession)) {
            return restResponse.error(response, 500, "can not login");
        }
        // 登录成功
        return restResponse.success();
    }
}
