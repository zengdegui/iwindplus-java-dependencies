/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.wechat.domain;

import lombok.Data;
import me.chanjar.weixin.common.api.WxConsts;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 微信公众号相关属性.
 *
 * @author zengdegui
 * @since 2018/10/10
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
     * 网页授权的scope（默认：不弹出授权页面，直接跳转，只能获取用户openid）.
     */
    private String webScope = WxConsts.OAuth2Scope.SNSAPI_BASE;

    /**
     * 重定向授权地址.
     */
    private String redirectUri;

    /**
     * 绑定成功后跳转地址.
     */
    private String bindSuccessUri;

    /**
     * 绑定用户地址.
     */
    private String bindUserUri;
}
