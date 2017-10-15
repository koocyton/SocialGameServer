package com.doopp.gauss.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.doopp.gauss.api.service.LoginService;
import com.doopp.gauss.api.service.RegisterService;
import com.doopp.gauss.api.service.RestResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 登录界面
 *
 * Created by henry on 2017/7/11.
 */
@Controller
@RequestMapping(value = "api/v1/")
public class LoginController {

    // private final Logger logger = LoggerFactory.getLogger(getClass());

    private final LoginService loginService;

    private final RegisterService registerService;

    private final RestResponseService restService;

    @Autowired
    public LoginController(LoginService loginService, RegisterService registerService, RestResponseService restService) {
        this.loginService = loginService;
        this.registerService = registerService;
        this.restService = restService;
    }

    /*
     * 提交登录
     */
    @ResponseBody
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public JSONObject login(HttpServletResponse response,
                            @RequestParam("account") String account,
                            @RequestParam("password") String password) {

        // 校验用户名，密码
        if (!loginService.checkLoginRequest(account, password)) {
            // 告诉客户端密码错误
            return restService.error(response, 404, "Account or password is failed");
        }
        // 注册一个登录用户，生成 access token ，并缓存这个 key 对应的值 (account)
        String accessToken = loginService.registerLogin(account);
        if (accessToken==null) {
            return restService.error(response, 500, "can not login");
        }
        // 下发 access token
        return restService.loginSuccess(accessToken);
        // 登录成功
        // return restResponse.success();
    }

    /*
     * 退出登录
     */
    @ResponseBody
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public JSONObject logout(HttpServletResponse response,
                             @RequestHeader("access-token") String accessToken) {

        // 当前用户
        // UserEntity currentUser = userService.getUserByToken(accessToken);

        // 清空 cookie
        if (!loginService.unregisterLogin(accessToken)) {
            // 退出登录失败
            return restService.error(response, 500, "logout failed");
        }
        // 退出登录成功
        return restService.success();
    }

    /*
     * 快速登录，并进入房间
     */
    @ResponseBody
    @RequestMapping(value = "fast-login", method = RequestMethod.POST)
    public JSONObject fastLogin(@RequestParam("account") String account) {
        // 尝试注册一个用户
        registerService.registerUser(account, "a12345678");
        // 登录这个用户
        String accessToken = loginService.registerLogin(account);
        // 下发 access token
        return restService.loginSuccess(accessToken);
    }
}
