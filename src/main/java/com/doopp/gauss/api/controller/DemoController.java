package com.doopp.gauss.api.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

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
    public String chatRoom(HttpServletRequest request) {
        return isMobileClient(request) ? "socket/demo/mobile_chat_room" : "socket/demo/chat_room";
    }

    private boolean isMobileClient(HttpServletRequest request) {
        String ua = request.getHeader("User-Agent");
        String[] agent = { "Android", "iPhone", "iPod", "iPad", "Windows Phone", "MQQBrowser" };
        boolean flag = false;
        if (!ua.contains("Windows NT") || (ua.contains("Windows NT") && ua.contains("compatible; MSIE 9.0;"))) {
            // 排除 苹果桌面系统
            if (!ua.contains("Windows NT") && !ua.contains("Macintosh")) {
                for (String item : agent) {
                    if (ua.contains(item)) {
                        flag = true;
                        break;
                    }
                }
            }
        }
        return flag;
    }
}
