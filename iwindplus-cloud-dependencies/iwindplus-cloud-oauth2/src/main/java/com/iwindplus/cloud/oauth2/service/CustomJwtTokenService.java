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
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Map;

/**
 * 自定义jwt token服务.
 *
 * @author zengdegui
 * @since 2020年4月24日
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomJwtTokenService implements ResourceServerTokenServices {
    private TokenStore tokenStore;
    private DefaultAccessTokenConverter defaultAccessTokenConverter;
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken)
            throws AuthenticationException, InvalidTokenException {
        OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(accessToken);
        Map<String, ?> map = jwtAccessTokenConverter.convertAccessToken(readAccessToken(accessToken),
                oAuth2Authentication);
        return defaultAccessTokenConverter.extractAuthentication(map);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        return tokenStore.readAccessToken(accessToken);
    }
}
