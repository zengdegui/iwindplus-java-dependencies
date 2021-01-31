/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.excel.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * 基于注解excel导入数据传输对象，上传文件方式.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ExcelImportStreamDTO extends ExcelImportBaseDTO {
    private static final long serialVersionUID = 6172605689302817727L;

    /**
     * 上传的文件对象
     */
    private MultipartFile file;
}
