/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.web.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;
import java.util.Properties;

/**
 * 国际化配置.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Configuration
public class I18nConfig {
	@Autowired
	private MessageSource messageSource;

	/**
	 * 统一校验规则到国际化信息文件.
	 *
	 * @return Validator
	 */
	@Primary
	@Bean
	public Validator getValidator() {
		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		validator.setValidationMessageSource(this.messageSource);
		Properties properties = new Properties();
		properties.setProperty("hibernate.validator.fail_fast", "true");
		validator.setValidationProperties(properties);
		return validator;
	}

	/**
	 * 根据code获取，没有则取默认消息.
	 *
	 * @param code 编码
	 * @return String
	 */
	public String getMessage(String code) {
		return this.getMessageByOS(code, null);
	}

	/**
	 * 根据应用部署的服务器系统来决定国际化.
	 *
	 * @param code           编码
	 * @param defaultMessage 默认消息
	 * @return String
	 */
	public String getMessageByOS(String code, String defaultMessage) {
		return this.messageSource.getMessage(code, null, defaultMessage, LocaleContextHolder.getLocale());
	}
}
