package com.doopp.gauss.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.doopp.gauss.api.utils.CommonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class DemoController {

    // private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    @ResponseBody
    @RequestMapping(value = "/aaa", method = RequestMethod.GET)
    public JSONObject bbq() {
        return new JSONObject();
    }

    @RequestMapping(value = "/web-socket-demo")
    public String socket() {
        return "socket/demo/web_socket_demo";
    }

    @RequestMapping(value = "/chat-room")
    public String chatRoom(HttpServletRequest request) {
        // logger.info(" >>> " + JarToolUtil.getJarDir() );
        return CommonUtils.isMobileClient(request) ? "socket/demo/mobile_chat_room" : "socket/demo/chat_room";
    }
}
