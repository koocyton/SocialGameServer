package com.doopp.gauss.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.api.service.UserService;
import com.doopp.gauss.api.service.impl.LoginServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    /*
     * 好友列表
     */
    @ResponseBody
    @RequestMapping(value = "{userId}/friendList", method = RequestMethod.GET)
    public JSONObject userFriendList(@PathVariable("userId") Long userId) {
        List<Map<String, UserEntity>> friendList = userService.getUserFriendList(userId);
        for(Map tmp:friendList)
        {
            logger.info(tmp.toString());
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("friendList", friendList);
        jsonObject.put("user", userService.getUserInfo(userId).toJsonObject());
        return jsonObject;
    }

//    /*
//     * 添加好友
//     */
//    @ResponseBody
//    @RequestMapping(value = "become-friend/{userId}", method = RequestMethod.GET)
//    public JSONObject becomeFriend(@PathVariable("userId") Long userId) {
//        this.currentUser.addFriend(userId);
//        return this.currentUser.toJsonObject();
//    }
//
//    /*
//     * 取消好友
//     */
//    @ResponseBody
//    @RequestMapping(value = "cancel-friend/{userId}", method = RequestMethod.GET)
//    public JSONObject removeFriend(@PathVariable("userId") Long userId) {
//        this.currentUser.delFriend(userId);
//        return this.currentUser.toJsonObject();
//    }
}
