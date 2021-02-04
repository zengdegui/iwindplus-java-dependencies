/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.sms.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwindplus.boot.sms.domain.AliyunSmsProperty;
import com.iwindplus.boot.sms.domain.dto.SmsLogDTO;
import com.iwindplus.boot.sms.service.AliyunSmsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * 阿里云短信业务层接口实现类.
 *
 * @author zengdegui
 * @since 2019/8/13
 */
@Slf4j
public class AliyunSmsServiceImpl extends AbstractSmsServiceImpl implements AliyunSmsService {
	@Autowired
	private AliyunSmsProperty aliyunSmsProperty;

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * 初始化.
	 *
	 * @param accessKeyId 短信AK
	 * @param secret      短信AK证书
	 * @return IAcsClient
	 */
	private static IAcsClient accessClient(String accessKeyId, String secret) {
		DefaultProfile profile = DefaultProfile.getProfile("default", accessKeyId, secret);
		IAcsClient client = new DefaultAcsClient(profile);
		return client;
	}

	/**
	 * 发送短信，成功返回流水号(只支持一个参数).
	 *
	 * @param accessKeyId   短信AK
	 * @param secret        短信AK证书
	 * @param signName      短信签名
	 * @param templateCode  短信模板
	 * @param templateParam 短信模板参数
	 * @param phoneNumbers  手机号(多个逗号分隔)
	 * @param captcha       短信验证码
	 * @param serialNumber  外部流水号(可选)
	 * @param upExtendCode  上行短信扩展码(可选)
	 * @return CommonResponse
	 */
	private CommonResponse send(String accessKeyId, String secret, String signName, String templateCode,
			String templateParam, String phoneNumbers, String captcha, String serialNumber, String upExtendCode) {
		IAcsClient acsClient = accessClient(accessKeyId, secret);
		CommonRequest commonRequest = new CommonRequest();
		commonRequest.setSysMethod(MethodType.POST);
		commonRequest.setSysDomain("dysmsapi.aliyuncs.com");
		commonRequest.setSysVersion("2017-05-25");
		commonRequest.setSysAction("SendSms");
		commonRequest.putQueryParameter("PhoneNumbers", phoneNumbers);
		commonRequest.putQueryParameter("SignName", signName);
		commonRequest.putQueryParameter("TemplateCode", templateCode);
		commonRequest.putQueryParameter("OutId", serialNumber);
		Map<String, Object> templateParamAndValue = new HashMap<>();
		templateParamAndValue.put(templateParam, captcha);
		try {
			String json = this.objectMapper.writeValueAsString(templateParamAndValue);
			commonRequest.putQueryParameter("TemplateParam", json);
			CommonResponse response = acsClient.getCommonResponse(commonRequest);
			String resultJson = this.objectMapper.writeValueAsString(response.getData());
			log.info("Alicloud SMS sending return value [{}]", resultJson);
			return response;
		} catch (JsonProcessingException ex) {
			log.error("Json processing exception [{}]", ex);
		} catch (ServerException e) {
			log.error("Alicloud SMS service is abnormal [{}]", e);
		} catch (ClientException e) {
			log.error("Abnormal SMS connection to alicloud service [{}]", e);
		}
		return null;
	}

	@Override
	public boolean sendMobileCaptcha(String mobile, Boolean flagCheckMobile, String appId) {
		this.check(mobile, flagCheckMobile, appId, aliyunSmsProperty.getLimitCountEveryDay(),
				aliyunSmsProperty.getLimitCountHour());
		Integer length = this.aliyunSmsProperty.getCaptchaLength();
		// 产生随机数字短信验证码
		String captcha = RandomUtil.randomNumbers(length);
		CommonResponse response = this.send(this.aliyunSmsProperty.getAccessKey(),
				this.aliyunSmsProperty.getSecretKey(), this.aliyunSmsProperty.getSignName(),
				this.aliyunSmsProperty.getTemplateCode(), this.aliyunSmsProperty.getTemplateParam(), mobile,
				captcha, IdUtil.fastSimpleUUID(), null);
		if (null != response && response.getHttpStatus() == 200) {
			if (StringUtils.isNotBlank(response.getData())) {
				JSONObject json = JSONUtil.parseObj(response.getData());
				if (json.getStr("Code").equals("OK")) {
					String bizId = json.getStr("BizId");
					Long milliSecond = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
					Long timeout = milliSecond + this.aliyunSmsProperty.getCaptchaTimeout() * 1000;
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
			}
		}
		return false;
	}
}
