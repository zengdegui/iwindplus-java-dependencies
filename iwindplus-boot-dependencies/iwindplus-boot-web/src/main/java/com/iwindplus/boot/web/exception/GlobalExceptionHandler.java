/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.web.exception;

import com.iwindplus.boot.web.domain.enumerate.WebCodeEnum;
import com.iwindplus.boot.web.domain.vo.ResultVO;
import com.iwindplus.boot.web.i18n.I18nConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理.
 *
 * @author zengdegui
 * @since 2018年9月1日
 */
@Order(2)
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @Autowired
    private I18nConfig i18nConfig;

    /**
     * 全局异常捕获.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultVO> exception(Exception ex) {
        ResultVO result = this.getError(ex);
        return this.getResponse(result);
    }

    /**
     * 获取异常错误.
     *
     * @param ex 异常
     * @return ResultVO
     */
    public ResultVO getError(Exception ex) {
        log.error("Global exception handling [{}]", ex);
        ResultVO result = ExceptionUtil.getCommonException(ex);
        if (null == result) {
            HttpStatus status = null;
            String code = null;
            String message = null;
            Object data = null;
            String className = ex.getClass().getName();
            if (StringUtils.contains(className, "BaseException")) {
                // 自定义异常
                status = ((BaseException) ex).getStatus();
                code = ((BaseException) ex).getCode();
                message = ((BaseException) ex).getMessage();
                data = ((BaseException) ex).getData();
            } else if (StringUtils.contains(className, "BaseShiroAuthcException")) {
                // shiro认证异常
                status = ((BaseShiroAuthcException) ex).getStatus();
                code = ((BaseShiroAuthcException) ex).getCode();
                message = ((BaseShiroAuthcException) ex).getMessage();
                data = ((BaseShiroAuthcException) ex).getData();
            }
            if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(message)) {
                result = ResultVO.builder().status(status).code(code).message(message).data(data).build();
            }
        }
        return result;
    }

    /**
     * 返回响应信息.
     *
     * @param entity 结果
     * @return ResponseEntity<ResultVO>
     */
    public ResponseEntity<ResultVO> getResponse(ResultVO entity) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (null == entity) {
            String code = WebCodeEnum.FAILED.value();
            String message = WebCodeEnum.FAILED.desc();
            entity = ResultVO.builder().status(status).code(code).message(message).build();
        } else {
            if (null != entity.getStatus()) {
                status = entity.getStatus();
            }
        }
        this.getI18nInfo(entity);
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 错误信息国际化.
     *
     * @param entity 结果
     */
    protected void getI18nInfo(ResultVO entity) {
        String message = this.i18nConfig.getMessage(entity.getCode());
        if (StringUtils.isNotBlank(message)) {
            entity.setMessage(message);
        }
    }
}
