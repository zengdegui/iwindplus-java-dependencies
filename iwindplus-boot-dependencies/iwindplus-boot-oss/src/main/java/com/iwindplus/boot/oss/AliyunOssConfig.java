/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.oss;

import com.iwindplus.boot.oss.service.AliyunOssService;
import com.iwindplus.boot.oss.service.impl.AliyunOssServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

/**
 * 阿里云对象存储操作配置.
 *
 * @author zengdegui
 * @since 2019年8月13日
 */
@Slf4j
public class AliyunOssConfig {
    /**
     * 创建AliyunOssService.
     *
     * @return AliyunSmsService
     */
    @Bean
    public AliyunOssService alibabaOssService() {
        AliyunOssServiceImpl alibabaOssService = new AliyunOssServiceImpl();
        log.info("AlibabaOssService [{}]", alibabaOssService);
        return alibabaOssService;
    }
}
