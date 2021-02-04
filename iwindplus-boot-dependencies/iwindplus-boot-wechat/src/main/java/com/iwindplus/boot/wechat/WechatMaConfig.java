/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.wechat;

import cn.binarywang.wx.miniapp.config.impl.WxMaRedisBetterConfigImpl;
import com.iwindplus.boot.wechat.domain.WechatMaProperty;
import com.iwindplus.boot.wechat.domain.constant.WechatConstant;
import com.iwindplus.boot.wechat.service.WechatMaService;
import com.iwindplus.boot.wechat.service.impl.WechatMaServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.redis.RedisTemplateWxRedisOps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 微信小程序配置管理.
 *
 * @author zengdegui
 * @since 2019/7/16
 */
@Slf4j
public class WechatMaConfig {
    @Autowired
    private WechatMaProperty wechatMaProperty;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 创建 WechatMaService.
     *
     * @return WechatMaService
     */
    @Bean
    public WechatMaService wechatMaService() {
        RedisTemplateWxRedisOps wxRedisOps = new RedisTemplateWxRedisOps(redisTemplate);
        WxMaRedisBetterConfigImpl config = new WxMaRedisBetterConfigImpl(wxRedisOps, WechatConstant.WECHAT_MP_PRIFIX);
        config.setAppid(StringUtils.trimToNull(this.wechatMaProperty.getAppId()));
        config.setSecret(StringUtils.trimToNull(this.wechatMaProperty.getSecret()));
        config.setToken(StringUtils.trimToNull(this.wechatMaProperty.getToken()));
        config.setAesKey(StringUtils.trimToNull(this.wechatMaProperty.getAesKey()));
        config.setMsgDataFormat(StringUtils.trimToNull(this.wechatMaProperty.getMsgDataFormat()));
        WechatMaServiceImpl wechatMaService = new WechatMaServiceImpl();
        wechatMaService.setWxMaConfig(config);
        log.info("WechatMaService [{}]", wechatMaService);
        return wechatMaService;
    }
}