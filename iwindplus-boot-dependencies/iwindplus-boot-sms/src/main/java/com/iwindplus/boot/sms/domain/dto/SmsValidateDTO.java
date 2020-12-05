/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.sms.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 短信验证数据传输对象.
 *
 * @author zengdegui
 * @since 2018年9月1日
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsValidateDTO implements Serializable {
    private static final long serialVersionUID = -7294630501228380972L;

    /**
     * 手机（必填）.
     */
    @NotBlank(message = "{mobile.notBlank}")
    private String mobile;

    /**
     * 验证码（必填）.
     */
    @NotBlank(message = "{captcha.notBlank}")
    private String captcha;
}
