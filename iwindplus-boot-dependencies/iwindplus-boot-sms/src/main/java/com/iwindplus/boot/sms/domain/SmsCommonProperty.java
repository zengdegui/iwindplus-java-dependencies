/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.sms.domain;

import lombok.Data;

/**
 * 短信相关公用属性.
 *
 * @author zengdegui
 * @since 2020年9月20日
 */
@Data
public class SmsCommonProperty {
    /**
     * 短信模板.
     */
    private String templateCode;

    /**
     * 短信模板参数.
     */
    private String templateParam;

    /**
     * 短信验证码有效时间，单位：秒（默认10分钟）.
     */
    private Integer captchaTimeout = 600;

    /**
     * 短信验证码位数（默认6位）.
     */
    private Integer captchaLength = 6;

    /**
     * 限制每小时次数（默认5次）.
     */
    private Integer limitCountHour = 5;

    /**
     * 限制手机每天次数（默认20次）.
     */
    private Integer limitCountEveryDay = 20;
}
