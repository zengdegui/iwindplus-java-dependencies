/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.cloud.web.exception;

import com.iwindplus.boot.web.domain.enumerate.WebCodeEnum;
import com.iwindplus.boot.web.domain.vo.ArgumentInvalidResultVO;
import com.iwindplus.boot.web.domain.vo.ResultVO;
import com.iwindplus.boot.web.exception.BaseException;
import com.iwindplus.boot.web.exception.GlobalExceptionHandler;
import com.iwindplus.boot.web.i18n.I18nConfig;
import com.iwindplus.cloud.feign.exception.FeignErrorException;
import com.iwindplus.cloud.web.exception.domain.enumerate.CloudWebCodeEnum;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import feign.FeignException;
import feign.RetryableException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedClientException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedResponseTypeException;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 全局异常处理.
 *
 * @author zengdegui
 * @since 2018年9月1日
 */
@Order(1)
@RestControllerAdvice
public class CloudGlobalExceptionHandler extends GlobalExceptionHandler {
    @Autowired
    private I18nConfig i18nConfig;

    /**
     * 处理空指针异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ResultVO> exceptionHandler(NullPointerException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String code = WebCodeEnum.NULL_POINTER.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.NULL_POINTER.desc();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理找不到异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ResultVO> exceptionHandler(NoHandlerFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String code = WebCodeEnum.NOT_FOUND.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.NOT_FOUND.desc();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理错误的请求方式异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResultVO> exceptionHandler(HttpRequestMethodNotSupportedException ex) {
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
        String code = WebCodeEnum.METHOD_NOT_ALLOWED.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.METHOD_NOT_ALLOWED.desc();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理不支持的媒体类型异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ResultVO> exceptionHandler(HttpMediaTypeNotSupportedException ex) {
        HttpStatus status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
        String code = WebCodeEnum.UNSUPPORTED_MEDIA_TYPE.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.UNSUPPORTED_MEDIA_TYPE.desc();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理不可接受异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ResultVO> exceptionHandler(HttpMediaTypeNotAcceptableException ex) {
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
        String code = WebCodeEnum.NOT_ACCEPTABLE.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.NOT_ACCEPTABLE.desc();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理转换不支持异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(ConversionNotSupportedException.class)
    public ResponseEntity<ResultVO> exceptionHandler(ConversionNotSupportedException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String code = WebCodeEnum.CONVERSION_NOT_SUPPORTED.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.CONVERSION_NOT_SUPPORTED.desc();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理不可读异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResultVO> exceptionHandler(HttpMessageNotReadableException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String code = WebCodeEnum.NOT_READ.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.NOT_READ.desc();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理不可写异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(HttpMessageNotWritableException.class)
    public ResponseEntity<ResultVO> exceptionHandler(HttpMessageNotWritableException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String code = WebCodeEnum.NOT_WRITABLE.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.NOT_WRITABLE.desc();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理不合法的参数异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResultVO> exceptionHandler(IllegalArgumentException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String code = WebCodeEnum.ILLEGAL_REQUEST.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.ILLEGAL_REQUEST.desc();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理参数缺失异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResultVO> exceptionHandler(MissingServletRequestParameterException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String code = WebCodeEnum.PARAM_MISS.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.PARAM_MISS.desc();
        ArgumentInvalidResultVO data = ArgumentInvalidResultVO.builder()
                .field(ex.getParameterName())
                .message(ex.getMessage())
                .build();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).data(data).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理参数类型不正确异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResultVO> exceptionHandler(MethodArgumentTypeMismatchException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String code = WebCodeEnum.PARAM_TYPE_ERROR.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.PARAM_TYPE_ERROR.desc();
        ArgumentInvalidResultVO data = ArgumentInvalidResultVO.builder()
                .field(ex.getName())
                .value(ex.getValue())
                .message(ex.getMessage())
                .build();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).data(data).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理不合法的参数异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResultVO> exceptionHandler(ConstraintViolationException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String code = WebCodeEnum.PARAM_ERROR.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.PARAM_ERROR.desc();
        List<ArgumentInvalidResultVO> data = new ArrayList<>();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        violations.forEach(item -> {
            ArgumentInvalidResultVO argumentInvalidResultVO = ArgumentInvalidResultVO.builder()
                    .field(item.getExecutableParameters()[0].toString())
                    .value(item.getInvalidValue())
                    .message(item.getMessage())
                    .build();
            data.add(argumentInvalidResultVO);
        });
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).data(data).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理不合法的参数异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResultVO> exceptionHandler(MethodArgumentNotValidException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String code = WebCodeEnum.PARAM_ERROR.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.PARAM_ERROR.desc();
        List<ArgumentInvalidResultVO> invalidArguments = new ArrayList<>();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        fieldErrors.forEach(item -> {
            ArgumentInvalidResultVO argumentInvalidResultVO = ArgumentInvalidResultVO.builder()
                    .field(item.getField())
                    .value(item.getRejectedValue())
                    .message(item.getDefaultMessage())
                    .build();
            invalidArguments.add(argumentInvalidResultVO);
        });
        ArgumentInvalidResultVO data = invalidArguments.get(0);
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).data(data).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理自定义异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ResultVO> exceptionHandler(BaseException ex) {
        HttpStatus status = null != ex.getStatus() ? ex.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR;
        String code = ex.getCode();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : ex.getMessage();
        Object data = ex.getData();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).data(data).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理账号不存在异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ResultVO> exceptionHandler(UsernameNotFoundException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String code = CloudWebCodeEnum.ACCOUNT_NOT_EXIST.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.ACCOUNT_NOT_EXIST.desc();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理错误凭证异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResultVO> exceptionHandler(BadCredentialsException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String code = CloudWebCodeEnum.PASSWORD_ERROR.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.PASSWORD_ERROR.desc();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理无效账号异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(AccountExpiredException.class)
    public ResponseEntity<ResultVO> exceptionHandler(AccountExpiredException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String code = CloudWebCodeEnum.INVALID_ACCOUNT.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.INVALID_ACCOUNT.desc();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理账号锁定异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ResultVO> exceptionHandler(LockedException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String code = CloudWebCodeEnum.ACCOUNT_LOCKED.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.ACCOUNT_LOCKED.desc();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理账号禁用异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ResultVO> exceptionHandler(DisabledException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String code = CloudWebCodeEnum.ACCOUNT_DISABLED.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.ACCOUNT_DISABLED.desc();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理无效凭证异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(CredentialsExpiredException.class)
    public ResponseEntity<ResultVO> exceptionHandler(CredentialsExpiredException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String code = CloudWebCodeEnum.INVALID_CREDENTIAL.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.INVALID_CREDENTIAL.desc();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理无效凭证异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ResultVO> exceptionHandler(InternalAuthenticationServiceException ex) {
        if (StringUtils.contains(ex.getCause().toString(), "BadCredentialsException") && ex.getMessage()
                .contains(
                        this.i18nConfig.getMessage(StringUtils.lowerCase(CloudWebCodeEnum.ACCOUNT_NOT_EXIST.name())))) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            String code = CloudWebCodeEnum.ACCOUNT_NOT_EXIST.value();
            String msg = this.i18nConfig.getMessage(code);
            String message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.ACCOUNT_NOT_EXIST.desc();
            ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
            return ResponseEntity.status(status).body(entity);
        }
        return null;
    }

    /**
     * 处理无效token异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ResultVO> exceptionHandler(InvalidTokenException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String code = CloudWebCodeEnum.INVALID_TOKEN.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.INVALID_TOKEN.desc();
        if (StringUtils.contains(ex.getMessage(), "Token was not recognised") || StringUtils.contains(ex.getMessage(),
                "Invalid access token")) {
            code = CloudWebCodeEnum.INVALID_ACCESS_TOKEN.value();
            msg = this.i18nConfig.getMessage(code);
            message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.INVALID_ACCESS_TOKEN.desc();
        } else if (StringUtils.contains(ex.getMessage(), "Token has expired")) {
            code = CloudWebCodeEnum.ACCESS_TOKEN_EXPIRED.value();
            msg = this.i18nConfig.getMessage(code);
            message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.ACCESS_TOKEN_EXPIRED.desc();
        } else if (StringUtils.contains(ex.getMessage(), "Cannot convert access token to JSON")) {
            code = CloudWebCodeEnum.ACCESS_TOKEN_FORMAT_ERROR.value();
            msg = this.i18nConfig.getMessage(code);
            message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.ACCESS_TOKEN_FORMAT_ERROR.desc();
        } else if (StringUtils.contains(ex.getMessage(), "Invalid refresh token (expired)")) {
            code = CloudWebCodeEnum.REFRESH_TOKEN_EXPIRED.value();
            msg = this.i18nConfig.getMessage(code);
            message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.REFRESH_TOKEN_EXPIRED.desc();
        }
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理无效凭证异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<ResultVO> exceptionHandler(InsufficientAuthenticationException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String code = WebCodeEnum.UNAUTHORIZED.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.UNAUTHORIZED.desc();
        if (StringUtils.contains(ex.getMessage(), "Invalid access token")) {
            code = CloudWebCodeEnum.INVALID_ACCESS_TOKEN.value();
            msg = this.i18nConfig.getMessage(code);
            message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.INVALID_ACCESS_TOKEN.desc();
        }
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理无效凭证异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ResultVO> exceptionHandler(AuthenticationCredentialsNotFoundException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String code = WebCodeEnum.UNAUTHORIZED.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.UNAUTHORIZED.desc();
        if (StringUtils.contains(ex.getMessage(), "Invalid access token")) {
            code = CloudWebCodeEnum.INVALID_ACCESS_TOKEN.value();
            msg = this.i18nConfig.getMessage(code);
            message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.INVALID_ACCESS_TOKEN.desc();
        }
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理rpc调用异常异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ResultVO> exceptionHandler(FeignException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String code = CloudWebCodeEnum.RPC_ERROR.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.RPC_ERROR.desc();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理无效客户端异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(BadClientCredentialsException.class)
    public ResponseEntity<ResultVO> exceptionHandler(BadClientCredentialsException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String code = CloudWebCodeEnum.INVALID_CLIENT.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.INVALID_CLIENT.desc();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理无效客户端异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(InvalidClientException.class)
    public ResponseEntity<ResultVO> exceptionHandler(InvalidClientException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String code = CloudWebCodeEnum.INVALID_CLIENT.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.INVALID_CLIENT.desc();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理客户端无权限异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(UnauthorizedClientException.class)
    public ResponseEntity<ResultVO> exceptionHandler(UnauthorizedClientException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String code = CloudWebCodeEnum.UNAUTHORIZED_CLIENT.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.UNAUTHORIZED_CLIENT.desc();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理无效授权异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(InvalidGrantException.class)
    public ResponseEntity<ResultVO> exceptionHandler(InvalidGrantException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String code = CloudWebCodeEnum.INVALID_GRANT.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.INVALID_GRANT.desc();
        if (StringUtils.contains(ex.getMessage(), "Bad credentials")) {
            code = CloudWebCodeEnum.PASSWORD_ERROR.value();
            msg = this.i18nConfig.getMessage(code);
            message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.PASSWORD_ERROR.desc();
        } else if (StringUtils.contains(ex.getMessage(), "User is disabled")) {
            code = CloudWebCodeEnum.ACCOUNT_DISABLED.value();
            msg = this.i18nConfig.getMessage(code);
            message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.ACCOUNT_DISABLED.desc();
        } else if (StringUtils.contains(ex.getMessage(), "User account is locked")) {
            code = CloudWebCodeEnum.ACCOUNT_LOCKED.value();
            msg = this.i18nConfig.getMessage(code);
            message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.ACCOUNT_LOCKED.desc();
        } else if (StringUtils.contains(ex.getMessage(), "Invalid refresh token")) {
            code = CloudWebCodeEnum.INVALID_REFRESH_TOKEN.value();
            msg = this.i18nConfig.getMessage(code);
            message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.INVALID_REFRESH_TOKEN.desc();
        } else if (StringUtils.contains(ex.getMessage(), "Invalid authorization code")) {
            code = CloudWebCodeEnum.INVALID_CODE.value();
            msg = this.i18nConfig.getMessage(code);
            message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.INVALID_CODE.desc();
        }
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理无效授权范围异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(InvalidScopeException.class)
    public ResponseEntity<ResultVO> exceptionHandler(InvalidScopeException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String code = CloudWebCodeEnum.INVALID_SCOPE.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.INVALID_SCOPE.desc();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理无效请求异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ResultVO> exceptionHandler(InvalidRequestException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String code = WebCodeEnum.BAD_REQUEST.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.BAD_REQUEST.desc();
        if (StringUtils.contains(ex.getMessage(), "Missing grant type")) {
            code = CloudWebCodeEnum.GRANT_TYPE_NOT_NULL.value();
            msg = this.i18nConfig.getMessage(code);
            message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.GRANT_TYPE_NOT_NULL.desc();
        }
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理不支持的授权类型异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(UnsupportedGrantTypeException.class)
    public ResponseEntity<ResultVO> exceptionHandler(UnsupportedGrantTypeException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String code = CloudWebCodeEnum.UNSUPPORTED_GRANT_TYPE.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.UNSUPPORTED_GRANT_TYPE.desc();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理不支持的响应类型异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(UnsupportedResponseTypeException.class)
    public ResponseEntity<ResultVO> exceptionHandler(UnsupportedResponseTypeException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String code = CloudWebCodeEnum.UNSUPPORTED_RESPONSE_TYPE.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.UNSUPPORTED_RESPONSE_TYPE.desc();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理无权限异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(UserDeniedAuthorizationException.class)
    public ResponseEntity<ResultVO> exceptionHandler(UserDeniedAuthorizationException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String code = WebCodeEnum.UNAUTHORIZED.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.UNAUTHORIZED.desc();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理重试超时捕获异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(RetryableException.class)
    public ResponseEntity<ResultVO> exceptionHandler(RetryableException ex) {
        HttpStatus status = HttpStatus.REQUEST_TIMEOUT;
        String code = CloudWebCodeEnum.REQUEST_TIMEOUT.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.REQUEST_TIMEOUT.desc();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理熔断器超时异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(HystrixRuntimeException.class)
    public ResponseEntity<ResultVO> exceptionHandler(HystrixRuntimeException ex) {
        HttpStatus status = HttpStatus.REQUEST_TIMEOUT;
        String code = CloudWebCodeEnum.REQUEST_TIMEOUT.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : CloudWebCodeEnum.REQUEST_TIMEOUT.desc();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理熔断器降级捕获异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(FeignErrorException.class)
    public ResponseEntity<ResultVO> exceptionHandler(FeignErrorException ex) {
        HttpStatus status = null != ex.getStatus() ? ex.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR;
        String code = ex.getCode();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : ex.getMessage();
        Object data = ex.getData();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).data(data).build();
        return ResponseEntity.status(status).body(entity);
    }

    /**
     * 处理其他异常.
     *
     * @param ex 异常
     * @return ResponseEntity<ResultVO>
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultVO> exceptionHandler(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String code = WebCodeEnum.FAILED.value();
        String msg = this.i18nConfig.getMessage(code);
        String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.FAILED.desc();
        ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
        return ResponseEntity.status(status).body(entity);
    }
}
