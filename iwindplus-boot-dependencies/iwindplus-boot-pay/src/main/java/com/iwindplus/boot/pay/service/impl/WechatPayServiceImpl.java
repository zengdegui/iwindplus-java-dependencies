/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.pay.service.impl;

import cn.hutool.core.date.DateUtil;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.iwindplus.boot.pay.domain.constant.PayConstant;
import com.iwindplus.boot.pay.domain.dto.PayOrderDTO;
import com.iwindplus.boot.pay.service.PayBaseService;
import com.iwindplus.boot.pay.service.WechatPayService;
import com.iwindplus.boot.web.domain.enumerate.WebCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * 微信支付相关业务层接口实现类.
 *
 * @author zengdegui
 * @since 2020/11/29
 */
@Slf4j
public class WechatPayServiceImpl extends WxPayServiceImpl implements WechatPayService {
    @Autowired
    private PayBaseService payBaseService;

    @Override
    public String payCallback(HttpServletRequest request, HttpServletResponse response) {
        try {
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            WxPayOrderNotifyResult data = this.parseOrderNotifyResult(xmlResult);
            if (StringUtils.equalsIgnoreCase(WebCodeEnum.SUCCESS.name(), data.getResultCode())) {
                if (updateOrderStatus(data)) {
                    return WxPayNotifyResponse.success(WebCodeEnum.SUCCESS.value());
                }
            }
        } catch (Exception e) {
            log.error("Wechat pay Exception [{}]", e);
        }
        return WxPayNotifyResponse.fail(WebCodeEnum.FAILED.value());
    }

    private boolean updateOrderStatus(WxPayOrderNotifyResult data) {
        // 微信订单号
        String thirdPayNo = data.getTransactionId();
        // 商户订单号
        String orderNo = data.getOutTradeNo();
        // 付款时间
        String timeEnd = data.getTimeEnd();
        LocalDateTime gmtPay = DateUtil.parseLocalDateTime(timeEnd);
        PayOrderDTO entity = PayOrderDTO.builder()
                .orderNo(orderNo)
                .gmtPay(gmtPay)
                .payChannel(PayConstant.WECHAT_PAY)
                .status(PayConstant.PAID)
                .thirdPayNo(thirdPayNo)
                .build();
        boolean result = payBaseService.editStatusByOrderNo(entity);
        if (result) {
            return true;
        }
        return false;
    }
}
