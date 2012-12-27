/*
 * Copyright (c) 2011 the original author or authors.
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

import org.apache.commons.jxpath.JXPathContext;
import org.aspectj.lang.JoinPoint;
import org.openinfinity.core.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builder class for argument information. Can be used with logging and audit trailing.
 * @author Ilkka Leinonen
 * @version 1.0.0
 * @since 1.0.0
 */
public class ArgumentBuilder {
	/**
	 * Logger for this class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ArgumentBuilder.class);
	
	
	private StringBuilder builder;
	
	/**
	 * Creates new argument builder.
	 */
	public ArgumentBuilder() {
		this.builder = new StringBuilder();
	}

	/**
	 * Returns builded argument information.
	 * 
	 * @param name
	 * @param argumentInfo
	 * @return
	 */
	public String buildArgumentDetails(String name, String argumentInfo) {
		StringBuilder builder = new StringBuilder();
		builder.append(name)
				.append(": ")
				.append((argumentInfo.length() > 0 ? argumentInfo
				: "without any parameters"));
		return builder.toString();
	}

	public void buildReturnValueDetails(String name, Object returnValue) {
		builder.append(name)
				.append(": ")
				.append((returnValue != null ? returnValue
				: "without any return value (void)"));
	}

	public void extractArgumentInfoByFilteringFields(JoinPoint joinPoint, String[] allowedFields) {
		builder.append(" Argument information: [");
		Object[] objects = joinPoint.getArgs();
		for (String allowedField : allowedFields) {
			for (Object object : objects) {
				try {
					JXPathContext context = JXPathContext.newContext(object);
					Object value = context.getValue(allowedField);
					builder
					.append(object==null?"null argument":object.getClass().getName()+"."+allowedField)
					.append("=[")
					.append((value==null?"null value":(value)))
					.append("] ");
				} catch(Throwable throwable) {
					LOGGER.warn(ExceptionUtil.getStackTraceString(throwable));
				}
			}
		}
		builder.append("]");
	}	

	public void extractArgumentInfo(JoinPoint joinPoint) {
		builder.append(" Argument information: [");
		Object[] objects = joinPoint.getArgs();		
		for (Object object : objects) {
			builder
			.append(object==null?"null argument":object.getClass().getName())
			.append("=[")
			.append((object==null?"null value":object))
			.append("] ");
		}
		builder.append("]");
	}

	public ArgumentBuilder append(String text) {
		builder.append(text);
		return this;
	}
	
	public ArgumentBuilder append(long currentTimeMillis) {
		builder.append(currentTimeMillis);
		return this;
	}
	
	@Override
	public String toString() {
		return builder.toString();
	}	
	
}