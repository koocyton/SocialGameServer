package com.doopp.gauss.api.controller;

import com.alibaba.fastjson.JSONObject;

import com.doopp.gauss.api.entity.RoomEntity;
import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.api.entity.dto.UserDTO;
import com.doopp.gauss.api.utils.CommonUtils;
import com.doopp.gauss.api.utils.UploadFileHelper;
import com.doopp.gauss.api.service.RestResponseService;
import com.doopp.gauss.api.service.RoomService;
import com.doopp.gauss.api.service.UserService;
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

    // private final Logger logger = LoggerFactory.getLogger(getClass());

    private final UserService userService;

    private final RoomService roomService;

    private final RestResponseService restService;

    @Autowired
    public UserController(UserService userService, RoomService roomService, RestResponseService restService) {
        this.userService = userService;
        this.roomService = roomService;
        this.restService = restService;
    }

    /*
     * 获取当前用户信息
     */
    @ResponseBody
    @RequestMapping(value = "user/me", method = RequestMethod.GET)
    public UserDTO me(@RequestHeader("access-token") String accessToken) {
        // 当前用户
        UserEntity currentUser = userService.getUserByToken(accessToken);
        return CommonUtils.modelMap(currentUser, UserDTO.class);
    }

    /*
     * 更新用户信息
     */
    @ResponseBody
    @RequestMapping(value = "user/me", method = RequestMethod.POST)
    public UserDTO updateMe(HttpServletRequest request,
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
        return CommonUtils.modelMap(currentUser, UserDTO.class);
    }

    /*
     * 获取某用户信息
     */
    @ResponseBody
    @RequestMapping(value = "user/{userId}", method = RequestMethod.GET)
    public UserDTO userInfo(@PathVariable("userId") Long userId) {
        return CommonUtils.modelMap(userService.getUserInfo(userId), UserDTO.class);
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
            return restService.error(500, "Can`t create room !");
        }
        return restService.data(roomEntity);
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
            return restService.error(500, "Can`t join room !");
        }
        return restService.data(roomEntity);
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
            return restService.error(500, "Can`t join room !");
        }
        return restService.data(roomEntity);
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
            return restService.error(500, "Can`t leave room !");
        }
        return restService.data(roomEntity);
    }
}
