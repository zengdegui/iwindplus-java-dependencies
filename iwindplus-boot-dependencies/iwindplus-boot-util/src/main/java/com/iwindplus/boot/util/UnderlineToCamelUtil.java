/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 驼峰下划线互转工具类.
 *
 * @author zengdegui
 * @since 2018年9月1日
 */
public class UnderlineToCamelUtil {
    private static Pattern underlinePattern = Pattern.compile("_(\\w)");

    private static Pattern camelPattern = Pattern.compile("[A-Z]");

    /**
     * 下划线转驼峰
     *
     * @param str 字符串
     * @return String
     */
    public static String underlineToCamel(String str) {
        str = str.toLowerCase();
        Matcher matcher = underlinePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 驼峰转下划线
     *
     * @param str 字符串
     * @return humpToLine2
     */
    public static String camelToUnderline(String str) {
        Matcher matcher = camelPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
