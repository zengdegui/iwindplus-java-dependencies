/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.wechat.service.impl;

import cn.hutool.core.util.IdUtil;
import com.iwindplus.boot.web.exception.BaseException;
import com.iwindplus.boot.wechat.domain.WechatMpProperty;
import com.iwindplus.boot.wechat.domain.constant.WechatConstant;
import com.iwindplus.boot.wechat.domain.dto.WechatMpQrCodeDTO;
import com.iwindplus.boot.wechat.domain.enumerate.WechatCodeEnum;
import com.iwindplus.boot.wechat.service.WechatMpService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpQrcodeService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 微信公众号相关业务层接口实现类.
 *
 * @author zengdegui
 * @since 2020/4/10
 */
@Slf4j
public class WechatMpServiceImpl extends WxMpServiceImpl implements WechatMpService {
    @Autowired
    private WechatMpProperty wechatMpProperty;

    @Override
    public String getQrCodeLogin() {
        String redirectURL = this.buildQrConnectUrl(this.wechatMpProperty.getRedirectUri(),
                WxConsts.QrConnectScope.SNSAPI_LOGIN, IdUtil.fastSimpleUUID());
        log.info("getQrCodeLogin redirectURL [{}]", redirectURL);
        return redirectURL;
    }

    @Override
    public String authorize(String clientId, String clientSecret) {
        StringBuilder sb = new StringBuilder(this.wechatMpProperty.getRedirectUri());
        if (StringUtils.isNotBlank(clientId) && StringUtils.isNotBlank(clientSecret)) {
            sb.append("?").append(WechatConstant.CLIENT_ID).append("=").append(clientId).append("&")
                    .append(WechatConstant.CLIENT_SECRET).append("=").append(clientSecret);
        }
        String redirectUri = sb.toString();
        String webScope = this.wechatMpProperty.getWebScope();
        String redirectURL = this.getOAuth2Service().buildAuthorizationUrl(redirectUri, webScope,
                IdUtil.fastSimpleUUID());
        log.info("authorize redirectURL [{}]", redirectURL);
        return redirectURL;
    }

    @Override
    public String getQrCode(WechatMpQrCodeDTO entity) {
        try {
            WxMpQrcodeService qrcodeService = this.getQrcodeService();
            WxMpQrCodeTicket data = qrcodeService.qrCodeCreateLastTicket(entity.getScene());
            if (null != data) {
                return qrcodeService.qrCodePictureUrl(data.getTicket(), true);
            }
        } catch (WxErrorException ex) {
            int errorCode = ex.getError().getErrorCode();
            String errorMsg = ex.getError().getErrorMsg();
            log.error("WxErrorException [code:{}, messsage:{}]", errorCode, errorMsg);
            if (errorCode == 45009) {
                throw new BaseException(WechatCodeEnum.FREQUENCY_LIMIT.value(),
                        WechatCodeEnum.FREQUENCY_LIMIT.desc());
            } else if (errorCode == 41030) {
                throw new BaseException(WechatCodeEnum.PAGE_NOT_EXIST.value(),
                        WechatCodeEnum.PAGE_NOT_EXIST.desc());
            }
        }
        return null;
    }
}
