/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.excel.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.File;

/**
 * 基于注解本地文件excel导入数据传输对象.
 *
 * @author zengdegui
 * @since 2018年9月1日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ExcelImportFileDTO extends ExcelImportBaseDTO {
    private static final long serialVersionUID = 6172605689302817727L;

    /**
     * 文件对象.
     */
    private File file;
}
