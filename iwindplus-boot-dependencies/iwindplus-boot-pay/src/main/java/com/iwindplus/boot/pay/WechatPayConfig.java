/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.pay;

import com.iwindplus.boot.pay.domain.WechatPayProperty;
import com.iwindplus.boot.pay.service.WechatPayService;
import com.iwindplus.boot.pay.service.impl.WechatPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * 微信支付配置管理.
 *
 * @author zengdegui
 * @since 2018年10月18日
 */
@Slf4j
public class WechatPayConfig {
    @Autowired
    private WechatPayProperty wechatPayProperty;

    /**
     * 创建 WechatPayService.
     *
     * @return WechatPayService
     */
    @Bean
    public WechatPayService wechatPayService() {
        WechatPayService wechatPayService = new WechatPayServiceImpl();
        wechatPayService.setConfig(this.wechatPayProperty);
        log.info("WechatPayService [{}]", wechatPayService);
        return wechatPayService;
    }
}
