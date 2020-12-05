/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.sms.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 验证短信日志查数量数据传输对象.
 *
 * @author zengdegui
 * @since 2018年9月1日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SmsLogValidateDTO extends SmsLogCountDTO {
    private static final long serialVersionUID = 6017600784799310544L;

    /**
     * 验证码.
     */
    private String captcha;
}
