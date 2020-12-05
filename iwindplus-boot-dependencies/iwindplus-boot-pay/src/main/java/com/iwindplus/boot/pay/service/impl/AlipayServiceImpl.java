/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.pay.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwindplus.boot.pay.domain.AlipayProperty;
import com.iwindplus.boot.pay.domain.constant.PayConstant;
import com.iwindplus.boot.pay.domain.dto.PayOrderDTO;
import com.iwindplus.boot.pay.domain.enumerate.PayCodeEnum;
import com.iwindplus.boot.pay.domain.vo.AlipayNotifyVO;
import com.iwindplus.boot.pay.service.AlipayService;
import com.iwindplus.boot.pay.service.PayBaseService;
import com.iwindplus.boot.util.HttpUtil;
import com.iwindplus.boot.web.domain.enumerate.WebCodeEnum;
import com.iwindplus.boot.web.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 支付宝支付相关业务层接口实现类.
 *
 * @author zengdegui
 * @since 2020/11/29
 */
@Slf4j
public class AlipayServiceImpl extends DefaultAlipayClient implements AlipayService {
    @Autowired
    private AlipayProperty alipayProperty;

    @Autowired
    private PayBaseService payBaseService;

    @Autowired
    private ThreadPoolTaskExecutor executorService;

    @Autowired
    private ObjectMapper objectMapper;

    public AlipayServiceImpl(String serverUrl, String appId, String privateKey, String format, String charset, String alipayPublicKey, String signType) {
        super(serverUrl, appId, privateKey, format, charset, alipayPublicKey, signType);
    }

    @Override
    public String createOrderByQrcode(AlipayTradePrecreateModel entity) {
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setBizModel(entity);
        try {
            AlipayTradePrecreateResponse data = this.execute(request);
            return data.getQrCode();
        } catch (AlipayApiException e) {
            throw new BaseException(PayCodeEnum.CREATE_ORDER_ERROR.value(),
                    PayCodeEnum.CREATE_ORDER_ERROR.desc());
        }
    }

    @Override
    public String payCallback(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = HttpUtil.getParams(request);
        try {
            // 验证签名
            boolean result = AlipaySignature.rsaCheckV1(params, alipayProperty.getAlipayPublicKey(), alipayProperty.getCharset(), alipayProperty.getSignType());
            if (result) {
                String json = this.objectMapper.writeValueAsString(params);
                AlipayNotifyVO data = this.objectMapper.readValue(json, AlipayNotifyVO.class);
                if (StringUtils.equalsIgnoreCase(WebCodeEnum.SUCCESS.name(), data.getTradeStatus())) {
                    // 支付宝订单号
                    String thirdPayNo = data.getTradeNo();
                    // 商户订单号
                    String orderNo = data.getOutTradeNo();
                    // 付款时间
                    Date timeEnd = data.getGmtPayment();
                    LocalDateTime gmtPay = DateUtil.toLocalDateTime(timeEnd);
                    Future<Boolean> submit = this.executorService.submit(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            PayOrderDTO entity = PayOrderDTO.builder()
                                    .orderNo(orderNo)
                                    .gmtPay(gmtPay)
                                    .payChannel(PayConstant.ALIPAY)
                                    .status(PayConstant.PAID)
                                    .thirdPayNo(thirdPayNo)
                                    .build();
                            boolean result = payBaseService.editStatusByOrderNo(entity);
                            return result;
                        }
                    });
                    if (submit.get().booleanValue()) {
                        return WebCodeEnum.SUCCESS.value();
                    }
                }
            }
        } catch (Exception e) {
            log.error("Alipay callBack Exception [{}]", e);
        }
        return WebCodeEnum.FAILED.value();
    }
}
