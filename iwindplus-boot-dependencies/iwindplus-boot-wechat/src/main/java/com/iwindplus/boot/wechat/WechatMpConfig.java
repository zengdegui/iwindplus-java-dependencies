/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.wechat;

import com.iwindplus.boot.wechat.domain.WechatMpProperty;
import com.iwindplus.boot.wechat.domain.constant.WechatConstant;
import com.iwindplus.boot.wechat.service.WechatMpService;
import com.iwindplus.boot.wechat.service.impl.WechatMpServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.redis.RedisTemplateWxRedisOps;
import me.chanjar.weixin.mp.config.impl.WxMpRedisConfigImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 微信公众号配置管理.
 *
 * @author zengdegui
 * @since 2020/4/10
 */
@Slf4j
public class WechatMpConfig {
    @Autowired
    private WechatMpProperty wechatMpProperty;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 创建 WechatMpService.
     *
     * @return WechatMpService
     */
    @Bean
    public WechatMpService wechatMpService() {
        RedisTemplateWxRedisOps wxRedisOps = new RedisTemplateWxRedisOps(redisTemplate);
        WxMpRedisConfigImpl config = new WxMpRedisConfigImpl(wxRedisOps, WechatConstant.WECHAT_MP_PRIFIX);
        config.setAppId(StringUtils.trimToNull(this.wechatMpProperty.getAppId()));
        config.setSecret(StringUtils.trimToNull(this.wechatMpProperty.getSecret()));
        config.setToken(StringUtils.trimToNull(this.wechatMpProperty.getToken()));
        config.setAesKey(StringUtils.trimToNull(this.wechatMpProperty.getAesKey()));
        WechatMpServiceImpl wechatMpService = new WechatMpServiceImpl();
        wechatMpService.setWxMpConfigStorage(config);
        log.info("WechatMpService [{}]", wechatMpService);
        return wechatMpService;
    }
}
