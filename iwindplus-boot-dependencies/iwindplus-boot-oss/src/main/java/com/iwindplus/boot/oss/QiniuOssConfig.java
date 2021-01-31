/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.oss;

import com.iwindplus.boot.oss.service.QiniuOssService;
import com.iwindplus.boot.oss.service.impl.QiniuOssServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

/**
 * 七牛云对象存储操作配置.
 *
 * @author zengdegui
 * @since 2019/8/13
 */
@Slf4j
public class QiniuOssConfig {
    /**
     * 创建QiniuOssService.
     *
     * @return QiniuOssService
     */
    @Bean
    public QiniuOssService qiniuOssService() {
        QiniuOssServiceImpl qiniuOssService = new QiniuOssServiceImpl();
        log.info("QiniuOssService [{}]", qiniuOssService);
        return qiniuOssService;
    }
}
