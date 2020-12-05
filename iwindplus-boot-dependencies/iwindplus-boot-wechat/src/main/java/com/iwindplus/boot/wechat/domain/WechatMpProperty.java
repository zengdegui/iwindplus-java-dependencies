/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.wechat.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 微信公众号相关属性.
 *
 * @author zengdegui
 * @since 2018年10月10日
 */
@Data
@ConfigurationProperties(prefix = "wechat.mp")
public class WechatMpProperty {
    /**
     * 设置微信公众号的appid.
     */
    private String appId;

    /**
     * 设置微信公众号的Secret.
     */
    private String secret;

    /**
     * 设置微信公众号的token.
     */
    private String token;

    /**
     * 设置微信公众号的EncodingAESKey.
     */
    private String aesKey;

    /**
     * 重定向地址.
     */
    private String redirectUri;

    /**
     * 登录成功地址.
     */
    private String loginSuccessUri;

    /**
     * 网页授权的scope（默认：不弹出授权页面，直接跳转，只能获取用户openid）.
     */
    private String webScope;

    /**
     * 绑定用户地址.
     */
    private String bindUserUri;

    /**
     * 发送短信地址.
     */
    private String sendSmsUri;
}
