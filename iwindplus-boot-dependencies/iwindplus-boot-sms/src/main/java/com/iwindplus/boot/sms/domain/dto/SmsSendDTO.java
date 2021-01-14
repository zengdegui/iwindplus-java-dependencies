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
 * 短信发送数据传输对象.
 *
 * @author zengdegui
 * @since 2018年9月1日
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSendDTO implements Serializable {
    private static final long serialVersionUID = 6606567510714509456L;

    /**
     * 手机（必填）.
     */
    @NotBlank(message = "{mobile.notBlank}")
    private String mobile;

    /**
     * 是否校验手机标志，默认：false.
     */
    @Builder.Default
    private Boolean flagCheckMobile = false;

    /**
     * 应用主键.
     */
    private String appId;
}
