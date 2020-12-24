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
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.jdbc.BadSqlGrammarException;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
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
		log.error("NullPointerException [{}]", ex.getMessage(), ex);
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
		log.error("NoHandlerFoundException [{}]", ex.getMessage(), ex);
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
		log.error("HttpRequestMethodNotSupportedException [{}]", ex.getMessage(), ex);
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
		log.error("HttpMediaTypeNotSupportedException [{}]", ex.getMessage(), ex);
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
		log.error("HttpMediaTypeNotAcceptableException [{}]", ex.getMessage(), ex);
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
		log.error("ConversionNotSupportedException [{}]", ex.getMessage(), ex);
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
		log.error("HttpMessageNotReadableException [{}]", ex.getMessage(), ex);
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
		log.error("HttpMessageNotWritableException [{}]", ex.getMessage(), ex);
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
		log.error("IllegalArgumentException [{}]", ex.getMessage(), ex);
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
		log.error("MissingServletRequestParameterException [{}]", ex.getMessage(), ex);
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
		log.error("MethodArgumentTypeMismatchException [{}]", ex.getMessage(), ex);
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
		log.error("ConstraintViolationException [{}]", ex.getMessage(), ex);
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
		log.error("MethodArgumentNotValidException [{}]", ex.getMessage(), ex);
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
	 * 文件找不到
	 *
	 * @param ex 异常
	 * @return ResponseEntity<ResultVO>
	 */
	@ExceptionHandler(FileNotFoundException.class)
	public ResponseEntity<ResultVO> exceptionHandler(FileNotFoundException ex) {
		log.error("FileNotFoundException [{}]", ex.getMessage(), ex);
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String code = WebCodeEnum.FILE_NOT_FOUND.value();
		String msg = this.i18nConfig.getMessage(code);
		String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.FILE_NOT_FOUND.desc();
		ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
		return ResponseEntity.status(status).body(entity);
	}

	/**
	 * 类型转换异常
	 *
	 * @param ex 异常
	 * @return ResponseEntity<ResultVO>
	 */
	@ExceptionHandler(ClassCastException.class)
	public ResponseEntity<ResultVO> exceptionHandler(ClassCastException ex) {
		log.error("ClassCastException [{}]", ex.getMessage(), ex);
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String code = WebCodeEnum.TYPE_CONVERSION_ERROR.value();
		String msg = this.i18nConfig.getMessage(code);
		String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.TYPE_CONVERSION_ERROR.desc();
		ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
		return ResponseEntity.status(status).body(entity);
	}

	/**
	 * 数字格式异常
	 *
	 * @param ex 异常
	 * @return ResponseEntity<ResultVO>
	 */
	@ExceptionHandler(NumberFormatException.class)
	public ResponseEntity<ResultVO> exceptionHandler(NumberFormatException ex) {
		log.error("NumberFormatException [{}]", ex.getMessage(), ex);
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String code = WebCodeEnum.NUMBER_FORMAT_FOUND.value();
		String msg = this.i18nConfig.getMessage(code);
		String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.NUMBER_FORMAT_FOUND.desc();
		ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
		return ResponseEntity.status(status).body(entity);
	}

	/**
	 * 安全异常
	 *
	 * @param ex 异常
	 * @return ResponseEntity<ResultVO>
	 */
	@ExceptionHandler(SecurityException.class)
	public ResponseEntity<ResultVO> exceptionHandler(SecurityException ex) {
		log.error("SecurityException [{}]", ex.getMessage(), ex);
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String code = WebCodeEnum.SECURITY_ERROR.value();
		String msg = this.i18nConfig.getMessage(code);
		String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.SECURITY_ERROR.desc();
		ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
		return ResponseEntity.status(status).body(entity);
	}

	/**
	 * sql语法错误异常异常
	 *
	 * @param ex 异常
	 * @return ResponseEntity<ResultVO>
	 */
	@ExceptionHandler(BadSqlGrammarException.class)
	public ResponseEntity<ResultVO> exceptionHandler(BadSqlGrammarException ex) {
		log.error("BadSqlGrammarException [{}]", ex.getMessage(), ex);
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String code = WebCodeEnum.BAD_SQL_GRAMMAR.value();
		String msg = this.i18nConfig.getMessage(code);
		String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.BAD_SQL_GRAMMAR.desc();
		ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
		return ResponseEntity.status(status).body(entity);
	}

	/**
	 * sql异常
	 *
	 * @param ex 异常
	 * @return ResponseEntity<ResultVO>
	 */
	@ExceptionHandler(SQLException.class)
	public ResponseEntity<ResultVO> exceptionHandler(SQLException ex) {
		log.error("SQLException [{}]", ex.getMessage(), ex);
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String code = WebCodeEnum.SQL_ERROR.value();
		String msg = this.i18nConfig.getMessage(code);
		String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.SQL_ERROR.desc();
		ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
		return ResponseEntity.status(status).body(entity);
	}

	/**
	 * 类型不存在异常
	 *
	 * @param ex 异常
	 * @return ResponseEntity<ResultVO>
	 */
	@ExceptionHandler(TypeNotPresentException.class)
	public ResponseEntity<ResultVO> exceptionHandler(TypeNotPresentException ex) {
		log.error("TypeNotPresentException [{}]", ex.getMessage(), ex);
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String code = WebCodeEnum.TYPE_NOT_PRESENT.value();
		String msg = this.i18nConfig.getMessage(code);
		String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.TYPE_NOT_PRESENT.desc();
		ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
		return ResponseEntity.status(status).body(entity);
	}

	/**
	 * IO异常
	 *
	 * @param ex 异常
	 * @return ResponseEntity<ResultVO>
	 */
	@ExceptionHandler(IOException.class)
	public ResponseEntity<ResultVO> exceptionHandler(IOException ex) {
		log.error("IOException [{}]", ex.getMessage(), ex);
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String code = WebCodeEnum.IO_ERROR.value();
		String msg = this.i18nConfig.getMessage(code);
		String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.IO_ERROR.desc();
		ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
		return ResponseEntity.status(status).body(entity);
	}

	/**
	 * 未知方法异常
	 *
	 * @param ex 异常
	 * @return ResponseEntity<ResultVO>
	 */
	@ExceptionHandler(NoSuchMethodException.class)
	public ResponseEntity<ResultVO> exceptionHandler(NoSuchMethodException ex) {
		log.error("NoSuchMethodException [{}]", ex.getMessage(), ex);
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String code = WebCodeEnum.NO_SUCH_METHOD.value();
		String msg = this.i18nConfig.getMessage(code);
		String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.NO_SUCH_METHOD.desc();
		ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
		return ResponseEntity.status(status).body(entity);
	}

	/**
	 * 数组越界异常
	 *
	 * @param ex 异常
	 * @return ResponseEntity<ResultVO>
	 */
	@ExceptionHandler(IndexOutOfBoundsException.class)
	public ResponseEntity<ResultVO> exceptionHandler(IndexOutOfBoundsException ex) {
		log.error("IndexOutOfBoundsException [{}]", ex.getMessage(), ex);
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String code = WebCodeEnum.INDEX_OUT_OF_BOUNDS.value();
		String msg = this.i18nConfig.getMessage(code);
		String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.INDEX_OUT_OF_BOUNDS.desc();
		ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
		return ResponseEntity.status(status).body(entity);
	}

	/**
	 * 无法注入bean异常
	 *
	 * @param ex 异常
	 * @return ResponseEntity<ResultVO>
	 */
	@ExceptionHandler(NoSuchBeanDefinitionException.class)
	public ResponseEntity<ResultVO> exceptionHandler(NoSuchBeanDefinitionException ex) {
		log.error("NoSuchBeanDefinitionException [{}]", ex.getMessage(), ex);
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String code = WebCodeEnum.NO_SUCH_BEAN.value();
		String msg = this.i18nConfig.getMessage(code);
		String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.NO_SUCH_BEAN.desc();
		ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
		return ResponseEntity.status(status).body(entity);
	}

	/**
	 * 类型不匹配异常
	 *
	 * @param ex 异常
	 * @return ResponseEntity<ResultVO>
	 */
	@ExceptionHandler(TypeMismatchException.class)
	public ResponseEntity<ResultVO> exceptionHandler(TypeMismatchException ex) {
		log.error("TypeMismatchException [{}]", ex.getMessage(), ex);
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String code = WebCodeEnum.TYPE_MISMATCH.value();
		String msg = this.i18nConfig.getMessage(code);
		String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.TYPE_MISMATCH.desc();
		ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
		return ResponseEntity.status(status).body(entity);
	}

	/**
	 * 栈溢出异常
	 *
	 * @param ex 异常
	 * @return ResponseEntity<ResultVO>
	 */
	@ExceptionHandler(StackOverflowError.class)
	public ResponseEntity<ResultVO> exceptionHandler(StackOverflowError ex) {
		log.error("StackOverflowError [{}]", ex.getMessage(), ex);
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String code = WebCodeEnum.STACK_OVERFLOW.value();
		String msg = this.i18nConfig.getMessage(code);
		String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.STACK_OVERFLOW.desc();
		ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
		return ResponseEntity.status(status).body(entity);
	}

	/**
	 * 除数不能为0异常
	 *
	 * @param ex 异常
	 * @return ResponseEntity<ResultVO>
	 */
	@ExceptionHandler(ArithmeticException.class)
	public ResponseEntity<ResultVO> exceptionHandler(ArithmeticException ex) {
		log.error("ArithmeticException [{}]", ex.getMessage(), ex);
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String code = WebCodeEnum.ARITHMETIC_ERROR.value();
		String msg = this.i18nConfig.getMessage(code);
		String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.ARITHMETIC_ERROR.desc();
		ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
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
		log.error("BaseException [{}]", ex.getMessage(), ex);
		HttpStatus status = null != ex.getStatus() ? ex.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR;
		String code = ex.getCode();
		String msg = this.i18nConfig.getMessage(code);
		String message = StringUtils.isNotBlank(msg) ? msg : ex.getMessage();
		Object data = ex.getData();
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
		log.error("UnauthorizedException [{}]", ex.getMessage(), ex);
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
		log.error("BaseShiroAuthcException [{}]", ex.getMessage(), ex);
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
		log.error("Exception [{}]", ex.getMessage(), ex);
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		String code = WebCodeEnum.FAILED.value();
		String msg = this.i18nConfig.getMessage(code);
		String message = StringUtils.isNotBlank(msg) ? msg : WebCodeEnum.FAILED.desc();
		ResultVO entity = ResultVO.builder().status(status).code(code).message(message).build();
		return ResponseEntity.status(status).body(entity);
	}
}
