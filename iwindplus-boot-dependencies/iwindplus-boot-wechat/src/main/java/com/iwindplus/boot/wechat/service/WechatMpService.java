/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.wechat.service;

import com.iwindplus.boot.wechat.domain.dto.WechatMpQrCodeDTO;
import me.chanjar.weixin.mp.api.WxMpService;

/**
 * 微信公众号相关业务层接口类.
 *
 * @author zengdegui
 * @since 2020年4月10日
 */
public interface WechatMpService extends WxMpService {
    /**
     * 获取微信公众号扫码登录二维码.
     *
     * @return String
     */
    String getQrCodeLogin();

    /**
     * 获取微信公众号授权地址.
     *
     * @param clientId     客户端ID(非必填)
     * @param clientSecret 客户端密匙(非必填)
     * @return String
     */
    String authorize(String clientId, String clientSecret);

    /**
     * 获取微信公众号二维码.
     *
     * @param entity 对象
     * @return String
     */
    String getQrCode(WechatMpQrCodeDTO entity);
}
