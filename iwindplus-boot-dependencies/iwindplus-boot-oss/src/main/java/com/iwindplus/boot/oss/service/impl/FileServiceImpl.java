/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.oss.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.iwindplus.boot.oss.domain.dto.DownloadDTO;
import com.iwindplus.boot.oss.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件操作业务层接口实现类.
 *
 * @author zengdegui
 * @since 2019年8月14日
 */
@Slf4j
public class FileServiceImpl extends AbstractOssServiceImpl implements FileService {
    private static final long BSIZE = 4096;
    @Autowired
    private MultipartProperties multipartProperties;

    @Override
    public void downloadFile(DownloadDTO entity, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Path path = null;
        if (Boolean.TRUE.equals(entity.getFlagRemote())) {
            path = Paths.get(this.multipartProperties.getLocation(), entity.getRelativePath());
            this.downloadFile(path.toFile(), entity.getFileName(), request, response);
        } else {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(entity.getRelativePath());
            if (resources.length == 1) {
                InputStream inputStream = resources[0].getInputStream();
                String suffix = entity.getRelativePath().substring(entity.getRelativePath().lastIndexOf("."));
                String rootPath;
                if (StringUtils.isNotBlank(this.multipartProperties.getLocation())) {
                    rootPath = this.multipartProperties.getLocation();
                } else {
                    rootPath = System.getProperty(USER_DIR);
                }
                path = Paths.get(rootPath, TMP);
                if (!Files.exists(path)) {
                    Files.createDirectories(path);
                }
                Path filePath = Files.createTempFile(path, IdUtil.fastSimpleUUID(), suffix);
                FileUtil.writeFromStream(inputStream, filePath.toString());
                this.downloadFile(filePath.toFile(), entity.getFileName(), request, response);
                // 删除缓存文件
                Files.delete(filePath);
            }
        }
    }

    private void downloadFile(File file, String fileName, HttpServletRequest request, HttpServletResponse response) {
        RandomAccessFile in = null;
        BufferedOutputStream bos = null;
        try {
            ServletContext context = request.getServletContext();
            String mimeType = context.getMimeType(file.getAbsolutePath());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }
            response.setContentType(mimeType);
            if (StringUtils.isBlank(fileName)) {
                fileName = file.getName();
            } else {
                String suffixName = file.getName().substring(file.getName().lastIndexOf("."));
                fileName = new StringBuilder().append(fileName).append(suffixName).toString();
            }
            // 针对IE或者以IE为内核的浏览器
            String userAgent = request.getHeader("user-agent").toLowerCase(LocaleContextHolder.getLocale());
            if (userAgent.toLowerCase(LocaleContextHolder.getLocale()).contains("msie") || StringUtils.contains(
                    userAgent, "trident") || userAgent.toLowerCase(LocaleContextHolder.getLocale())
                    .contains("like gecko") || userAgent.toLowerCase(LocaleContextHolder.getLocale())
                    .contains("mozilla")) {
                fileName = new String(fileName.getBytes(Charset.defaultCharset()), "ISO8859-1");
            } else {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            }

            String headerValue = String.format(LocaleContextHolder.getLocale(), "attachment; filename=\"%s\"", fileName);
            response.setHeader("Content-Disposition", headerValue);
            // 解析断点续传相关信息
            response.setHeader("Accept-Ranges", "bytes");
            long downloadSize = file.length();
            long fromPos = 0;
            long toPos = 0;
            if (request.getHeader("Range") == null) {
                response.setHeader("Content-Length", downloadSize + "");
            } else {
                // 若客户端传来Range，说明之前下载了一部分，设置206状态(SC_PARTIAL_CONTENT)
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                String range = request.getHeader("Range");
                String bytes = range.replaceAll("bytes=", "");
                String[] ary = bytes.split("-");
                fromPos = Long.parseLong(ary[0]);
                if (ary.length == 2) {
                    toPos = Long.parseLong(ary[1]);
                }
                int size;
                if (toPos > fromPos) {
                    size = (int) (toPos - fromPos);
                } else {
                    size = (int) (downloadSize - fromPos);
                }
                response.setHeader("Content-Length", size + "");
                downloadSize = size;
            }

            in = new RandomAccessFile(file, "rw");
            bos = new BufferedOutputStream(response.getOutputStream());
            // 设置下载起始位置
            if (fromPos > 0) {
                in.seek(fromPos);
            }
            // 缓冲区大小
            int bufLen = (int) (downloadSize < BSIZE ? downloadSize : BSIZE);
            byte[] buffer = new byte[bufLen];
            int num;
            int count = 0; // 当前写到客户端的大小
            while ((num = in.read(buffer)) != -1) {
                bos.write(buffer, 0, num);
                count += num;
                // 处理最后一段，计算不满缓冲区的大小
                if (downloadSize - count < bufLen) {
                    bufLen = (int) (downloadSize - count);
                    if (bufLen == 0) {
                        break;
                    }
                    buffer = new byte[bufLen];
                }
            }
            response.flushBuffer();
        } catch (IOException ex) {
            log.error("Data is suspended or interrupted [{}]", ex);
        } finally {
            if (null != bos) {
                try {
                    bos.close();
                } catch (IOException ex) {
                    log.error("Closing output stream exception [{}]", ex);
                }
            }
            if (null != in) {
                try {
                    in.close();
                } catch (IOException ex) {
                    log.error("Closing output stream exception [{}]", ex);
                }
            }
        }
    }
}
