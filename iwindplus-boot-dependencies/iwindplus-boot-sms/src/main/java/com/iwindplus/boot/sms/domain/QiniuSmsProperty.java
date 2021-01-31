/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.sms.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 七牛云短信相关属性.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "sms.qiniu", ignoreUnknownFields = true)
public class QiniuSmsProperty extends SmsCommonProperty {
    /**
     * 访问key.
     */
    private String accessKey;

    /**
     * 密匙.
     */
    private String secretKey;
}
