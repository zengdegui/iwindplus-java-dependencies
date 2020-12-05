/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.sms.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 短信日志对象.
 *
 * @author zengdegui
 * @since 2018年9月1日
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsLogDTO implements Serializable {
    private static final long serialVersionUID = 5677984894610398895L;

    /**
     * 业务流水号.
     */
    private String bizId;

    /**
     * 手机.
     */
    private String mobile;

    /**
     * 内容.
     */
    private String content;

    /**
     * 过期时间.
     */
    private LocalDateTime gmtTimeout;
}