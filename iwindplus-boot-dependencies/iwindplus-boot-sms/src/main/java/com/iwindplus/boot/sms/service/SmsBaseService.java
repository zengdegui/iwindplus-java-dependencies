/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.sms.service;

import com.iwindplus.boot.sms.domain.dto.SmsLogDTO;

import java.time.LocalDateTime;

/**
 * 短信业务层基础接口类.
 *
 * @author zengdegui
 * @since 2019/8/13
 */
public interface SmsBaseService {
	/**
	 * 校验手机存在.
	 *
	 * @param mobile 手机
	 * @param appId  应用主键
	 * @return boolean
	 */
	boolean getMobileIsExist(String mobile, String appId);

	/**
	 * 查找某一范围手机发送记录总数.
	 *
	 * @param mobile    手机
	 * @param beginTime 开始时间
	 * @param endTime   结束时间
	 * @param appId     应用主键
	 * @return Integer
	 */
	Integer countByMobile(String mobile, LocalDateTime beginTime, LocalDateTime endTime, String appId);

	/**
	 * 通过手机号查询最近一小时记录总数.
	 *
	 * @param mobile 手机
	 * @param appId  应用主键
	 * @return Integer
	 */
	Integer countHourByMobile(String mobile, String appId);

	/**
	 * 查找过期时间.
	 *
	 * @param mobile    手机
	 * @param captcha   验证码
	 * @param beginTime 开始时间
	 * @param endTime   结束时间
	 * @param appId     应用主键
	 * @return LocalDateTime
	 */
	LocalDateTime getGmtTimeoutByMobile(String mobile, String captcha, LocalDateTime beginTime, LocalDateTime endTime, String appId);

	/**
	 * 保存.
	 *
	 * @param entity 对象
	 * @return boolean
	 */
	boolean save(SmsLogDTO entity);
}
