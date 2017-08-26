package com.doopp.gauss.api.service;

import javax.servlet.http.HttpServletRequest;

/**
 * Web socket service
 *
 * Created by Henry on 2017/7/20.
 */
public interface WebSocketService {

    void sendStringToUser(String message, Long... usersId);

    void sendStringToAll(String message);

    boolean disconnectSocket(Long userId);
}
