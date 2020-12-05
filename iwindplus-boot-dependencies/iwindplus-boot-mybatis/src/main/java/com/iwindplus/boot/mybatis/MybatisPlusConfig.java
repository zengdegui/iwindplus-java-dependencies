/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.iwindplus.boot.mybatis.service.MyBatisAutoFillHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis plus配置.
 *
 * @author zengdegui
 * @since 2018年9月1日
 */
@Slf4j
@Configuration
public class MybatisPlusConfig {
    /**
     * 单页分页条数限制.
     */
    private static final Long maxLimit = 1000L;

    /**
     * 创建MybatisPlusInterceptor.
     *
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setMaxLimit(maxLimit);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        // 乐观锁
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 防止全表更新与删除
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        log.info("mybatisPlusInterceptor [{}]", interceptor);
        return interceptor;
    }

    /**
     * 字段自动化填充.
     *
     * @return MetaObjectHandler
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        MyBatisAutoFillHandler myBatisAutoFillHandler = new MyBatisAutoFillHandler();
        log.info("MetaObjectHandler [{}]", myBatisAutoFillHandler);
        return myBatisAutoFillHandler;
    }
}
