package com.doopp.gauss.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.api.service.RestResponseService;
import com.doopp.gauss.api.service.UserService;
import com.doopp.gauss.api.service.impl.LoginServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 好友的 API Controller
 *
 * Created by henry on 2017/7/15.
 */
@Controller
@RequestMapping(value = "api/v1/")
public class FriendController {

    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RestResponseService restResponseService;

    /*
     * 好友列表
     */
    @ResponseBody
    @RequestMapping(value = "{userId}/friendList", method = RequestMethod.GET)
    public JSONObject userFriendList(@RequestHeader("access-token") String accessToken) {
        // 当前用户
        UserEntity currentUser = userService.getUserByToken(accessToken);
        List<Map<String, UserEntity>> friendList = userService.getUserFriendList(currentUser.getId());
        for(Map tmp:friendList)
        {
            logger.info(tmp.toString());
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("friendList", friendList);
        jsonObject.put("user", userService.getUserInfo(currentUser.getId()).toJsonObject());
        return jsonObject;
    }

    /*
     * 添加好友
     */
    @ResponseBody
    @RequestMapping(value = "apply-friend/{userId}", method = RequestMethod.GET)
    public JSONObject applyFriend(@RequestHeader("access-token") String accessToken,
                                  @PathVariable("userId") Long userId) {
        // 当前用户
        UserEntity currentUser = userService.getUserByToken(accessToken);
        // 如果申请成为好友成功
        if (userService.applyFriend(currentUser, userId)) {
            // 成功申请好友
            return restResponseService.success();
        }
        // 申请成为好友失败
        return restResponseService.error(500, "can not apply friend");
    }

    /*
     * 通过好友申请
     */
    @ResponseBody
    @RequestMapping(value = "accept-friend/{userId}", method = RequestMethod.GET)
    public JSONObject acceptFriend(@RequestHeader("access-token") String accessToken,
                                   @PathVariable("userId") Long userId) {
        // 当前用户
        UserEntity currentUser = userService.getUserByToken(accessToken);
        // 通过
        if (userService.acceptFriend(currentUser, userId)) {
            // 成功
            return restResponseService.success();
        }
        // 没有通过
        return restResponseService.error(500, "can not accept this friend");
    }

    /*
     * 拒绝好友申请
     */
    @ResponseBody
    @RequestMapping(value = "reject-friend/{userId}", method = RequestMethod.GET)
    public JSONObject rejectFriend(@RequestHeader("access-token") String accessToken,
                                   @PathVariable("userId") Long userId) {
        // 当前用户
        UserEntity currentUser = userService.getUserByToken(accessToken);
        // 拒绝申请
        if (userService.rejectFriend(currentUser, userId)) {
            // 完成拒绝
            return restResponseService.success();
        }
        // 拒绝失败 ( 比如重复的操作，或以及成为好友了 )
        return restResponseService.error(500, "can not reject this friend");
    }

    /*
     * 取消好友
     */
    @ResponseBody
    @RequestMapping(value = "cancel-friend/{userId}", method = RequestMethod.GET)
    public JSONObject cancelFriend(@RequestHeader("access-token") String accessToken,
                                   @PathVariable("userId") Long userId) {
        // 当前用户
        UserEntity currentUser = userService.getUserByToken(accessToken);
        // 取消成功
        if (userService.cancelFriend(currentUser, userId)) {
            // 完成
            return restResponseService.success();
        }
        // 不应该有取消失败的情况
        // 总应该最后，用户和此好友断开好友关系
        return restResponseService.error(500, "can not cancel this friend");
    }
}
