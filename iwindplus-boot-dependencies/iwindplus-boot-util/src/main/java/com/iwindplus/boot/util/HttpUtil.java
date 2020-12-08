/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * HttpUtil操作工具类.
 *
 * @author zengdegui
 * @since 2018年9月1日
 */
@Slf4j
public class HttpUtil extends cn.hutool.http.HttpUtil {
    /**
     * Wap网关Via头信息中特有的描述信息.
     */
    private static String[] mobileGateWayHeaders = new String[]{"ZXWAP", "chinamobile.com", "monternet.com", "infoX",
            "XMS 724Solutions HTG", "Bytemobile",};
    /**
     * 电脑上的IE或Firefox浏览器等的User-Agent关键词.
     */
    private static String[] pcHeaders = new String[]{"Windows 98", "Windows ME", "Windows 2000", "Windows XP",
            "Windows NT", "Ubuntu"};
    /**
     * 手机浏览器的User-Agent里的关键词.
     */
    private static String[] mobileUserAgents = new String[]{"Nokia", "SAMSUNG", "MIDP-2", "CLDC1.1", "SymbianOS",
            "MAUI", "UNTRUSTED/1.0", "Windows CE", "iPhone", "iPad", "Android", "BlackBerry", "UCWEB", "ucweb", "BREW",
            "J2ME", "YULONG", "YuLong", "COOLPAD", "TIANYU", "TY-", "K-Touch", "Haier", "DOPOD", "Lenovo", "LENOVO",
            "HUAQIN", "AIGO-", "CTC/1.0", "CTC/2.0", "CMCC", "DAXIAN", "MOT-", "SonyEricsson", "GIONEE", "HTC", "ZTE",
            "HUAWEI", "webOS", "GoBrowser", "IEMobile", "WAP2.0"};

    /**
     * 获取服务器url.
     *
     * @param request 请求
     * @return String
     */
    public static String getServerUrl(HttpServletRequest request) {
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath();
        return url;
    }

    /**
     * http获取请求参数.
     *
     * @param request 请求
     * @return Map<String, String>
     */
    public static Map<String, String> getParams(HttpServletRequest request) {
        log.debug(">>>>" + request.getQueryString());
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        if (MapUtils.isEmpty(requestParams)) {
            return null;
        }
        requestParams.entrySet().stream().forEach(entity -> {
            String name = entity.getKey();
            String[] values = entity.getValue();
            String valueStr = StringUtils.join(values, ",");
            try {
                valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                log.error("Unsupported encoding exception [{}]", e);
            }
            params.put(name, valueStr);
        });
        return params;
    }

    /**
     * 判读是否是ajax请求.
     *
     * @param request 请求
     * @return boolean
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("x-requested-with");
        if (requestedWith != null && StringUtils.equalsIgnoreCase(requestedWith, "XMLHttpRequest")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * http响应信息输出.
     *
     * @param status   状态码
     * @param result   结果
     * @param response 响应
     */
    public static void getJson(Integer status, String result, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(status);
            writer = response.getWriter();
            writer.write(result);
            writer.flush();
        } catch (IOException e) {
            log.error("Abnormal response output [{}]", e);
        } finally {
            if (null != writer) {
                writer.close();
            }
        }
    }

    /**
     * 构建异常信息.
     *
     * @param ex      异常
     * @param request 请求
     * @return String
     */
    public static String buildMessage(Throwable ex, HttpServletRequest request) {
        StringBuilder message = new StringBuilder("Failed to handle request [");
        message.append(request.getMethod());
        message.append(" ");
        message.append(request.getRequestURI());
        message.append("]");
        if (ex != null && StringUtils.isNotBlank(ex.getMessage())) {
            message.append(": ");
            message.append(ex.getMessage());
        }
        return message.toString();
    }

    /**
     * 根据请求头判断是手机还是电脑.
     *
     * @param request 请求
     * @return boolean
     */
    public static boolean isMobileDevice(HttpServletRequest request) {
        boolean isMobile = false;
        boolean pcFlag = false;
        boolean mobileFlag = false;
        String via = request.getHeader("Via");
        String userAgent = request.getHeader("user-agent");
        for (int i = 0; via != null && !via.trim().equals("") && i < mobileGateWayHeaders.length; i++) {
            if (via.contains(mobileGateWayHeaders[i])) {
                mobileFlag = true;
                break;
            }
        }
        for (int i = 0; !mobileFlag && userAgent != null && !userAgent.trim().equals("")
                && i < mobileUserAgents.length; i++) {
            if (userAgent.contains(mobileUserAgents[i])) {
                mobileFlag = true;
                break;
            }
        }
        for (int i = 0; userAgent != null && !userAgent.trim().equals("") && i < pcHeaders.length; i++) {
            if (userAgent.contains(pcHeaders[i])) {
                pcFlag = true;
                break;
            }
        }
        if (mobileFlag == true && pcFlag == false) {
            isMobile = true;
        }
        return isMobile;
    }

    /**
     * 判断是否为IOS系统访问.
     *
     * @param request 请求
     * @return String
     */
    public static boolean isIOSDevice(HttpServletRequest request) {
        boolean isMobile = false;
        final String[] ios_sys = {"iPhone", "iPad", "iPod"};
        String userAgent = request.getHeader("user-agent");
        for (int i = 0; !isMobile && userAgent != null && !userAgent.trim().equals("") && i < ios_sys.length; i++) {
            if (userAgent.contains(ios_sys[i])) {
                isMobile = true;
                break;
            }
        }
        return isMobile;
    }

    /**
     * 判断是否是微信访问.
     *
     * @param request 请求
     * @return boolean
     */
    public static boolean isWeChat(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent").toLowerCase();
        return userAgent == null || userAgent.indexOf("micromessenger") == -1 ? false : true;
    }
}
