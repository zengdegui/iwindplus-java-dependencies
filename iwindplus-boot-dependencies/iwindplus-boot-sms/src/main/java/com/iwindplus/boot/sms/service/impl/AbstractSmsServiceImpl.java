/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.sms.service.impl;

import com.iwindplus.boot.sms.domain.SmsCommonProperty;
import com.iwindplus.boot.sms.domain.dto.SmsSendDTO;
import com.iwindplus.boot.sms.domain.dto.SmsValidateDTO;
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
	public void validate(SmsValidateDTO entity) {
		LocalDateTime data = this.smsBaseService.getGmtTimeoutByMobile(entity.getMobile().trim(),
				entity.getCaptcha(), DateUtil.getTimesMorning(), DateUtil.getTimesNight(), entity.getAppId());
		if (null == data) {
			throw new BaseException(SmsCodeEnum.CAPTCHA_ERROR.value(), SmsCodeEnum.CAPTCHA_ERROR.desc());
		}

		LocalDateTime now = LocalDateTime.now();
		if (now.isAfter(data)) {
			throw new BaseException(SmsCodeEnum.CAPTCHA_EXPIRED.value(), SmsCodeEnum.CAPTCHA_EXPIRED.desc());
		}
	}

	/**
	 * 验证，校验.
	 *
	 * @param entity   对象
	 * @param property 属性
	 */
	protected void check(SmsSendDTO entity, SmsCommonProperty property) {
		// 当校验手机标志位位true时，校验手机是否存在.
		if (null != entity.getFlagCheckMobile() && entity.getFlagCheckMobile()) {
			// 校验手机是否存在.
			if (!this.smsBaseService.getMobileIsExist(entity.getMobile().trim(), entity.getAppId())) {
				throw new BaseException(SmsCodeEnum.MOBILE_NOT_EXIST.value(),
						SmsCodeEnum.MOBILE_NOT_EXIST.desc());
			}
		}
		// 限制每天发送次数.
		Integer countByMobile = this.smsBaseService.countByMobile(entity.getMobile().trim(),
				DateUtil.getTimesMorning(), DateUtil.getTimesNight(), entity.getAppId());
		if (countByMobile >= property.getLimitCountEveryDay()) {
			throw new BaseException(SmsCodeEnum.CAPTCHA_LIMIT_EVERYDAY.value(),
					SmsCodeEnum.CAPTCHA_LIMIT_EVERYDAY.desc());
		}
		// 限制每小时发送条数.
		Integer countHourByMobile = this.smsBaseService.countHourByMobile(entity.getMobile(), entity.getAppId());
		if (countHourByMobile >= property.getLimitCountHour()) {
			throw new BaseException(SmsCodeEnum.CAPTCHA_LIMIT_HOUR.value(),
					SmsCodeEnum.CAPTCHA_LIMIT_HOUR.desc());
		}
	}
}
