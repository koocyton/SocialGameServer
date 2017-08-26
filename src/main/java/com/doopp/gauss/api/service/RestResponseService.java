package com.doopp.gauss.api.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 返回的 JSON Service 处理
 *
 * Created by Henry on 2017/8/19.
 */
public interface RestResponseService {

    JSONObject error(HttpServletResponse response, int errorCode, String errorMessage);

    JSONObject error(int errorCode, String errorMessage);

    JSONObject helper(String message, String docUrl);

    JSONObject success();

    static void writeErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(501);
        String data = "{\"errcode\":501, \"errmsg\":\"" + message + "\"}";
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(data);
    }
}
