/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.oss.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 阿里对象存储相关属性.
 *
 * @author zengdegui
 * @since 2018年9月1日
 */
@Data
@ConfigurationProperties(prefix = "oss.aliyun", ignoreUnknownFields = true)
public class AliyunOssProperty {
    /**
     * 访问key.
     */
    private String accessKey;

    /**
     * 密匙.
     */
    private String secretKey;

    /**
     * 空间名.
     */
    private String bucket;

    /**
     * 是否自定义访问域名.
     */
    private Boolean flagCustom;

    /**
     * 端点url.
     */
    private String endpoint;

    /**
     * 文件大小限制（单位字节）.
     */
    private Long maxFileSize;
}
