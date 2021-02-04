/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */
package com.iwindplus.boot.oss.domain.enumerate;

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
public enum OssCodeEnum {
    FILE_NOT_FOUND("file_not_found", "文件找不到"),
    FILE_TOO_BIG("file_too_big", "文件太大"),
    FILE_UPLOAD_FAILED("file_upload_failed", "文件上传失败"),
    CREATE_DIR_FAILED("create_dir_failed", "创建目录失败"),
    ;

    /**
     * 值.
     */
    private final String value;

    /**
     * 描述.
     */
    private final String desc;

    private OssCodeEnum(final String value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 通过描述查找枚举.
     *
     * @param desc 描述
     * @return OssCodeEnum
     */
    public static OssCodeEnum valueOfDesc(String desc) {
        for (OssCodeEnum val : OssCodeEnum.values()) {
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
     * @return OssCodeEnum
     */
    public static OssCodeEnum valueOfValue(String value) {
        for (OssCodeEnum val : OssCodeEnum.values()) {
            if (Objects.equals(value, val.value())) {
                return val;
            }
        }
        return null;
    }
}
