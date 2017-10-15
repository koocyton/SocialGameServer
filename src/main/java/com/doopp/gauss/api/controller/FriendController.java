package com.doopp.gauss.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.api.service.RestResponseService;
import com.doopp.gauss.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 好友的 API Controller
 *
 * Created by henry on 2017/7/15.
 */
@Controller
@RequestMapping(value = "api/v1/")
public class FriendController {

    // private final Logger logger = LoggerFactory.getLogger(getClass());

    private final UserService userService;

    private final RestResponseService restService;

    @Autowired
    public FriendController(UserService userService, RestResponseService restService) {
        this.userService = userService;
        this.restService = restService;
    }

    /*
     * 好友列表
     */
    @ResponseBody
    @RequestMapping(value = "{userId}/friendList", method = RequestMethod.GET)
    public JSONObject userFriendList(@RequestHeader("access-token") String accessToken) {
        // 当前用户
        UserEntity currentUser = userService.getUserByToken(accessToken);
        List<UserEntity> friendList = userService.getUserFriendList(currentUser.getId());
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
            return restService.success();
        }
        // 申请成为好友失败
        return restService.error(500, "can not apply friend");
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
            return restService.success();
        }
        // 没有通过
        return restService.error(500, "can not accept this friend");
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
            return restService.success();
        }
        // 拒绝失败 ( 比如重复的操作，或以及成为好友了 )
        return restService.error(500, "can not reject this friend");
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
            return restService.success();
        }
        // 不应该有取消失败的情况
        // 总应该最后，用户和此好友断开好友关系
        return restService.error(500, "can not cancel this friend");
    }
}
