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

import static org.openinfinity.core.util.ExceptionUtil.getStackTraceString;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedCheckedException;
import org.springframework.core.NestedRuntimeException;

import org.openinfinity.core.exception.ApplicationException;
import org.openinfinity.core.exception.BusinessViolationException;
import org.openinfinity.core.exception.SystemException;

/**
 * Aspect for handling service level exception translation. 
 * Service level exceptions should always be instance of 
 * <code>org.openinfinity.core.exception.ApplicationException</code> (inherited from the 
 * <code>org.openinfinity.core.exception.AbstractCoreException</code>) 
 * or 
 * <code>org.openinfinity.core.exception.SystemException</code> (inherited from the 
 * <code>org.openinfinity.core.exception.AbstractCoreException</code>)
 * or 
 * <code>org.openinfinity.core.exception.BusinessViolationException</code> (inherited from the 
 * <code>org.openinfinity.core.exception.AbstractCoreException</code>).
 * 
 * Unknown exceptions will be translated to <code>org.openinfinity.core.exception.SystemException</code> (inherited from the 
 * <code>org.openinfinity.core.exception.AbstractCoreException</code>).
 * 
 * @author Ilkka Leinonen
 * @version 1.0.0 - Initial version.
 * @since 1.0.0
 */
@Aspect
public class ExceptionTranslatorAspect {
	
	/**
	 * Logger for this class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionTranslatorAspect.class);
	
	
	/**
	 *  Uses <code>org.springframework.stereotype.Controller</code> annotation for the point cut resolving.
	 */
	@Pointcut("execution (* ((@org.springframework.stereotype.Controller *))+.*(..))")
	public void anyControllerMethod() {}
	
	/**
	 *  Uses <code>org.springframework.stereotype.Service</code> annotation for the point cut resolving.
	 */
	@Pointcut("execution (* ((@org.springframework.stereotype.Service *))+.*(..))")
	public void anyServiceMethod() {}
	
	/**
	 * Catches service level exceptions and throws known exceptions forward.
	 * 
	 * @param throwable Represents the thrown Exception which will be caught and resolved as either <code>org.openinfinity.core.exception.ServiceApplicationException</code> or <code>org.openinfinity.core.exception.ServiceSystemException</code>.
	 * @throws Throwable When exception has been resolved as <code>org.openinfinity.core.exception.ServiceApplicationException</code> or <code>org.openinfinity.core.exception.ServiceSystemException</code>.
	 */
	@AfterThrowing(throwing="throwable", pointcut="anyControllerMethod()")
	public void translateControllerException(Throwable throwable) throws Throwable {
		resolveException(throwable);
	}
	
	/**
	 * Catches controller level exceptions and throws known exceptions forward.
	 * 
	 * @param throwable Represents the thrown Exception which will be caught and resolved as either <code>org.openinfinity.core.exception.ServiceApplicationException</code> or <code>org.openinfinity.core.exception.ServiceSystemException</code>.
	 * @throws Throwable When exception has been resolved as <code>org.openinfinity.core.exception.ServiceApplicationException</code> or <code>org.openinfinity.core.exception.ServiceSystemException</code>.
	 */
	@AfterThrowing(throwing="throwable", pointcut="anyServiceMethod()")
	public void translateServiceException(Throwable throwable) throws Throwable {
		resolveException(throwable);
	}

	private void resolveException(Throwable throwable) {
		if (throwable instanceof ApplicationException) {
			ApplicationException applicationException = (ApplicationException)throwable;
			if (!applicationException.isLogged()) {
				LOGGER.warn(getStackTraceString(applicationException));
				applicationException.setLogged(true);
			}
			throw applicationException;
		} else if (throwable instanceof SystemException) {
			SystemException systemException = (SystemException)throwable;
			if (!systemException.isLogged()) {
				LOGGER.error(getStackTraceString(systemException));
				systemException.setLogged(true);
			}
			throw systemException;
		} else if (throwable instanceof BusinessViolationException) {
			BusinessViolationException businessViolationException = (BusinessViolationException)throwable;
			if (!businessViolationException.isLogged()) {
				LOGGER.error(getStackTraceString(businessViolationException));
				businessViolationException.setLogged(true);
			}
			throw businessViolationException;
		} else if (throwable instanceof NestedCheckedException) {
			LOGGER.warn("Resolving NestedCheckedException");
			SystemException systemException = new SystemException("Caught NestedCheckedException, converting to SystemException: ", ((NestedCheckedException)throwable).getMostSpecificCause());
			systemException.setStackTrace(throwable.getStackTrace());
			LOGGER.error(getStackTraceString(systemException));
			systemException.setLogged(true);
			throw systemException;
		} else if (throwable instanceof NestedRuntimeException) {
			LOGGER.warn("Resolving NestedRuntimeException");
			SystemException systemException = new SystemException("Caught NestedRuntimeException, converting to SystemException: ", ((NestedRuntimeException)throwable).getMostSpecificCause());
			systemException.setStackTrace(throwable.getStackTrace());
			LOGGER.error(getStackTraceString(systemException));
			systemException.setLogged(true);
			throw systemException;
		} else {
			LOGGER.warn("Resolving unknown exception (use BusinessViolationException, SystemException or ApplicationException instead).");
			SystemException systemException = new SystemException("Caught unknown exception, converting to SystemException: " + throwable.toString());
			systemException.setStackTrace(throwable.getStackTrace());
			LOGGER.error(getStackTraceString(systemException));
			systemException.setLogged(true);
			throw systemException;
		}
	}
	
}