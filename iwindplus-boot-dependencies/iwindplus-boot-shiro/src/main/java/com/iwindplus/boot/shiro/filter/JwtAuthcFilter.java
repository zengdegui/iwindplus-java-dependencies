/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.shiro.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwindplus.boot.shiro.domain.ShiroProperty;
import com.iwindplus.boot.shiro.domain.constant.ShiroConstant;
import com.iwindplus.boot.shiro.domain.dto.JwtTokenDTO;
import com.iwindplus.boot.util.HttpUtil;
import com.iwindplus.boot.web.domain.enumerate.WebCodeEnum;
import com.iwindplus.boot.web.domain.vo.ResultVO;
import com.iwindplus.boot.web.i18n.I18nConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * 无状态认证过滤器.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Slf4j
public class JwtAuthcFilter extends BasicHttpAuthenticationFilter {
    @Autowired
    private ShiroProperty shiroProperty;

    @Autowired
    private I18nConfig i18nConfig;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 输出json.
     * 
     * @param httpServletResponse 响应
     */
    private void getException(HttpServletResponse httpServletResponse) {
        Integer status = HttpStatus.UNAUTHORIZED.value();
        String code = StringUtils.lowerCase(WebCodeEnum.UNAUTHORIZED.name());
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.UNAUTHORIZED.desc();
        ResultVO result = ResultVO.builder().code(code).message(message).build();
        try {
            String json = this.objectMapper.writeValueAsString(result);
            HttpUtil.getJson(status, json, httpServletResponse);
        } catch (JsonProcessingException ex) {
            log.error("Json processing exception [{}]", ex);
        }
    }

    /**
     * 判断用户是否想要登入.
     * 检测 header里面是否包含 access_token字段
     *
     * @param request  请求
     * @param response 响应
     * @return boolean
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader(ShiroConstant.ACCESS_TOKEN);
        if (token == null) {
            return false;
        }
        return true;
    }

    /**
     * 如果带有 token，则对 token 进行检查，否则直接通过.
     *
     * @param request     请求
     * @param response    响应
     * @param mappedValue mappedValue
     * @return boolean
     * @throws UnauthorizedException
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws UnauthorizedException {
        HttpServletResponse res = (HttpServletResponse) response;
        // 如果请求头存在token,则执行登陆操作,直接返回true
        if (isLoginAttempt(request, response)) {
            try {
                executeLogin(request, response);
                return true;
            } catch (Exception e) {
                responseError(response, e.getMessage());
                return false;
            }
        }
        this.getException(res);
        return false;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(ShiroConstant.ACCESS_TOKEN);
        JwtTokenDTO jwtToken = new JwtTokenDTO(token);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(jwtToken);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 对跨域提供支持.
     *
     * @param request  请求
     * @param response 响应
     * @return boolean
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers",
                httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 将非法请求跳转到 /unauthorized/**.
     *
     * @param response 响应
     * @param message  消息
     */
    private void responseError(ServletResponse response, String message) {
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            // 设置编码，否则中文字符在重定向时会变为空字符串
            message = URLEncoder.encode(message, "UTF-8");
            httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), message);
            httpServletResponse.sendRedirect(this.shiroProperty.getUnauthorizedUrl());
        } catch (IOException e) {
            log.error("IO exception [{}]", e);
        }
    }
}
