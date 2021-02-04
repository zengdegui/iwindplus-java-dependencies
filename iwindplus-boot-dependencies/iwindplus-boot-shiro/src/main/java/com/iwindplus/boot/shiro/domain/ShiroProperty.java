/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.shiro.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * shiro权限相关属性.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Data
@ConfigurationProperties(prefix = "shiro", ignoreUnknownFields = true)
public class ShiroProperty {
    /**
     * 登录地址.
     */
    private String loginUrl = "/login";

    /**
     * 登录成功地址.
     */
    private String successUrl = "/index";

    /**
     * 无权限地址.
     */
    private String unauthorizedUrl = "/unauthorized";

    /**
     * 设置调度时间间隔，单位:秒，默认就是1 小时.
     */
    private Integer sessionValid = 3600;

    /**
     * sessionId cookie名称.
     */
    private String sessionidCookieName = "sid";

    /**
     * 记住密码cookie名称.
     */
    private String rememberName = "rememberMe";

    /**
     * 记住密码cookie失效时间（一周），单位:秒.
     */
    private Integer rememberMeTimeout = 604800;

    /**
     * 记住密码cookie加密密匙.
     */
    private String rememberCipherKey = "3AvVhmFLUs0KTA3Kprsdag==";

    /**
     * session缓存key前缀.
     */
    private String sessionCacheKeyPrefix = "shiro-session:";

    /**
     * 活动当前session缓存名.
     */
    private String activeSessionsCacheName = "shiro-activeSessionCache";

    /**
     * 是否启用认证授权数据缓存，不配置默认不启用.
     */
    private Boolean authCacheEnabled = false;

    /**
     * 设置全局会话，缓存超时时间，单位秒.
     */
    private Integer cacheTimeout = 7200;

    /**
     * 缓存key前缀.
     */
    private String cacheKeyPrefix = "shiro-cache:";

    /**
     * 认证缓存名.
     */
    private String authcCacheName = "shiro-authenticationCache";

    /**
     * 鉴权缓存名.
     */
    private String authzCacheName = "shiro-authorizationCache";

    /**
     * 认证key.
     */
    private String filterAuthc = "authc";

    /**
     * 满足任一角色key.
     */
    private String filterRoles = "roles";

    /**
     * 满足任一权限key.
     */
    private String filterPerms = "perms";

}
