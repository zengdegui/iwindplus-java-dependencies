/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.shiro.service.manager;

import com.iwindplus.boot.shiro.domain.dto.JwtTokenDTO;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * 扩展自DefaultWebSubjectFactory,对于无状态的TOKEN不创建session.
 *
 * @author zengdegui
 * @since 2018/9/5
 */
public class SubjectFactory extends DefaultWebSubjectFactory {
    private final DefaultSessionStorageEvaluator storageEvaluator;

    public SubjectFactory(DefaultSessionStorageEvaluator storageEvaluator) {
        this.storageEvaluator = storageEvaluator;
    }

    @Override
    public Subject createSubject(SubjectContext context) {
        this.storageEvaluator.setSessionStorageEnabled(Boolean.TRUE);
        AuthenticationToken token = context.getAuthenticationToken();
        if (token instanceof JwtTokenDTO) {
            // 不创建 session.
            context.setSessionCreationEnabled(false);
            // 不持久化session.
            this.storageEvaluator.setSessionStorageEnabled(Boolean.FALSE);
        }
        return super.createSubject(context);
    }
}
