/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.sms.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 阿里云短信相关属性.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "sms.aliyun", ignoreUnknownFields = true)
public class AliyunSmsProperty extends SmsCommonProperty {
    /**
     * 访问key.
     */
    private String accessKey;

    /**
     * 密匙.
     */
    private String secretKey;

    /**
     * 短信签名.
     */
    private String signName;
}
