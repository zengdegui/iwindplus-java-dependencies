/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.oss.service;

import com.iwindplus.boot.oss.domain.vo.UploadVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 对象存储业务层接口类.
 *
 * @author zengdegui
 * @since 2019/8/14
 */
public interface OssService {
    /**
     * 文件批量上传.
     *
     * @param entities 对象
     * @return List<UploadVO>
     * @throws Exception
     */
    List<UploadVO> uploadBatchFile(List<MultipartFile> entities) throws Exception;
}
