package com.doopp.gauss.api.utils;

import com.google.common.base.Strings;
import org.modelmapper.ModelMapper;

import javax.servlet.http.HttpServletRequest;

public class CommonUtils {

    /**
     * Returns 是否是移动客户端
     *
     * @param request Http Servlet Request
     * @return true or false
     */
    public static boolean isMobileClient(HttpServletRequest request) {
        String ua = request.getHeader("User-Agent");
        String[] agent = {"Android", "iPhone", "iPod", "iPad", "Windows Phone", "MQQBrowser"};
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

    /**
     * Returns 客户端的 IP
     *
     * @param request Http Servlet Request
     * @return true or false
     */
    public static String clientIp(HttpServletRequest request)
    {
        String ip = request.getHeader("X-Forwarded-For");
        //
        if (Strings.isNullOrEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (Strings.isNullOrEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    public static <D> D modelMap(Object source, Class<D> destinationType) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(source, destinationType);
    }
}
