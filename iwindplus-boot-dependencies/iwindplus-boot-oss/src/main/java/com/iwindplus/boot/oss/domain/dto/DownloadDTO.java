/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.oss.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 下载文件数据传输对象.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class DownloadDTO implements Serializable {
    private static final long serialVersionUID = -7300052045738862546L;

    /**
     * 是否远程获取，默认：false，从src/main/resources下获取.
     */
    @Builder.Default
    private Boolean flagRemote = false;

    /**
     * 相对路径（必填）.
     */
    @NotBlank(message = "{relativePath.notBlank}")
    private String relativePath;

    /**
     * 下载文件名（可选）.
     */
    private String fileName;

    public void setFileName(String fileName) {
        try {
            this.fileName = URLDecoder.decode(URLDecoder.decode(fileName, "UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("Unsupported encoding exception [{}]", e);
        }
    }
}
