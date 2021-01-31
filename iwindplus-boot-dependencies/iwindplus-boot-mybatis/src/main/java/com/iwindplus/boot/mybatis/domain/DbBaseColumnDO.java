/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.mybatis.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 关系型数据库基础通用列实体类.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class DbBaseColumnDO<T extends Model<T>> extends Model<T> {
    private static final long serialVersionUID = 6082828120669040748L;

    /**
     * 主机，IP（添加时自动维护）.
     */
    @TableField(fill = FieldFill.INSERT)
    protected String host;

    /**
     * 创建时间（添加时自动维护）.
     */
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime gmtCreate;

    /**
     * 创建人（添加时自动维护）.
     */
    @TableField(fill = FieldFill.INSERT)
    protected String creater;

    /**
     * 更新时间（添加，编辑时自动维护）.
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime gmtModified;

    /**
     * 更新人（编辑时自动维护）.
     */
    @TableField(fill = FieldFill.UPDATE)
    protected String modifier;

    /**
     * 是否删除（0：未删除，1：已删除）（添加时自动维护）.
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    protected Boolean flagDelete;

    /**
     * 乐观锁（添加时自动维护）.
     */
    @TableField(fill = FieldFill.INSERT)
    @Version
    protected Integer version;
}
