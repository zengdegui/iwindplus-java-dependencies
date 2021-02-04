/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.sms;

import com.iwindplus.boot.sms.service.AliyunSmsService;
import com.iwindplus.boot.sms.service.impl.AliyunSmsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

/**
 * 阿里云短信配置.
 *
 * @author zengdegui
 * @since 2019/8/13
 */
@Slf4j
public class AliyunSmsConfig {
    /**
     * 创建 AliyunSmsService.
     *
     * @return AliyunSmsService
     */
    @Bean
    public AliyunSmsService smsService() {
        AliyunSmsServiceImpl smsService = new AliyunSmsServiceImpl();
        log.info("AliyunSmsService [{}]", smsService);
        return smsService;
    }
}
