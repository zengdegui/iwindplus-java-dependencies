/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.oss;

import com.iwindplus.boot.oss.domain.AliyunOssProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 控制是否启用阿里对象存储操作服务.
 *
 * @author zengdegui
 * @since 2019年8月13日
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AliyunOssConfig.class)
@EnableConfigurationProperties(AliyunOssProperty.class)
public @interface EnableAlibabaOss {
}
