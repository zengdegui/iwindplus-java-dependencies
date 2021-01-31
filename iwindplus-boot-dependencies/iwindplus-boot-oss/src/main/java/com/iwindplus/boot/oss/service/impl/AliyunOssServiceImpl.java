/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.oss.service.impl;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.google.common.collect.Lists;
import com.iwindplus.boot.oss.domain.AliyunOssProperty;
import com.iwindplus.boot.oss.domain.enumerate.OssCodeEnum;
import com.iwindplus.boot.oss.domain.vo.UploadVO;
import com.iwindplus.boot.oss.service.AliyunOssService;
import com.iwindplus.boot.web.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * 阿里云对象存储业务层接口实现类.
 *
 * @author zengdegui
 * @since 2019/8/9
 */
@Slf4j
public class AliyunOssServiceImpl extends AbstractOssServiceImpl implements AliyunOssService {
    @Autowired
    private AliyunOssProperty alibabaOssProperty;

    @Override
    public List<UploadVO> uploadBatchFile(List<MultipartFile> entities) throws Exception {
        if (CollectionUtils.isEmpty(entities)) {
            throw new BaseException(OssCodeEnum.FILE_NOT_FOUND.value(), OssCodeEnum.FILE_NOT_FOUND.desc());
        }
        List<UploadVO> list = Lists.newArrayList();
        entities.stream().forEach(entity -> {
            long fileSize = entity.getSize();
            super.checkFile(fileSize, this.alibabaOssProperty.getMaxFileSize());
            String sourceFileName = entity.getOriginalFilename();
            InputStream inputStream = null;
            OSS ossClient = null;
            try {
                inputStream = entity.getInputStream();
                ossClient = getOssClient();
                String fileName = super.getFileName(sourceFileName);
                getResponse(list, fileSize, sourceFileName, inputStream, ossClient, fileName);
            } catch (IOException e) {
                log.error("IOException [{}]", e);
                throw new BaseException(OssCodeEnum.FILE_UPLOAD_FAILED.value(), OssCodeEnum.FILE_UPLOAD_FAILED.desc());
            } finally {
                if (null != ossClient) {
                    ossClient.shutdown();
                }
                closeInputStream(inputStream);
            }
        });
        return list;
    }

    private void closeInputStream(InputStream inputStream) {
        if (null != inputStream) {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error("IOException [{}]", e);
            }
        }
    }

    private OSS getOssClient() {
        OSS ossClient = null;
        if (null != this.alibabaOssProperty.getFlagCustom() && Boolean.TRUE.equals(
                this.alibabaOssProperty.getFlagCustom())) {
            ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
            // 创建ClientConfiguration实例，按照您的需要修改默认参数。
            conf.setSupportCname(true);
            // 开启支持CNAME。CNAME是指将自定义域名绑定到存储空间上。
            ossClient = new OSSClientBuilder().build(this.alibabaOssProperty.getEndpoint(),
                    this.alibabaOssProperty.getAccessKey(), this.alibabaOssProperty.getSecretKey(), conf);
        } else {
            ossClient = new OSSClientBuilder().build(this.alibabaOssProperty.getEndpoint(),
                    this.alibabaOssProperty.getAccessKey(), this.alibabaOssProperty.getSecretKey());
        }
        return ossClient;
    }

    private void getResponse(List<UploadVO> list, long fileSize, String sourceFileName, InputStream inputStream,
            OSS ossClient, String fileName) {
        // 创建上传Object的Metadata
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(fileSize);
        PutObjectResult response = ossClient.putObject(this.alibabaOssProperty.getBucket(), fileName, inputStream,
                meta);
        if (null != response) {
            Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000);
            URL url = ossClient.generatePresignedUrl(this.alibabaOssProperty.getBucket(), fileName, expiration);
            String absolutePath = url.toString();
            UploadVO data = UploadVO.builder()
                    .sourceFileName(sourceFileName)
                    .fileName(fileName)
                    .fileSize(fileSize)
                    .absolutePath(absolutePath)
                    .relativePath(fileName)
                    .build();
            list.add(data);
        }
    }

    @Override
    public void download(String fileName, HttpServletRequest request, HttpServletResponse response) {
        // TODO Auto-generated method stub

    }
}
