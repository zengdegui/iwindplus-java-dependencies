/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.wechat.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 微信公众号二维码数据传输对象.
 *
 * @author zengdegui
 * @since 2019/7/16
 */
@Data
public class WechatMpQrCodeDTO implements Serializable {
    private static final long serialVersionUID = 1837554375198878953L;

    /**
     * 场景值.
     */
    @NotBlank(message = "{scene.notBlank}")
    private String scene;
}
