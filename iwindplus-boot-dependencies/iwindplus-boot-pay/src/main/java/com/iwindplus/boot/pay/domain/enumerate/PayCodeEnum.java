/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.pay.domain.enumerate;

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
public enum PayCodeEnum {
    CREATE_ORDER_ERROR("create_order_error", "下单失败"),
    PAY_CALLBACK_ERROR("pay_callback_error", "支付回调失败"),
    ;

    private final String value;

    private final String desc;

    private PayCodeEnum(final String value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static PayCodeEnum valueOfDesc(String desc) {
        for (PayCodeEnum val : PayCodeEnum.values()) {
            if (Objects.equals(desc, val.desc())) {
                return val;
            }
        }
        return null;
    }

    public static PayCodeEnum valueOfValue(String value) {
        for (PayCodeEnum val : PayCodeEnum.values()) {
            if (Objects.equals(value, val.value())) {
                return val;
            }
        }
        return null;
    }
}
