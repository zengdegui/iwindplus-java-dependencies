/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.pay.domain;

import com.github.binarywang.wxpay.config.WxPayConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 微信支付相关属性.
 *
 * @author zengdegui
 * @since 2018/10/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "wechat.pay")
public class WechatPayProperty extends WxPayConfig {
}
