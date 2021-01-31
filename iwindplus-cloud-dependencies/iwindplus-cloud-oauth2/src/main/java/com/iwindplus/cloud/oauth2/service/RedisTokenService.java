/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.cloud.oauth2.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 资源服务器.
 *
 * @author zengdegui
 * @since 2020/4/1
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RedisTokenService implements ResourceServerTokenServices {
    private TokenStore tokenStore;

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken)
            throws AuthenticationException, InvalidTokenException {
        OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(accessToken);
        return oAuth2Authentication;
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        return tokenStore.readAccessToken(accessToken);
    }
}
