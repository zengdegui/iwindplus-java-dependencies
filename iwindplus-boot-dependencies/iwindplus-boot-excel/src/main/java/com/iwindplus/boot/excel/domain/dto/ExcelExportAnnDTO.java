/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.excel.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 基于注解导出excel数据传输对象.
 *
 * @author zengdegui
 * @since 2020/9/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ExcelExportAnnDTO extends ExcelExportBaseDTO {
    private static final long serialVersionUID = -9017994885185823654L;

    /**
     * 数据（必填）.
     */
    private List<?> list;

    /**
     * 反射类（必填）.
     */
    private Class<?> pojoClass;

    /**
     * 导出表格文件类型（xls,xlsx）默认：xlsx.
     */
    private String excelType = "xlsx";

    /**
     * 颜色反射类（可选）.
     */
    private Class<?> colorClass;
}
