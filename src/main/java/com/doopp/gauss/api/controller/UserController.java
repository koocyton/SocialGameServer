package com.doopp.gauss.api.controller;

import com.alibaba.fastjson.JSONObject;

import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.api.helper.UploadFileHelper;
import com.doopp.gauss.api.service.RoomService;
import com.doopp.gauss.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 用户的 Api Controller
 *
 * Created by henry on 2017/7/14.
 */
@RequestMapping(value = "api/v1/")
@SessionAttributes("currentUser")
@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    /*
     * 获取当前用户信息
     */
    @ResponseBody
    @RequestMapping(value = "user/me", method = RequestMethod.GET)
    public JSONObject me(@ModelAttribute("currentUser") UserEntity currentUser) {
        logger.info(" >>> " + currentUser);
        return currentUser.toJsonObject();
    }

    /*
     * 更新用户信息
     */
    @ResponseBody
    @RequestMapping(value = "user/me", method = RequestMethod.POST)
    public JSONObject updateMe(HttpServletRequest request,
                               @RequestParam("portrait") MultipartFile file,
                               @ModelAttribute("currentUser") UserEntity currentUser) throws IOException {
        // 更新昵称
        currentUser.setNickname(request.getParameter("nickname"));
        // 跟新头像
        if (file!=null) {
            String portraitUrl = UploadFileHelper.savePhoto(file, "/tmp", String.valueOf(currentUser.getId()));
            currentUser.setPortrait(portraitUrl);
        }
        // userDao.update(currentUser);
        return currentUser.toJsonObject();
    }

    /*
     * 随机加入房间
     */
    @ResponseBody
    @RequestMapping(value = "user/join-room", method = RequestMethod.GET)
    public JSONObject joinRoom(@ModelAttribute("currentUser") UserEntity currentUser,
                               @RequestParam("rid") int roomId) {
        roomService.joinRoom(currentUser, roomId);
        return currentUser.toJsonObject();
    }

    /*
     * 加入指定的房间
     */
    @ResponseBody
    @RequestMapping(value = "user/join-room", method = RequestMethod.GET)
    public JSONObject randomJoinRoom(@ModelAttribute("currentUser") UserEntity currentUser) {
        roomService.joinRoom(currentUser, 0);
        return currentUser.toJsonObject();
    }

    /*
     * 获取某用户信息
     */
    @ResponseBody
    @RequestMapping(value = "user/{userId}", method = RequestMethod.GET)
    public JSONObject userInfo(@PathVariable("userId") Long userId) {
        return userService.getUserInfo(userId).toJsonObject();
    }
}
