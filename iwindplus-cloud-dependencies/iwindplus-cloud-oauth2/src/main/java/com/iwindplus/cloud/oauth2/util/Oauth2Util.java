/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.cloud.oauth2.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.ReflectUtil;
import com.iwindplus.cloud.oauth2.domain.Oauth2Property;
import com.iwindplus.cloud.oauth2.domain.vo.UserDetailsVO;
import com.iwindplus.cloud.oauth2.service.CustomJwtTokenService;
import com.iwindplus.cloud.oauth2.service.RedisTokenService;
import com.iwindplus.cloud.oauth2.token.CustomJwtTokenEnhancer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Map;

/**
 * 认证信息帮助类.
 *
 * @author zengdegui
 * @since 2020/4/1
 */
@Slf4j
public class Oauth2Util {
    /**
     * 构建jwtToken转换器.
     *
     * @param property 属性
     * @return JwtAccessTokenConverter
     * @throws Exception
     */
    public static JwtAccessTokenConverter buildJwtTokenEnhancer(Oauth2Property property) throws Exception {
        JwtAccessTokenConverter converter = new CustomJwtTokenEnhancer();
        converter.setSigningKey(property.getJwtSigningKey());
        converter.afterPropertiesSet();
        return converter;
    }

    /**
     * 构建自定义远程Token服务类.
     *
     * @param property 属性
     * @return RemoteTokenServices
     */
    public static RemoteTokenServices buildRemoteTokenServices(Oauth2Property property) {
        // 使用自定义系统用户凭证转换器
        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setCheckTokenEndpointUrl(property.getCheckTokenUri());
        tokenServices.setClientId(property.getClientId());
        tokenServices.setClientSecret(property.getClientSecret());
        tokenServices.setAccessTokenConverter(accessTokenConverter);
        log.info("buildRemoteTokenServices[{}]", tokenServices);
        return tokenServices;
    }

    /**
     * 构建资源服务器JwtToken服务类.
     *
     * @param property 属性
     * @return ResourceServerTokenServices
     * @throws Exception
     */
    public static ResourceServerTokenServices buildJwtTokenServices(Oauth2Property property) throws Exception {
        // 使用自定义系统用户凭证转换器
        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        CustomJwtTokenService tokenServices = new CustomJwtTokenService();
        // 这里的签名key 保持和认证中心一致
        JwtAccessTokenConverter converter = buildJwtTokenEnhancer(property);
        JwtTokenStore jwtTokenStore = new JwtTokenStore(converter);
        tokenServices.setTokenStore(jwtTokenStore);
        tokenServices.setJwtAccessTokenConverter(converter);
        tokenServices.setDefaultAccessTokenConverter(accessTokenConverter);
        log.info("buildJwtTokenServices[{}]", tokenServices);
        return tokenServices;
    }

    /**
     * 构建资源服务器RedisToken服务类.
     *
     * @param redisConnectionFactory 工厂
     * @return ResourceServerTokenServices
     * @throws Exception
     */
    public static ResourceServerTokenServices buildRedisTokenServices(RedisConnectionFactory redisConnectionFactory)
            throws Exception {
        RedisTokenService tokenServices = new RedisTokenService();
        // 这里的签名key 保持和认证中心一致
        TokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
        tokenServices.setTokenStore(tokenStore);
        log.info("buildRedisTokenServices[{}]", tokenServices);
        return tokenServices;
    }

    /**
     * 获取认证用户信息.
     *
     * @return UserDetailsVO
     */
    @SuppressWarnings({"rawtypes"})
    public static UserDetailsVO getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null != authentication && authentication.isAuthenticated()
                && authentication instanceof OAuth2Authentication) {
            if (authentication.getPrincipal() instanceof UserDetailsVO) {
                UserDetailsVO userDetails = (UserDetailsVO) authentication.getPrincipal();
                return userDetails;
            }
            if (authentication.getPrincipal() instanceof Map) {
                Map principal = (Map) authentication.getPrincipal();
                UserDetailsVO userDetails = BeanUtil.mapToBean(principal, UserDetailsVO.class, false,
                        CopyOptions.create().setIgnoreError(false));
                return userDetails;
            }
        }
        return null;
    }

    /**
     * 更新用户.
     *
     * @param tokenStore tokenStore
     * @param entity     用户信息
     */
    public static void updateUser(TokenStore tokenStore, UserDetailsVO entity) {
        if (entity == null) {
            return;
        }
        Assert.notNull(entity.getClientId(), "客户端ID不能为空");
        Assert.notNull(entity.getUsername(), "用户名不能为空");
        // 动态更新客户端生成的token
        Collection<OAuth2AccessToken> accessTokens = tokenStore.findTokensByClientIdAndUserName(entity.getClientId(),
                entity.getUsername());
        if (CollectionUtils.isNotEmpty(accessTokens)) {
            updateToken(tokenStore, entity, accessTokens);
        }
    }

    private static void updateToken(TokenStore tokenStore, UserDetailsVO entity,
            Collection<OAuth2AccessToken> accessTokens) {
        accessTokens.forEach(accessToken -> {
            // 由于没有set方法,使用反射机制强制赋值
            OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(accessToken);
            if (null != oAuth2Authentication) {
                Authentication authentication = oAuth2Authentication.getUserAuthentication();
                ReflectUtil.setFieldValue(authentication, "principal", entity);
                // 移除access_token
                tokenStore.removeAccessToken(accessToken);
                // 移除refresh_token
                if (null != accessToken.getRefreshToken()) {
                    tokenStore.removeRefreshToken(accessToken.getRefreshToken());
                }
                // 重新保存
                tokenStore.storeAccessToken(accessToken, oAuth2Authentication);
            }
        });
    }

    /**
     * 更新客户端权限.
     *
     * @param tokenStore  tokenStore
     * @param clientId    客户端
     * @param authorities 权限
     */
    public static void updateClientAuthorities(TokenStore tokenStore, String clientId,
            Collection<? extends GrantedAuthority> authorities) {
        if (authorities == null) {
            return;
        }
        // 动态更新客户端生成的token
        Collection<OAuth2AccessToken> accessTokens = tokenStore.findTokensByClientId(clientId);
        if (CollectionUtils.isNotEmpty(accessTokens)) {
            updateAuthorities(tokenStore, authorities, accessTokens);
        }
    }

    private static void updateAuthorities(TokenStore tokenStore, Collection<? extends GrantedAuthority> authorities,
            Collection<OAuth2AccessToken> accessTokens) {
        accessTokens.forEach(accessToken -> {
            // 由于没有set方法,使用反射机制强制赋值
            OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(accessToken);
            if (null != oAuth2Authentication) {
                Collection<GrantedAuthority> authorities2 = oAuth2Authentication.getAuthorities();
                ReflectUtil.setFieldValue(authorities2, "authorities", authorities);
                // 移除access_token
                tokenStore.removeAccessToken(accessToken);
                // 移除refresh_token
                if (null != accessToken.getRefreshToken()) {
                    tokenStore.removeRefreshToken(accessToken.getRefreshToken());
                }
                // 重新保存
                tokenStore.storeAccessToken(accessToken, oAuth2Authentication);
            }
        });
    }
}
