package com.rs.common.aspect;

import com.alibaba.dubbo.rpc.RpcContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * rpc提供者和消费者日志打印
 * @author liegou
 * @date
 */
@Aspect
@Component
public class RpcLogAspect {

	private static Logger log = LoggerFactory.getLogger(RpcLogAspect.class);
	// 开始时间
	private long startTime = 0L;
	// 结束时间
	private long endTime = 0L;

	@Before("execution(* *..rpc..*.*(..))")
	public void doBeforeInServiceLayer(JoinPoint joinPoint) {
		log.debug("doBeforeInServiceLayer");
		startTime = System.currentTimeMillis();
	}

	@After("execution(* *..rpc..*.*(..))")
	public void doAfterInServiceLayer(JoinPoint joinPoint) {
		log.debug("doAfterInServiceLayer");
	}

	@Around("execution(* *..rpc..*.*(..))")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		Object result = pjp.proceed();
		// 是否是消费端
		boolean consumerSide = RpcContext.getContext().isConsumerSide();
		// 获取最后一次提供方或调用方IP
		String ip = RpcContext.getContext().getRemoteHost();
		// 服务url
		String rpcUrl = RpcContext.getContext().getUrl().getParameter("application");
		log.debug("consumerSide={}, ip={}, url={}", consumerSide, ip, rpcUrl);
		return result;
	}

}
