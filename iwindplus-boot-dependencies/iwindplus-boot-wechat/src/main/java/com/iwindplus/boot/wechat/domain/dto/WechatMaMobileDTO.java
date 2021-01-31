/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.wechat.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 微信小程获取手机方式数据传输对象.
 *
 * @author zengdegui
 * @since 2020/3/12
 */
@Data
public class WechatMaMobileDTO implements Serializable {
    private static final long serialVersionUID = 7484299015606107236L;

    /**
     * 微信小程序code码.
     */
    @NotBlank(message = "{code.notBlank}")
    private String code;

    /**
     * 用户加密数据.
     */
    @NotBlank(message = "{encryptedData.notBlank}")
    private String encryptedData;

    /**
     * 加密算法的初始向量.
     */
    @NotBlank(message = "{iv.notBlank}")
    private String iv;
}
