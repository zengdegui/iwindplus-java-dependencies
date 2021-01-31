/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.oss.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 阿里云对象存储业务层接口类.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
public interface AliyunOssService extends OssService {
    /**
     * 文件下载.
     *
     * @param fileName 下载文件名
     * @param request  请求
     * @param response 响应
     */
    void download(String fileName, HttpServletRequest request, HttpServletResponse response);
}
