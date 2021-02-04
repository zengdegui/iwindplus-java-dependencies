/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.shiro.service.realm;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.iwindplus.boot.shiro.domain.dto.JwtTokenDTO;
import com.iwindplus.boot.shiro.domain.enumerate.ShiroCodeEnum;
import com.iwindplus.boot.shiro.domain.vo.LoginVO;
import com.iwindplus.boot.shiro.service.ShiroService;
import com.iwindplus.boot.util.JwtUtil;
import com.iwindplus.boot.web.exception.BaseShiroAuthcException;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.List;
import java.util.Map;

/**
 * 无状态方式relam.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Setter
public class JwtRealm extends AuthorizingRealm {
    private ShiroService shiroService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtTokenDTO;
    }

    /**
     * 认证.
     *
     * @param authcToken token
     * @return AuthenticationInfo
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
            throws AuthenticationException {
        JwtTokenDTO param = (JwtTokenDTO) authcToken;
        // 获取token.
        String token = param.getToken();
        Map<String, String> map = null;
        try {
            map = JwtUtil.verifyToken(token);
        } catch (TokenExpiredException e) {
            throw new BaseShiroAuthcException(ShiroCodeEnum.ACCESS_TOKEN_EXPIRED.value(),
                    ShiroCodeEnum.ACCESS_TOKEN_EXPIRED.desc());
        }
        String userName = map.get("username");
        // 检查账号.
        LoginVO data = this.shiroService.getByUserName(userName);

        // 没找到帐号.
        if (null == data) {
            throw new BaseShiroAuthcException(ShiroCodeEnum.ACCOUNT_NOT_EXIST.value(),
                    ShiroCodeEnum.ACCOUNT_NOT_EXIST.desc());
        }
        // 帐号锁定.
        Integer status = data.getStatus();
        if (0 == status) {
            throw new BaseShiroAuthcException(ShiroCodeEnum.ACCOUNT_DISABLED.value(),
                    ShiroCodeEnum.ACCOUNT_DISABLED.desc());
        } else if (2 == status) {
            throw new BaseShiroAuthcException(ShiroCodeEnum.ACCOUNT_LOCKED.value(),
                    ShiroCodeEnum.ACCOUNT_LOCKED.desc());
        }
        // 放入shiro.调用CredentialsMatcher检验.
        return new SimpleAuthenticationInfo(data, token, this.getName());

    }

    /**
     * 鉴权.
     *
     * @param principals 用户信息
     * @return AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        LoginVO data = (LoginVO) principals.getPrimaryPrincipal();
        String userName = data.getName();
        List<String> permissions = this.shiroService.listOperatePermissionByUserName(userName);
        if (CollectionUtils.isNotEmpty(permissions)) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            info.addStringPermissions(permissions);
            return info;
        }
        return null;
    }

    /**
     * 检验访问token.
     *
     * @param token token
     * @param info  认证信息
     * @throws AuthenticationException
     */
    @Override
    protected void assertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info)
            throws AuthenticationException {
        CredentialsMatcher cm = getCredentialsMatcher();
        if (!cm.doCredentialsMatch(token, info)) {
            throw new BaseShiroAuthcException(ShiroCodeEnum.INVALID_ACCESS_TOKEN.value(),
                    ShiroCodeEnum.INVALID_ACCESS_TOKEN.desc());
        }
    }

    /**
     * 清空当前用户认证缓存.
     *
     * @param principals 用户信息
     */
    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        Cache<Object, AuthenticationInfo> cache = this.getAuthenticationCache();
        LoginVO data = (LoginVO) principals.getPrimaryPrincipal();
        String userName = data.getName();
        cache.remove(userName);
    }

    /**
     * 清除授权缓存和认证缓存.
     */
    public void clearAllCache() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }
}