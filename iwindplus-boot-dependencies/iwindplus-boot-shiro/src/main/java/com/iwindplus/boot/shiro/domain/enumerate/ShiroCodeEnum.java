/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.shiro.domain.enumerate;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * 业务编码返回值枚举.
 *
 * @author zengdegui
 * @since 2020/6/13
 */
@Getter
@Accessors(fluent = true)
public enum ShiroCodeEnum {
    ACCOUNT_NOT_EXIST("account_not_exist", "账号不存在"),
    ACCOUNT_LOCKED("account_locked", "账号被锁定"),
    ACCOUNT_DISABLED("account_disabled", "账号被禁用"),
    INVALID_ACCESS_TOKEN("invalid_access_token", "无效访问token"),
    ACCESS_TOKEN_EXPIRED("access_token_expired", "访问token过期"),
    PASSWORD_ERROR("password_error", "密码错误"),
    ;

    /**
     * 值.
     */
    private final String value;

    /**
     * 描述.
     */
    private final String desc;

    private ShiroCodeEnum(final String value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 通过描述查找枚举.
     *
     * @param desc 描述
     * @return ShiroCodeEnum
     */
    public static ShiroCodeEnum valueOfDesc(String desc) {
        for (ShiroCodeEnum val : ShiroCodeEnum.values()) {
            if (Objects.equals(desc, val.desc())) {
                return val;
            }
        }
        return null;
    }

    /**
     * 通过值查找枚举.
     *
     * @param value 值
     * @return ShiroCodeEnum
     */
    public static ShiroCodeEnum valueOfValue(String value) {
        for (ShiroCodeEnum val : ShiroCodeEnum.values()) {
            if (Objects.equals(value, val.value())) {
                return val;
            }
        }
        return null;
    }
}
