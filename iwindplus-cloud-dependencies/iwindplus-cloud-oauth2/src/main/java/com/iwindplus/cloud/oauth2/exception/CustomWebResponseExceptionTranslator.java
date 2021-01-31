/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.cloud.oauth2.exception;

import com.iwindplus.boot.web.domain.vo.ResultVO;
import com.iwindplus.cloud.web.exception.CloudGlobalExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

/**
 * 异常转换器.
 *
 * @author zengdegui
 * @since 2019/6/4
 */
@SuppressWarnings("rawtypes")
public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator {
    @Autowired
    private CloudGlobalExceptionHandler globalExceptionHandler;

    @Override
    public ResponseEntity translate(Exception ex) throws Exception {
        ResponseEntity<ResultVO> result = this.globalExceptionHandler.exception(ex);
        return result;
    }
}
