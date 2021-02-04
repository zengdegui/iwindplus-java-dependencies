/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.sms.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 凌凯短信相关属性.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "sms.lingkai", ignoreUnknownFields = true)
public class LingkaiSmsProperty extends SmsCommonProperty {
    /**
     * 请求路径.
     */
    private String url = "https://mb345.com/ws/BatchSend2.aspx";

    /**
     * 用户名.
     */
    private String username;

    /**
     * 密码.
     */
    private String password;
}
