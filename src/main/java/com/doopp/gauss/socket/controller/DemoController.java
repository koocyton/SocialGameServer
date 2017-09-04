package com.doopp.gauss.socket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DemoController {

    @RequestMapping(value = "/ap/v1/web/socket")
    public String socket() {
        return "socket/demo/web_socket_demo";
    }

    @RequestMapping(value = "/chat-room")
    public String chatRoom() {
        return "socket/demo/chat_room";
    }
}
