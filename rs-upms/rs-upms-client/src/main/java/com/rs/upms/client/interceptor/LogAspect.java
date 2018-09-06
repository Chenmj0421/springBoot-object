package com.rs.upms.client.interceptor;

import com.alibaba.fastjson.JSON;
import com.reger.dubbo.annotation.Inject;
import com.rs.common.util.RequestUtil;
import com.rs.upms.client.shiro.util.ShiroUtil;
import com.rs.upms.dao.model.UpmsLog;
import com.rs.upms.rpc.api.UpmsApiService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.ObjectUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 日志记录AOP实现
 *
 * @author liegou
 * @date
 */
@Aspect
@Component
public class LogAspect {

    private static Logger _log = LoggerFactory.getLogger(LogAspect.class);

    // 开始时间
    private long startTime = 0L;
    // 结束时间
    private long endTime = 0L;
    private Date nowDate = null;

    @Inject
    UpmsApiService upmsApiService;

    @Before("execution(* *..controller..*.*(..))")
    public void doBeforeInServiceLayer(JoinPoint joinPoint) {
        _log.debug("doBeforeInServiceLayer");
        startTime = System.currentTimeMillis();
        nowDate = new Date();
    }

    @After("execution(* *..controller..*.*(..))")
    public void doAfterInServiceLayer(JoinPoint joinPoint) {
        _log.debug("doAfterInServiceLayer");
    }

    @Around("execution(* *..controller..*.*(..))")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        // 获取request
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();

        UpmsLog upmsLog = new UpmsLog();
        // 从注解中获取操作名称、获取响应结果
        Object result = pjp.proceed();
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method.isAnnotationPresent(ApiOperation.class)) {
            ApiOperation log = method.getAnnotation(ApiOperation.class);
            upmsLog.setDescription(log.value());
        }
        if (method.isAnnotationPresent(RequiresPermissions.class)) {
            RequiresPermissions requiresPermissions = method.getAnnotation(RequiresPermissions.class);
            String[] permissions = requiresPermissions.value();
            if (permissions.length > 0) {
                upmsLog.setPermissions(permissions[0]);
            }
        }
        endTime = System.currentTimeMillis();
        _log.debug("doAround>>>result={},耗时：{}", result, endTime - startTime);

        upmsLog.setBasePath(RequestUtil.getBasePath(request));
        upmsLog.setIp(RequestUtil.getIpAddr(request));
        upmsLog.setMethod(request.getMethod());
        if (request.getMethod().equalsIgnoreCase("GET")) {
            upmsLog.setParameter(request.getQueryString());
        } else {
            upmsLog.setParameter(ObjectUtils.toString(request.getParameterMap()));
        }
        upmsLog.setResult(JSON.toJSONString(result));
        upmsLog.setSpendTime((int) (endTime - startTime));
        upmsLog.setStartTime(nowDate);
        upmsLog.setUri(request.getRequestURI());
        upmsLog.setUrl(ObjectUtils.toString(request.getRequestURL()));
        upmsLog.setUserAgent(request.getHeader("User-Agent"));
        upmsLog.setUserName(ShiroUtil.getUpmsUser() == null ? null : ShiroUtil.getUpmsUser().getUserName());
        upmsLog.setUserId(ShiroUtil.getUpmsUser() == null ? null : ShiroUtil.getUserId());
        upmsApiService.insertUpmsLogSelective(upmsLog);
        return result;
    }

}