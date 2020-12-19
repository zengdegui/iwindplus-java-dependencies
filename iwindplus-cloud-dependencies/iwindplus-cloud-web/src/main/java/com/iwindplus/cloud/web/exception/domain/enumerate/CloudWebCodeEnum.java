/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.cloud.web.exception.domain.enumerate;

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
public enum CloudWebCodeEnum {
    REQUEST_TIMEOUT("request_timeout", "请求超时"),
    RPC_ERROR("rpc_error", "rpc调用异常"),
    GRANT_TYPE_NOT_NULL("grant_type_not_null", "授权类型不能为空"),
    UNSUPPORTED_GRANT_TYPE("unsuppored_grant_type", "不支持的授权类型"),
    UNSUPPORTED_RESPONSE_TYPE("unsuppored_response_type", "不支持的响应类型"),
    DATA_NOT_EXIST("data_not_exist", "数据不存在"),
    INVALID_ACCOUNT("invalid_account", "无效账号"),
    ACCOUNT_LOCKED("account_locked", "账号被锁定"),
    ACCOUNT_DISABLED("account_disabled", "账号被禁用"),
    INVALID_CREDENTIAL("invalid_credentials", "无效凭证"),
    INVALID_ACCESS_TOKEN("invalid_access_token", "无效访问token"),
    INVALID_CLIENT("invalid_client", "无效客户端"),
    CLIENT_DISABLED("client_disabled", "客户端被禁用"),
    CLIENT_LOCKED("client_locked", "客户端被锁定"),
    UNAUTHORIZED_CLIENT("unauthorized_client", "未经授权客户端"),
    INVALID_GRANT("invalid_grant", "无效授权"),
    INVALID_REFRESH_TOKEN("invalid_refresh_token", "无效刷新token"),
    INVALID_SCOPE("invalid_scope", "无效授权范围"),
    INVALID_TOKEN("invalid_token", "无效token"),
    INVALID_CODE("invalid_code", "无效code"),
    ACCESS_TOKEN_EXPIRED("access_token_expired", "访问token过期"),
    ACCESS_TOKEN_FORMAT_ERROR("access_token_format_error", "访问token格式错误"),
    REFRESH_TOKEN_EXPIRED("refresh_token_expired", "刷新token过期"),
    REFRESH_TOKEN_NOT_NULL("refresh_token_not_null", "刷新token不能为空"),
    CAPTCHA_NOT_NULL("captcha_not_null", "验证码不能为空"),
    CAPTCHA_ERROR("captcha_error", "验证码错误"),
    CAPTCHA_EXPIRED("captcha_expired", "验证码过期"),
    CAPTCHA_LIMIT_EVERYDAY("captcha_limit_every_day", "限制验证码每天发送次数"),
    CAPTCHA_LIMIT_HOUR("captcha_limit_hour", "限制验证码每小时发送次数"),
    FREQUENCY_LIMIT("frequency_limit", "频率限制"),
    PAGE_NOT_EXIST("page_not_exist", "page路径不存在"),
    CODE_USERD("code_used", "code只能使用一次"),
    USER_INFO_INCOMPLETE("user_info_incomplete", "用户信息不完整"),
    MOBILE_NOT_EXIST("mobile_not_exist", "手机不存在"),
    ACCOUNT_NOT_EXIST("account_not_exist", "账号不存在"),
    PASSWORD_NOT_NULL("password_not_null", "密码不能为空"),
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

    private CloudWebCodeEnum(final String value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 通过描述查找枚举.
     *
     * @param desc 描述
     * @return CloudWebCodeEnum
     */
    public static CloudWebCodeEnum valueOfDesc(String desc) {
        for (CloudWebCodeEnum val : CloudWebCodeEnum.values()) {
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
     * @return CloudWebCodeEnum
     */
    public static CloudWebCodeEnum valueOfValue(String value) {
        for (CloudWebCodeEnum val : CloudWebCodeEnum.values()) {
            if (Objects.equals(value, val.value())) {
                return val;
            }
        }
        return null;
    }
}
