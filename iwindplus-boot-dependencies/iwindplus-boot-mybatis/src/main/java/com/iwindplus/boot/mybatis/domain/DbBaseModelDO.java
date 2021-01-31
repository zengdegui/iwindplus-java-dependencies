/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.mybatis.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.iwindplus.boot.mybatis.domain.validation.EditIntFc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 关系型数据库基础实体类.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class DbBaseModelDO<T extends Model<T>> extends DbBaseColumnDO<T> {
    private static final long serialVersionUID = -407100883907464557L;

    /**
     * 主键.
     */
    @NotBlank(message = "{id.notBlank}", groups = {EditIntFc.class})
    @TableId
    protected String id;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
