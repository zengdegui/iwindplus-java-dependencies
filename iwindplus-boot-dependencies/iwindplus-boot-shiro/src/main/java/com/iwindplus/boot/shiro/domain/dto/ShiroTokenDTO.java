/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.shiro.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 用户和密码（包含验证码）令牌类数据传输对象.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ShiroTokenDTO extends UsernamePasswordToken {
    private static final long serialVersionUID = 622321361554522354L;

    /**
     * 构造方法.
     *
     * @param username   用户名（必填）
     * @param password   密码 必填）
     * @param rememberMe 是否记住我（为null默认false,否则true）
     */
    public ShiroTokenDTO(final String username, final String password, final Boolean rememberMe) {
        super(username, password, null == rememberMe ? false : true);
    }
}
