/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.pay;

import com.iwindplus.boot.pay.domain.AlipayProperty;
import com.iwindplus.boot.pay.service.AlipayService;
import com.iwindplus.boot.pay.service.impl.AlipayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * 支付宝支付配置管理.
 *
 * @author zengdegui
 * @since 2018/10/18
 */
@Slf4j
public class AlipayConfig {
    @Autowired
    private AlipayProperty alipayProperty;

    /**
     * 创建 AlipayService.
     *
     * @return AlipayService
     */
    @Bean
    public AlipayService alipayService() {
        AlipayService alipayService = new AlipayServiceImpl(
                this.alipayProperty.getServerUrl(),
                this.alipayProperty.getAppId(),
                this.alipayProperty.getPrivateKey(),
                this.alipayProperty.getFormat(),
                this.alipayProperty.getCharset(),
                this.alipayProperty.getAlipayPublicKey(),
                this.alipayProperty.getSignType());
        log.info("AlipayService [{}]", alipayService);
        return alipayService;
    }
}
