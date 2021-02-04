/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.pay.service;

import com.iwindplus.boot.pay.domain.dto.PayOrderDTO;

/**
 * 支付业务层基础接口.
 *
 * @author zengdegui
 * @since 2020/11/29
 */
public interface PayBaseService {
    /**
     * 通过商户订单号修改订单状态.
     *
     * @param entity 对象
     * @return boolean
     */
    boolean editStatusByOrderNo(PayOrderDTO entity);
}
