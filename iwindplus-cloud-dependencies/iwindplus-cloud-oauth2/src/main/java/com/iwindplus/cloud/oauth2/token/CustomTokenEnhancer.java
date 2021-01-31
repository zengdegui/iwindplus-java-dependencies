/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.cloud.oauth2.token;

import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Maps;
import com.iwindplus.cloud.oauth2.domain.constant.Oauth2Constant;
import com.iwindplus.cloud.oauth2.domain.vo.UserDetailsVO;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;

import java.util.Map;

/**
 * 用户自定义accessToken，增加额外信息.
 *
 * @author zengdegui
 * @since 2019/10/12
 */
public class CustomTokenEnhancer extends TokenEnhancerChain {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(accessToken);
        token.setValue(this.buildTokenValue());
        if (token.getRefreshToken() != null) {
            if (token.getRefreshToken() instanceof DefaultExpiringOAuth2RefreshToken) {
                DefaultExpiringOAuth2RefreshToken refreshToken = (DefaultExpiringOAuth2RefreshToken) token
                        .getRefreshToken();
                token.setRefreshToken(
                        new DefaultExpiringOAuth2RefreshToken(this.buildTokenValue(), refreshToken.getExpiration()));
            } else {
                token.setRefreshToken(new DefaultOAuth2RefreshToken(this.buildTokenValue()));
            }
        }
        if (null != authentication.getPrincipal() && authentication.getPrincipal() instanceof UserDetailsVO) {
            // 设置额外用户信息
            UserDetailsVO userDetails = ((UserDetailsVO) authentication.getPrincipal());
            userDetails.setClientId(authentication.getOAuth2Request().getClientId());
            Map<String, Object> additionalInformation = Maps.newHashMap();
            additionalInformation.put(Oauth2Constant.OPENID, userDetails.getOpenid());
            token.setAdditionalInformation(additionalInformation);
        }
        return super.enhance(token, authentication);
    }

    private String buildTokenValue() {
        String tokenValue = IdUtil.objectId();
        return tokenValue;
    }
}
