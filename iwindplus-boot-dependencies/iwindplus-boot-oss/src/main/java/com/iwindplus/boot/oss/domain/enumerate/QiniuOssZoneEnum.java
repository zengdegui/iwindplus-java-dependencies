/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */
package com.iwindplus.boot.oss.domain.enumerate;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * 七牛云对象存储服务器区域枚举.
 *
 * @author zengdegui
 * @since 2018年10月11日
 */
@Getter
@Accessors(fluent = true)
public enum QiniuOssZoneEnum {
    EAST_CHINA(0, "华东"),
    NORTH_CHINA(1, "华北"),
    SOUTH_CHINA(2, "华南"),
    NORTH_AMERICA(3, "北美"),
    SOUTHEAST_ASIA(4, "东南亚"),
    ;

    /**
     * 值
     */
    private final Integer value;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 通过描述查找枚举
     *
     * @param desc 描述
     * @return QiniuOssZoneEnum
     */
    private QiniuOssZoneEnum(final Integer value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 通过值查找枚举
     *
     * @param value 值
     * @return QiniuOssZoneEnum
     */
    public static QiniuOssZoneEnum valueOfValue(String value) {
        for (QiniuOssZoneEnum val : QiniuOssZoneEnum.values()) {
            if (Objects.equals(value, val.value())) {
                return val;
            }
        }
        return null;
    }
}
