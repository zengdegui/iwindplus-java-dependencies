/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.pay.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 支付相关业务层接口.
 *
 * @author zengdegui
 * @since 2020/11/29
 */
public interface PayService {
    /**
     * 支付回调.
     *
     * @param request  请求
     * @return String
     */
    String payCallback(HttpServletRequest request);
}
