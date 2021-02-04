/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.shiro.domain.dto;

import lombok.Builder;
import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * 无状态令牌类.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Data
@Builder
public class JwtTokenDTO implements AuthenticationToken {
    private static final long serialVersionUID = -7838912794581842158L;

    /**
     * 密钥.
     */
    private String token;

    public JwtTokenDTO(String token) {
        super();
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return this.token;
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }
}