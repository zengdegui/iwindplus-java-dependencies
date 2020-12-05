/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.sms.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.iwindplus.boot.sms.domain.LingkaiSmsProperty;
import com.iwindplus.boot.sms.domain.dto.SmsLogDTO;
import com.iwindplus.boot.sms.domain.dto.SmsSendDTO;
import com.iwindplus.boot.sms.service.LingkaiSmsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 凌凯短信业务层接口实现类
 *
 * @author zengdegui
 * @since 2019年8月13日
 */
@Slf4j
public class LingkaiSmsServiceImpl extends AbstractSmsServiceImpl implements LingkaiSmsService {
    @Autowired
    private LingkaiSmsProperty lingkaiSmsProperty;

    /**
     * 发送短信，成功返回流水号(只支持一个参数).
     *
     * @param url           请求地址
     * @param username      用户名
     * @param password      密码
     * @param templateCode  短信模板
     * @param templateParam 短信模板参数
     * @param phoneNumbers  手机号(多个逗号分隔)
     * @param captcha       短信验证码
     * @return String
     */
    private String send(String url, String username, String password, String templateCode, String templateParam,
                        String phoneNumbers, String captcha) {
        String data = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            templateCode = templateCode.replaceAll(templateParam, captcha);
            String content = URLEncoder.encode(templateCode, "GBK");
            String path = new StringBuilder().append(url).append("?CorpID=").append(username).append("&Pwd=")
                    .append(password).append("&Mobile=").append(phoneNumbers).append("&Content=").append(content)
                    .toString();
            isr = new InputStreamReader(new URL(path).openStream());
            br = new BufferedReader(isr);
            data = br.readLine();
        } catch (UnsupportedEncodingException e) {
            log.error("Unsupported encoding exception [{}]", e);
        } catch (IOException e) {
            log.error("IO exception [{}]", e);
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != isr) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    @Override
    public void sendMobileCaptcha(SmsSendDTO entity) {
        this.check(entity, lingkaiSmsProperty);
        Integer length = this.lingkaiSmsProperty.getCaptchaLength();
        // 产生随机数字短信验证码
        String captcha = RandomUtil.randomNumbers(length);
        String response = this.send(this.lingkaiSmsProperty.getUrl(), this.lingkaiSmsProperty.getUsername(),
                this.lingkaiSmsProperty.getPassword(), this.lingkaiSmsProperty.getTemplateCode(),
                this.lingkaiSmsProperty.getTemplateParam(), entity.getMobile(), captcha);
        log.info("Return value of Lingkai SMS [{}]", response);
        if (StringUtils.isNotBlank(response) && Integer.valueOf(response).intValue() > 0) {
            SmsLogDTO param = new SmsLogDTO();
            param.setMobile(entity.getMobile());
            param.setContent(captcha);
            LocalDateTime now = LocalDateTime.now();
            Long milliSecond = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            Long timeout = milliSecond + this.lingkaiSmsProperty.getCaptchaTimeout() * 1000;
            LocalDateTime gmtTimeout = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeout), ZoneId.systemDefault());
            param.setGmtTimeout(gmtTimeout);
            this.smsBaseService.save(param);
        }
    }
}
