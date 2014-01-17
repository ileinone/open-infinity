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
package org.openinfinity.core.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openinfinity.core.exception.ApplicationException;
import org.openinfinity.core.exception.BusinessViolationException;
import org.openinfinity.core.exception.ExceptionLevel;
import org.openinfinity.core.exception.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exception utility class which offers basic methods for exception throwing and stack trace resolving. 
 * 
 * @author Ilkka Leinonen
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ExceptionUtil {
	
	private static final String BUSINESS_VIOLATION_EXCEPTION_MESSAGE = "Throwing business violation exception, cause: ";
	private static final String SYSTEM_EXCEPTION_MESSAGE = "Throwing system exception, cause: ";
	private static final String APPLICATION_EXCEPTION_MESSAGE = "Throwing application exception, cause: ";
	
	/**
	 * Logger for this class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionUtil.class);
	
	/**
	 * Throws and logs application exception.
	 * 
	 * @param cause Represents the message of the exception.
	 * @param throwable Represents the original exception.
	 * @throws ApplicationException
	 */
	public static void throwApplicationException(String cause, Throwable throwable) throws ApplicationException {
		LOGGER.debug(APPLICATION_EXCEPTION_MESSAGE + cause, throwable);
		throw new ApplicationException(cause, throwable);
	}
	
	/**
	 * Throws and logs system exception.
	 * 
	 * @param cause Represents the message of the exception.
	 * @param throwable Represents the original exception.
	 * @throws ApplicationException
	 */
	public static void throwSystemException(String cause, Throwable throwable) throws SystemException {
		LOGGER.debug(SYSTEM_EXCEPTION_MESSAGE + cause, throwable);
		throw new SystemException(cause, throwable);
	}
	
	/**
	 * Throws and logs business violation exception.
	 * 
	 * @param cause Represents the message of the exception.
	 * @param throwable Represents the original exception.
	 * @throws BusinessViolationException
	 */
	public static void throwBusinessViolationException(String cause, Throwable throwable) throws BusinessViolationException {
		LOGGER.debug(BUSINESS_VIOLATION_EXCEPTION_MESSAGE + cause, throwable);
		throw new BusinessViolationException(cause, throwable);
	}
	
	/**
	 * Throws and logs application exception.
	 * 
	 * @param cause Represents the message of the exception.
	 * @param throwable Represents the original exception.
	 * @param exceptionLevel Represents the exception level.
	 * @param serviceSpecificUniqueId Represents the unique id of the exception.
	 * @throws ApplicationException
	 */
	public static void throwApplicationException(String cause, Throwable throwable, ExceptionLevel exceptionLevel, String serviceSpecificUniqueId) throws ApplicationException {
		LOGGER.debug(APPLICATION_EXCEPTION_MESSAGE + cause, throwable);
		ApplicationException applicationException = new ApplicationException(cause, throwable);
		applicationException.addExceptionLevelBasedUniqueId(exceptionLevel, serviceSpecificUniqueId);
		throw applicationException;
	}
	
	/**
	 * Throws and logs system exception.
	 * 
	 * @param cause Represents the message of the exception.
	 * @param throwable Represents the original exception.
	 * @param exceptionLevel Represents the exception level.
	 * @param serviceSpecificUniqueId Represents the unique id of the exception.
	 * @throws SystemException
	 */
	public static void throwSystemException(String cause, Throwable throwable, ExceptionLevel exceptionLevel, String serviceSpecificUniqueId) throws SystemException {
		LOGGER.debug(SYSTEM_EXCEPTION_MESSAGE + cause, throwable);
		SystemException systemException = new SystemException(cause, throwable);
		systemException.addExceptionLevelBasedUniqueId(exceptionLevel, serviceSpecificUniqueId);
		throw systemException;
	}
	
	/**
	 * Throws and logs business violation exception.
	 * 
	 * @param cause Represents the message of the exception.
	 * @param throwable Represents the original exception.
	 * @param exceptionLevel Represents the exception level.
	 * @param serviceSpecificUniqueId Represents the unique id of the exception.
	 * @throws BusinessViolationException
	 */
	public static void throwBusinessViolationException(String cause, Throwable throwable, ExceptionLevel exceptionLevel, String serviceSpecificUniqueId) throws BusinessViolationException {
		LOGGER.debug(BUSINESS_VIOLATION_EXCEPTION_MESSAGE + cause, throwable);
		BusinessViolationException businessViolationException = new BusinessViolationException(cause, throwable);
		businessViolationException.addExceptionLevelBasedUniqueId(exceptionLevel, serviceSpecificUniqueId);
		throw businessViolationException;
	}

	/**
	 * Throws and logs application exception.
	 * 
	 * @param cause Represents the message of the exception.
	 * @param exceptionLevel Represents the exception level.
	 * @param serviceSpecificUniqueId Represents the unique id of the exception.
	 * @throws ApplicationException
	 */
	public static void throwApplicationException(String cause, ExceptionLevel exceptionLevel, String serviceSpecificUniqueId) throws ApplicationException {
		LOGGER.debug(APPLICATION_EXCEPTION_MESSAGE + cause);
		ApplicationException applicationException = new ApplicationException(cause);
		applicationException.addExceptionLevelBasedUniqueId(exceptionLevel, serviceSpecificUniqueId);
		throw applicationException;
	}
	
	/**
	 * Throws and logs system exception.
	 * 
	 * @param cause Represents the message of the exception.
	 * @param exceptionLevel Represents the exception level.
	 * @param serviceSpecificUniqueId Represents the unique id of the exception.
	 * @throws ApplicationException
	 */
	public static void throwSystemException(String cause, ExceptionLevel exceptionLevel, String serviceSpecificUniqueId) throws SystemException {
		LOGGER.debug(SYSTEM_EXCEPTION_MESSAGE + cause);
		SystemException systemException = new SystemException(cause);
		systemException.addExceptionLevelBasedUniqueId(exceptionLevel, serviceSpecificUniqueId);
		throw systemException;
	}

	/**
	 * Throws and logs business violation exception.
	 * 
	 * @param cause Represents the message of the exception.
	 * @param exceptionLevel Represents the exception level.
	 * @param serviceSpecificUniqueId Represents the unique id of the exception.
	 * @throws BusinessViolationException
	 */
	public static void throwBusinessViolationException(String cause, ExceptionLevel exceptionLevel, String serviceSpecificUniqueId) throws BusinessViolationException {
		LOGGER.debug(BUSINESS_VIOLATION_EXCEPTION_MESSAGE + cause);
		BusinessViolationException businessViolationException = new BusinessViolationException(cause);
		businessViolationException.addExceptionLevelBasedUniqueId(exceptionLevel, serviceSpecificUniqueId);
		throw businessViolationException;
	}

	/**
	 * Throws and logs application exception.
	 * 
	 * @param cause Represents the message of the exception.
	 * @throws ApplicationException
	 */
	public static void throwApplicationException(String cause) throws ApplicationException {
		LOGGER.debug(APPLICATION_EXCEPTION_MESSAGE + cause);
		throw new ApplicationException(cause);
	}
	
	/**
	 * Throws and logs system exception.
	 * 
	 * @param cause Represents the message of the exception.
	 * @throws ApplicationException
	 */
	public static void throwSystemException(String cause) throws SystemException {
		LOGGER.debug(SYSTEM_EXCEPTION_MESSAGE + cause);
		throw new SystemException(cause);
	}
	
	/**
	 * Throws and logs system exception.
	 * 
	 * @param cause Represents the message of the exception.
	 * @throws ApplicationException
	 */
	public static void throwSystemException(Throwable throwable) throws SystemException {
		LOGGER.debug(SYSTEM_EXCEPTION_MESSAGE + throwable.toString());
		throw new SystemException(throwable);
	}

	/**
	 * Throws and logs business violation exception.
	 * 
	 * @param cause Represents the message of the exception.
	 * @throws BusinessViolationException
	 */
	public static void throwBusinessViolationException(String cause) throws BusinessViolationException {
		LOGGER.debug(BUSINESS_VIOLATION_EXCEPTION_MESSAGE + cause);
		throw new BusinessViolationException(cause);
	}
	
	/**
     * Get stacktrace as string from given throwable.
     * 
     * @param throwable exception which stacktrace should be printed out
     * @return stacktrace as string
     */
	public static String getStackTraceString(Throwable throwable) {
    	StringWriter sw = null;
    	PrintWriter pw = null;
    	try {
	    	sw = new StringWriter();
	        pw = new PrintWriter(sw, true);
	        throwable.printStackTrace(pw);
	        pw.flush();
	        sw.flush();
	        return sw.toString();
        } finally {
        	IOUtil.closeWriter(sw);
			IOUtil.closeWriter(pw);
        }
	}
	
}
