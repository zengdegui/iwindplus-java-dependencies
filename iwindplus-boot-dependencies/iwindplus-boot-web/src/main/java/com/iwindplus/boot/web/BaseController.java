/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.web;

import com.iwindplus.boot.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 基础控制层类.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Slf4j
public class BaseController {
    /**
     * 请求.
     */
    @Autowired
    protected HttpServletRequest request;

    /**
     * 响应.
     */
    @Autowired
    protected HttpServletResponse response;

    /**
     * 获取request请求参数.
     *
     * @return Map<String, String>
     */
    public Map<String, String> getParameterMap() {
        return HttpUtil.getParams(request);
    }
}
