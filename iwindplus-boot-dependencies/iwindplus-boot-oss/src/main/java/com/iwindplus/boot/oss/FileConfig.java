/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.oss;

import com.iwindplus.boot.oss.service.FileService;
import com.iwindplus.boot.oss.service.impl.FileServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文件操作配置.
 *
 * @author zengdegui
 * @since 2019年8月13日
 */
@Slf4j
@Configuration
public class FileConfig {
    /**
     * 创建FileService.
     *
     * @return FileService
     */
    @Bean
    public FileService fileService() {
        FileServiceImpl fileService = new FileServiceImpl();
        log.info("FileService [{}]", fileService);
        return fileService;
    }
}
