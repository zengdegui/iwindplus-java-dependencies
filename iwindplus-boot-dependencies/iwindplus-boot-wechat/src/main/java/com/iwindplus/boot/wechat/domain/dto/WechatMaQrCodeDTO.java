/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.wechat.domain.dto;

import cn.binarywang.wx.miniapp.bean.WxaCodeUnlimit;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 微信小程序二维码数据传输对象.
 *
 * @author zengdegui
 * @since 2019年7月16日
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WechatMaQrCodeDTO extends WxaCodeUnlimit {
    private static final long serialVersionUID = 1837554375198878953L;

    /**
     * 场景值.
     */
    @NotBlank(message = "{scene.notBlank}")
    private String scene;
}
