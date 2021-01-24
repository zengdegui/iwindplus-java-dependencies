/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.sms.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwindplus.boot.sms.domain.QiniuSmsProperty;
import com.iwindplus.boot.sms.domain.dto.SmsLogDTO;
import com.iwindplus.boot.sms.service.QiniuSmsService;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.sms.SmsManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * 七牛云短信业务层接口实现类.
 *
 * @author zengdegui
 * @since 2019年8月13日
 */
@Slf4j
public class QiniuSmsServiceImpl extends AbstractSmsServiceImpl implements QiniuSmsService {
	@Autowired
	private QiniuSmsProperty qiniuSmsProperty;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public boolean sendMobileCaptcha(String mobile, Boolean flagCheckMobile, String appId) {
		this.check(mobile, flagCheckMobile, appId, this.qiniuSmsProperty.getLimitCountEveryDay(),
				this.qiniuSmsProperty.getLimitCountHour());
		Integer length = this.qiniuSmsProperty.getCaptchaLength();
		// 产生随机数字短信验证码.
		String captcha = RandomUtil.randomNumbers(length);
		Auth auth = Auth.create(this.qiniuSmsProperty.getAccessKey(), this.qiniuSmsProperty.getSecretKey());
		// 实例化一个SmsManager对象.
		SmsManager smsManager = new SmsManager(auth);
		Map<String, String> parameters = new HashMap<>();
		parameters.put(this.qiniuSmsProperty.getTemplateParam(), captcha);
		try {
			Response response = smsManager.sendMessage(this.qiniuSmsProperty.getTemplateCode(),
					new String[]{mobile}, parameters);
			String json = this.objectMapper.writeValueAsString(response);
			log.info("Qiniu cloud SMS sending return value [{}]", json);
			if (null != response && response.isOK()) {
				String bizId = getBizId(response);
				Long milliSecond = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
				Long timeout = milliSecond + this.qiniuSmsProperty.getCaptchaTimeout() * 1000;
				LocalDateTime gmtTimeout = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeout), ZoneId.systemDefault());
				SmsLogDTO param = SmsLogDTO
						.builder()
						.bizId(bizId)
						.mobile(mobile)
						.content(captcha)
						.gmtTimeout(gmtTimeout)
						.appId(appId)
						.build();
				return this.smsBaseService.save(param);
			}
		} catch (JsonProcessingException ex) {
			log.error("Json processing exception [{}]", ex);
		} catch (QiniuException e) {
			log.error("Qiniu cloud SMS service is abnormal [{}]", e);
		}
		return false;
	}

	private String getBizId(Response response) throws JsonProcessingException, QiniuException {
		Map<?, ?> map = this.objectMapper.readValue(response.bodyString(), Map.class);
		if (MapUtils.isNotEmpty(map)) {
			Object jobId = map.get("job_id");
			if (null != jobId) {
				return jobId.toString();
			}
		}
		return null;
	}
}
