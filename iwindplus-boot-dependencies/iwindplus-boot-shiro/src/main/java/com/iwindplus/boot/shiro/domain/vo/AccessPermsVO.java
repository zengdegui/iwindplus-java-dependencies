/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.shiro.domain.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 访问权限权限视图对象.
 *
 * @author zengdegui
 * @since 2018年9月1日
 */
@Data
@Builder
public class AccessPermsVO implements Serializable {
    private static final long serialVersionUID = -6048908144419760842L;

    /**
     * 路径.
     */
    private String url;

    /**
     * 权限.
     */
    private String authority;
}
