/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.mybatis.service;

import cn.hutool.core.net.NetUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwindplus.boot.mybatis.domain.constant.MybatisConstant;
import com.iwindplus.boot.web.xss.XssHttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 公共字段自动填充.
 *
 * @author zengdegui
 * @since 2018年9月1日
 */
@Slf4j
public class MyBatisAutoFillHandler implements MetaObjectHandler {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest request;

    /**
     * 添加填充.
     *
     * @param metaObject metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("Mybatis auto fill when public fields are added");
        if (null == metaObject.getValue(MybatisConstant.GMT_CREATE)) {
            // 设置创建时间.
            this.strictInsertFill(metaObject, MybatisConstant.GMT_CREATE, LocalDateTime.class, LocalDateTime.now());
        }
        if (null == metaObject.getValue(MybatisConstant.GMT_MODIFIED)) {
            // 设置更新时间.
            this.strictInsertFill(metaObject, MybatisConstant.GMT_MODIFIED, LocalDateTime.class, LocalDateTime.now());
        }
        if (null == metaObject.getValue(MybatisConstant.CREATER)) {
            // 设置创建人.
            String operator = this.getOperator();
            if (StringUtils.isNotBlank(operator)) {
                this.strictInsertFill(metaObject, MybatisConstant.CREATER, String.class, operator);
            }
        }
        if (null == metaObject.getValue(MybatisConstant.HOST)) {
            // 设置主机.
            String host = NetUtil.getLocalhostStr();
            if (StringUtils.isNotBlank(host)) {
                this.strictInsertFill(metaObject, MybatisConstant.HOST, String.class, host);
            }
        }
        // 设置是否删除.
        this.strictInsertFill(metaObject, MybatisConstant.FLAG_DELETE, Boolean.class, false);
        // 设置乐观锁.
        this.strictInsertFill(metaObject, MybatisConstant.VERSION, Integer.class, 0);
    }

    /**
     * 更新填充.
     *
     * @param metaObject metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("Mybatis auto fill when editing public fields");
        if (null == metaObject.getValue(MybatisConstant.GMT_MODIFIED)) {
            // 设置更新时间
            this.strictUpdateFill(metaObject, MybatisConstant.GMT_MODIFIED, LocalDateTime.class, LocalDateTime.now());
        }
        if (null == metaObject.getValue(MybatisConstant.MODIFIER)) {
            // 设置更新人
            String operator = this.getOperator();
            if (StringUtils.isNotBlank(operator)) {
                this.strictUpdateFill(metaObject, MybatisConstant.MODIFIER, String.class, operator);
            }
        }
    }

    /**
     * 获取操作人.
     *
     * @return String
     */
    private String getOperator() {
        if (null == this.request) {
            return null;
        }
        String operator = null;
        XssHttpServletRequestWrapper requestWrapper = new XssHttpServletRequestWrapper(this.request);
        // Cookie不为空返回
        Cookie[] cookies = requestWrapper.getCookies();
        for (Cookie cookie : cookies) {
            if (StringUtils.equals(MybatisConstant.OPERATOR, cookie.getName())) {
                operator = cookie.getValue();
                return operator;
            }
        }
        // 请求头不为空返回
        operator = requestWrapper.getHeader(MybatisConstant.OPERATOR);
        if (StringUtils.isNotBlank(operator)) {
            return operator;
        }
        // 请求参数不为空返回
        operator = requestWrapper.getParameter(MybatisConstant.OPERATOR);
        if (StringUtils.isNotBlank(operator)) {
            return operator;
        }
        // 请求body体不为空返回
        byte[] body = requestWrapper.getBody();
        if (ArrayUtils.isNotEmpty(body)) {
            return this.getOperatorByBody(body);
        }
        return null;
    }

    private String getOperatorByBody(byte[] body) {
        try {
            Map<?, ?> map = this.objectMapper.readValue(new String(body, Charset.defaultCharset()), Map.class);
            if (MapUtils.isNotEmpty(map)) {
                Object operator = map.get(MybatisConstant.OPERATOR);
                if (null != operator) {
                    return operator.toString();
                }
            }
        } catch (JsonProcessingException ex) {
            log.error("Json processing exception [{}]", ex);
        }
        return null;
    }
}
