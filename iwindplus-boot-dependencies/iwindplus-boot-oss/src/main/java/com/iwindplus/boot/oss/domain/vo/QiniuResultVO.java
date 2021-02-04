/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.oss.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 七牛云返回结果对象
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Data
public class QiniuResultVO implements Serializable {
    private static final long serialVersionUID = -4123186163536842225L;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件hash值
     */
    private String hash;

    /**
     * 空间名
     */
    private String bucket;

    /**
     * 文件大小
     */
    private long fileSize;
}
