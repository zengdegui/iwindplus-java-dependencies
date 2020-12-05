/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.pay.domain;

import com.alipay.api.AlipayConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 支付宝支付相关属性.
 *
 * @author zengdegui
 * @since 2018年10月10日
 */
@Data
@ConfigurationProperties(prefix = "alipay")
public class AlipayProperty {
    /**
     * 支付宝appid.
     */
    private String appId;

    /**
     * 支付宝公钥.
     */
    private String alipayPublicKey;

    /**
     * 商户私钥.
     */
    private String privateKey;

    /**
     * 签名类型.
     */
    private String signType = AlipayConstants.SIGN_TYPE_RSA2;

    /**
     * 格式.
     */
    private String format = AlipayConstants.FORMAT_JSON;

    /**
     * 编码.
     */
    private String charset = AlipayConstants.CHARSET_UTF8;

    /**
     * 支付完成以后的回调接口地址.
     */
    private String returnUrl;

    /**
     * 支付异步通知接口地址.
     */
    private String notifyUrl;

    /**
     * 支付宝服务地址.
     */
    private String serverUrl = "https://openapi.alipaydev.com/gateway.do";
}
