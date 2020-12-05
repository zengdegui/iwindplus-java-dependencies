/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.sms;

import com.iwindplus.boot.sms.service.QiniuSmsService;
import com.iwindplus.boot.sms.service.impl.QiniuSmsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

/**
 * 七牛云短信配置.
 *
 * @author zengdegui
 * @since 2019年8月13日
 */
@Slf4j
public class QiniuSmsConfig {
    /**
     * 创建 QiniuSmsService.
     *
     * @return QiniuSmsService
     */
    @Bean
    public QiniuSmsService smsService() {
        QiniuSmsServiceImpl smsService = new QiniuSmsServiceImpl();
        log.info("LingkaiSmsService [{}]", smsService);
        return smsService;
    }
}
