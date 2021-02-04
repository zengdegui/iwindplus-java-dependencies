/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.sms;

import com.iwindplus.boot.sms.service.LingkaiSmsService;
import com.iwindplus.boot.sms.service.impl.LingkaiSmsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

/**
 * 凌凯短信配置.
 *
 * @author zengdegui
 * @since 2019/8/13
 */
@Slf4j
public class LingkaiSmsConfig {
    /**
     * 创建 LingkaiSmsService.
     *
     * @return LingkaiSmsService
     */
    @Bean
    public LingkaiSmsService smsService() {
        LingkaiSmsServiceImpl smsService = new LingkaiSmsServiceImpl();
        log.info("LingkaiSmsService [{}]", smsService);
        return smsService;
    }
}
