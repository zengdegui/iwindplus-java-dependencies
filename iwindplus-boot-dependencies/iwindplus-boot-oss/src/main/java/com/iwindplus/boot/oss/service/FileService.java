/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.oss.service;

import com.iwindplus.boot.oss.domain.dto.DownloadDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 文件操作业务层接口类.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
public interface FileService extends OssService {
    /**
     * 文件下载.
     *
     * @param entity   对象
     * @param request  请求
     * @param response 响应
     * @throws Exception
     */
    void downloadFile(DownloadDTO entity, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
