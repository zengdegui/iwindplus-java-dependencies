/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */
package com.iwindplus.boot.web.domain.enumerate;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * 业务编码返回值枚举.
 *
 * @author zengdegui
 * @since 2020年6月13日
 */
@Getter
@Accessors(fluent = true)
public enum WebCodeEnum {
	FAILED("failed", "失败"),
	SUCCESS("success", "成功"),
	METHOD_NOT_ALLOWED("method_not_allowed", "请求方式不正确"),
	PARAM_MISS("param_miss", "参数缺失"),
	PARAM_REQUIRED("param_required", "参数必填"),
	PARAM_TYPE_ERROR("param_type_error", "参数类型不正确"),
	PARAM_ERROR("param_error", "参数不合法"),
	BAD_REQUEST("bad_request", "错误请求"),
	NULL_POINTER("null_pointer", "空指针异常"),
	NOT_FOUND("not_found", "请检查请求路径或者类型是否正确"),
	UNAUTHORIZED("unauthorized", "无权限"),
	CONVERSION_NOT_SUPPORTED("conversion_not_supported", "转换不支持"),
	TYPE_CONVERSION_ERROR("type_conversion_error", "类型转换错误"),
	UNSUPPORTED_MEDIA_TYPE("unsuppored_media_type", "不支持的媒体类型"),
	NOT_ACCEPTABLE("not_acceptable", "不可接受"),
	NOT_WRITABLE("not_writable", "不可写"),
	NOT_READ("not_read", "不可读"),
	ILLEGAL_REQUEST("illegal_request", "非法请求，可能属于伪造的请求"),
	DATA_NOT_EXIST("data_not_exist", "数据不存在"),
	SERVER_BUSY("server_busy", "服务器正忙，请稍后再试!"),
	FILE_NOT_FOUND("file_not_found", "文件找不到"),
	NUMBER_FORMAT_FOUND("number_format_error", "数字格式错误"),
	SECURITY_ERROR("security_error", "安全错误"),
	BAD_SQL_GRAMMAR("bad_sql_grammar", "sql语法错误"),
	SQL_ERROR("sql_error", "sql错误"),
	TYPE_NOT_PRESENT("type_not_present", "类型不存在"),
	IO_ERROR("type_not_present", "IO错误"),
	NO_SUCH_METHOD("no_such_method", "未知方法"),
	INDEX_OUT_OF_BOUNDS("index_out_of_bounds", "sql错误"),
	NO_SUCH_BEAN("no_such_bean", "无法注入bean错误"),
	TYPE_MISMATCH("type_mismatch", "类型不匹配"),
	STACK_OVERFLOW("stack_overflow", "栈溢出"),
	ARITHMETIC_ERROR("arithmetic_error", "除数不能为0"),
	;

	/**
	 * 值.
	 */
	private final String value;

	/**
	 * 描述.
	 */
	private final String desc;

	private WebCodeEnum(final String value, final String desc) {
		this.value = value;
		this.desc = desc;
	}

	/**
	 * 通过描述查找枚举.
	 *
	 * @param desc 描述
	 * @return WebCodeEnum
	 */
	public static WebCodeEnum valueOfDesc(String desc) {
		for (WebCodeEnum val : WebCodeEnum.values()) {
			if (Objects.equals(desc, val.desc())) {
				return val;
			}
		}
		return null;
	}

	/**
	 * 通过值查找枚举.
	 *
	 * @param value 值
	 * @return WebCodeEnum
	 */
	public static WebCodeEnum valueOfValue(String value) {
		for (WebCodeEnum val : WebCodeEnum.values()) {
			if (Objects.equals(value, val.value())) {
				return val;
			}
		}
		return null;
	}
}
