/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.web.exception;

import com.iwindplus.boot.web.domain.enumerate.WebCodeEnum;
import com.iwindplus.boot.web.domain.vo.ResultVO;
import com.iwindplus.boot.web.i18n.I18nConfig;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 覆盖springboot默认异常处理.
 *
 * @author zengdegui
 * @since 2019/6/4
 */
@Configuration
public class CustomErrorController extends BasicErrorController {
    /**
     * 国际化
     */
    @Autowired
    protected I18nConfig i18nConfig;

    public CustomErrorController(ServerProperties serverProperties) {
        super(new DefaultErrorAttributes(), serverProperties.getError());
    }

    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request, ErrorAttributeOptions.of(Include.MESSAGE));
        HttpStatus status = getStatus(request);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(ResultVO.ERROR_CODE, status.name().toLowerCase(LocaleContextHolder.getLocale()));
        map.put(ResultVO.ERROR_MSG, this.i18nConfig.getMessage(WebCodeEnum.FAILED.value()));
        // 输出自定义的Json格式
        if (MapUtils.isNotEmpty(body)) {
            Object object = body.get("message");
            if (null != object) {
                this.getMap(map, object);
            }
        }
        return new ResponseEntity<Map<String, Object>>(map, status);
    }

    private void getMap(Map<String, Object> map, Object object) {
        String message = object.toString();
        WebCodeEnum errorCodeEnum = WebCodeEnum.valueOfDesc(message);
        if (null != errorCodeEnum) {
            map.put(ResultVO.ERROR_CODE, errorCodeEnum.value());
            String errorMsg = this.i18nConfig.getMessage(errorCodeEnum.value());
            if (StringUtils.isNotBlank(errorMsg)) {
                map.put(ResultVO.ERROR_MSG, errorMsg);
            } else {
                map.put(ResultVO.ERROR_MSG, errorCodeEnum.desc());
            }
        }
    }
}
