/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.wechat.domain.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 微信小程序获取用户信息视图对象.
 *
 * @author zengdegui
 * @since 2019年10月10日
 */
@Data
public class WechatUserInfoVO implements Serializable {
    private static final long serialVersionUID = 7088480415033756556L;

    /**
     * 绑定类型.
     */
    private Integer type;

    /**
     * 用户唯一标识.
     */
    @NotBlank(message = "{openid.notBlank}")
    private String openid;

    /**
     * 用户在开放平台的唯一标识符.
     */
    private String unionid;

    /**
     * 手机.
     */
    @NotBlank(message = "{mobile.notBlank}")
    private String mobile;

    /**
     * 昵称.
     */
    private String nickName;

    /**
     * 头像地址.
     */
    private String avatarUrl;

    /**
     * 性别.
     */
    private Integer sex;

    /**
     * 国家.
     */
    private String country;

    /**
     * 省份.
     */
    private String province;

    /**
     * 城市.
     */
    private String city;
}
