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
 * @since 2018/10/11
 */
@Getter
@Accessors(fluent = true)
public enum QiniuOssZoneEnum {
    EAST_CHINA("east_china", "华东"),
    NORTH_CHINA("north_china", "华北"),
    SOUTH_CHINA("south_china", "华南"),
    NORTH_AMERICA("north_america", "北美"),
    SOUTHEAST_ASIA("southeast_asia", "东南亚"),
    ;

    /**
     * 值.
     */
    private final String value;

    /**
     * 描述.
     */
    private final String desc;

    /**
     * 通过描述查找枚举.
     *
     * @param desc 描述
     * @return QiniuOssZoneEnum
     */
    private QiniuOssZoneEnum(final String value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 通过值查找枚举.
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
