/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.wechat.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.iwindplus.boot.web.domain.enumerate.WebCodeEnum;
import com.iwindplus.boot.web.exception.BaseException;
import com.iwindplus.boot.wechat.domain.dto.WechatMaMobileDTO;
import com.iwindplus.boot.wechat.domain.dto.WechatMaQrCodeDTO;
import com.iwindplus.boot.wechat.domain.dto.WechatMaUserInfoDTO;
import com.iwindplus.boot.wechat.domain.enumerate.WechatCodeEnum;
import com.iwindplus.boot.wechat.domain.vo.WechatUserInfoVO;
import com.iwindplus.boot.wechat.service.WechatMaService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Base64Utils;

/**
 * 微信相关业务接口实现类.
 *
 * @author zengdegui
 * @since 2019年10月10日
 */
@Slf4j
public class WechatMaServiceImpl extends WxMaServiceImpl implements WechatMaService {
    @Override
    public String getQrCode(WechatMaQrCodeDTO entity) {
        try {
            byte[] bytes = this.getQrcodeService().createWxaCodeUnlimitBytes(entity.getScene(),
                    entity.getPage(), entity.getWidth(), entity.isAutoColor(), entity.getLineColor(),
                    entity.isHyaline());
            if (ArrayUtils.isNotEmpty(bytes)) {
                String base64String = Base64Utils.encodeToString(bytes);
                return "data:image/png;base64," + base64String;
            }
        } catch (WxErrorException ex) {
            int errorCode = ex.getError().getErrorCode();
            String errorMsg = ex.getError().getErrorMsg();
            log.error("Get QR code of small program [code:{}, message:{}]", errorCode, errorMsg);
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

    /**
     * 获取openid等.
     *
     * @param code code
     * @return WxMaJscode2SessionResult
     */
    private WxMaJscode2SessionResult getSessionInfo(String code) {
        WxMaUserService wxMaUserService = this.getUserService();
        try {
            WxMaJscode2SessionResult sessionInfo = wxMaUserService.getSessionInfo(code);
            return sessionInfo;
        } catch (WxErrorException ex) {
            log.error("Wechat authorization exception [{}]", ex);
            int errorCode = ex.getError().getErrorCode();
            if (errorCode == 40029) {
                throw new BaseException(WechatCodeEnum.INVALID_CODE.value(), WechatCodeEnum.INVALID_CODE.desc());
            } else if (errorCode == 45011) {
                throw new BaseException(WechatCodeEnum.FREQUENCY_LIMIT.value(), WechatCodeEnum.FREQUENCY_LIMIT.desc());
            } else if (errorCode == 40163) {
                throw new BaseException(WechatCodeEnum.CODE_USERD.value(), WechatCodeEnum.CODE_USERD.desc());
            } else if (errorCode == -1) {
                throw new BaseException(WebCodeEnum.FAILED.value(), WebCodeEnum.FAILED.desc());
            }
        }
        return null;
    }

    @Override
    public WechatUserInfoVO getWechatUserInfoByMobile(WechatMaMobileDTO entity) {
        WxMaUserService wxMaUserService = this.getUserService();
        WxMaJscode2SessionResult sessionInfo = this.getSessionInfo(entity.getCode());
        if (null != sessionInfo) {
            String sessionKey = sessionInfo.getSessionKey();
            String encryptedData = entity.getEncryptedData();
            String ivStr = entity.getIv();
            WechatUserInfoVO data = new WechatUserInfoVO();
            // 解密用户手机号信息
            WxMaPhoneNumberInfo phoneNoInfo = wxMaUserService.getPhoneNoInfo(sessionKey, encryptedData, ivStr);
            if (null != phoneNoInfo) {
                data.setMobile(phoneNoInfo.getPurePhoneNumber());
                data.setType(1);
                data.setOpenid(sessionInfo.getOpenid());
                data.setUnionid(sessionInfo.getUnionid());
            }
            return data;
        }
        return null;
    }

    @Override
    public WechatUserInfoVO getWechatUserInfo(WechatMaUserInfoDTO entity) {
        WxMaUserService wxMaUserService = this.getUserService();
        WxMaJscode2SessionResult sessionInfo = this.getSessionInfo(entity.getCode());
        if (null != sessionInfo) {
            String sessionKey = sessionInfo.getSessionKey();
            String rawData = entity.getRawData();
            String signature = entity.getSignature();
            String encryptedData = entity.getEncryptedData();
            String ivStr = entity.getIv();
            if (StringUtils.isNotBlank(rawData) && StringUtils.isNotBlank(signature)) {
                // 验证用户信息完整性
                boolean checkUserInfo = wxMaUserService.checkUserInfo(sessionKey, rawData, signature);
                if (!checkUserInfo) {
                    throw new BaseException(WechatCodeEnum.USER_INFO_INCOMPLETE.value(),
                            WechatCodeEnum.USER_INFO_INCOMPLETE.desc());
                }
            }
            // 解密用户敏感数据
            WxMaUserInfo userInfo = wxMaUserService.getUserInfo(sessionKey, encryptedData, ivStr);
            WechatUserInfoVO data = new WechatUserInfoVO();
            data.setMobile(entity.getMobile());
            if (null != userInfo) {
                data.setCountry(userInfo.getCountry());
                data.setProvince(userInfo.getProvince());
                data.setCity(userInfo.getCity());
                data.setNickName(userInfo.getNickName());
                data.setAvatarUrl(userInfo.getAvatarUrl());
                String gender = userInfo.getGender();
                data.setSex(Integer.valueOf(gender));
                data.setOpenid(userInfo.getOpenId());
                data.setUnionid(userInfo.getUnionId());
                data.setType(1);
            }
            return data;
        }
        return null;
    }
}
