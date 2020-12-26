/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.cloud.oauth2.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwindplus.boot.util.HttpUtil;
import com.iwindplus.boot.web.domain.vo.ResultVO;
import com.iwindplus.cloud.web.exception.CloudGlobalExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义未认证处理.
 *
 * @author zengdegui
 * @since 2020年3月26日
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Autowired
    private CloudGlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        ResponseEntity<ResultVO> responseEntity = this.globalExceptionHandler.exception(exception);
        ResultVO result = responseEntity.getBody();
        if (null != result) {
            Integer status = HttpStatus.UNAUTHORIZED.value();
            String json = this.objectMapper.writeValueAsString(result);
            HttpUtil.getJson(status, json, response);
        }
    }
}