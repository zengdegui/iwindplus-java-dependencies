/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.oss.service.impl;

import com.google.common.collect.Lists;
import com.iwindplus.boot.oss.domain.QiniuOssProperty;
import com.iwindplus.boot.oss.domain.enumerate.OssCodeEnum;
import com.iwindplus.boot.oss.domain.vo.QiniuResultVO;
import com.iwindplus.boot.oss.domain.vo.UploadVO;
import com.iwindplus.boot.oss.service.QiniuOssService;
import com.iwindplus.boot.web.exception.BaseException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 七牛云对象存储操作业务层接口实现类.
 *
 * @author zengdegui
 * @since 2019年8月9日
 */
@Slf4j
public class QiniuOssServiceImpl extends AbstractOssServiceImpl implements QiniuOssService {
    @Autowired
    private QiniuOssProperty qiniuOssProperty;

    @Autowired
    private MultipartProperties multipartProperties;

    @Override
    public List<UploadVO> uploadBatchFile(List<MultipartFile> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            throw new BaseException(OssCodeEnum.FILE_NOT_FOUND.value(), OssCodeEnum.FILE_NOT_FOUND.desc());
        }
        List<UploadVO> list = Lists.newArrayList();
        entities.stream().forEach(entity -> {
            long fileSize = entity.getSize();
            super.checkFile(fileSize, this.qiniuOssProperty.getMaxFileSize());
            String sourceFileName = entity.getOriginalFilename();
            try {
                InputStream inputStream = entity.getInputStream();
                String upToken = this.getUpToken();
                String fileName = super.getFileName(sourceFileName);
                UploadManager uploadManager = this.getUploadManager();
                if (null != uploadManager) {
                    Response response = uploadManager.put(inputStream, fileName, upToken, null, null);
                    if (response.isOK()) {
                        QiniuResultVO qiniuResultVO = response.jsonToObject(QiniuResultVO.class);
                        String absolutePath = new StringBuilder(this.qiniuOssProperty.getAccessDomain()).append("/")
                                .append(qiniuResultVO.getFileName()).toString();
                        UploadVO data = UploadVO.builder().sourceFileName(sourceFileName).fileName(fileName).fileSize(fileSize)
                                .absolutePath(absolutePath).relativePath(qiniuResultVO.getFileName()).build();
                        list.add(data);
                    }
                    response.close();
                }
                inputStream.close();
            } catch (Exception e) {
                log.error("Exception [{}]", e);
                throw new BaseException(OssCodeEnum.FILE_UPLOAD_FAILED.value(),
                        OssCodeEnum.FILE_UPLOAD_FAILED.desc());
            }
        });
        return list;
    }

    /**
     * 获取上传管理器.
     *
     * @return UploadManager
     * @throws Exception
     */
    private UploadManager getUploadManager() throws Exception {
        Configuration cfg = null;
        if (null == this.qiniuOssProperty.getZone()) {
            cfg = new Configuration(Region.autoRegion());
        } else {
            switch (this.qiniuOssProperty.getZone()) {
                case EAST_CHINA: {
                    cfg = new Configuration(Region.huadong());
                    break;
                }
                case NORTH_CHINA: {
                    cfg = new Configuration(Region.huabei());
                    break;
                }
                case SOUTH_CHINA: {
                    cfg = new Configuration(Region.huanan());
                    break;
                }
                case NORTH_AMERICA: {
                    cfg = new Configuration(Region.beimei());
                    break;
                }
                case SOUTHEAST_ASIA: {
                    cfg = new Configuration(Region.xinjiapo());
                    break;
                }
                default: {
                    cfg = new Configuration(Region.autoRegion());
                    break;
                }
            }
        }
        UploadManager uploadManager = null;
        if (null != this.qiniuOssProperty.getBreakpointEnabled()
                && Boolean.TRUE.equals(this.qiniuOssProperty.getBreakpointEnabled())) {
            String rootPath;
            if (StringUtils.isNotBlank(this.multipartProperties.getLocation())) {
                rootPath = this.multipartProperties.getLocation();
            } else {
                rootPath = System.getProperty(USER_DIR);
            }
            Path path = Paths.get(rootPath, TMP);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            // 设置断点续传文件进度保存目录
            FileRecorder fileRecorder = new FileRecorder(path.toString());
            uploadManager = new UploadManager(cfg, fileRecorder);
        } else {
            uploadManager = new UploadManager(cfg);
        }
        return uploadManager;
    }

    /**
     * 获取签名.
     *
     * @return String
     */
    private String getUpToken() {
        Auth auth = Auth.create(this.qiniuOssProperty.getAccessKey(), this.qiniuOssProperty.getSecretKey());
        StringMap putPolicy = new StringMap();
        putPolicy.put("callbackBodyType", "application/json");
        putPolicy.put("returnBody",
                "{\"fileName\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fileSize\":$(fsize)}");
        long expireSeconds = 3600;
        return auth.uploadToken(this.qiniuOssProperty.getBucket(), null, expireSeconds, putPolicy);
    }
}
