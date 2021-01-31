/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.iwindplus.boot.shiro.domain.ShiroProperty;
import com.iwindplus.boot.shiro.domain.vo.AccessPermsVO;
import com.iwindplus.boot.shiro.filter.FormAuthcFilter;
import com.iwindplus.boot.shiro.filter.PermsAuthzFilter;
import com.iwindplus.boot.shiro.filter.RolesAuthzFilter;
import com.iwindplus.boot.shiro.service.ShiroService;
import com.iwindplus.boot.shiro.service.manager.RedisCacheManager;
import com.iwindplus.boot.shiro.service.manager.ReloadManager;
import com.iwindplus.boot.shiro.service.manager.ShiroSessionManager;
import com.iwindplus.boot.shiro.service.realm.ShiroRealm;
import com.iwindplus.boot.shiro.service.session.RedisSessionDAO;
import com.iwindplus.boot.shiro.util.ShiroUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 有状态配置.
 *
 * @author zengdegui
 * @since 2018/9/6
 */
@Slf4j
public class ShiroConfig {
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
        FormAuthcFilter filterAuthc = new FormAuthcFilter();
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
     * @param shiroRealm        shiroRealm
     * @param sessionManager    sessionManager
     * @param rememberMeManager rememberMeManager
     * @param redisCacheManager redisCacheManager
     * @return SecurityManager
     */
    @Bean
    public SecurityManager securityManager(ShiroRealm shiroRealm, ShiroSessionManager sessionManager,
            RememberMeManager rememberMeManager, RedisCacheManager redisCacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        List<Realm> realms = new LinkedList<>();
        realms.add(shiroRealm);
        // 设置Realm，用于获取认证凭证
        securityManager.setRealms(realms);
        // session管理器
        securityManager.setSessionManager(sessionManager);
        // 记住我管理器
        securityManager.setRememberMeManager(rememberMeManager);
        // 缓存管理器
        securityManager.setCacheManager(redisCacheManager);
        log.info("SecurityManager [{}]", securityManager);
        return securityManager;
    }

    /**
     * 密码方式权限登录器.
     *
     * @param redisCacheManager redisCacheManager
     * @return ShiroRealm
     */
    @Bean
    public ShiroRealm shiroRealm(RedisCacheManager redisCacheManager) {
        ShiroRealm realm = new ShiroRealm();
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
        log.info("ShiroRealm [{}]", realm);
        return realm;
    }

    /**
     * 有状态记住我管理器.
     *
     * @param rememberMeCookie rememberMeCookie
     * @return CookieRememberMeManager
     */
    @Bean
    public CookieRememberMeManager rememberMeManager(SimpleCookie rememberMeCookie) {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie);
        // rememberMe cookie加密的密钥 建议每个项目都不一样
        // 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode(this.shiroProperty.getRememberCipherKey()));
        log.info("CookieRememberMeManager [{}]", cookieRememberMeManager);
        return cookieRememberMeManager;
    }

    /**
     * 有状态记住我cookie对象.
     *
     * @return SimpleCookie
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        // 这个参数是cookie的名称，对应前端的checkbox的name =
        // rememberMe
        SimpleCookie simpleCookie = new SimpleCookie(this.shiroProperty.getRememberName());
        simpleCookie.setHttpOnly(true);
        // 记住我cookie生效时间30天 ,单位秒
        simpleCookie.setMaxAge(this.shiroProperty.getRememberMeTimeout());
        log.info("rememberMeCookie [{}]", simpleCookie);
        return simpleCookie;
    }

    /**
     * 有状态session管理器.
     *
     * @param sessionDAO                                sessionDAO
     * @param redisCacheManager                         redisCacheManager
     * @param sessionIdCookie                           sessionIdCookie
     * @param executorServiceSessionValidationScheduler executorServiceSessionValidationScheduler
     * @return ShiroSessionManager
     */
    @Bean
    public ShiroSessionManager sessionManager(RedisSessionDAO sessionDAO, RedisCacheManager redisCacheManager,
            SimpleCookie sessionIdCookie,
            ExecutorServiceSessionValidationScheduler executorServiceSessionValidationScheduler) {
        ShiroSessionManager sessionManager = new ShiroSessionManager();
        // 去掉URL中的JSESSIONID
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        // 删除失效session
        sessionManager.setSessionValidationSchedulerEnabled(true);
        // 设置全局会话超时时间，单位:秒，默认30
        // 分钟，即如果30分钟内没有访问会话将过期
        sessionManager.setGlobalSessionTimeout(this.shiroProperty.getCacheTimeout().longValue() * 1000);
        // 是否删除无效的，默认也是开启
        sessionManager.setDeleteInvalidSessions(true);
        // 是否开启 检测，默认开启
        sessionManager.setSessionValidationSchedulerEnabled(true);
        // 设置调度时间间隔，单位:秒，默认就是1 小时；
        sessionManager.setSessionValidationInterval(this.shiroProperty.getSessionValid().longValue() * 1000);
        // 设置会话验证调度器
        sessionManager.setSessionValidationScheduler(executorServiceSessionValidationScheduler);
        // 注入sessionDao
        sessionManager.setSessionDAO(sessionDAO);
        sessionManager.setSessionIdCookie(sessionIdCookie);
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setCacheManager(redisCacheManager);
        log.info("ShiroSessionManager [{}]", sessionManager);
        return sessionManager;
    }

    /**
     * 有状态定时清除无效的session
     *
     * @return ExecutorServiceSessionValidationScheduler
     */
    @Bean
    public ExecutorServiceSessionValidationScheduler executorServiceSessionValidationScheduler() {
        ExecutorServiceSessionValidationScheduler scheduler = new ExecutorServiceSessionValidationScheduler();
        scheduler.setInterval(this.shiroProperty.getSessionValid().longValue() * 1000);
        log.info("ExecutorServiceSessionValidationScheduler [{}]", scheduler);
        return scheduler;
    }

    /**
     * 有状态sessionId
     *
     * @return SimpleCookie
     */
    @Bean
    public SimpleCookie sessionIdCookie() {
        SimpleCookie cookie = new SimpleCookie(this.shiroProperty.getSessionidCookieName());
        cookie.setHttpOnly(true);
        // cookie生效时间，单位:秒
        cookie.setMaxAge(this.shiroProperty.getCacheTimeout());
        log.info("sessionIdCookie [{}]", cookie);
        return cookie;
    }

    /**
     * 有状态session dao
     *
     * @param redisCacheManager redisCacheManager
     * @return RedisSessionDAO
     */
    @Bean
    public RedisSessionDAO sessionDAO(RedisCacheManager redisCacheManager) {
        RedisSessionDAO sessionDAO = new RedisSessionDAO();
        sessionDAO.setRedisTemplate(this.redisTemplate);
        sessionDAO.setKeyPrefix(this.shiroProperty.getSessionCacheKeyPrefix());
        // 单位:秒
        sessionDAO.setTimeout(this.shiroProperty.getCacheTimeout());
        sessionDAO.setCacheManager(redisCacheManager);
        sessionDAO.setActiveSessionsCacheName(this.shiroProperty.getActiveSessionsCacheName());
        log.info("RedisSessionDAO [{}]", sessionDAO);
        return sessionDAO;
    }

    /**
     * 缓存管理器
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
     * shiro工具类
     *
     * @param shiroFilterFactoryBean shiroFilterFactoryBean
     * @param shiroRealm             shiroRealm
     * @return ShiroUtil
     */
    @Bean
    public ShiroUtil shiroUtil(ShiroFilterFactoryBean shiroFilterFactoryBean, ShiroRealm shiroRealm) {
        ShiroUtil shiroUtil = new ShiroUtil();
        ReloadManager reloadManager = new ReloadManager();
        reloadManager.setShiroFilterFactoryBean(shiroFilterFactoryBean);
        reloadManager.setShiroService(this.shiroService);
        shiroUtil.setReloadManager(reloadManager);
        shiroUtil.setPwdRealm(shiroRealm);
        log.info("ShiroUtil [{}]", shiroUtil);
        return shiroUtil;
    }

    /**
     * 添加ShiroDialect 为了在thymeleaf里使用shiro的标签的bean
     *
     * @return ShiroDialect
     */
    @Bean
    public ShiroDialect shiroDialect() {
        ShiroDialect shiroDialect = new ShiroDialect();
        log.info("ShiroDialect [{}]", shiroDialect);
        return shiroDialect;
    }
}
