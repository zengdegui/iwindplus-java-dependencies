/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.cloud.feign.exception;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 自定义运行时异常.

 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Getter
public class FeignErrorException extends HystrixBadRequestException {
    private static final long serialVersionUID = -6783551049743441442L;

    /**
     * 状态码.
     */
    private HttpStatus status;

    /**
     * 错误编码.
     */
    private String code;

    /**
     * 数据.
     */
    private Object data;

    public FeignErrorException(String code, String message) {
        super(message);
        this.code = code;
    }

    public FeignErrorException(HttpStatus status, String code, String message) {
        this(code, message);
        this.status = status;
    }

    public FeignErrorException(HttpStatus status, String code, String message, Object data) {
        this(status, code, message);
        this.data = data;
    }

    public FeignErrorException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
