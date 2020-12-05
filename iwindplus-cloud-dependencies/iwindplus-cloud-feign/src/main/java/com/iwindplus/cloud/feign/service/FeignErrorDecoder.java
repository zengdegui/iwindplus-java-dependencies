/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.cloud.feign.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwindplus.cloud.feign.domain.enumerate.FeignCodeEnum;
import com.iwindplus.cloud.feign.domain.vo.FeignErrorVO;
import com.iwindplus.cloud.feign.exception.FeignErrorException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * feign统一的错误对象.
 *
 * @author zengdegui
 * @since 2019年11月20日
 */
@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        // response只能获取一次，获取完就被清空了
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String code = FeignCodeEnum.FAILED.value();
        String message = FeignCodeEnum.FAILED.desc();
        Object data = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String resStr = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
            log.error("FeignErrorDecoder [{}]", resStr);
            FeignErrorVO result = objectMapper.readValue(resStr, FeignErrorVO.class);
            if (null != result) {
                status = Optional.ofNullable(result.getStatus()).orElse(HttpStatus.INTERNAL_SERVER_ERROR);
                code = Optional.ofNullable(result.getCode()).orElse(FeignCodeEnum.FAILED.value());
                message = Optional.ofNullable(result.getMessage()).orElse(FeignCodeEnum.FAILED.desc());
                data = result.getData();
                return new FeignErrorException(status, code, message, data);
            } else {
                OAuth2Exception oAuth2Exception = objectMapper.readValue(resStr, OAuth2Exception.class);
                code = Optional.ofNullable(oAuth2Exception.getOAuth2ErrorCode())
                        .orElse(FeignCodeEnum.FAILED.value());
                message = Optional.ofNullable(oAuth2Exception.getMessage()).orElse(FeignCodeEnum.FAILED.desc());
                return new FeignErrorException(status, code, message, data);
            }
        } catch (IOException e) {
            log.error("IO exception [{}]", e);
        }
        return new FeignErrorException(status, code, message, data);
    }
}
