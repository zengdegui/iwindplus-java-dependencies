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
 * 短信日志查询数量数据传输对象.
 *
 * @author zengdegui
 * @since 2018年9月1日
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsLogCountDTO implements Serializable {
    private static final long serialVersionUID = 6017600784799310544L;

    /**
     * 手机.
     */
    private String mobile;

    /**
     * 开始时间.
     */
    private LocalDateTime beginTime;

    /**
     * 结束时间.
     */
    private LocalDateTime endTime;
}
