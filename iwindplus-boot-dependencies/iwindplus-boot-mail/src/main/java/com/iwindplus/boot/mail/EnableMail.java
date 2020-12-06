/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.mail;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 控制是否启用邮件操作服务.
 *
 * @author zengdegui
 * @since 2019年8月13日
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MailConfig.class)
public @interface EnableMail {
}
