/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.shiro.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwindplus.boot.util.HttpUtil;
import com.iwindplus.boot.web.domain.enumerate.WebCodeEnum;
import com.iwindplus.boot.web.domain.vo.ResultVO;
import com.iwindplus.boot.web.i18n.I18nConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登陆过滤，器扩展自FormAuthenticationFilter：增加了针对ajax请求的处理.
 *
 * @author zengdegui
 * @since 2018年9月6日
 */
@Slf4j
public class FormAuthcFilter extends FormAuthenticationFilter {
    @Autowired
    private I18nConfig i18nConfig;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 判断是否允许访问,,返回false,则跳到onAccessDenied处理.
     *
     * @param request     请求
     * @param response    响应
     * @param mappedValue mappedValue
     * @return boolean
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        // 如果已经登陆，还停留在登陆页面，跳转到登陆成功页面
        if (null != getSubject(request, response) && getSubject(request, response).isAuthenticated()) {
            if (isLoginRequest(request, response)) {
                try {
                    // 重定向到成功地址
                    super.issueSuccessRedirect(request, response);
                    return true;
                } catch (Exception e) {
                    log.error("Authc authentication failed [{}]", e);
                }
            }
        }
        // 父类判断是否放行
        return super.isAccessAllowed(request, response, mappedValue);
    }

    /**
     * 表示当访问拒绝时是否已经处理了；如果返回true表示需要继续处理；如果返回false表示该拦截器实例已经处理了，将直接返回即可.
     * onAccessDenied是否执行取决于isAccessAllowed的值，如果返回true则onAccessDenied不会执行；如果返回false，执行onAccessDenied
     * 如果onAccessDenied也返回false，则直接返回，不会进入请求的方法（只有isAccessAllowed和onAccessDenied的情况下）
     *
     * @param request  请求
     * @param response 响应
     * @return boolean
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                return executeLogin(request, response);
            } else {
                return true;
            }
        } else {
            if (HttpUtil.isAjaxRequest(httpServletRequest)) {
                Integer status = HttpStatus.UNAUTHORIZED.value();
                String code = StringUtils.lowerCase(WebCodeEnum.UNAUTHORIZED.name());
                String msg = this.i18nConfig.getMessage(code);
                String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.UNAUTHORIZED.desc();
                ResultVO result = ResultVO.builder().code(code).message(message).build();
                String json = this.objectMapper.writeValueAsString(result);
                HttpUtil.getJson(status, json, httpServletResponse);
            } else {
                saveRequestAndRedirectToLogin(request, response);
            }
            return false;
        }
    }

    /**
     * 登录成功增加ajax支持.
     *
     * @param token    token
     * @param subject  subject
     * @param request  请求
     * @param response 响应
     * @return boolean
     * @throws Exception
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
            ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // 判断是否是ajax请求
        if (HttpUtil.isAjaxRequest(httpServletRequest)) {
            Integer status = HttpStatus.OK.value();
            String code = StringUtils.lowerCase(WebCodeEnum.SUCCESS.name());
            String msg = this.i18nConfig.getMessage(code);
            String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.SUCCESS.desc();
            ResultVO result = ResultVO.builder().code(code).message(message).build();
            String json = this.objectMapper.writeValueAsString(result);
            HttpUtil.getJson(status, json, httpServletResponse);
        } else {
            // 重定向到成功地址
            super.issueSuccessRedirect(request, response);
        }
        return false;
    }

    /**
     * 登录失败增加ajax支持.
     *
     * @param token    token
     * @param e        异常
     * @param request  请求
     * @param response 响应
     * @return boolean
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
            ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        // 判断是否是ajax请求
        if (HttpUtil.isAjaxRequest(httpServletRequest)) {
            Integer status = HttpStatus.BAD_REQUEST.value();
            String code = StringUtils.lowerCase(WebCodeEnum.FAILED.name());
            String msg = this.i18nConfig.getMessage(code);
            String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.FAILED.desc();
            ResultVO result = ResultVO.builder().code(code).message(message).build();
            String json;
            try {
                json = this.objectMapper.writeValueAsString(result);
                HttpUtil.getJson(status, json, httpServletResponse);
            } catch (JsonProcessingException ex) {
                log.error("Json processing exception [{}]", ex);
            }
            // 过滤器链停止.
        } else {
            // 重定向到登录地址.
            try {
                super.saveRequestAndRedirectToLogin(request, response);
            } catch (IOException ex) {
                log.error("Login failed [{}]", ex);
            }
        }
        return false;
    }
}
