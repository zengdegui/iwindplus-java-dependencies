/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.sms;

import com.iwindplus.boot.sms.domain.QiniuSmsProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 控制是否启用七牛云短信操作服务.
 *
 * @author zengdegui
 * @since 2019/8/13
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(QiniuSmsConfig.class)
@EnableConfigurationProperties(QiniuSmsProperty.class)
public @interface EnableQiniuSms {
}
