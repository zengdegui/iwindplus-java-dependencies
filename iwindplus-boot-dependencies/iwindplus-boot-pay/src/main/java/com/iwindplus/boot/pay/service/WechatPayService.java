/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.pay.service;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.service.WxPayService;

/**
 * 微信支付相关业务层接口.
 *
 * @author zengdegui
 * @since 2020/11/29
 */
public interface WechatPayService extends WxPayService, PayService {
    /**
     * 获取支付二维码
     *
     * @param entity 对象
     * @return String
     */
    String createOrderByQrcode(WxPayUnifiedOrderRequest entity);
}
