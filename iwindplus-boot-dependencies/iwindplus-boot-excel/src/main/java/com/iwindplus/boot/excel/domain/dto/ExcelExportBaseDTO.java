/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.excel.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * excel导出基础通用实体类.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcelExportBaseDTO implements Serializable {
    private static final long serialVersionUID = 5705091198076903621L;

    /**
     * 单sheet最大值（03版本默认6W行,07默认100W）.
     */
    @Builder.Default
    private Integer maxNum = 0;

    /**
     * 表格标题（默认：null）.
     */
    @Builder.Default
    private String title = null;

    /**
     * 表格sheet名称（默认：null）.
     */
    @Builder.Default
    private String sheetName = null;

    /**
     * 导出文件名（可选，有默认值）.
     */
    private String fileName;

    /**
     * 请求（必填）.
     */
    private HttpServletRequest request;

    /**
     * 响应（必填）.
     */
    private HttpServletResponse response;
}
