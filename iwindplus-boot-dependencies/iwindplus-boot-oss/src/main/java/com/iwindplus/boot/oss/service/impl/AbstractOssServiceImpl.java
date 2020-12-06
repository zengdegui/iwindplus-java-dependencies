/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.oss.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Lists;
import com.iwindplus.boot.oss.domain.enumerate.OssCodeEnum;
import com.iwindplus.boot.oss.domain.vo.UploadVO;
import com.iwindplus.boot.oss.service.OssService;
import com.iwindplus.boot.util.DateUtil;
import com.iwindplus.boot.web.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 对象存储业务抽象类.
 *
 * @author zengdegui
 * @since 2020年3月13日
 */
@Slf4j
public abstract class AbstractOssServiceImpl implements OssService {
    public static final String UOLOAD_DIR = "upload";
    public static final String USER_DIR = "user.dir";
    public static final String TMP = "tmp";

    @Autowired
    private MultipartProperties multipartProperties;

    @Override
    public List<UploadVO> uploadBatchFile(List<MultipartFile> entities) throws Exception {
        if (CollectionUtils.isEmpty(entities)) {
            throw new BaseException(OssCodeEnum.FILE_NOT_FOUND.value(), OssCodeEnum.FILE_NOT_FOUND.desc());
        }
        // 生成目录
        String rootPath;
        if (StringUtils.isNotBlank(this.multipartProperties.getLocation())) {
            rootPath = this.multipartProperties.getLocation();
        } else {
            rootPath = System.getProperty(USER_DIR);
        }

        Path relativeDir = Paths.get(UOLOAD_DIR, DateUtil.getStringDate(DatePattern.PURE_DATE_PATTERN));
        Path absoluteDir = Paths.get(rootPath).resolve(relativeDir);
        if (!Files.exists(absoluteDir)) {
            try {
                Files.createDirectories(absoluteDir);
            } catch (IOException e) {
                e.printStackTrace();
                throw new BaseException(OssCodeEnum.CREATE_DIR_FAILED.value(),
                        OssCodeEnum.CREATE_DIR_FAILED.desc());
            }
        }
        List<UploadVO> list = Lists.newArrayList();
        entities.stream().forEach(entity -> {
            long fileSize = entity.getSize();
            this.checkFile(fileSize, this.multipartProperties.getMaxFileSize().toBytes());
            String sourceFileName = entity.getOriginalFilename();
            try {
                String fileName = this.getFileName(sourceFileName);
                Path relativePath = relativeDir.resolve(fileName);
                Path absolutePath = absoluteDir.resolve(fileName);
                Files.createFile(absolutePath);
                // 上传
                entity.transferTo(absolutePath.toFile());
                UploadVO data = UploadVO.builder().sourceFileName(sourceFileName).fileName(fileName).fileSize(fileSize)
                        .absolutePath(absolutePath.toString()).relativePath(relativePath.toString()).build();
                list.add(data);
            } catch (IOException e) {
                throw new BaseException(OssCodeEnum.FILE_UPLOAD_FAILED.value(),
                        OssCodeEnum.FILE_UPLOAD_FAILED.desc());
            }
        });
        return list;
    }

    /**
     * 获取新文件名.
     *
     * @param sourceFileName 源文件
     * @return String
     */
    String getFileName(String sourceFileName) {
        String ext = sourceFileName.substring(sourceFileName.lastIndexOf("."));
        return new StringBuilder(IdUtil.fastSimpleUUID()).append(ext).toString();
    }

    /**
     * 校验文件大小.
     *
     * @param fileSize    文件大小
     * @param maxFileSize 最大文件大小
     */
    void checkFile(long fileSize, Long maxFileSize) {
        if (null != maxFileSize) {
            if (fileSize > maxFileSize.longValue()) {
                log.error("The file is too large,the file size [{}]", fileSize);
                throw new BaseException(OssCodeEnum.FILE_TOO_BIG.value(), OssCodeEnum.FILE_TOO_BIG.desc());
            }
        }
    }
}
