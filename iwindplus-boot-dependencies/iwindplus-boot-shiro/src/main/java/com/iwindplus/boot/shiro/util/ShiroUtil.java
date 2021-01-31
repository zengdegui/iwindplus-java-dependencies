/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.shiro.util;

import com.iwindplus.boot.shiro.domain.vo.LoginVO;
import com.iwindplus.boot.shiro.service.manager.ReloadManager;
import com.iwindplus.boot.shiro.service.realm.JwtRealm;
import com.iwindplus.boot.shiro.service.realm.ShiroRealm;
import lombok.Setter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * shiro工具类.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Setter
public class ShiroUtil {
    private ShiroRealm pwdRealm;
    private JwtRealm jwtRealm;
    private ReloadManager reloadManager;

    /**
     * 清除授权缓存和认证缓存.
     */
    public void clearCachedJwt() {
        this.jwtRealm.clearAllCache();
    }

    /**
     * 清除授权缓存和认证缓存.
     */
    public void clearCached() {
        this.pwdRealm.clearAllCache();
    }

    /**
     * 刷新动态过滤规则.
     * 如果角色-资源对应关系发生变化，可以通过此方法进行同步刷新，从而达到URL动态过滤的效果。
     */
    public void reloadFilterRules() {
        this.reloadManager.updatePermission();
    }

    /**
     * 获取用户信息.
     *
     * @return LoginVO
     */
    public LoginVO getUserInfo() {
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        if (null != principal) {
            return (LoginVO) principal;
        }
        return null;
    }
}
