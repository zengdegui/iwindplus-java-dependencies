/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.mail.service.impl;

import com.iwindplus.boot.mail.domain.dto.MailDTO;
import com.iwindplus.boot.mail.service.MailService;
import com.iwindplus.boot.oss.domain.vo.UploadVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;
import java.util.Properties;

/**
 * 邮箱业务层接口实现类.
 *
 * @author zengdegui
 * @since 2018年9月1日
 */
@Slf4j
public class MailServiceImpl implements MailService {
	@Override
	public void send(MailDTO entity) throws Exception {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost(entity.getHost());
		sender.setUsername(entity.getUsername());
		sender.setPassword(entity.getPassword());
		sender.setDefaultEncoding("UTF-8");
		Properties props = new Properties();
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.starttls.enable", "true");
		props.setProperty("mail.smtp.starttls.required", "true");
		sender.setJavaMailProperties(props);

		MimeMessage message = null;
		message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(entity.getTo());
		if (entity.getCc() != null && entity.getCc().length > 0) {
			helper.setCc(entity.getCc());
		}
		if (entity.getBcc() != null && entity.getBcc().length > 0) {
			helper.setBcc(entity.getBcc());
		}
		helper.setFrom(new InternetAddress(sender.getUsername(), entity.getNickName(), "UTF-8"));
		helper.setSubject(entity.getSubject());
		helper.setText(entity.getContent(), true);
		this.addAttachments(helper, entity);
		sender.send(message);
	}

	/**
	 * 添加附件
	 *
	 * @param helper helper
	 * @param entity 对象
	 */
	private void addAttachments(MimeMessageHelper helper, MailDTO entity) {
		List<UploadVO> attachments = entity.getAttachments();
		if (CollectionUtils.isNotEmpty(attachments)) {
			attachments.forEach(param -> {
				try {
					helper.addAttachment(param.getSourceFileName(), new File(param.getAbsolutePath()));
				} catch (MessagingException e) {
					log.error("Messaging Exception [{}]", e);
				}
			});
		}
	}
}
