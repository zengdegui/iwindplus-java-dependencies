/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.wechat.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.iwindplus.boot.wechat.domain.dto.WechatMaMobileDTO;
import com.iwindplus.boot.wechat.domain.dto.WechatMaQrCodeDTO;
import com.iwindplus.boot.wechat.domain.dto.WechatMaUserInfoDTO;
import com.iwindplus.boot.wechat.domain.vo.WechatUserInfoVO;

/**
 * 微信小程序相关业务层接口类.
 *
 * @author zengdegui
 * @since 2019年10月10日
 */
public interface WechatMaService extends WxMaService {
    /**
     * 获取小程序二维码.
     *
     * @param entity 对象
     * @return String
     */
    String getQrCode(WechatMaQrCodeDTO entity);

    /**
     * 通过手机授权方式，获取微信用户信息.
     *
     * @param entity 对象
     * @return WechatUserInfoVO
     */
    WechatUserInfoVO getWechatUserInfoByMobile(WechatMaMobileDTO entity);

    /**
     * 通过用户信息授权方式，获取微信用户信息.
     *
     * @param entity 对象
     * @return WechatUserInfoVO
     */
    WechatUserInfoVO getWechatUserInfo(WechatMaUserInfoDTO entity);
}
