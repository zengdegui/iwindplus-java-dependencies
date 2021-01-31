/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.util;

import cn.hutool.core.date.DatePattern;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * 日期处理工具类.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
public class DateUtil extends cn.hutool.core.date.DateUtil {
    /**
     * 获取当前时间字符串日期格式：yyyy-MM-dd HH:mm:ss.
     *
     * @return String
     */
    public static String getStringDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN));
    }

    /**
     * 获取当前时间字符串自定义格式.
     *
     * @param pattern 格式
     * @return String
     */
    public static String getStringDate(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 根据格式获取字符串日期或时间.
     *
     * @param date    日期
     * @param pattern 格式
     * @return String
     */
    public static String getStringDate(LocalDateTime date, String pattern) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        return df.format(date);
    }

    /**
     * 将字符串日期转为LocalDateTime.
     *
     * @param stringDate 字符串日期
     * @param pattern    格式
     * @return LocalDateTime
     */
    public static LocalDateTime parseDate(String stringDate, String pattern) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(stringDate, df);
    }

    /**
     * 获取两个时间的差值秒.
     *
     * @param d1 日期
     * @param d2 日期
     * @return Integer
     */
    public static Integer getSecondBetweenDate(LocalDateTime d1, LocalDateTime d2) {
        Duration duration = Duration.between(d1, d2);
        Long second = duration.toMillis() / 1000;
        return second.intValue();
    }

    /**
     * 获得当天0点时间.
     *
     * @return LocalDateTime
     */
    public static LocalDateTime getTimesMorning() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
    }

    /**
     * 获得当天结束时间.
     *
     * @return LocalDateTime
     */
    public static LocalDateTime getTimesNight() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    }

    /**
     * Date转换LocalDateTime.
     *
     * @param date 日期
     * @return LocalDateTime
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * LocalDateTime转换Date.
     *
     * @param localDateTime LocalDateTime
     * @return Date
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    /**
     * 判断两个时间间隔时长是否符合.
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @param interval  相差时长 单位：秒
     * @return boolean
     */
    public static boolean isContinuous(LocalTime beginTime, LocalTime endTime, int interval) {
        return endTime.until(beginTime, ChronoUnit.SECONDS) <= +Math.abs(interval)
                && endTime.until(beginTime, ChronoUnit.SECONDS) >= -Math.abs(interval);
    }

    /**
     * 毫秒转LocalDateTime.
     *
     * @param milliSecond 毫秒
     * @return LocalDateTime
     */
    public static LocalDateTime milliSecondToLocalDateTime(Long milliSecond) {
        LocalDateTime data = LocalDateTime.ofEpochSecond(milliSecond / 1000, 0, ZoneOffset.ofHours(8));
        return data;
    }

    /**
     * 获取当前时间戳（yyyyMMddHHmmss.
     *
     * @return String
     */
    public static String getCurrentTimestampStr() {
        return getStringDate(DatePattern.PURE_DATETIME_PATTERN);
    }
}
