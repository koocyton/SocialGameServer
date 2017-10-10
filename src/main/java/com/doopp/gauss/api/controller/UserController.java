package com.doopp.gauss.api.controller;

import com.alibaba.fastjson.JSONObject;

import com.doopp.gauss.api.entity.RoomEntity;
import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.api.utils.UploadFileHelper;
import com.doopp.gauss.api.service.RestResponseService;
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
@Controller
@RequestMapping(value = "api/v1/")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RestResponseService restResponse;

    /*
     * 获取当前用户信息
     */
    @ResponseBody
    @RequestMapping(value = "user/me", method = RequestMethod.GET)
    public JSONObject me(@RequestHeader("access-token") String accessToken) {
        // 当前用户
        UserEntity currentUser = userService.getUserByToken(accessToken);
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
                               @RequestHeader("access-token") String accessToken) throws IOException {
        // 当前用户
        UserEntity currentUser = userService.getUserByToken(accessToken);
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
     * 获取某用户信息
     */
    @ResponseBody
    @RequestMapping(value = "user/{userId}", method = RequestMethod.GET)
    public JSONObject userInfo(@PathVariable("userId") Long userId) {
        return userService.getUserInfo(userId).toJsonObject();
    }

    /*
     * 创建新房间
     */
    @ResponseBody
    @RequestMapping(value = "user/create-room", method = RequestMethod.GET)
    public JSONObject createRoom(@RequestHeader("access-token") String accessToken) {
        // 当前用户
        UserEntity currentUser = userService.getUserByToken(accessToken);
        // 创建了新房间
        RoomEntity roomEntity = roomService.userCreateRoom(currentUser);
        if (roomEntity==null) {
            return restResponse.error(500, "Can`t create room !");
        }
        return restResponse.data(roomEntity);
    }

    /*
     * 加入指定房间
     */
    @ResponseBody
    @RequestMapping(value = "user/join-room/{roomId}", method = RequestMethod.GET)
    public JSONObject joinRoom(@RequestHeader("access-token") String accessToken,
                               @PathVariable("roomId") int roomId) {
        // 当前用户
        UserEntity currentUser = userService.getUserByToken(accessToken);
        // 用户加入到指定 ID 的房间
        RoomEntity roomEntity = roomService.userJoinRoom(currentUser, roomId);
        if (roomEntity==null) {
            return restResponse.error(500, "Can`t join room !");
        }
        return restResponse.data(roomEntity);
    }

    /*
     * 随机加入房间
     */
    @ResponseBody
    @RequestMapping(value = "user/random-room", method = RequestMethod.GET)
    public JSONObject randomJoinRoom(@RequestHeader("access-token") String accessToken) {
        // 当前用户
        UserEntity currentUser = userService.getUserByToken(accessToken);
        // logger.info(" >>> " + currentUser);
        // 用户加入到指定 ID 的房间
        RoomEntity roomEntity = roomService.userJoinFreeRoom(currentUser);
        // logger.info(" >>> roomEntity.toString() " + roomEntity.toString());
        if (roomEntity==null) {
            return restResponse.error(500, "Can`t join room !");
        }
        return restResponse.data(roomEntity);
    }

    /*
     * 用户离开房间
     */
    @ResponseBody
    @RequestMapping(value = "user/leave-room", method = RequestMethod.GET)
    public JSONObject leaveRoom(@RequestHeader("access-token") String accessToken) {
        // 当前用户
        UserEntity currentUser = userService.getUserByToken(accessToken);
        // 用户离开房间
        RoomEntity roomEntity = roomService.userLeaveRoom(currentUser);
        //
        if (roomEntity==null) {
            return restResponse.error(500, "Can`t leave room !");
        }
        return restResponse.data(roomEntity);
    }
}
