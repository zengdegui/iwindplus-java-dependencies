/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.excel.domain.dto;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 自定义导出excel数据传输对象.
 *
 * @author zengdegui
 * @since 2018年9月1日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ExcelExportCustomDTO extends ExcelExportBaseDTO {
    private static final long serialVersionUID = -6174951112826018144L;

    /**
     * 数据（必填）.
     */
    private List<Map<String, Object>> list;

    /**
     * 封装自定义导出字段和标题（必填）.
     */
    private List<ExcelExportEntity> entityList;

    /**
     * 导出表格文件类型（xls,xlsx）默认：xlsx.
     */
    private String excelType = "xlsx";

    /**
     * 颜色反射类（可选）.
     */
    private Class<?> colorClass;
}
