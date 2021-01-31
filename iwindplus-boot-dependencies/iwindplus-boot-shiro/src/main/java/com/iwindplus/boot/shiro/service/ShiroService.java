/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.shiro.service;

import com.iwindplus.boot.shiro.domain.vo.AccessPermsVO;
import com.iwindplus.boot.shiro.domain.vo.LoginVO;
import com.iwindplus.boot.web.exception.BaseShiroAuthcException;

import java.util.List;

/**
 * shiro业务层接口.
 *
 * @author zengdegui
 * @since 2019/4/17
 */
public interface ShiroService {
    /**
     * 通过用户名查找.
     *
     * @param userName 用户名
     * @return LoginVO
     * @throws BaseShiroAuthcException
     */
    LoginVO getByUserName(String userName) throws BaseShiroAuthcException;

    /**
     * 查询用户操作权限.
     *
     * @param userName 用户名
     * @return List<String>
     * @throws BaseShiroAuthcException
     */
    List<String> listOperatePermissionByUserName(String userName) throws BaseShiroAuthcException;

    /**
     * 查找访问权限.
     *
     * @return List<AccessPermsVO>
     */
    List<AccessPermsVO> listAccessPerms();
}
