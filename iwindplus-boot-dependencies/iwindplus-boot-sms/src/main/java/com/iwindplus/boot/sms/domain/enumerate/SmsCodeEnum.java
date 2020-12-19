/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */
package com.iwindplus.boot.sms.domain.enumerate;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * 业务编码返回值枚举.
 *
 * @author zengdegui
 * @since 2020年6月13日
 */
@Getter
@Accessors(fluent = true)
public enum SmsCodeEnum {
    MOBILE_NOT_EXIST("mobile_not_exist", "手机不存在"),
    CAPTCHA_ERROR("captcha_error", "验证码错误"),
    CAPTCHA_EXPIRED("captcha_expired", "验证码过期"),
    CAPTCHA_LIMIT_EVERYDAY("captcha_limit_every_day", "限制验证码每天发送次数"),
    CAPTCHA_LIMIT_HOUR("captcha_limit_hour", "限制验证码每小时发送次数"),
    ;

    /**
     * 值.
     */
    private final String value;

    /**
     * 描述.
     */
    private final String desc;

    private SmsCodeEnum(final String value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 通过描述查找枚举.
     *
     * @param desc 描述
     * @return SmsCodeEnum
     */
    public static SmsCodeEnum valueOfDesc(String desc) {
        for (SmsCodeEnum val : SmsCodeEnum.values()) {
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
     * @return SmsCodeEnum
     */
    public static SmsCodeEnum valueOfValue(String value) {
        for (SmsCodeEnum val : SmsCodeEnum.values()) {
            if (Objects.equals(value, val.value())) {
                return val;
            }
        }
        return null;
    }
}
