/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.mybatis.domain.dto;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 基础通用分页相关实体类.
 *
 * @author zengdegui
 * @since 2018年9月1日
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasePageDTO implements Serializable {
    private static final long serialVersionUID = 2194827517811749510L;

    /**
     * 当前页（默认为：1）.
     */
    private Long current = 1L;

    /**
     * 每页显示条数（默认为：10）.
     */
    private Long size = 10L;

    /**
     * 排序字段集合.
     */
    private List<OrderItem> orders;
}
