/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.pay.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 支付订单数据传输对象.
 *
 * @author zengdegui
 * @since 2020/11/29
 */
@Data
@Builder
public class PayOrderDTO implements Serializable {
    /**
     * 订单号.
     */
    private String orderNo;

    /**
     * 第三方支付流水号.
     */
    private String thirdPayNo;

    /**
     * 付款时间.
     */
    private LocalDateTime gmtPay;

    /**
     * 支付渠道.
     */
    private String payChannel;

    /**
     * 订单状态.
     */
    private String status;
}
