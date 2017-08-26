package com.doopp.gauss.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.doopp.gauss.api.service.RestResponseService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回 Rest 的数据
 * Created by Henry on 2017/8/19.
 */
@Service
public class RestResponseServiceImpl implements RestResponseService {

    @Override
    public JSONObject error(HttpServletResponse response, int errorCode, String errorMessage) {
        response.setStatus(errorCode);
        return error(errorCode, errorMessage);
    }

    public JSONObject error(int errorCode, String errorMessage) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("errcode", errorCode);
        jsonObject.put("errmsg", errorMessage);
        return jsonObject;
    }

    public JSONObject helper(String message, String docUrl) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", message);
        jsonObject.put("documentation_url", docUrl);
        return jsonObject;
    }

    public JSONObject success() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 1);
        jsonObject.put("message", "success");
        return jsonObject;
    }
}
