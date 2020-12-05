package com.iwindplus.cloud.oauth2.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.compress.utils.Lists;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 自定义认证用户信息.
 *
 * @author zengdegui
 * @since 2020年4月3日
 */
public class UserDetailsVO implements UserDetails {
    private static final long serialVersionUID = -123308657146774881L;

    /**
     * 用户唯一标识.
     */
    private String openid;

    /**
     * 登录名.
     */
    private String username;

    /**
     * 密码.
     */
    private String password;

    /**
     * 客户端.
     */
    private String clientId;

    /**
     * 用户权限.
     */
    private List<String> authorities;

    /**
     * 是否已锁定.
     */
    private boolean accountNonLocked;

    /**
     * 是否已过期.
     */
    private boolean accountNonExpired;

    /**
     * 是否启用.
     */
    private boolean enabled;

    /**
     * 密码是否已过期.
     */
    private boolean credentialsNonExpired;

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        if (null == authorities) {
            return Collections.emptyList();
        }
        Collection<GrantedAuthority> list = Lists.newArrayList();
        List<String> authorities2 = this.authorities;
        authorities2.forEach(param -> {
            GrantedAuthority grantedAuthority = new GrantedAuthority() {
                private static final long serialVersionUID = 1L;

                @Override
                public String getAuthority() {
                    return param;
                }
            };
            list.add(grantedAuthority);
        });
        return list;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
