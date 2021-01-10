/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.cloud.feign.service;

import com.iwindplus.cloud.feign.domain.constant.FeignConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.UUID;

/**
 * 微服务之间feign调用请求头丢失的问题 加入微服务之间传递的唯一标识,便于追踪.
 *
 * @author zengdegui
 * @since 2020年4月23日
 */
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != attributes) {
            // 设置子线程共享
            RequestContextHolder.setRequestAttributes(attributes, true);
            deliveryHeader(template, attributes);
        }
    }

    private void deliveryHeader(RequestTemplate template, ServletRequestAttributes attributes) {
        HttpServletRequest httpServletRequest = attributes.getRequest();
        if (null != httpServletRequest) {
            // 传递表单acccess_token参数
            String accessToken = httpServletRequest.getParameter(OAuth2AccessToken.ACCESS_TOKEN);
            if (StringUtils.isNotBlank(accessToken)) {
                template.query(OAuth2AccessToken.ACCESS_TOKEN, accessToken);
            }
            bulidHeader(template, httpServletRequest);
            // 微服务之间传递的唯一标识,区分大小写所以通过httpServletRequest获取
            String requestId = httpServletRequest.getHeader(FeignConstant.X_REQUEST_ID);
            if (StringUtils.isBlank(requestId)) {
                String sid = String.valueOf(UUID.randomUUID());
                template.header(FeignConstant.X_REQUEST_ID, sid);
            }
        }
    }

    private void bulidHeader(RequestTemplate template, HttpServletRequest httpServletRequest) {
        // 传递请求头
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        if (null != headerNames) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String values = httpServletRequest.getHeader(name);
                // 跳过content-length
                if ("content-length".equals(name)) {
                    continue;
                }
                template.header(name, values);
            }
        }
    }
}
