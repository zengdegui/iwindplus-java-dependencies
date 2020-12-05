/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.web.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 校验无效数据结果视图对象.
 *
 * @author zengdegui
 * @since 2020年11月6日
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArgumentInvalidResultVO {
    /**
     * 字段.
     */
    private String field;

    /**
     * 值.
     */
    private Object value;

    /**
     * 信息.
     */
    private String message;
}
