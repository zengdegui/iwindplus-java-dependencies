/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.cloud.feign.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 结果视图对象.
 *
 * @author zengdegui
 * @since 2020年10月31日
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeignErrorVO implements Serializable {
    private static final long serialVersionUID = 7869786563361406291L;
    /**
     * 错误编码.
     */
    public static final String ERROR_CODE = "error";
    /**
     * 错误信息.
     */
    public static final String ERROR_MSG = "error_description";

    /**
     * 状态码.
     */
    @JsonIgnore
    @Expose
    private HttpStatus status;

    /**
     * 错误编码.
     */
    @JsonProperty(ERROR_CODE)
    @SerializedName(ERROR_CODE)
    private String code;

    /**
     * 错误信息.
     */
    @JsonProperty(ERROR_MSG)
    @SerializedName(ERROR_MSG)
    private String message;

    /**
     * 数据.
     */
    private Object data;

    private static final GsonBuilder INSTANCE = new GsonBuilder();

    public static FeignErrorVO fromJson(String json) {
        final FeignErrorVO result = INSTANCE.create().fromJson(json, FeignErrorVO.class);
        if (StringUtils.isNotBlank(result.getCode())) {
            return result;
        }
        return result;
    }
}
