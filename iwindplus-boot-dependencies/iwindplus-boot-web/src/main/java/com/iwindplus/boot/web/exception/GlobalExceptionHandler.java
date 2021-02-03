/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.web.exception;

import com.iwindplus.boot.web.domain.enumerate.WebCodeEnum;
import com.iwindplus.boot.web.domain.vo.ArgumentInvalidResultVO;
import com.iwindplus.boot.web.domain.vo.ResultVO;
import com.iwindplus.boot.web.i18n.I18nConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 全局异常处理.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Order(2)
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @Autowired
    private I18nConfig i18nConfig;

    private static ResultVO getException(Exception ex) {
        String className = ex.getClass().getName();
        if (StringUtils.contains(className, "UnauthorizedException")) {
            return getResultVO(HttpStatus.UNAUTHORIZED, WebCodeEnum.UNAUTHORIZED, null);
        } else if (StringUtils.contains(className, "NullPointerException")) {
            return getResultVO(HttpStatus.BAD_REQUEST, WebCodeEnum.NULL_POINTER, null);
        } else if (StringUtils.contains(className, "NoHandlerFoundException")) {
            return getResultVO(HttpStatus.NOT_FOUND, WebCodeEnum.NOT_FOUND, null);
        } else if (StringUtils.contains(className, "HttpRequestMethodNotSupportedException")) {
            return getResultVO(HttpStatus.METHOD_NOT_ALLOWED, WebCodeEnum.METHOD_NOT_ALLOWED, null);
        } else if (StringUtils.contains(className, "HttpMediaTypeNotSupportedException")) {
            return getResultVO(HttpStatus.UNSUPPORTED_MEDIA_TYPE, WebCodeEnum.UNSUPPORTED_MEDIA_TYPE, null);
        } else if (StringUtils.contains(className, "HttpMediaTypeNotAcceptableException")) {
            return getResultVO(HttpStatus.NOT_ACCEPTABLE, WebCodeEnum.NOT_ACCEPTABLE, null);
        } else if (StringUtils.contains(className, "HttpMessageNotReadableException")) {
            return getResultVO(HttpStatus.BAD_REQUEST, WebCodeEnum.NOT_READ, null);
        } else if (StringUtils.contains(className, "HttpMessageNotWritableException")) {
            return getResultVO(HttpStatus.BAD_REQUEST, WebCodeEnum.NOT_WRITABLE, null);
        } else if (StringUtils.contains(className, "ConversionNotSupportedException")) {
            return getResultVO(HttpStatus.BAD_REQUEST, WebCodeEnum.CONVERSION_NOT_SUPPORTED, null);
        } else if (StringUtils.contains(className, "IllegalArgumentException")) {
            return getResultVO(HttpStatus.BAD_REQUEST, WebCodeEnum.ILLEGAL_REQUEST, null);
        } else if (StringUtils.contains(className, "FileNotFoundException")) {
            return getResultVO(HttpStatus.BAD_REQUEST, WebCodeEnum.FILE_NOT_FOUND, null);
        } else if (StringUtils.contains(className, "ClassCastException")) {
            return getResultVO(HttpStatus.BAD_REQUEST, WebCodeEnum.TYPE_CONVERSION_ERROR, null);
        } else if (StringUtils.contains(className, "NumberFormatException")) {
            return getResultVO(HttpStatus.BAD_REQUEST, WebCodeEnum.NUMBER_FORMAT_FOUND, null);
        } else if (StringUtils.contains(className, "SecurityException")) {
            return getResultVO(HttpStatus.BAD_REQUEST, WebCodeEnum.SECURITY_ERROR, null);
        } else if (StringUtils.contains(className, "BadSqlGrammarException")) {
            return getResultVO(HttpStatus.BAD_REQUEST, WebCodeEnum.BAD_SQL_GRAMMAR, null);
        } else if (StringUtils.contains(className, "SQLException")) {
            return getResultVO(HttpStatus.BAD_REQUEST, WebCodeEnum.SQL_ERROR, null);
        } else if (StringUtils.contains(className, "TypeNotPresentException")) {
            return getResultVO(HttpStatus.BAD_REQUEST, WebCodeEnum.TYPE_NOT_PRESENT, null);
        } else if (StringUtils.contains(className, "IOException")) {
            return getResultVO(HttpStatus.BAD_REQUEST, WebCodeEnum.IO_ERROR, null);
        } else if (StringUtils.contains(className, "NoSuchMethodException")) {
            return getResultVO(HttpStatus.BAD_REQUEST, WebCodeEnum.NO_SUCH_METHOD, null);
        } else if (StringUtils.contains(className, "IndexOutOfBoundsException")) {
            return getResultVO(HttpStatus.BAD_REQUEST, WebCodeEnum.INDEX_OUT_OF_BOUNDS, null);
        } else if (StringUtils.contains(className, "NoSuchBeanDefinitionException")) {
            return getResultVO(HttpStatus.BAD_REQUEST, WebCodeEnum.NO_SUCH_BEAN, null);
        } else if (StringUtils.contains(className, "TypeMismatchException")) {
            return getResultVO(HttpStatus.BAD_REQUEST, WebCodeEnum.TYPE_MISMATCH, null);
        }
        return null;
    }

    private static ResultVO getException2(Exception ex) {
        ResultVO result = getException(ex);
        if (null == result) {
            String className = ex.getClass().getName();
            if (StringUtils.contains(className, "StackOverflowError")) {
                return getResultVO(HttpStatus.BAD_REQUEST, WebCodeEnum.STACK_OVERFLOW, null);
            } else if (StringUtils.contains(className, "ArithmeticException")) {
                return getResultVO(HttpStatus.BAD_REQUEST, WebCodeEnum.ARITHMETIC_ERROR, null);
            } else if (StringUtils.contains(className, "MissingServletRequestParameterException")) {
                MissingServletRequestParameterException item = (MissingServletRequestParameterException) ex;
                ArgumentInvalidResultVO argumentInvalidResultVO = ArgumentInvalidResultVO.builder()
                        .field(item.getParameterName())
                        .message(item.getMessage())
                        .build();
                return getResultVO(HttpStatus.BAD_REQUEST, WebCodeEnum.PARAM_MISS, argumentInvalidResultVO);
            } else if (StringUtils.contains(className, "MethodArgumentTypeMismatchException")) {
                MethodArgumentTypeMismatchException item = (MethodArgumentTypeMismatchException) ex;
                ArgumentInvalidResultVO argumentInvalidResultVO = ArgumentInvalidResultVO.builder()
                        .field(item.getName())
                        .value(item.getValue())
                        .message(item.getMessage())
                        .build();
                return getResultVO(HttpStatus.BAD_REQUEST, WebCodeEnum.PARAM_TYPE_ERROR, argumentInvalidResultVO);
            } else if (StringUtils.contains(className, "ConstraintViolationException")) {
                List<ArgumentInvalidResultVO> invalidArguments = new ArrayList<>();
                ConstraintViolationException exs = (ConstraintViolationException) ex;
                Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
                violations.forEach(item -> {
                    ArgumentInvalidResultVO argumentInvalidResultVO = ArgumentInvalidResultVO.builder()
                            .field(item.getExecutableParameters()[0].toString())
                            .value(item.getInvalidValue())
                            .message(item.getMessage())
                            .build();
                    invalidArguments.add(argumentInvalidResultVO);
                });
                return getResultVO(HttpStatus.BAD_REQUEST, WebCodeEnum.PARAM_ERROR, invalidArguments);
            }
        }
        return result;
    }

    private static ResultVO getException3(Exception ex) {
        ResultVO result = getException2(ex);
        if (null == result) {
            String className = ex.getClass().getName();
            if (StringUtils.contains(className, "MethodArgumentNotValidException")) {
                List<ArgumentInvalidResultVO> invalidArguments = new ArrayList<>();
                MethodArgumentNotValidException exs = (MethodArgumentNotValidException) ex;
                List<FieldError> fieldErrors = exs.getBindingResult().getFieldErrors();
                fieldErrors.forEach(item -> {
                    ArgumentInvalidResultVO argumentInvalidResultVO = ArgumentInvalidResultVO.builder()
                            .field(item.getField())
                            .value(item.getRejectedValue())
                            .message(item.getDefaultMessage())
                            .build();
                    invalidArguments.add(argumentInvalidResultVO);
                });
                return getResultVO(HttpStatus.BAD_REQUEST, WebCodeEnum.PARAM_ERROR, invalidArguments.get(0));
            } else if (StringUtils.contains(className, "BindException")) {
                List<ArgumentInvalidResultVO> invalidArguments = new ArrayList<>();
                BindException exs = (BindException) ex;
                List<FieldError> fieldErrors = exs.getBindingResult().getFieldErrors();
                fieldErrors.forEach(item -> {
                    ArgumentInvalidResultVO argumentInvalidResultVO = ArgumentInvalidResultVO.builder()
                            .field(item.getField())
                            .value(item.getRejectedValue())
                            .message(item.getDefaultMessage())
                            .build();
                    invalidArguments.add(argumentInvalidResultVO);
                });
                return getResultVO(HttpStatus.BAD_REQUEST, WebCodeEnum.PARAM_ERROR, invalidArguments.get(0));
            }
        }
        return result;
    }

    /**
     * 获取异常错误.
     *
     * @param ex 异常
     * @return ResultVO
     */
    public static ResultVO getError(Exception ex) {
        log.error("Global exception handling [{}]", ex);
        ResultVO result = getException3(ex);
        if (null == result) {
            String className = ex.getClass().getName();
            if (StringUtils.contains(className, "BaseException")) {
                // 自定义异常
                return ResultVO.builder()
                        .status(((BaseException) ex).getStatus())
                        .code(((BaseException) ex).getCode())
                        .message(((BaseException) ex).getCode())
                        .data(((BaseException) ex).getData())
                        .build();
            } else if (StringUtils.contains(className, "BaseShiroAuthcException")) {
                // shiro认证异常
                return ResultVO.builder()
                        .status(((BaseShiroAuthcException) ex).getStatus())
                        .code(((BaseShiroAuthcException) ex).getCode())
                        .message(((BaseShiroAuthcException) ex).getCode())
                        .data(((BaseShiroAuthcException) ex).getData())
                        .build();
            }
        }
        return result;
    }

    private static ResultVO getResultVO(HttpStatus status, WebCodeEnum webCodeEnum, Object data) {
        return ResultVO.builder()
                .status(status)
                .code(webCodeEnum.value())
                .message(webCodeEnum.desc())
                .data(data)
                .build();
    }

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
