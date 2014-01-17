/*
 * Copyright (c) 2011-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openinfinity.core.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.openinfinity.core.annotation.Log;
import org.openinfinity.core.annotation.Log.LogLevel;
import org.openinfinity.core.util.AspectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;

/**
 * This class is responsible of the logging using AOP with annotation <code>org.openinfinity.core.annotation.Log</code>.
 * 
 * @author Ilkka Leinonen
 * @version 1.0.0
 * @since 1.0.0
 */
@Aspect
public class LogAspect extends ArgumentGatheringJoinPointInterceptor implements Ordered {
	/**
	 * Represents the main logger for the application.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);
	
	/** 
	 * Represents the debug logging level.
	 */
	public static final Integer LOG_LEVEL_DEBUG = LogLevel.DEBUG.getValue();
	
	/** 
	 * Represents the info logging level.
	 */
	public static final Integer LOG_LEVEL_INFO = LogLevel.INFO.getValue();
	
	/** 
	 * Represents the warn logging level.
	 */
	public static final Integer LOG_LEVEL_WARN = LogLevel.WARN.getValue();
	
	/** 
	 * Represents the error logging level.
	 */
	public static final Integer LOG_LEVEL_ERROR = LogLevel.ERROR.getValue();
	
	/** 
	 * Represents the trace logging level.
	 */
	public static final Integer LOG_LEVEL_TRACE = LogLevel.TRACE.getValue();
	
	/**
	 * Represents the default logging level.
	 */
	private static final Integer LOG_LEVEL_NOT_SET = -1; 
	
	/**
	 * Represents the default debug level of the application.
	 */
	private Integer defaultLogLevel = LOG_LEVEL_NOT_SET;
	
	/**
	 * Represents the execution order of the aspect.
	 */
	private int order;
	
	/**
	 * Setter for the order.
	 * 
	 * @param order Represents the execution order of the aspect.
	 */
	public void setOrder(int order) {
		this.order = order;
	}
	
	/**
	 *  Uses <code>org.openinfinity.core.annotation.Log</code> annotation for the point cut resolving.
	 */
	@Pointcut("@annotation(org.openinfinity.core.annotation.Log)")
	public void loggedMethod(){}
	
	/**
	 * Setter for the default log level.
	 * 
	 * @param defaultLogLevel Represents the default log level for the aspect.
	 */
	public void setDefaultLogLevel(Integer defaultLogLevel) {
		this.defaultLogLevel = defaultLogLevel;
	}

	/**
	 * Logs the method information based on the <code>org.openinfinity.core.annotation.Logging</code> annotation.
	 * Log level will be based on the value setted (debug=1, info=2, warn=3, error=4, trace=5) in the 
	 * configuration file or by the annotation <code>org.openinfinity.core.annotation.Log.LogLevel</code>.
	 * Highest level will be the actual log level.
	 * 
	 * @param method Represents the method which has been invoked.
	 * @return Object Represents the object to be returned.
	 * @throws Throwable Thrown if something goes wrong and will be handled by the frame itself.
	 */
	@Around("loggedMethod() && @annotation(log)")
	public Object logMethod(ProceedingJoinPoint method, Log log) throws Throwable {
		LogLevel level = log.level();
		if (level.getValue() < this.defaultLogLevel)
			return this.overrideLevelWithAnnotation(method, level, log);
		else  
			return this.debugWithDefaultLevel(method, log);	
	}
	
	private Object debugWithDefaultLevel(ProceedingJoinPoint method, Log log) throws Throwable{
		if (this.defaultLogLevel == LOG_LEVEL_TRACE)
			return logTraceAndProceed(method, log);
		else if (this.defaultLogLevel == LOG_LEVEL_DEBUG)
			return logDebugAndProceed(method, log);
		else if (this.defaultLogLevel == LOG_LEVEL_INFO)
			return logInfoAndProceed(method, log);
		else if (this.defaultLogLevel == LOG_LEVEL_ERROR)
			return logErrorAndProceed(method, log);
		else if (this.defaultLogLevel == LOG_LEVEL_WARN) 
			return logWarningAndProceed(method, log);
		else return logDebugAndProceed(method, log);		
	}

	private Object overrideLevelWithAnnotation(ProceedingJoinPoint method, LogLevel level, Log log) throws Throwable {
		switch (level) {
			case DEBUG: return (LOGGER.isDebugEnabled())?logDebugAndProceed(method, log):method.proceed();
			case ERROR: return (LOGGER.isErrorEnabled())?logErrorAndProceed(method, log):method.proceed();
			case INFO: return (LOGGER.isInfoEnabled())?logInfoAndProceed(method, log):method.proceed();
			case TRACE: return (LOGGER.isTraceEnabled())?logTraceAndProceed(method, log):method.proceed();
			case WARN: return (LOGGER.isWarnEnabled())?logWarningAndProceed(method, log):method.proceed();
			default: return (LOGGER.isDebugEnabled())?logDebugAndProceed(method, log):method.proceed();
		}
	}
	
	private String gatherArgumentDetailsToLogBasedOnDesicionLogic(JoinPoint joinPoint, Log log, String name) {
		ArgumentBuilder builder = new ArgumentBuilder();
		ArgumentStrategy argumentStrategy = log.argumentStrategy();
		switch(argumentStrategy) {
			case ALL : builder.extractArgumentInfo(joinPoint); return builder.toString(); 
			case CUSTOM : builder.extractArgumentInfoByFilteringFields(joinPoint, log.value()); return builder.toString();
			case NONE : return "not showing arguments";
			default: return getArgumentInfo(joinPoint);
		}
	}
	
	private Object logWarningAndProceed(ProceedingJoinPoint method, Log log) throws Throwable {
		String name = AspectUtil.createJoinPointTraceName(method);
		long startTime = System.currentTimeMillis();
		try {
			LOGGER.warn(name + ": initialized");
			String argumentInfo = gatherArgumentDetailsToLogBasedOnDesicionLogic(method, log, name);
			LOGGER.warn(buildArgumentDetails(name, argumentInfo));
			Object o = method.proceed();
			LOGGER.warn(buildReturnValueDetails(name, o));
			return o;
		} finally {
			LOGGER.warn(name + ": finalized in " + (System.currentTimeMillis()-startTime) + " ms");
		}
	}
	
	private Object logInfoAndProceed(ProceedingJoinPoint method, Log log) throws Throwable {
		String name = AspectUtil.createJoinPointTraceName(method);
		long startTime = System.currentTimeMillis();
		try {
			LOGGER.info(name + ": initialized");
			String argumentInfo = gatherArgumentDetailsToLogBasedOnDesicionLogic(method, log, name);
			LOGGER.info(buildArgumentDetails(name, argumentInfo));
			Object o = method.proceed();
			LOGGER.info(buildReturnValueDetails(name, o ));
			return o;
		} finally {
			LOGGER.info(name + ": finalized in " + (System.currentTimeMillis()-startTime) + " ms");
		}
	}
	
	private Object logErrorAndProceed(ProceedingJoinPoint method, Log log) throws Throwable {
		String name = AspectUtil.createJoinPointTraceName(method);
		long startTime = System.currentTimeMillis();
		try {
			LOGGER.error(name + ": initialized");
			String argumentInfo = gatherArgumentDetailsToLogBasedOnDesicionLogic(method, log, name);
			LOGGER.error(buildArgumentDetails(name, argumentInfo) );
			Object o = method.proceed();
			LOGGER.error(buildReturnValueDetails(name, o));
			return o;
		} finally {
			LOGGER.error(name + ": finalized in " + (System.currentTimeMillis()-startTime) + " ms");
		}
	}

	private Object logTraceAndProceed(ProceedingJoinPoint method, Log log) throws Throwable {
		String name = AspectUtil.createJoinPointTraceName(method);
		long startTime = System.currentTimeMillis();
		try {
			LOGGER.trace(name + ": initialized");
			String argumentInfo = gatherArgumentDetailsToLogBasedOnDesicionLogic(method, log, name);
			LOGGER.trace(buildArgumentDetails(name, argumentInfo));
			Object o = method.proceed();
			LOGGER.trace(buildReturnValueDetails(name, o));
			return o;
		} finally {
			LOGGER.trace(name + ": finalized in " + (System.currentTimeMillis()-startTime) + " ms");
		}
	}
	
	private Object logDebugAndProceed(ProceedingJoinPoint method, Log log) throws Throwable {
		String name = AspectUtil.createJoinPointTraceName(method);
		long startTime = System.currentTimeMillis();
		try {
			LOGGER.debug(name + ": initialized");
			String argumentInfo = gatherArgumentDetailsToLogBasedOnDesicionLogic(method, log, name);
			LOGGER.debug(buildArgumentDetails(name, argumentInfo));
			Object o = method.proceed();
			LOGGER.debug(buildReturnValueDetails(name, o));
			return o;
		} finally {
			LOGGER.debug(name + ": finalized in " + (System.currentTimeMillis()-startTime) + " ms");
		}
	}

	@Override
	public int getOrder() {
		return order;
	}
	
}