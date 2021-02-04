/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.shiro;

import com.iwindplus.boot.shiro.domain.ShiroProperty;
import com.iwindplus.boot.shiro.domain.vo.AccessPermsVO;
import com.iwindplus.boot.shiro.filter.JwtAuthcFilter;
import com.iwindplus.boot.shiro.filter.PermsAuthzFilter;
import com.iwindplus.boot.shiro.filter.RolesAuthzFilter;
import com.iwindplus.boot.shiro.service.ShiroService;
import com.iwindplus.boot.shiro.service.manager.RedisCacheManager;
import com.iwindplus.boot.shiro.service.manager.ReloadManager;
import com.iwindplus.boot.shiro.service.manager.SubjectFactory;
import com.iwindplus.boot.shiro.service.realm.JwtRealm;
import com.iwindplus.boot.shiro.util.ShiroUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 无状态配置.
 *
 * @author zengdegui
 * @since 2018/9/6
 */
@Slf4j
public class JwtConfig {
    @Lazy
    @Autowired
    private ShiroService shiroService;

    @Autowired
    private ShiroProperty shiroProperty;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 管理shiro bean生命周期.
     *
     * @return LifecycleBeanPostProcessor
     */
    @Bean("lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        LifecycleBeanPostProcessor lifecycleBeanPostProcessor = new LifecycleBeanPostProcessor();
        log.info("LifecycleBeanPostProcessor [{}]", lifecycleBeanPostProcessor);
        return lifecycleBeanPostProcessor;
    }

    /**
     * 创建DefaultAdvisorAutoProxyCreator.
     *
     * @return DefaultAdvisorAutoProxyCreator
     */
    @Bean
    @ConditionalOnMissingBean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib
        creator.setProxyTargetClass(true);
        log.info("DefaultAdvisorAutoProxyCreator [{}]", creator);
        return creator;
    }

    /**
     * 开启shiro aop注解支持. 使用代理方式;所以需要开启代码支持, Controller才能使用@RequiresPermissions.
     *
     * @param securityManager 核心安全事务管理器
     * @return AuthorizationAttributeSourceAdvisor
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        log.info("AuthorizationAttributeSourceAdvisor [{}]", advisor);
        return advisor;
    }

    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题.
     * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，因为在
     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     * <p>
     * Filter Chain定义说明
     * 1、一个URL可以配置多个Filter，使用逗号分隔
     * 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     *
     * @param securityManager 核心安全事务管理器
     * @return ShiroFilterFactoryBean
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 配置访问权限，动态加载权限（从数据库读取然后配置）
        List<AccessPermsVO> entities = this.shiroService.listAccessPerms();
        if (!CollectionUtils.isEmpty(entities)) {
            entities.stream().forEach(entity -> {
                String url = entity.getUrl();
                String authority = entity.getAuthority();
                filterChainDefinitionMap.put(url, authority);
            });
        }
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        // 配置登录的url和登录成功的url
        shiroFilterFactoryBean.setLoginUrl(this.shiroProperty.getLoginUrl());
        shiroFilterFactoryBean.setSuccessUrl(this.shiroProperty.getSuccessUrl());
        shiroFilterFactoryBean.setUnauthorizedUrl(this.shiroProperty.getUnauthorizedUrl());
        Map<String, Filter> filters = new ConcurrentHashMap<String, Filter>();
        // 认证过滤器
        JwtAuthcFilter filterAuthc = new JwtAuthcFilter();
        filters.put(this.shiroProperty.getFilterAuthc(), filterAuthc);
        // 角色过滤器
        RolesAuthzFilter filterRoles = new RolesAuthzFilter();
        filters.put(this.shiroProperty.getFilterRoles(), filterRoles);
        // 权限过滤器
        PermsAuthzFilter filterPerms = new PermsAuthzFilter();
        filters.put(this.shiroProperty.getFilterPerms(), filterPerms);
        shiroFilterFactoryBean.setFilters(filters);
        log.info("ShiroFilterFactoryBean [{}]", shiroFilterFactoryBean);
        return shiroFilterFactoryBean;
    }

    /**
     * 核心安全事务管理器.
     *
     * @param jwtRealm          jwtRealm
     * @param redisCacheManager redisCacheManager
     * @return SecurityManager
     */
    @Bean
    public SecurityManager securityManager(JwtRealm jwtRealm, RedisCacheManager redisCacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        List<Realm> realms = new LinkedList<>();
        realms.add(jwtRealm);
        // 设置Realm，用于获取认证凭证
        securityManager.setRealms(realms);
        if (securityManager.getSubjectDAO() instanceof DefaultSubjectDAO) {
            DefaultSubjectDAO subjectDAO = (DefaultSubjectDAO) securityManager.getSubjectDAO();
            if (subjectDAO.getSessionStorageEvaluator() instanceof DefaultSessionStorageEvaluator) {
                DefaultSessionStorageEvaluator storageEvaluator
                        = (DefaultSessionStorageEvaluator) subjectDAO.getSessionStorageEvaluator();
                SubjectFactory subjectFactory = new SubjectFactory(storageEvaluator);
                securityManager.setSubjectFactory(subjectFactory);
            }
        }
        // 缓存管理器
        securityManager.setCacheManager(redisCacheManager);
        log.info("SecurityManager [{}]", securityManager);
        return securityManager;
    }

    /**
     * 无状态权限登录器.
     *
     * @param redisCacheManager redisCacheManager
     * @return JwtRealm
     */
    @Bean
    public JwtRealm jwtRealm(RedisCacheManager redisCacheManager) {
        JwtRealm realm = new JwtRealm();
        realm.setShiroService(this.shiroService);
        if (this.shiroProperty.getAuthCacheEnabled()) {
            realm.setCachingEnabled(true);
            // 开启授权缓存
            realm.setAuthorizationCachingEnabled(true);
            realm.setAuthorizationCacheName(this.shiroProperty.getAuthzCacheName());
            // 开启认证缓存
            realm.setAuthenticationCachingEnabled(true);
            realm.setAuthenticationCacheName(this.shiroProperty.getAuthcCacheName());
            realm.setCacheManager(redisCacheManager);
        }
        log.info("JwtRealm [{}]", realm);
        return realm;
    }

    /**
     * 缓存管理器.
     *
     * @return RedisCacheManager
     */
    @Bean
    public RedisCacheManager redisCacheManager() {
        RedisCacheManager cacheManager = new RedisCacheManager();
        cacheManager.setRedisTemplate(this.redisTemplate);
        cacheManager.setKeyPrefix(this.shiroProperty.getCacheKeyPrefix());
        // 单位:秒
        cacheManager.setTimeout(this.shiroProperty.getCacheTimeout());
        log.info("RedisCacheManager [{}]", cacheManager);
        return cacheManager;
    }

    /**
     * shiro工具类.
     *
     * @param shiroFilterFactoryBean shiroFilterFactoryBean
     * @param jwtRealm               jwtRealm
     * @return ShiroUtil
     */
    @Bean
    public ShiroUtil shiroUtil(ShiroFilterFactoryBean shiroFilterFactoryBean, JwtRealm jwtRealm) {
        ShiroUtil shiroUtil = new ShiroUtil();
        ReloadManager reloadManager = new ReloadManager();
        reloadManager.setShiroFilterFactoryBean(shiroFilterFactoryBean);
        reloadManager.setShiroService(this.shiroService);
        shiroUtil.setReloadManager(reloadManager);
        shiroUtil.setJwtRealm(jwtRealm);
        log.info("ShiroUtil [{}]", shiroUtil);
        return shiroUtil;
    }
}
