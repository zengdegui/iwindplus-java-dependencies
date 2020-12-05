/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.wechat.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 微信小程序相关属性.
 *
 * @author zengdegui
 * @since 2018年9月1日
 */
@Data
@ConfigurationProperties(prefix = "wechat.ma")
public class WechatMaProperty {
    /**
     * 设置微信小程序的appid.
     */
    private String appId;

    /**
     * 设置微信小程序的Secret.
     */
    private String secret;

    /**
     * 设置微信小程序消息服务器配置的token.
     */
    private String token;

    /**
     * 设置微信小程序消息服务器配置的EncodingAESKey.
     */
    private String aesKey;

    /**
     * 消息格式，XML或者JSON.
     */
    private String msgDataFormat = "JSON";
}