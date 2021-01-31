/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.excel.domain.dto;

import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * excel导入基础通用实体类.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcelImportBaseDTO implements Serializable {
    private static final long serialVersionUID = 8869873769375392857L;

    /**
     * 反射类.
     */
    private Class<?> pojoClass;

    /**
     * 反射类.
     */
    @Builder.Default
    private Boolean needVerify = false;

    /**
     * 验证分组反射类.
     */
    private Class<?>[] verifyGroup;

    /**
     * 校验处理接口.
     */
    private IExcelVerifyHandler<?> verifyHandler;
}
