/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.sms.service;

/**
 * 短信业务层接口类.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
public interface SmsService {
	/**
	 * 发送短信验证码.
	 *
	 * @param mobile          手机
	 * @param flagCheckMobile 是否校验手机标志
	 * @param appId           应用主键
	 * @return boolean
	 */
	boolean sendMobileCaptcha(String mobile, Boolean flagCheckMobile, String appId);

	/**
	 * 判断短信验证码是否正确.
	 *
	 * @param mobile  手机
	 * @param captcha 验证码
	 * @param appId   应用主键
	 * @return boolean
	 */
	boolean validate(String mobile, String captcha, String appId);
}
