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
		HttpStatus status = null;
		String code = null;
		String message = null;
		Object data = null;
		if (StringUtils.contains(className, "NullPointerException")) {
			status = HttpStatus.BAD_REQUEST;
			code = WebCodeEnum.NULL_POINTER.value();
			message = WebCodeEnum.NULL_POINTER.desc();
		} else if (StringUtils.contains(className, "NoHandlerFoundException")) {
			status = HttpStatus.NOT_FOUND;
			code = WebCodeEnum.NOT_FOUND.value();
			message = WebCodeEnum.NOT_FOUND.desc();
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
		} else if (StringUtils.contains(className, "HttpMessageNotReadableException")) {
			status = HttpStatus.BAD_REQUEST;
			code = WebCodeEnum.NOT_READ.value();
			message = WebCodeEnum.NOT_READ.desc();
		} else if (StringUtils.contains(className, "HttpMessageNotWritableException")) {
			status = HttpStatus.BAD_REQUEST;
			code = WebCodeEnum.NOT_WRITABLE.value();
			message = WebCodeEnum.NOT_WRITABLE.desc();
		} else if (StringUtils.contains(className, "ConversionNotSupportedException")) {
			status = HttpStatus.BAD_REQUEST;
			code = WebCodeEnum.CONVERSION_NOT_SUPPORTED.value();
			message = WebCodeEnum.CONVERSION_NOT_SUPPORTED.desc();
		} else if (StringUtils.contains(className, "IllegalArgumentException")) {
			status = HttpStatus.BAD_REQUEST;
			code = WebCodeEnum.ILLEGAL_REQUEST.value();
			message = WebCodeEnum.ILLEGAL_REQUEST.desc();
		}
		if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(message)) {
			ResultVO result = ResultVO.builder().status(status).code(code).message(message).data(data).build();
			return result;
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
			if (StringUtils.contains(className, "UnauthorizedException")) {
				status = HttpStatus.UNAUTHORIZED;
				code = WebCodeEnum.UNAUTHORIZED.value();
				message = WebCodeEnum.UNAUTHORIZED.desc();
			} else if (StringUtils.contains(className, "FileNotFoundException")) {
				status = HttpStatus.BAD_REQUEST;
				code = WebCodeEnum.FILE_NOT_FOUND.value();
				message = WebCodeEnum.FILE_NOT_FOUND.desc();
			} else if (StringUtils.contains(className, "ClassCastException")) {
				status = HttpStatus.BAD_REQUEST;
				code = WebCodeEnum.TYPE_CONVERSION_ERROR.value();
				message = WebCodeEnum.TYPE_CONVERSION_ERROR.desc();
			} else if (StringUtils.contains(className, "NumberFormatException")) {
				status = HttpStatus.BAD_REQUEST;
				code = WebCodeEnum.NUMBER_FORMAT_FOUND.value();
				message = WebCodeEnum.NUMBER_FORMAT_FOUND.desc();
			} else if (StringUtils.contains(className, "SecurityException")) {
				status = HttpStatus.BAD_REQUEST;
				code = WebCodeEnum.SECURITY_ERROR.value();
				message = WebCodeEnum.SECURITY_ERROR.desc();
			} else if (StringUtils.contains(className, "BadSqlGrammarException")) {
				status = HttpStatus.BAD_REQUEST;
				code = WebCodeEnum.BAD_SQL_GRAMMAR.value();
				message = WebCodeEnum.BAD_SQL_GRAMMAR.desc();
			} else if (StringUtils.contains(className, "SQLException")) {
				status = HttpStatus.BAD_REQUEST;
				code = WebCodeEnum.SQL_ERROR.value();
				message = WebCodeEnum.SQL_ERROR.desc();
			} else if (StringUtils.contains(className, "TypeNotPresentException")) {
				status = HttpStatus.BAD_REQUEST;
				code = WebCodeEnum.TYPE_NOT_PRESENT.value();
				message = WebCodeEnum.TYPE_NOT_PRESENT.desc();
			}
			if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(message)) {
				result = ResultVO.builder().status(status).code(code).message(message).data(data).build();
			}
		}
		return result;
	}

	private static ResultVO getException3(Exception ex) {
		String className = ex.getClass().getName();
		ResultVO result = getException2(ex);
		if (null == result) {
			HttpStatus status = null;
			String code = null;
			String message = null;
			Object data = null;
			if (StringUtils.contains(className, "IOException")) {
				status = HttpStatus.BAD_REQUEST;
				code = WebCodeEnum.IO_ERROR.value();
				message = WebCodeEnum.IO_ERROR.desc();
			} else if (StringUtils.contains(className, "NoSuchMethodException")) {
				status = HttpStatus.BAD_REQUEST;
				code = WebCodeEnum.NO_SUCH_METHOD.value();
				message = WebCodeEnum.NO_SUCH_METHOD.desc();
			} else if (StringUtils.contains(className, "IndexOutOfBoundsException")) {
				status = HttpStatus.BAD_REQUEST;
				code = WebCodeEnum.INDEX_OUT_OF_BOUNDS.value();
				message = WebCodeEnum.INDEX_OUT_OF_BOUNDS.desc();
			} else if (StringUtils.contains(className, "NoSuchBeanDefinitionException")) {
				status = HttpStatus.BAD_REQUEST;
				code = WebCodeEnum.NO_SUCH_BEAN.value();
				message = WebCodeEnum.NO_SUCH_BEAN.desc();
			} else if (StringUtils.contains(className, "TypeMismatchException")) {
				status = HttpStatus.BAD_REQUEST;
				code = WebCodeEnum.TYPE_MISMATCH.value();
				message = WebCodeEnum.TYPE_MISMATCH.desc();
			} else if (StringUtils.contains(className, "StackOverflowError")) {
				status = HttpStatus.BAD_REQUEST;
				code = WebCodeEnum.STACK_OVERFLOW.value();
				message = WebCodeEnum.STACK_OVERFLOW.desc();
			} else if (StringUtils.contains(className, "ArithmeticException")) {
				status = HttpStatus.BAD_REQUEST;
				code = WebCodeEnum.ARITHMETIC_ERROR.value();
				message = WebCodeEnum.ARITHMETIC_ERROR.desc();
			}
			if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(message)) {
				result = ResultVO.builder().status(status).code(code).message(message).data(data).build();
			}
		}
		return result;
	}

	private static ResultVO getException4(Exception ex) {
		String className = ex.getClass().getName();
		ResultVO result = getException3(ex);
		if (null == result) {
			HttpStatus status = null;
			String code = null;
			String message = null;
			Object data = null;
			if (StringUtils.contains(className, "MissingServletRequestParameterException")) {
				status = HttpStatus.BAD_REQUEST;
				code = WebCodeEnum.PARAM_MISS.value();
				message = WebCodeEnum.PARAM_MISS.desc();
				MissingServletRequestParameterException item = (MissingServletRequestParameterException) ex;
				ArgumentInvalidResultVO argumentInvalidResultVO = ArgumentInvalidResultVO.builder()
						.field(item.getParameterName())
						.message(item.getMessage())
						.build();
				data = argumentInvalidResultVO;
			} else if (StringUtils.contains(className, "MethodArgumentTypeMismatchException")) {
				status = HttpStatus.BAD_REQUEST;
				code = WebCodeEnum.PARAM_TYPE_ERROR.value();
				message = WebCodeEnum.PARAM_TYPE_ERROR.desc();
				MethodArgumentTypeMismatchException item = (MethodArgumentTypeMismatchException) ex;
				ArgumentInvalidResultVO argumentInvalidResultVO = ArgumentInvalidResultVO.builder()
						.field(item.getName())
						.value(item.getValue())
						.message(item.getMessage())
						.build();
				data = argumentInvalidResultVO;
			} else if (StringUtils.contains(className, "ConstraintViolationException")) {
				status = HttpStatus.BAD_REQUEST;
				code = WebCodeEnum.PARAM_ERROR.value();
				message = WebCodeEnum.PARAM_ERROR.desc();
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
				data = invalidArguments;
			}
			if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(message)) {
				result = ResultVO.builder().status(status).code(code).message(message).data(data).build();
			}
		}
		return result;
	}

	private static ResultVO getException5(Exception ex) {
		String className = ex.getClass().getName();
		ResultVO result = getException4(ex);
		if (null == result) {
			HttpStatus status = null;
			String code = null;
			String message = null;
			Object data = null;
			if (StringUtils.contains(className, "MethodArgumentNotValidException")) {
				status = HttpStatus.BAD_REQUEST;
				code = WebCodeEnum.PARAM_ERROR.value();
				message = WebCodeEnum.PARAM_ERROR.desc();
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
				data = invalidArguments.get(0);
			} else if (StringUtils.contains(className, "BindException")) {
				status = HttpStatus.BAD_REQUEST;
				code = WebCodeEnum.PARAM_ERROR.value();
				message = WebCodeEnum.PARAM_ERROR.desc();
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
				data = invalidArguments.get(0);
			}
			if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(message)) {
				result = ResultVO.builder().status(status).code(code).message(message).data(data).build();
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
		ResultVO result = getException5(ex);
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
