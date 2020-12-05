/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.wechat.domain.enumerate;

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
public enum WechatCodeEnum {
    FREQUENCY_LIMIT("frequency_limit", "频率限制"),
    PAGE_NOT_EXIST("page_not_exist", "page路径不存在"),
    CODE_USERD("code_used", "code只能使用一次"),
    INVALID_CODE("invalid_code", "无效code"),
    USER_INFO_INCOMPLETE("user_info_incomplete", "用户信息不完整"),
    ;

    private final String value;

    private final String desc;

    private WechatCodeEnum(final String value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static WechatCodeEnum valueOfDesc(String desc) {
        for (WechatCodeEnum val : WechatCodeEnum.values()) {
            if (Objects.equals(desc, val.desc())) {
                return val;
            }
        }
        return null;
    }

    public static WechatCodeEnum valueOfValue(String value) {
        for (WechatCodeEnum val : WechatCodeEnum.values()) {
            if (Objects.equals(value, val.value())) {
                return val;
            }
        }
        return null;
    }
}
