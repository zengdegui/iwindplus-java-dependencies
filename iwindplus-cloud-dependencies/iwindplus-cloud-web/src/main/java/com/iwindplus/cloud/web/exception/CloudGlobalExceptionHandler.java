/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.cloud.web.exception;

import com.iwindplus.boot.web.domain.enumerate.WebCodeEnum;
import com.iwindplus.boot.web.domain.vo.ResultVO;
import com.iwindplus.boot.web.exception.GlobalExceptionHandler;
import com.iwindplus.cloud.feign.exception.FeignErrorException;
import com.iwindplus.cloud.web.exception.domain.enumerate.CloudWebCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Order(1)
@RestControllerAdvice
public class CloudGlobalExceptionHandler extends GlobalExceptionHandler {
    private static ResultVO getException(Exception ex) {
        ResultVO result = getError(ex);
        if (null == result) {
            String className = ex.getClass().getName();
            if (StringUtils.contains(className, "UsernameNotFoundException")) {
                return getResultVO(HttpStatus.BAD_REQUEST, CloudWebCodeEnum.ACCOUNT_NOT_EXIST, null);
            } else if (StringUtils.contains(className, "BadCredentialsException")) {
                return getResultVO(HttpStatus.BAD_REQUEST, CloudWebCodeEnum.PASSWORD_ERROR, null);
            } else if (StringUtils.contains(className, "AccountExpiredException")) {
                return getResultVO(HttpStatus.BAD_REQUEST, CloudWebCodeEnum.INVALID_ACCOUNT, null);
            } else if (StringUtils.contains(className, "LockedException")) {
                return getResultVO(HttpStatus.BAD_REQUEST, CloudWebCodeEnum.ACCOUNT_LOCKED, null);
            } else if (StringUtils.contains(className, "DisabledException")) {
                return getResultVO(HttpStatus.BAD_REQUEST, CloudWebCodeEnum.ACCOUNT_DISABLED, null);
            } else if (StringUtils.contains(className, "CredentialsExpiredException")) {
                return getResultVO(HttpStatus.BAD_REQUEST, CloudWebCodeEnum.INVALID_CREDENTIAL, null);
            } else if (StringUtils.contains(className, "InvalidTokenException")) {
                return getResultVO(HttpStatus.BAD_REQUEST, CloudWebCodeEnum.INVALID_ACCESS_TOKEN, null);
            } else if (StringUtils.contains(className, "InvalidScopeException")) {
                return getResultVO(HttpStatus.BAD_REQUEST, CloudWebCodeEnum.INVALID_SCOPE, null);
            } else if (StringUtils.contains(className, "UnauthorizedClientException")) {
                return getResultVO(HttpStatus.UNAUTHORIZED, CloudWebCodeEnum.UNAUTHORIZED_CLIENT, null);
            } else if (StringUtils.contains(className, "InsufficientAuthenticationException") || StringUtils.contains(
                    className, "AuthenticationCredentialsNotFoundException")) {
                if (StringUtils.contains(ex.getMessage(), "Invalid access token")) {
                    return getResultVO(HttpStatus.UNAUTHORIZED, CloudWebCodeEnum.INVALID_ACCESS_TOKEN, null);
                }
                return getResultVO(HttpStatus.UNAUTHORIZED, WebCodeEnum.UNAUTHORIZED, null);
            } else if (StringUtils.contains(className, "ClientException") || StringUtils.contains(className,
                    "FeignException")) {
                return getResultVO(HttpStatus.BAD_REQUEST, CloudWebCodeEnum.RPC_ERROR, null);
            } else if (StringUtils.contains(className, "BadClientCredentialsException")) {
                return getResultVO(HttpStatus.BAD_REQUEST, CloudWebCodeEnum.INVALID_CLIENT, null);
            } else if (StringUtils.contains(className, "NoSuchClientException") || StringUtils.contains(className,
                    "InvalidClientException")) {
                return getResultVO(HttpStatus.BAD_REQUEST, CloudWebCodeEnum.INVALID_CLIENT, null);
            } else if (StringUtils.contains(className, "InternalAuthenticationServiceException")) {
                if (StringUtils.contains(ex.getCause().toString(), "BadCredentialsException")) {
                    return getResultVO(HttpStatus.BAD_REQUEST, CloudWebCodeEnum.ACCOUNT_NOT_EXIST, null);
                }
                return getResultVO(HttpStatus.BAD_REQUEST, CloudWebCodeEnum.INVALID_CREDENTIAL, null);
            }
        }
        return result;
    }

    private static ResultVO getException2(Exception ex) {
        ResultVO result = getException(ex);
        if (null == result) {
            String className = ex.getClass().getName();
            if (StringUtils.contains(className, "InvalidGrantException")) {
                if (StringUtils.contains(ex.getMessage(), "Bad credentials")) {
                    return getResultVO(HttpStatus.BAD_REQUEST, CloudWebCodeEnum.PASSWORD_ERROR, null);
                } else if (StringUtils.contains(ex.getMessage(), "User is disabled")) {
                    return getResultVO(HttpStatus.BAD_REQUEST, CloudWebCodeEnum.ACCOUNT_DISABLED, null);
                } else if (StringUtils.contains(ex.getMessage(), "User account is locked")) {
                    return getResultVO(HttpStatus.BAD_REQUEST, CloudWebCodeEnum.ACCOUNT_LOCKED, null);
                } else if (StringUtils.contains(ex.getMessage(), "Invalid refresh token")) {
                    return getResultVO(HttpStatus.BAD_REQUEST, CloudWebCodeEnum.INVALID_REFRESH_TOKEN, null);
                } else if (StringUtils.contains(ex.getMessage(), "Invalid authorization code")) {
                    return getResultVO(HttpStatus.BAD_REQUEST, CloudWebCodeEnum.INVALID_CODE, null);
                }
                return getResultVO(HttpStatus.BAD_REQUEST, CloudWebCodeEnum.INVALID_GRANT, null);
            } else if (StringUtils.contains(className, "InvalidTokenException")) {
                if (StringUtils.contains(ex.getMessage(), "Token was not recognised") || StringUtils.contains(
                        ex.getMessage(), "Invalid access token")) {
                    return getResultVO(HttpStatus.BAD_REQUEST, CloudWebCodeEnum.INVALID_ACCESS_TOKEN, null);
                } else if (StringUtils.contains(ex.getMessage(), "Token has expired")) {
                    return getResultVO(HttpStatus.BAD_REQUEST, CloudWebCodeEnum.ACCESS_TOKEN_EXPIRED, null);
                } else if (StringUtils.contains(ex.getMessage(), "Cannot convert access token to JSON")) {
                    return getResultVO(HttpStatus.BAD_REQUEST, CloudWebCodeEnum.ACCESS_TOKEN_FORMAT_ERROR, null);
                } else if (StringUtils.contains(ex.getMessage(), "Invalid refresh token (expired)")) {
                    return getResultVO(HttpStatus.BAD_REQUEST, CloudWebCodeEnum.REFRESH_TOKEN_EXPIRED, null);
                }
                return getResultVO(HttpStatus.BAD_REQUEST, CloudWebCodeEnum.INVALID_TOKEN, null);
            }
        }
        return result;
    }

    private static ResultVO getException3(Exception ex) {
        ResultVO result = getException2(ex);
        if (null == result) {
            String className = ex.getClass().getName();
            if (StringUtils.contains(className, "InvalidRequestException")) {
                if (StringUtils.contains(ex.getMessage(), "Missing grant type")) {
                    return getResultVO(HttpStatus.BAD_REQUEST, CloudWebCodeEnum.GRANT_TYPE_NOT_NULL, null);
                }
                return getResultVO(HttpStatus.BAD_REQUEST, WebCodeEnum.BAD_REQUEST, null);
            } else if (StringUtils.contains(className, "UnsupportedGrantTypeException")) {
                return getResultVO(HttpStatus.BAD_REQUEST, CloudWebCodeEnum.UNSUPPORTED_GRANT_TYPE, null);
            } else if (StringUtils.contains(className, "UnsupportedResponseTypeException")) {
                return getResultVO(HttpStatus.BAD_REQUEST, CloudWebCodeEnum.UNSUPPORTED_RESPONSE_TYPE, null);
            } else if (StringUtils.contains(className, "UserDeniedAuthorizationException") || StringUtils.contains(
                    className, "UserDenAccessDeniedExceptioniedAuthorizationException")) {
                return getResultVO(HttpStatus.UNAUTHORIZED, WebCodeEnum.UNAUTHORIZED, null);
            } else if (StringUtils.contains(className, "RetryableException") || StringUtils.contains(className,
                    "HystrixRuntimeException")) {
                return getResultVO(HttpStatus.REQUEST_TIMEOUT, CloudWebCodeEnum.REQUEST_TIMEOUT, null);
            } else if (StringUtils.contains(className, "FeignErrorException")) {
                // 熔断器降级捕获异常
                return ResultVO.builder()
                        .status(((FeignErrorException) ex).getStatus())
                        .code(((FeignErrorException) ex).getCode())
                        .message(((FeignErrorException) ex).getMessage())
                        .data(((FeignErrorException) ex).getData())
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

    private static ResultVO getResultVO(HttpStatus status, CloudWebCodeEnum cloudWebCodeEnum, Object data) {
        return ResultVO.builder()
                .status(status)
                .code(cloudWebCodeEnum.value())
                .message(cloudWebCodeEnum.desc())
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
        ResultVO result = getException3(ex);
        return this.getResponse(result);
    }
}
