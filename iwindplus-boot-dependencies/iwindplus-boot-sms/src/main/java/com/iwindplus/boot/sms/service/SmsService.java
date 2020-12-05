/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.sms.service;

import com.iwindplus.boot.sms.domain.dto.SmsSendDTO;
import com.iwindplus.boot.sms.domain.dto.SmsValidateDTO;

/**
 * 短信业务层接口类.
 *
 * @author zengdegui
 * @since 2018年9月1日
 */
public interface SmsService {
    /**
     * 发送短信验证码.
     *
     * @param entity 对象
     */
    void sendMobileCaptcha(SmsSendDTO entity);

    /**
     * 判断短信验证码是否正确.
     *
     * @param entity 对象
     * @return boolean
     */
    void validate(SmsValidateDTO entity);
}
