/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.shiro.service.manager;

import com.iwindplus.boot.shiro.domain.vo.AccessPermsVO;
import com.iwindplus.boot.shiro.service.ShiroService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.util.CollectionUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * shiro 热加载权限.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
@Slf4j
@Setter
public class ReloadManager {
    private ShiroFilterFactoryBean shiroFilterFactoryBean;
    private ShiroService shiroService;

    /**
     * 重新加载权限.
     */
    public void updatePermission() {
        synchronized (this.shiroFilterFactoryBean) {
            AbstractShiroFilter shiroFilter = null;
            try {
                shiroFilter = (AbstractShiroFilter) this.shiroFilterFactoryBean.getObject();
                if (null != shiroFilter) {
                    reloadPermission(shiroFilter);
                }
            } catch (Exception e) {
                log.error("get ShiroFilter from shiroFilterFactoryBean error! [{}]", e);
            }
        }
    }

    private void reloadPermission(AbstractShiroFilter shiroFilter) {
        PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter
                .getFilterChainResolver();
        DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver
                .getFilterChainManager();
        // 清空老的权限控制.
        manager.getFilterChains().clear();
        this.shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
        // 配置访问权限.
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // 配置访问权限，动态加载权限（从数据库读取然后配置）.
        List<AccessPermsVO> entities = this.shiroService.listAccessPerms();
        if (!CollectionUtils.isEmpty(entities)) {
            entities.stream().forEach(entity -> {
                String url = entity.getUrl();
                String authority = entity.getAuthority();
                filterChainDefinitionMap.put(url, authority);
            });
        }
        this.shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        // 重新构建生成.
        Map<String, String> chains = this.shiroFilterFactoryBean.getFilterChainDefinitionMap();
        // 重新生成过滤链.
        if (MapUtils.isNotEmpty(chains)) {
            chains.entrySet().stream().forEach(chain -> {
                manager.createChain(chain.getKey(), chain.getValue().replace(" ", ""));
            });
        }
        log.info("pdate shiro permission！！");
    }
}
