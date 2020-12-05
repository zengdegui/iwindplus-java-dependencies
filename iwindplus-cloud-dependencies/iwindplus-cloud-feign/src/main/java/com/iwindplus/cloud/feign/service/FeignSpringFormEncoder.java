/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.cloud.feign.service;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.form.ContentType;
import feign.form.MultipartFormContentProcessor;
import feign.form.spring.SpringFormEncoder;
import feign.form.spring.SpringManyMultipartFilesWriter;
import feign.form.spring.SpringSingleMultipartFileWriter;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

/**
 * 处理多个文件上传.
 *
 * @author zengdegui
 * @since 2020年3月26日
 */
public class FeignSpringFormEncoder extends SpringFormEncoder {
    public FeignSpringFormEncoder(Encoder delegate) {
        super(delegate);
        MultipartFormContentProcessor processor = (MultipartFormContentProcessor) getContentProcessor(
                ContentType.MULTIPART);
        processor.addWriter(new SpringSingleMultipartFileWriter());
        processor.addWriter(new SpringManyMultipartFilesWriter());
    }

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
        if (bodyType != null && bodyType.equals(MultipartFile[].class) && object != null) {
            MultipartFile[] file = (MultipartFile[]) object;
            if (file.length == 0) {
                return;
            }
            Map<String, Object> data = Collections.singletonMap(file[0].getName(), object);
            super.encode(data, MAP_STRING_WILDCARD, template);
            return;
        }
        super.encode(object, bodyType, template);
    }
}
