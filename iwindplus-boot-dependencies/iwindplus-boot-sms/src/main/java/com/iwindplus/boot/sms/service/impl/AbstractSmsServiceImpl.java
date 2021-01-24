/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.sms.service.impl;

import com.iwindplus.boot.sms.domain.enumerate.SmsCodeEnum;
import com.iwindplus.boot.sms.service.SmsBaseService;
import com.iwindplus.boot.sms.service.SmsService;
import com.iwindplus.boot.util.DateUtil;
import com.iwindplus.boot.web.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * 短信业务抽象类.
 *
 * @author zengdegui
 * @since 2020年3月13日
 */
public abstract class AbstractSmsServiceImpl implements SmsService {
	/**
	 * 短信业务层基础接口类
	 */
	@Autowired
	protected SmsBaseService smsBaseService;

	@Override
	public boolean validate(String mobile, String captcha, String appId) {
		LocalDateTime data = this.smsBaseService.getGmtTimeoutByMobile(mobile,
				captcha, DateUtil.getTimesMorning(), DateUtil.getTimesNight(), appId);
		if (null == data) {
			throw new BaseException(SmsCodeEnum.CAPTCHA_ERROR.value(), SmsCodeEnum.CAPTCHA_ERROR.desc());
		}

		LocalDateTime now = LocalDateTime.now();
		if (now.isAfter(data)) {
			throw new BaseException(SmsCodeEnum.CAPTCHA_EXPIRED.value(), SmsCodeEnum.CAPTCHA_EXPIRED.desc());
		}
		return true;
	}

	/**
	 * 验证，校验.
	 *
	 * @param mobile             手机
	 * @param flagCheckMobile    是否校验手机标志
	 * @param appId              应用主键
	 * @param limitCountEveryDay 限制每小时次数
	 * @param limitCountHour     限制手机每天次数
	 */
	protected boolean check(String mobile, Boolean flagCheckMobile, String appId, Integer limitCountEveryDay, Integer limitCountHour) {
		// 当校验手机标志位位true时，校验手机是否存在.
		if (null != flagCheckMobile && flagCheckMobile) {
			// 校验手机是否存在.
			if (!this.smsBaseService.getMobileIsExist(mobile, appId)) {
				throw new BaseException(SmsCodeEnum.MOBILE_NOT_EXIST.value(),
						SmsCodeEnum.MOBILE_NOT_EXIST.desc());
			}
		}
		// 限制每天发送次数.
		if (null != limitCountEveryDay) {
			Integer countByMobile = this.smsBaseService.countByMobile(mobile,
					DateUtil.getTimesMorning(), DateUtil.getTimesNight(), appId);
			if (countByMobile >= limitCountEveryDay) {
				throw new BaseException(SmsCodeEnum.CAPTCHA_LIMIT_EVERYDAY.value(),
						SmsCodeEnum.CAPTCHA_LIMIT_EVERYDAY.desc());
			}
		}
		// 限制每小时发送条数.
		if (null != limitCountHour) {
			Integer countHourByMobile = this.smsBaseService.countHourByMobile(mobile, appId);
			if (countHourByMobile >= limitCountHour) {
				throw new BaseException(SmsCodeEnum.CAPTCHA_LIMIT_HOUR.value(),
						SmsCodeEnum.CAPTCHA_LIMIT_HOUR.desc());
			}
		}
		return true;
	}
}
