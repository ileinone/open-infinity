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
package org.openinfinity.core.exception;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for maintaining logging information and unique ids with different exception levels in the runtime. This class extends <code>java.lang.RuntimeException</code> exception.
 * 
 * Runtime object is not thread safe.
 * 
 * @author Ilkka Leinonen
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AbstractCoreException extends RuntimeException {
	
	/**
	 * Exception message if service specific id allready exists.
	 */
	private static final String EXCEPTION_MESSAGE_SERVICE_SPECIFIC_ID_ALLREADY_EXISTS = "Service specific id allready exists: ";

	/**
	 * Represents the serial version UID.
	 */
	private static final long serialVersionUID = 6320097415034236002L;
	
	/**
	 * Represents the the state of the logging.
	 */
	private boolean logged;
	
	/**
	 * Represents the exception level based unique ids for the exception. 
	 */
	private Map<ExceptionLevel, Collection<String>> exceptionLevelBasedUniqueErrorIds = new HashMap<ExceptionLevel, Collection<String>>();

	/**
	 * Represents exception details class for storing multiple exception messages within one exception.
	 */
	private Collection<ExceptionDetails<?>> exceptionDetailsCollection = new ArrayList<ExceptionDetails<?>>();
	
    /** Constructs a new abstract core exception with <code>null</code> as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public AbstractCoreException() {
    	super();
    }

    /** Constructs a new abstract core exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for 
     *          later retrieval by the {@link #getMessage()} method.
     */
    public AbstractCoreException(String message) {
    	super(message);
    }

    /**
     * Constructs a new abstract core exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * <code>cause</code> is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     *
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public AbstractCoreException(String message, Throwable cause) {
        super(message, cause);
    }

    /** Constructs a new abstract core exception with the specified cause and a
     * detail message of <tt>(cause==null ? null : cause.toString())</tt>
     * (which typically contains the class and detail message of
     * <tt>cause</tt>).  This constructor is useful for runtime exceptions
     * that are little more than wrappers for other throwables.
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public AbstractCoreException(Throwable cause) {
        super(cause);
    }

	/**
	 * Returns information about the logged exception.
	 * @return Boolean.TRUE if logged else false.
	 */
	public boolean isLogged() {
		return logged;
	}

	/**
	 * Setter for logging information. 
	 * @param logged
	 */
	public void setLogged(boolean logged) {
		this.logged = logged;
	}

	/**
	 * Adds exception level specific exception id to thrown exception.
	 * 
	 * @param exceptionLevel Represents the <code>org.openinfinity.core.exception.ExceptionLevel</code> specific level to be added to exception information.
	 * @param serviceSpecificUniqueId Represents the service specific exception ids' (note that every service should have unique exception id's and no collapses between other services must not occur).
	 */
	public void addExceptionLevelBasedUniqueId(ExceptionLevel exceptionLevel, String serviceSpecificUniqueId) {
		if (exceptionLevelBasedUniqueErrorIds.containsKey(exceptionLevel)) {
			Collection<String> uniqueIds = exceptionLevelBasedUniqueErrorIds.get(exceptionLevel);
			if (uniqueIds.contains(serviceSpecificUniqueId)){
				throw new SystemException(EXCEPTION_MESSAGE_SERVICE_SPECIFIC_ID_ALLREADY_EXISTS + serviceSpecificUniqueId);
			}
			uniqueIds.add(serviceSpecificUniqueId);
		} else {
			Collection<String> uniqueIds = Collections.checkedCollection(new ArrayList<String>(), String.class);
			uniqueIds.add(serviceSpecificUniqueId);
			exceptionLevelBasedUniqueErrorIds.put(exceptionLevel, uniqueIds);
		}
	}
	
//	public <T extends Object> void addExceptionLevelBasedUniqueId(T domain, ExceptionLevel exceptionLevel, String serviceSpecificUniqueId) {
//		ExceptionDetails<T> exceptionDetail = new ExceptionDetails<T>(domain);
//		exceptionDetail.addExceptionLevelBasedUniqueId(domain, exceptionLevel, serviceSpecificUniqueId);
//		exceptionDetailsCollection.add(exceptionDetail);
//	}
//	
//	public <T extends Object> void addExceptionDetails(ExceptionDetails<T> exceptionDetails) {
//		exceptionDetailsCollection.add(exceptionDetails);
//	}
	
	/**
	 * Getter for all informative level exception messages. 
	 * 
	 * @return Collection<String> Represents the service specific informative id's for the exception. If none exists null will be returned.
	 */ 
	public Collection<String> getInformativeLevelExceptionIds() {
		if (exceptionLevelBasedUniqueErrorIds.containsKey(ExceptionLevel.INFORMATIVE)) {
			return exceptionLevelBasedUniqueErrorIds.get(ExceptionLevel.INFORMATIVE);
		}
		return null;
	}
	
	/**
	 * Getter for all warning level exception messages. 
	 * 
	 * @return Collection<String> Represents the service specific warning id's for the exception. If none exists null will be returned.
	 */ 
	public Collection<String> getWarningLevelExceptionIds() {
		if (exceptionLevelBasedUniqueErrorIds.containsKey(ExceptionLevel.WARNING)) {
			return exceptionLevelBasedUniqueErrorIds.get(ExceptionLevel.WARNING);
		}
		return null;
	}
	
	/**
	 * Getter for all error level exception messages. 
	 * 
	 * @return Collection<String> Represents the service specific error id's for the exception. If none exists null will be returned.
	 */ 
	public Collection<String> getErrorLevelExceptionIds() {
		if (exceptionLevelBasedUniqueErrorIds.containsKey(ExceptionLevel.ERROR)) {
			return exceptionLevelBasedUniqueErrorIds.get(ExceptionLevel.ERROR);
		}
		return null;
	}
	
	/**
	 * Returns true if informative level exception messages are included.
	 * @return <code>Boolean.TRUE</code> if informative level exception messages are included. Otherwise <code>Boolean.FALSE</code> will be returned.
	 */
	public boolean isInformativeLevelExceptionMessagesIncluded() {
		return isExceptionMessagesIncluded(ExceptionLevel.INFORMATIVE);
	}
	
	/**
	 * Returns true if warning level exception messages are included.
	 * @return <code>Boolean.TRUE</code> if warning level exception messages are included. Otherwise <code>Boolean.FALSE</code> will be returned.
	 */
	public boolean isWarningLevelExceptionMessagesIncluded() {
		return isExceptionMessagesIncluded(ExceptionLevel.WARNING);
	}
	
	/**
	 * Returns true if error level exception messages are included.
	 * @return <code>Boolean.TRUE</code> if error level exception messages are included. Otherwise <code>Boolean.FALSE</code> will be returned.
	 */
	public boolean isErrorLevelExceptionMessagesIncluded() {
		return isExceptionMessagesIncluded(ExceptionLevel.ERROR);
	}
	
	private boolean isExceptionMessagesIncluded(ExceptionLevel exceptionLevel) {
		if (exceptionLevelBasedUniqueErrorIds != null && exceptionLevelBasedUniqueErrorIds.containsKey(exceptionLevel))
			if(exceptionLevelBasedUniqueErrorIds.get(exceptionLevel).size() > 0) 
				return Boolean.TRUE;
		return Boolean.FALSE;
	}

	/**
	 * Each exception inherited from <code>org.openinfinity.core.exception.AbstractCoreException</code> must override addAllExceptionLevelMessages method.
	 */
	public void addAllExceptionLevelMessages(AbstractCoreException abstractCoreException) {
		if (abstractCoreException.isErrorLevelExceptionMessagesIncluded()) {
			for(String exceptionId : abstractCoreException.getErrorLevelExceptionIds()) {
				addExceptionLevelBasedUniqueId(ExceptionLevel.ERROR, exceptionId);
			}
		}
		if (abstractCoreException.isWarningLevelExceptionMessagesIncluded()) {
			for(String exceptionId : abstractCoreException.getWarningLevelExceptionIds()) {
				addExceptionLevelBasedUniqueId(ExceptionLevel.WARNING, exceptionId);
			}
		}
		if (abstractCoreException.isInformativeLevelExceptionMessagesIncluded()) {
			for(String exceptionId : abstractCoreException.getInformativeLevelExceptionIds()) {
				addExceptionLevelBasedUniqueId(ExceptionLevel.INFORMATIVE, exceptionId);
			}
		}
	}

//	public Collection<ExceptionDetails<?>> getExceptionDetails() {
//		return exceptionDetailsCollection;
//	}
//
//	public void addExceptionDetails(ExceptionDetails<?> exceptionDetailsElement) throws SystemException {
//		if (exceptionDetailsCollection.contains(exceptionDetailsElement)) {
//			//ExceptionUtil.throwSystemException("ExceptionDetails allready exists: " + exceptionDetailsElement.toString());
//		}
//		this.exceptionDetailsCollection.add(exceptionDetailsElement);
//	}
//	
//	public <Domain extends Object> ExceptionDetails<Domain> getExceptionDetails(Domain domain) {
//		for (ExceptionDetails<?> exceptionDetailsWithDomain : exceptionDetailsCollection) {
//			if (exceptionDetailsWithDomain.getDomain().equals(domain)) {
//				return (ExceptionDetails<Domain>) exceptionDetailsWithDomain;
//			}
//		}
//		throw new SystemException("There are no domain specific error messages: " + domain.toString());
//	}
//	
//	/**
//	 * Each exception inherited from <code>org.openinfinity.core.exception.AbstractCoreException</code> must override addAllExceptionLevelMessages method.
//	 */
//	public void addAllExceptionLevelMessages(AbstractCoreException abstractCoreException) {
//		Collection<ExceptionDetails<?>> exceptionDetailsCollection = abstractCoreException.getExceptionDetails();
//			
//		
//		for (ExceptionDetails<?> exceptionDetailsWithDomain : exceptionDetailsCollection) 
//			if (exceptionDetailsWithDomain.getDomain().equals(domain)) {
//				return (ExceptionDetails<Domain>) exceptionDetailsWithDomain;
//			}
//		}
//		
		//		if (abstractCoreException.isErrorLevelExceptionMessagesIncluded()) {
//			for(String exceptionId : abstractCoreException.getErrorLevelExceptionIds()) {
//				addExceptionLevelBasedUniqueId(ExceptionLevel.ERROR, exceptionId);
//			}
//		}
//		if (abstractCoreException.isWarningLevelExceptionMessagesIncluded()) {
//			for(String exceptionId : abstractCoreException.getWarningLevelExceptionIds()) {
//				addExceptionLevelBasedUniqueId(ExceptionLevel.WARNING, exceptionId);
//			}
//		}
//		if (abstractCoreException.isInformativeLevelExceptionMessagesIncluded()) {
//			for(String exceptionId : abstractCoreException.getInformativeLevelExceptionIds()) {
//				addExceptionLevelBasedUniqueId(ExceptionLevel.INFORMATIVE, exceptionId);
//			}
//		}
//	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((exceptionLevelBasedUniqueErrorIds == null) ? 0
						: exceptionLevelBasedUniqueErrorIds.hashCode());
		result = prime * result + (logged ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractCoreException other = (AbstractCoreException) obj;
		if (exceptionLevelBasedUniqueErrorIds == null) {
			if (other.exceptionLevelBasedUniqueErrorIds != null)
				return false;
		} else if (!exceptionLevelBasedUniqueErrorIds
				.equals(other.exceptionLevelBasedUniqueErrorIds))
			return false;
		if (logged != other.logged)
			return false;
		return true;
	}

	
	
}