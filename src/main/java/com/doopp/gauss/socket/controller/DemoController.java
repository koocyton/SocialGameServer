package com.doopp.gauss.socket.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DemoController {

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
    public String chatRoom() {
        // return new ModelAndView("socket/demo/chat_room");
        return "socket/demo/chat_room";
    }

    @RequestMapping(value = "/mchat-room")
    public String mobileChatRoom() {
        return "socket/demo/mobile_chat_room";
    }
}
