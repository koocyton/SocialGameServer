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

    private final LoginService loginService;

    private final RegisterService registerService;

    private final RestResponseService restService;

    @Autowired
    public RegisterController(LoginService loginService, RegisterService registerService, RestResponseService restService) {
        this.loginService = loginService;
        this.registerService = registerService;
        this.restService = restService;
    }

    /*
     * 提交登录
     */
    @ResponseBody
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public JSONObject register(HttpServletResponse response,
                               @RequestParam("account") String account,
                               @RequestParam("password") String password)
    {
        if (!registerService.checkRegisterAccount(account)) {
            return restService.error(response, 500, "account must is an email");
        }
        if (!registerService.checkRegisterPassword(password)) {
            return restService.error(response, 500, "The password must be eight bits long");
        }
        // 注册一个用户
        if (!registerService.registerUser(account, password)) {
            return restService.error(response, 500, "can not register account : " + account);
        }
        // 注册一个登录用户，生成 access token ，并缓存这个 key 对应的值 (account)
        String accessToken = loginService.registerLogin(account);
        if (accessToken==null) {
            return restService.error(response, 500, "can not login");
        }
        // 登录成功
        return restService.loginSuccess(accessToken);
    }
}
