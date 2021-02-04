/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.shiro.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登录用户信息视图对象.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO implements Serializable {
    private static final long serialVersionUID = -2852699111159844963L;

    /**
     * 主键.
     */
    private String id;

    /**
     * 用户名.
     */
    private String name;

    /**
     * 密码.
     */
    private String password;

    /**
     * 状态（0：禁用，1：启用，2：锁定）.
     */
    private Integer status;
}
