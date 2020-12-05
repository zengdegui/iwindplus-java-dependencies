/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.cloud.oauth2.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 自定义网关配置.
 *
 * @author zengdegui
 * @since 2020年4月24日
 */
@Data
@ConfigurationProperties(prefix = "oauth2")
public class Oauth2Property {
    /**
     * 客户端Id.
     */
    private String clientId;

    /**
     * 客户端密钥.
     */
    private String clientSecret;

    /**
     * 校验token地址.
     */
    private String checkTokenUri;

    /**
     * jwt签名key.
     */
    private String jwtSigningKey;

    /**
     * 免鉴权地址.
     */
    private String[] permitUri;
}
