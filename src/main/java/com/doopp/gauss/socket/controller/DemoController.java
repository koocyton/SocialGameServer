package com.doopp.gauss.socket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DemoController {

    @RequestMapping(value = "/web-socket-demo")
    public String socket() {
        return "socket/demo/web_socket_demo";
    }

    @RequestMapping(value = "/chat-room")
    public String chatRoom() {
        return "socket/demo/chat_room";
    }

    @RequestMapping(value = "/mchat-room")
    public String mobileChatRoom() {
        return "socket/demo/mobile_chat_room";
    }
}
