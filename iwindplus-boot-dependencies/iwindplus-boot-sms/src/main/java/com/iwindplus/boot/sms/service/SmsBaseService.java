/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.sms.service;

import com.iwindplus.boot.sms.domain.dto.SmsLogCountDTO;
import com.iwindplus.boot.sms.domain.dto.SmsLogDTO;
import com.iwindplus.boot.sms.domain.dto.SmsLogValidateDTO;

import java.time.LocalDateTime;

/**
 * 短信业务层基础接口类.
 *
 * @author zengdegui
 * @since 2019年8月13日
 */
public interface SmsBaseService {
    /**
     * 校验手机存在.
     *
     * @param mobile 手机号
     * @return boolean
     */
    boolean getMobileIsExist(String mobile);

    /**
     * 查找某一范围手机发送记录总数.
     *
     * @param entity 对象
     * @return Integer
     */
    Integer countByMobile(SmsLogCountDTO entity);

    /**
     * 通过手机号查询最近一小时记录总数.
     *
     * @param mobile 手机
     * @return Integer
     */
    Integer countHourByMobile(String mobile);

    /**
     * 查找过期时间.
     *
     * @param entity 对象
     * @return LocalDateTime
     */
    LocalDateTime getGmtTimeoutByMobile(SmsLogValidateDTO entity);

    /**
     * 保存.
     *
     * @param entity 对象
     * @return boolean
     */
    boolean save(SmsLogDTO entity);
}
