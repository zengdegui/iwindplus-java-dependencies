/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.oss.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 文件上传视图对象
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadVO implements Serializable {
    private static final long serialVersionUID = 7180768190452534872L;

    /**
     * 原始文件名
     */
    private String sourceFileName;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 绝对路径
     */
    private String absolutePath;

    /**
     * 相对路径
     */
    private String relativePath;
}
