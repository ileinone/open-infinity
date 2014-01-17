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

/**
 * This enumeration represents the exception level thrown by the service layer.
 * 
 * @author Ilkka Leinonen 
 * @version 1.0.0
 * @since 1.0.0
 */
public enum ExceptionLevel {
	/**
	 * Represents the informative level exception.
	 */
	INFORMATIVE ("informative"),
	/**
	 * Represents the warning level exception.
	 */
	WARNING ("warning"),
	/**
	 * Represents the error level exception.
	 */
	ERROR ("error");
	
	private String exceptionLevel;
	
	private ExceptionLevel(String exceptionLevel) {
		this.exceptionLevel = exceptionLevel;
	}

	/**
	 * Returns level as <code>java.lang.String</code>.
	 */
	public String toString() {
		return this.exceptionLevel;
	}
	
}