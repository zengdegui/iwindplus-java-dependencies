/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.web;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.format.Formatter;

import java.math.BigInteger;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * localdate 期相关配置.
 *
 * @author zengdegui
 * @since 2020/11/8
 */
@Slf4j
@Configuration
public class LocalDateFormatConfig {
    /**
     * 创建Jackson2ObjectMapperBuilderCustomizer.
     * 处理期序列化，long类型数据丢失精度等问题
     *
     * @return Jackson2ObjectMapperBuilderCustomizer
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN, LocaleContextHolder.getLocale());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN, LocaleContextHolder.getLocale());
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN, LocaleContextHolder.getLocale());
        Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer = builder -> builder
                .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter))
                .serializerByType(LocalDate.class, new LocalDateSerializer(dateFormatter))
                .serializerByType(LocalTime.class, new LocalTimeSerializer(timeFormatter))
                .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter))
                .deserializerByType(LocalDate.class, new LocalDateDeserializer(dateFormatter))
                .deserializerByType(LocalTime.class, new LocalTimeDeserializer(timeFormatter))
                .serializerByType(Long.class, ToStringSerializer.instance)
                .serializerByType(Long.TYPE, ToStringSerializer.instance)
                .serializerByType(BigInteger.class, ToStringSerializer.instance);
        log.info("Jackson2ObjectMapperBuilderCustomizer [{}]", jackson2ObjectMapperBuilderCustomizer);
        return jackson2ObjectMapperBuilderCustomizer;
    }

    /**
     * LocalDateTime期转换器，用于接收期格式数据
     *
     * @return Converter<String, LocalDateTime>
     */
    @Bean
    public Formatter<LocalDateTime> localDateTimeFormatter() {
        Formatter<LocalDateTime> localDateTimeFormatter = new Formatter<LocalDateTime>() {
            @Override
            public String print(LocalDateTime localDateTime, Locale locale) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN, locale);
                return dateTimeFormatter.format(localDateTime);
            }

            @Override
            public LocalDateTime parse(String text, Locale locale) throws ParseException {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN, locale);
                return LocalDateTime.parse(text, dateTimeFormatter);
            }
        };
        log.info("Formatter<LocalDateTime> [{}]", localDateTimeFormatter);
        return localDateTimeFormatter;
    }
}
