/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.web.exception;

import com.iwindplus.boot.web.domain.enumerate.WebCodeEnum;
import com.iwindplus.boot.web.domain.vo.ArgumentInvalidResultVO;
import com.iwindplus.boot.web.domain.vo.ResultVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 异常操作工具类.
 *
 * @author zengdegui
 * @since 2020年10月17日
 */
public class ExceptionUtil {
    /**
     * 解析通用异常.
     *
     * @param ex 异常
     * @return ResultVO
     */
    public static ResultVO getCommonException(Exception ex) {
        HttpStatus status = null;
        String code = null;
        String message = null;
        Object data = null;
        String className = ex.getClass().getName();
        if (StringUtils.contains(className, "NoHandlerFoundException")) {
            status = HttpStatus.NOT_FOUND;
            code = WebCodeEnum.NOT_FOUND.value();
            message = WebCodeEnum.NOT_FOUND.desc();
        } else if (StringUtils.contains(className, "ConstraintViolationException")) {
            status = HttpStatus.BAD_REQUEST;
            code = WebCodeEnum.PARAM_ERROR.value();
            message = WebCodeEnum.PARAM_ERROR.desc();
            List<ArgumentInvalidResultVO> invalidArguments = new ArrayList<>();
            ConstraintViolationException exs = (ConstraintViolationException) ex;
            Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
            violations.forEach(item -> {
                ArgumentInvalidResultVO argumentInvalidResultVO = ArgumentInvalidResultVO
                        .builder()
                        .field(item.getExecutableParameters()[0].toString())
                        .value(item.getInvalidValue())
                        .message(item.getMessage())
                        .build();
                invalidArguments.add(argumentInvalidResultVO);
            });
            data = invalidArguments;
        } else if (StringUtils.contains(className, "MethodArgumentNotValidException")) {
            status = HttpStatus.BAD_REQUEST;
            code = WebCodeEnum.PARAM_ERROR.value();
            message = WebCodeEnum.PARAM_ERROR.desc();
            List<ArgumentInvalidResultVO> invalidArguments = new ArrayList<>();
            MethodArgumentNotValidException exs = (MethodArgumentNotValidException) ex;
            List<FieldError> fieldErrors = exs.getBindingResult().getFieldErrors();
            fieldErrors.forEach(item -> {
                ArgumentInvalidResultVO argumentInvalidResultVO = ArgumentInvalidResultVO
                        .builder()
                        .field(item.getField())
                        .value(item.getRejectedValue())
                        .message(item.getDefaultMessage())
                        .build();
                invalidArguments.add(argumentInvalidResultVO);
            });
            data = invalidArguments.get(0);
        }  else if (StringUtils.contains(className, "MethodArgumentTypeMismatchException")) {
            status = HttpStatus.BAD_REQUEST;
            code = WebCodeEnum.PARAM_TYPE_ERROR.value();
            message = WebCodeEnum.PARAM_TYPE_ERROR.desc();
            MethodArgumentTypeMismatchException item = (MethodArgumentTypeMismatchException) ex;
            ArgumentInvalidResultVO argumentInvalidResultVO = ArgumentInvalidResultVO
                    .builder()
                    .field(item.getName())
                    .value(item.getValue())
                    .message(item.getMessage())
                    .build();
            data = argumentInvalidResultVO;
        } else if (StringUtils.contains(className, "MissingServletRequestParameterException")) {
            status = HttpStatus.BAD_REQUEST;
            code = WebCodeEnum.PARAM_MISS.value();
            message = WebCodeEnum.PARAM_MISS.desc();
            MissingServletRequestParameterException item = (MissingServletRequestParameterException) ex;
            ArgumentInvalidResultVO argumentInvalidResultVO = ArgumentInvalidResultVO
                    .builder()
                    .field(item.getParameterName())
                    .message(item.getMessage())
                    .build();
            data = argumentInvalidResultVO;
        } else if (StringUtils.contains(className, "IllegalArgumentException")) {
            status = HttpStatus.BAD_REQUEST;
            code = WebCodeEnum.ILLEGAL_REQUEST.value();
            message = WebCodeEnum.ILLEGAL_REQUEST.desc();
        } else if (StringUtils.contains(className, "HttpRequestMethodNotSupportedException")) {
            status = HttpStatus.METHOD_NOT_ALLOWED;
            code = WebCodeEnum.METHOD_NOT_ALLOWED.value();
            message = WebCodeEnum.METHOD_NOT_ALLOWED.desc();
        } else if (StringUtils.contains(className, "HttpMediaTypeNotSupportedException")) {
            status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
            code = WebCodeEnum.UNSUPPORTED_MEDIA_TYPE.value();
            message = WebCodeEnum.UNSUPPORTED_MEDIA_TYPE.desc();
        } else if (StringUtils.contains(className, "HttpMediaTypeNotAcceptableException")) {
            status = HttpStatus.NOT_ACCEPTABLE;
            code = WebCodeEnum.NOT_ACCEPTABLE.value();
            message = WebCodeEnum.NOT_ACCEPTABLE.desc();
        } else if (StringUtils.contains(className, "ConversionNotSupportedException")) {
            status = HttpStatus.BAD_REQUEST;
            code = WebCodeEnum.CONVERSION_NOT_SUPPORTED.value();
            message = WebCodeEnum.CONVERSION_NOT_SUPPORTED.desc();
        } else if (StringUtils.contains(className, "HttpMessageNotReadableException")) {
            status = HttpStatus.BAD_REQUEST;
            code = WebCodeEnum.NOT_READ.value();
            message = WebCodeEnum.NOT_READ.desc();
        } else if (StringUtils.contains(className, "HttpMessageNotWritableException")) {
            status = HttpStatus.BAD_REQUEST;
            code = WebCodeEnum.NOT_WRITABLE.value();
            message = WebCodeEnum.NOT_WRITABLE.desc();
        } else if (StringUtils.contains(className, "UnauthorizedException")) {
            status = HttpStatus.UNAUTHORIZED;
            code = WebCodeEnum.UNAUTHORIZED.value();
            message = WebCodeEnum.UNAUTHORIZED.desc();
        }
        if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(message)) {
            ResultVO result = ResultVO.builder().status(status).code(code).message(message).data(data)
                    .build();
            return result;
        }
        return null;
    }
}
