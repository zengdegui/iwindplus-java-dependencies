/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.mail.domain.dto;

import com.iwindplus.boot.oss.domain.vo.UploadVO;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 邮件消息
 *
 * @author zengdegui
 * @since 2020年4月28日
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MailDTO implements Serializable {
    private static final long serialVersionUID = -1252086996292274033L;

    /**
     * 发件服务器域名（必填）.
     */
    private String host;

    /**
     * 发件服务器账户（必填）.
     */
    private String username;

    /**
     * 发件服务器密码（必填）.
     */
    private String password;

    /**
     * 发件人昵称（必填）.
     */
    private String nickName;

    /**
     * 收件人（必填）.
     */
    private String[] to;

    /**
     * 抄送人.
     */
    private String[] cc;

    /**
     * 邮件标题（必填）.
     */
    private String subject;

    /**
     * 邮件内容（必填）.
     */
    private String content;

    /**
     * 附件.
     */
    private List<UploadVO> attachments;
}
