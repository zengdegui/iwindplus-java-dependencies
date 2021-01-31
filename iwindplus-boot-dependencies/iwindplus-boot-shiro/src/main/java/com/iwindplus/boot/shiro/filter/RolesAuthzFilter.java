/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.shiro.filter;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.stream.Stream;

/**
 * 角色过滤器（或者关系）.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Slf4j
@Setter
public class RolesAuthzFilter extends AuthorizationFilter {
    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);
        if (mappedValue instanceof String[]) {
            String[] array = (String[]) mappedValue;
            if (array == null || array.length == 0) {
                return true;
            }
            return Stream.of(array).anyMatch(entity -> subject.hasRole(entity));
        }
        return false;
    }
}
