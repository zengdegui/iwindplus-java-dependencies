/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.mail;

import com.iwindplus.boot.mail.service.MailService;
import com.iwindplus.boot.mail.service.impl.MailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

/**
 * 邮件件操作配置.
 *
 * @author zengdegui
 * @since 2020/12/6
 */
@Slf4j
public class MailConfig {
    /**
     * 创建MailService.
     *
     * @return MailService
     */
    @Bean
    public MailService mailService() {
        MailServiceImpl mailService = new MailServiceImpl();
        log.info("MailService [{}]", mailService);
        return mailService;
    }
}
