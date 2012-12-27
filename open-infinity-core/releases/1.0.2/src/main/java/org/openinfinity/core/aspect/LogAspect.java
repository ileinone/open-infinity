/*
 * Copyright (c) 2012 the original author or authors.
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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openinfinity.core.annotation.Log;
import org.openinfinity.core.annotation.Log.LogLevel;
import org.openinfinity.core.util.AspectUtil;

/**
 * This class is responsible of the logging using AOP with annotation <code>org.openinfinity.core.annotation.Log</code>.
 * 
 * @author Ilkka Leinonen
 * @version 1.0.0
 * @since 1.0.0
 */
@Aspect
public class LogAspect extends ArgumentGatheringJoinPointInterceptor {
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
	 *  Uses <code>org.openinfinity.core.annotation.Log</code> annotation for the point cut resolving.
	 */
	@Pointcut("@annotation(org.openinfinity.core.annotation.Log)")
	public void loggedMethod(){}
	
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
		//Log log = AspectUtil.getAnnotation(method, Log.class);
		LogLevel level = log.level();
		if (level.getValue() < this.defaultLogLevel)
			return this.overrideLevelWithAnnotation(method, level);
		else  
			return this.debugWithDefaultLevel(method);	
	}
	
	private Object debugWithDefaultLevel(ProceedingJoinPoint method) throws Throwable{
		if (this.defaultLogLevel == LOG_LEVEL_TRACE)
			return logTraceAndProceed(method);
		else if (this.defaultLogLevel == LOG_LEVEL_DEBUG)
			return logDebugAndProceed( method );
		else if (this.defaultLogLevel == LOG_LEVEL_INFO)
			return logInfoAndProceed( method );
		else if (this.defaultLogLevel == LOG_LEVEL_ERROR)
			return logErrorAndProceed(method);
		else if (this.defaultLogLevel == LOG_LEVEL_WARN) 
			return logWarningAndProceed(method);
		else return logDebugAndProceed(method);		
	}

	private Object overrideLevelWithAnnotation(ProceedingJoinPoint method, LogLevel level) throws Throwable {
		switch (level) {
			case DEBUG: return (LOGGER.isDebugEnabled())?logDebugAndProceed(method):method.proceed();
			case ERROR: return (LOGGER.isErrorEnabled())?logErrorAndProceed(method):method.proceed();
			case INFO: return (LOGGER.isInfoEnabled())?logInfoAndProceed(method):method.proceed();
			case TRACE: return (LOGGER.isTraceEnabled())?logTraceAndProceed(method):method.proceed();
			case WARN: return (LOGGER.isWarnEnabled())?logWarningAndProceed(method):method.proceed();
			default: return (LOGGER.isDebugEnabled())?logDebugAndProceed(method):method.proceed();
		}
	}
	
	private Object logWarningAndProceed(ProceedingJoinPoint method) throws Throwable {
		String name = AspectUtil.createJoinPointTraceName(method);
		long startTime = System.currentTimeMillis();
		try {
			LOGGER.warn(name + ": initialized");
			String argumentInfo = getArgumentInfo(method);
			LOGGER.warn( buildArgumentDetails(name, argumentInfo));
			Object o = method.proceed();
			LOGGER.warn( buildReturnValueDetails(name, o));
			return o;
		} finally {
			LOGGER.warn(name + ": finalized in " + (System.currentTimeMillis()-startTime)+" ms");
		}
	}
	
	private Object logInfoAndProceed(ProceedingJoinPoint method) throws Throwable {
		String name = AspectUtil.createJoinPointTraceName(method);
		long startTime = System.currentTimeMillis();
		try {
			LOGGER.info(name + ": initialized");
			String argumentInfo = getArgumentInfo(method);
			LOGGER.info(buildArgumentDetails(name, argumentInfo));
			Object o = method.proceed();
			LOGGER.info(buildReturnValueDetails(name, o ));
			return o;
		} finally {
			LOGGER.info(name + ": finalized in " + (System.currentTimeMillis()-startTime)+" ms");
		}
	}

	private Object logErrorAndProceed(ProceedingJoinPoint method) throws Throwable {
		String name = AspectUtil.createJoinPointTraceName(method);
		long startTime = System.currentTimeMillis();
		try {
			LOGGER.error(name + ": initialized");
			String argumentInfo = getArgumentInfo(method);
			LOGGER.error(buildArgumentDetails(name, argumentInfo) );
			Object o = method.proceed();
			LOGGER.error(buildReturnValueDetails(name, o));
			return o;
		} finally {
			LOGGER.error(name + ": finalized in " + (System.currentTimeMillis()-startTime)+" ms");
		}
	}

	private Object logTraceAndProceed(ProceedingJoinPoint method) throws Throwable {
		String name = AspectUtil.createJoinPointTraceName(method);
		long startTime = System.currentTimeMillis();
		try {
			LOGGER.trace(name + ": initialized");
			String argumentInfo = getArgumentInfo(method);
			LOGGER.trace(buildArgumentDetails(name, argumentInfo));
			Object o = method.proceed();
			LOGGER.trace(buildReturnValueDetails(name, o));
			return o;
		} finally {
			LOGGER.trace(name + ": finalized in " + (System.currentTimeMillis()-startTime)+" ms");
		}
	}
	
	private Object logDebugAndProceed(ProceedingJoinPoint method) throws Throwable {
		String name = AspectUtil.createJoinPointTraceName(method);
		long startTime = System.currentTimeMillis();
		try {
			LOGGER.debug(name + ": initialized");
			String argumentInfo = getArgumentInfo(method);
			LOGGER.debug(buildArgumentDetails(name, argumentInfo));
			Object o = method.proceed();
			LOGGER.debug(buildReturnValueDetails(name, o));
			return o;
		} finally {
			LOGGER.debug(name + ": finalized in " + (System.currentTimeMillis()-startTime)+" ms");
		}
	}
	
}