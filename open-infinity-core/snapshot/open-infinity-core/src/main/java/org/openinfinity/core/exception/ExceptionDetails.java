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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.openinfinity.core.util.StringUtil;

/**
 * Exception details class for unique identification of the exception.
 * 
 * @author Ilkka Leinonen
 * @version 1.0.0
 * @since 1.0.0
 */
public class ExceptionDetails<Domain> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1714007226306127794L;

	/**
	 * Represents the exception level based unique ids for the exception. 
	 */
	private Map<ExceptionLevel, Collection<String>> exceptionLevelBasedUniqueErrorIds = new HashMap<ExceptionLevel, Collection<String>>();
	
	private Domain domain;
	
	public ExceptionDetails(Domain domain) {
		this.domain = domain;
	}
	
	public Domain getDomain() {
		return domain;
	}

	/**
	 * Exception message if service specific id allready exists.
	 */
	private static final String EXCEPTION_MESSAGE_SERVICE_SPECIFIC_ID_ALLREADY_EXISTS = "Service specific id allready exists: ";
	
	/**
	 * Adds exception level specific exception id to thrown exception.
	 * 
	 * @param exceptionLevel Represents the <code>org.openinfinity.core.exception.ExceptionLevel</code> specific level to be added to exception information.
	 * @param serviceSpecificUniqueId Represents the service specific exception ids' (note that every service should have unique exception id's and no collapses between other services must not occur).
	 */
	public void addExceptionLevelBasedUniqueId(Domain domain, ExceptionLevel exceptionLevel, String serviceSpecificUniqueId) {
		if (exceptionLevelBasedUniqueErrorIds.containsKey(exceptionLevel)) {
			Collection<String> uniqueIds = exceptionLevelBasedUniqueErrorIds.get(exceptionLevel);
			if(uniqueIds.contains(serviceSpecificUniqueId)){
				throw new SystemException(EXCEPTION_MESSAGE_SERVICE_SPECIFIC_ID_ALLREADY_EXISTS + serviceSpecificUniqueId);
			}
			uniqueIds.add(serviceSpecificUniqueId);
		} else {
			Collection<String> uniqueIds = Collections.checkedCollection(new ArrayList<String>(), String.class);
			uniqueIds.add(serviceSpecificUniqueId);
			exceptionLevelBasedUniqueErrorIds.put(exceptionLevel, uniqueIds);
		}
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
			if(uniqueIds.contains(serviceSpecificUniqueId)){
				throw new SystemException(EXCEPTION_MESSAGE_SERVICE_SPECIFIC_ID_ALLREADY_EXISTS + serviceSpecificUniqueId);
			}
			uniqueIds.add(serviceSpecificUniqueId);
		} else {
			Collection<String> uniqueIds = Collections.checkedCollection(new ArrayList<String>(), String.class);
			uniqueIds.add(serviceSpecificUniqueId);
			exceptionLevelBasedUniqueErrorIds.put(exceptionLevel, uniqueIds);
		}
	}
	
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

	@Override
	public String toString() {
		return StringUtil.toString(this);
	}
	
}