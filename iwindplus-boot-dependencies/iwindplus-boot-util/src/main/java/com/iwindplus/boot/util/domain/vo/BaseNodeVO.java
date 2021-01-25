/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.util.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 基础节点对象.
 *
 * @author zengdegui
 * @since 2018年9月1日
 */
@Data
public class BaseNodeVO<T> implements Serializable {
	private static final long serialVersionUID = -5005483907155448572L;

	/**
	 * 主键.
	 */
	private String id;

	/**
	 * 节点名称.
	 */
	private String name;

	/**
	 * 上级主键.
	 */
	private String parentId;

	/**
	 * 排序号.
	 */
	private Integer seq;

	/**
	 * 子节点.
	 */
	private List<T> nodes;
}
