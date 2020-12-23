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
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
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
@Order(2)
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
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
		MissingServletRequestParameterException item = (MissingServletRequestParameterException) ex;
		ArgumentInvalidResultVO data = ArgumentInvalidResultVO
				.builder()
				.field(item.getParameterName())
				.message(item.getMessage())
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
		MethodArgumentTypeMismatchException item = (MethodArgumentTypeMismatchException) ex;
		ArgumentInvalidResultVO data = ArgumentInvalidResultVO
				.builder()
				.field(item.getName())
				.value(item.getValue())
				.message(item.getMessage())
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
		ConstraintViolationException exs = (ConstraintViolationException) ex;
		Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
		violations.forEach(item -> {
			ArgumentInvalidResultVO argumentInvalidResultVO = ArgumentInvalidResultVO
					.builder()
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
		BaseException item = (BaseException) ex;
		HttpStatus status = null != item.getStatus() ? item.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR;
		String code = item.getCode();
		String msg = this.i18nConfig.getMessage(code);
		String message = StringUtils.isNotBlank(msg) ? msg : item.getMessage();
		Object data = item.getData();
		ResultVO entity = ResultVO.builder().status(status).code(code).message(message).data(data).build();
		return ResponseEntity.status(status).body(entity);
	}

	/**
	 * 处理无权限异常.
	 *
	 * @param ex 异常
	 * @return ResponseEntity<ResultVO>
	 */
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ResultVO> exceptionHandler(UnauthorizedException ex) {
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		String code = WebCodeEnum.UNAUTHORIZED.value();
		String msg = this.i18nConfig.getMessage(code);
		String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.UNAUTHORIZED.desc();
		ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
		return ResponseEntity.status(status).body(entity);
	}

	/**
	 * 处理自定义shiro异常.
	 *
	 * @param ex 异常
	 * @return ResponseEntity<ResultVO>
	 */
	@ExceptionHandler(BaseShiroAuthcException.class)
	public ResponseEntity<ResultVO> exceptionHandler(BaseShiroAuthcException ex) {
		BaseShiroAuthcException item = (BaseShiroAuthcException) ex;
		HttpStatus status = null != item.getStatus() ? item.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR;
		String code = item.getCode();
		String msg = this.i18nConfig.getMessage(code);
		String message = StringUtils.isNotBlank(msg) ? msg : item.getMessage();
		Object data = item.getData();
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
