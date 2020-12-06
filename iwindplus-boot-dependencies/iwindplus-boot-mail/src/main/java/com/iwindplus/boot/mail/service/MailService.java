/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.mail.service;

import com.iwindplus.boot.mail.domain.dto.MailDTO;

/**
 * 邮箱业务层接口类.
 *
 * @author zengdegui
 * @since 2020年4月28日
 */
public interface MailService {
    /**
     * 发送邮件.
     *
     * @param entity 对象
     * @throws Exception
     */
    void send(MailDTO entity) throws Exception;
}
