package com.rs.upms.client.shiro.config;

import com.rs.upms.client.shiro.filter.UpmsAuthenticationFilter;
import com.rs.upms.client.shiro.filter.UpmsSessionForceLogoutFilter;
import com.rs.upms.client.shiro.listener.UpmsSessionListener;
import com.rs.upms.client.shiro.realm.UpmsRealm;
import com.rs.upms.client.shiro.session.UpmsSessionDao;
import com.rs.upms.client.shiro.session.UpmsSessionFactory;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.*;

/**
 * shiro 配置类
 *
 * @author liegou
 */

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "rs.upms")
public class ShiroConfig {

    /**
     * 会话时长,单位毫秒
     */

    @Value("${rs.upms.session.timeout}")
    public Long rsUpmsSessionTimeout;
    /**
     * Cookie名称
     */
    @Value("${rs.upms.session.id}")
    public String cookieName;
    /**
     * 登录地址
     */
    @Value("${rs.upms.sso.server.url}")
    public String loginUrl;
    /**
     * 成功登录跳转地址
     */
    @Value("${rs.upms.successUrl}")
    public String successUrl;
    /**
     * 未授权跳转地址
     */
    @Value("${rs.upms.unauthorizedUrl}")
    public String unauthorizedUrl;

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager());
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        //注意过滤器配置顺序 不能颠倒
        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了，登出后跳转配置的loginUrl
        filterChainDefinitionMap.put("/manage/**", "upmsSessionForceLogoutFilter,authc");
        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/manage/index", "user");
        filterChainDefinitionMap.put("/druid/**", "user");
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/resources/**", "anon");
        filterChainDefinitionMap.put("/**", "anon");
        //配置shiro默认登录界面地址，前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据
        shiroFilterFactoryBean.setLoginUrl(loginUrl);
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl(successUrl);
        //未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl(unauthorizedUrl);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        Map<String, Filter> filters = new HashMap<String, Filter>();
        filters.put("authc", upmsAuthenticationFilter());
        filters.put("upmsSessionForceLogoutFilter", upmsSessionForceLogoutFilter());
        shiroFilterFactoryBean.setFilters(filters);
        return shiroFilterFactoryBean;
    }

    @Bean
    public AccessControlFilter upmsSessionForceLogoutFilter() {
        AccessControlFilter upmsSessionForceLogoutFilter = new UpmsSessionForceLogoutFilter();
        return upmsSessionForceLogoutFilter;

    }

    @Bean
    public AuthenticationFilter upmsAuthenticationFilter() {
        AuthenticationFilter upmsAuthenticationFilter = new UpmsAuthenticationFilter();
        return upmsAuthenticationFilter;
    }

    /**
     * realm实现，继承自AuthorizingRealm
     *
     * @return
     */
    @Bean(name = "shiroRealm")
    public AuthorizingRealm upmsRealm() {
        AuthorizingRealm realm = new UpmsRealm();
        return realm;
    }


    /**
     * 会话DAO，可重写，持久化session
     *
     * @return
     */
    @Bean
    public CachingSessionDAO upmsSessionDao() {
        CachingSessionDAO upmsSessionDao = new UpmsSessionDao();
        return upmsSessionDao;
    }

    /**
     * 会话Cookie模板
     *
     * @return
     */
    @Bean
    public SimpleCookie sessionIdCookie() {
        SimpleCookie sessionIdCookie = new SimpleCookie();
        //  不会暴露给客户端
        sessionIdCookie.setHttpOnly(true);
        // 设置Cookie的过期时间，秒为单位，默认-1表示关闭浏览器时过期Cookie
        sessionIdCookie.setMaxAge(-1);
        // Cookie名称
        sessionIdCookie.setName(cookieName);
        return sessionIdCookie;
    }

    /**
     * 会话监听器
     *
     * @return
     */
    @Bean
    public SessionListener upmsSessionListener() {
        SessionListener upmsSessionListener = new UpmsSessionListener();
        return upmsSessionListener;
    }

    /**
     * session工厂
     *
     * @return
     */
    @Bean
    public SessionFactory upmsSessionFactory() {
        SessionFactory upmsSessionFactory = new UpmsSessionFactory();
        return upmsSessionFactory;
    }

    /**
     * 会话管理器
     *
     * @return
     */
    @Bean
    public DefaultWebSessionManager defaultWebSessionManager() {
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setSessionDAO(upmsSessionDao());
        defaultWebSessionManager.setGlobalSessionTimeout(rsUpmsSessionTimeout);
        defaultWebSessionManager.setSessionIdCookieEnabled(true);
        defaultWebSessionManager.setSessionIdCookie(sessionIdCookie());
        defaultWebSessionManager.setSessionValidationSchedulerEnabled(false);
        Collection<SessionListener> listeners = new ArrayList();
        listeners.add(upmsSessionListener());
        defaultWebSessionManager.setSessionListeners(listeners);
        defaultWebSessionManager.setSessionFactory(upmsSessionFactory());
        return defaultWebSessionManager;
    }

    /**
     * rememberMe管理器
     *
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        // rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度（128 256 512 位）
        byte[] cipherKey = org.apache.shiro.codec.Base64.decode("4AvVhmFLUs0KTA3Kprsdag==");
        rememberMeManager.setCookie(rememberMeCookie());
        rememberMeManager.setCipherKey(cipherKey);
        return rememberMeManager;
    }

    /**
     * rememberMe缓存cookie
     *
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //  不会暴露给客户端
        simpleCookie.setHttpOnly(true);
        // 记住我cookie生效时间
        simpleCookie.setMaxAge(Integer.valueOf(rsUpmsSessionTimeout.toString()));
        return simpleCookie;

    }

    /**
     * 安全管理器
     *
     * @return
     */
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager() {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(upmsRealm());
        defaultWebSecurityManager.setSessionManager(defaultWebSessionManager());
        defaultWebSecurityManager.setRememberMeManager(rememberMeManager());
        return defaultWebSecurityManager;
    }


    /**
     * 设置SecurityUtils，相当于调用SecurityUtils.setSecurityManager(securityManager)
     *
     * @return
     */
    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean() {
        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
        methodInvokingFactoryBean.setArguments(defaultWebSecurityManager());
        methodInvokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        return methodInvokingFactoryBean;

    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(defaultWebSecurityManager());
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * LifecycleBeanPostProcessor，这是个DestructionAwareBeanPostProcessor的子类，
     * 负责org.apache.shiro.util.Initializable类型bean的生命周期的，初始化和销毁。
     * 主要是AuthorizingRealm类的子类，以及EhCacheManager类。
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator getAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }
}
