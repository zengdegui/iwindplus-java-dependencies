/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.oss.domain;

import com.iwindplus.boot.oss.domain.enumerate.QiniuOssZoneEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 七牛云对象存储相关属性.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Data
@ConfigurationProperties(prefix = "oss.qiniu", ignoreUnknownFields = true)
public class QiniuOssProperty {
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
     * 访问域名（前缀）.
     */
    private String accessDomain;

    /**
     * 是否开启断点上传.
     */
    private Boolean breakpointEnabled = false;

    /**
     * 服务器区域.
     */
    private QiniuOssZoneEnum zone;

    /**
     * 文件大小限制（单位字节）.
     */
    private Long maxFileSize;
}
