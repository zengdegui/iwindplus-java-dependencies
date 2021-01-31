/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.wechat.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 微信小程获取用户方式数据传输对象.
 *
 * @author zengdegui
 * @since 2020/3/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WechatMaUserInfoDTO extends WechatMaMobileDTO {
    private static final long serialVersionUID = 7484299015606107236L;

    /**
     * 用于校验用户信息签名.
     */
    private String signature;

    /**
     * 用户原始数据字符串.
     */
    private String rawData;

    /**
     * 手机.
     */
    @NotBlank(message = "{mobile.notBlank}")
    private String mobile;
}
