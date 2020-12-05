/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.cloud.oauth2.domain.enumerate;

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
public enum Oauth2CodeEnum {
    SKEY_NOT_EXIST("skey_not_exist", "绑定key不存在"),
    ;

    private final String value;

    private final String desc;

    private Oauth2CodeEnum(final String value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static Oauth2CodeEnum valueOfDesc(String desc) {
        for (Oauth2CodeEnum val : Oauth2CodeEnum.values()) {
            if (Objects.equals(desc, val.desc())) {
                return val;
            }
        }
        return null;
    }

    public static Oauth2CodeEnum valueOfValue(String value) {
        for (Oauth2CodeEnum val : Oauth2CodeEnum.values()) {
            if (Objects.equals(value, val.value())) {
                return val;
            }
        }
        return null;
    }
}
