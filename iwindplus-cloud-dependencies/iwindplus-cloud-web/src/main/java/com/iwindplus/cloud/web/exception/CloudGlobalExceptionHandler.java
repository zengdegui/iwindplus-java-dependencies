/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.cloud.web.exception;

import com.iwindplus.boot.web.domain.enumerate.WebCodeEnum;
import com.iwindplus.boot.web.domain.vo.ResultVO;
import com.iwindplus.boot.web.exception.GlobalExceptionHandler;
import com.iwindplus.boot.web.i18n.I18nConfig;
import com.iwindplus.cloud.feign.exception.FeignErrorException;
import com.iwindplus.cloud.web.exception.domain.enumerate.CloudWebCodeEnum;
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
@Order(0)
@RestControllerAdvice
public class CloudGlobalExceptionHandler extends GlobalExceptionHandler {
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
		ResultVO result = getException4(ex);
		return this.getResponse(result);
	}

	private static ResultVO getException(Exception ex) {
		String className = ex.getClass().getName();
		ResultVO result = getError(ex);
		if (null == result) {
			HttpStatus status = null;
			String code = null;
			String message = null;
			Object data = null;
			if (StringUtils.contains(className, "UsernameNotFoundException")) {
				code = CloudWebCodeEnum.ACCOUNT_NOT_EXIST.value();
				message = CloudWebCodeEnum.ACCOUNT_NOT_EXIST.desc();
			} else if (StringUtils.contains(className, "BadCredentialsException")) {
				code = CloudWebCodeEnum.PASSWORD_ERROR.value();
				message = CloudWebCodeEnum.PASSWORD_ERROR.desc();
			} else if (StringUtils.contains(className, "AccountExpiredException")) {
				code = CloudWebCodeEnum.INVALID_ACCOUNT.value();
				message = CloudWebCodeEnum.INVALID_ACCOUNT.desc();
			} else if (StringUtils.contains(className, "LockedException")) {
				code = CloudWebCodeEnum.ACCOUNT_LOCKED.value();
				message = CloudWebCodeEnum.ACCOUNT_LOCKED.desc();
			} else if (StringUtils.contains(className, "DisabledException")) {
				code = CloudWebCodeEnum.ACCOUNT_DISABLED.value();
				message = CloudWebCodeEnum.ACCOUNT_DISABLED.desc();
			} else if (StringUtils.contains(className, "CredentialsExpiredException")) {
				code = CloudWebCodeEnum.INVALID_CREDENTIAL.value();
				message = CloudWebCodeEnum.INVALID_CREDENTIAL.desc();
			} else if (StringUtils.contains(className, "InternalAuthenticationServiceException")) {
				if (StringUtils.contains(ex.getCause().toString(), "BadCredentialsException")) {
					code = CloudWebCodeEnum.ACCOUNT_NOT_EXIST.value();
					message = CloudWebCodeEnum.ACCOUNT_NOT_EXIST.desc();
				}
			} else if (StringUtils.contains(className, "InvalidTokenException")) {
				code = CloudWebCodeEnum.INVALID_ACCESS_TOKEN.value();
				message = CloudWebCodeEnum.INVALID_ACCESS_TOKEN.desc();
			}
			if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(message)) {
				result = ResultVO.builder().status(status).code(code).message(message).data(data).build();
			}
		}
		return null;
	}

	private static ResultVO getException2(Exception ex) {
		String className = ex.getClass().getName();
		ResultVO result = getException(ex);
		if (null == result) {
			HttpStatus status = null;
			String code = null;
			String message = null;
			Object data = null;
			if (StringUtils.contains(className, "InsufficientAuthenticationException")
					|| StringUtils.contains(className, "AuthenticationCredentialsNotFoundException")) {
				status = HttpStatus.UNAUTHORIZED;
				code = WebCodeEnum.UNAUTHORIZED.value();
				message = WebCodeEnum.UNAUTHORIZED.desc();
				if (StringUtils.contains(ex.getMessage(), "Invalid access token")) {
					code = CloudWebCodeEnum.INVALID_ACCESS_TOKEN.value();
					message = CloudWebCodeEnum.INVALID_ACCESS_TOKEN.desc();
				}
			} else if (StringUtils.contains(className, "ClientException")
					|| StringUtils.contains(className, "FeignException")) {
				status = HttpStatus.BAD_REQUEST;
				code = CloudWebCodeEnum.RPC_ERROR.value();
				message = CloudWebCodeEnum.RPC_ERROR.desc();
			} else if (StringUtils.contains(className, "BadClientCredentialsException")) {
				code = CloudWebCodeEnum.INVALID_CLIENT.value();
				message = CloudWebCodeEnum.INVALID_CLIENT.desc();
			} else if (StringUtils.contains(className, "NoSuchClientException")
					|| StringUtils.contains(className, "InvalidClientException")) {
				status = HttpStatus.BAD_REQUEST;
				code = CloudWebCodeEnum.INVALID_CLIENT.value();
				message = CloudWebCodeEnum.INVALID_CLIENT.desc();
			} else if (StringUtils.contains(className, "UnauthorizedClientException")) {
				status = HttpStatus.UNAUTHORIZED;
				code = CloudWebCodeEnum.UNAUTHORIZED_CLIENT.value();
				message = CloudWebCodeEnum.UNAUTHORIZED_CLIENT.desc();
			} else if (StringUtils.contains(className, "InvalidScopeException")) {
				code = CloudWebCodeEnum.INVALID_SCOPE.value();
				message = CloudWebCodeEnum.INVALID_SCOPE.desc();
			}
			if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(message)) {
				result = ResultVO.builder().status(status).code(code).message(message).data(data).build();
			}
		}
		return null;
	}

	private static ResultVO getException3(Exception ex) {
		String className = ex.getClass().getName();
		ResultVO result = getException2(ex);
		if (null == result) {
			HttpStatus status = null;
			String code = null;
			String message = null;
			Object data = null;
			if (StringUtils.contains(className, "InvalidGrantException")) {
				code = CloudWebCodeEnum.INVALID_GRANT.value();
				message = CloudWebCodeEnum.INVALID_GRANT.desc();
				if (StringUtils.contains(ex.getMessage(), "Bad credentials")) {
					code = CloudWebCodeEnum.PASSWORD_ERROR.value();
					message = CloudWebCodeEnum.PASSWORD_ERROR.desc();
				} else if (StringUtils.contains(ex.getMessage(), "User is disabled")) {
					code = CloudWebCodeEnum.ACCOUNT_DISABLED.value();
					message = CloudWebCodeEnum.ACCOUNT_DISABLED.desc();
				} else if (StringUtils.contains(ex.getMessage(), "User account is locked")) {
					code = CloudWebCodeEnum.ACCOUNT_LOCKED.value();
					message = CloudWebCodeEnum.ACCOUNT_LOCKED.desc();
				} else if (StringUtils.contains(ex.getMessage(), "Invalid refresh token")) {
					code = CloudWebCodeEnum.INVALID_REFRESH_TOKEN.value();
					message = CloudWebCodeEnum.INVALID_REFRESH_TOKEN.desc();
				} else if (StringUtils.contains(ex.getMessage(), "Invalid authorization code")) {
					code = CloudWebCodeEnum.INVALID_CODE.value();
					message = CloudWebCodeEnum.INVALID_CODE.desc();
				}
			} else if (StringUtils.contains(className, "InvalidTokenException")) {
				code = CloudWebCodeEnum.INVALID_TOKEN.value();
				message = CloudWebCodeEnum.INVALID_TOKEN.desc();
				if (StringUtils.contains(ex.getMessage(), "Token was not recognised")
						|| StringUtils.contains(ex.getMessage(), "Invalid access token")) {
					code = CloudWebCodeEnum.INVALID_ACCESS_TOKEN.value();
					message = CloudWebCodeEnum.INVALID_ACCESS_TOKEN.desc();
				} else if (StringUtils.contains(ex.getMessage(), "Token has expired")) {
					code = CloudWebCodeEnum.ACCESS_TOKEN_EXPIRED.value();
					message = CloudWebCodeEnum.ACCESS_TOKEN_EXPIRED.desc();
				} else if (StringUtils.contains(ex.getMessage(), "Cannot convert access token to JSON")) {
					code = CloudWebCodeEnum.ACCESS_TOKEN_FORMAT_ERROR.value();
					message = CloudWebCodeEnum.ACCESS_TOKEN_FORMAT_ERROR.desc();
				} else if (StringUtils.contains(ex.getMessage(), "Invalid refresh token (expired)")) {
					code = CloudWebCodeEnum.REFRESH_TOKEN_EXPIRED.value();
					message = CloudWebCodeEnum.REFRESH_TOKEN_EXPIRED.desc();
				}
			}
			if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(message)) {
				result = ResultVO.builder().status(status).code(code).message(message).data(data).build();
			}
		}
		return null;
	}

	private static ResultVO getException4(Exception ex) {
		String className = ex.getClass().getName();
		ResultVO result = getException3(ex);
		if (null == result) {
			HttpStatus status = null;
			String code = null;
			String message = null;
			Object data = null;
			if (StringUtils.contains(className, "InvalidRequestException")) {
				status = HttpStatus.BAD_REQUEST;
				code = WebCodeEnum.BAD_REQUEST.value();
				message = WebCodeEnum.BAD_REQUEST.desc();
				if (StringUtils.contains(ex.getMessage(), "Missing grant type")) {
					code = CloudWebCodeEnum.GRANT_TYPE_NOT_NULL.value();
					message = CloudWebCodeEnum.GRANT_TYPE_NOT_NULL.desc();
				}
			} else if (StringUtils.contains(className, "UnsupportedGrantTypeException")) {
				code = CloudWebCodeEnum.UNSUPPORTED_GRANT_TYPE.value();
				message = CloudWebCodeEnum.UNSUPPORTED_GRANT_TYPE.desc();
			} else if (StringUtils.contains(className, "UnsupportedResponseTypeException")) {
				code = CloudWebCodeEnum.UNSUPPORTED_RESPONSE_TYPE.value();
				message = CloudWebCodeEnum.UNSUPPORTED_RESPONSE_TYPE.desc();
			} else if (StringUtils.contains(className, "UserDeniedAuthorizationException")
					|| StringUtils.contains(className, "UserDenAccessDeniedExceptioniedAuthorizationException")) {
				status = HttpStatus.UNAUTHORIZED;
				code = WebCodeEnum.UNAUTHORIZED.value();
				message = WebCodeEnum.UNAUTHORIZED.desc();
			} else if (StringUtils.contains(className, "RetryableException")) {
				// 重试超时捕获异常
				status = HttpStatus.REQUEST_TIMEOUT;
				code = CloudWebCodeEnum.REQUEST_TIMEOUT.value();
				message = CloudWebCodeEnum.REQUEST_TIMEOUT.desc();
			} else if (StringUtils.contains(className, "HystrixRuntimeException")) {
				// 熔断器超时异常
				status = HttpStatus.REQUEST_TIMEOUT;
				code = CloudWebCodeEnum.REQUEST_TIMEOUT.value();
				message = CloudWebCodeEnum.REQUEST_TIMEOUT.desc();
			} else if (StringUtils.contains(className, "FeignErrorException")) {
				// 熔断器降级捕获异常
				status = ((FeignErrorException) ex).getStatus();
				code = ((FeignErrorException) ex).getCode();
				message = ((FeignErrorException) ex).getMessage();
				data = ((FeignErrorException) ex).getData();
			}
			if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(message)) {
				result = ResultVO.builder().status(status).code(code).message(message).data(data).build();
			}
		}
		return null;
	}
}
