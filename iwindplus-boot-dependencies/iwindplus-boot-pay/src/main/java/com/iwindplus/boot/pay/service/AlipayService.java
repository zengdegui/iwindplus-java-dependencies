/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.pay.service;

import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;

/**
 * 支付宝支付相关业务层接口.
 *
 * @author zengdegui
 * @since 2020/11/29
 */
public interface AlipayService extends AlipayClient, PayService {
    /**
     * 获取支付二维码.
     *
     * @param entity 对象
     * @return String
     */
    String createOrderByQrcode(AlipayTradePrecreateModel entity);
}
