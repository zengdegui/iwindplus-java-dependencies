/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.web.xss;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.*;

/**
 * 参数去除空格.
 *
 * @author zengdegui
 * @since 2020年4月3日
 */
@Slf4j
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] body;
    private Map<String, String> customHeaders;

    /**
     * XssHttpServletRequestWrapper.
     *
     * @param request 请求
     */
    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            IOUtils.copy(request.getInputStream(), baos);
        } catch (IOException e) {
            log.error("Abnormal interception request [{}]", e);
        }
        this.body = baos.toByteArray();
        this.customHeaders = new HashMap<>();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(this.body);
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (StringUtils.isNotBlank(value)) {
            String parameter = StringUtils.trim(value).trim();
            String escapeParameter = StringEscapeUtils.escapeHtml4(parameter);
            return escapeParameter;
        }
        return null;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] parameterValues = super.getParameterValues(name);
        if (parameterValues == null) {
            return null;
        }
        for (int i = 0; i < parameterValues.length; i++) {
            String value = parameterValues[i];
            parameterValues[i] = StringUtils.trim(value).trim();
        }
        return parameterValues;
    }

    @Override
    public String getHeader(String name) {
        String value = this.customHeaders.get(name);
        if (value != null) {
            return value;
        }
        return ((HttpServletRequest) getRequest()).getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        Set<String> set = new HashSet<>(this.customHeaders.keySet());
        Enumeration<String> enumeration = ((HttpServletRequest) getRequest()).getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            set.add(name);
        }
        return Collections.enumeration(set);
    }

    public void putHeader(String name, String value) {
        this.customHeaders.put(name, value);
    }

    public byte[] getBody() {
        return this.body;
    }
}
