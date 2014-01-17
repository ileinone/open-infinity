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

import org.apache.commons.jxpath.JXPathContext;
import org.aspectj.lang.JoinPoint;
import org.openinfinity.core.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for gathering argument information from the called methods through aspects.
 * 
 * @author Ilkka Leinonen
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class ArgumentGatheringJoinPointInterceptor {
	
	/**
	 * Logger for this class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ArgumentGatheringJoinPointInterceptor.class);
	
	protected String buildArgumentDetails(String name, String argumentInfo) {
		StringBuilder builder = new StringBuilder();
		builder.append(name)
				.append(": ")
				.append((argumentInfo.length() > 0 ? argumentInfo
				: "without any parameters"));
		return builder.toString();
	}

	protected String buildReturnValueDetails(String name, Object returnValue) {
		StringBuilder builder = new StringBuilder();
		builder.append(name)
				.append(": ")
				.append((returnValue != null ? returnValue
				: "without any return value (void)"));
		return builder.toString();
	}

	protected String getArgumentInfoByFilteringFields(JoinPoint joinPoint, String[] allowedFields) {
		StringBuilder builder = new StringBuilder();
		Object[] objects = joinPoint.getArgs();
		if (allowedFields == null) {
			buildAllArgumentInformation(builder, objects); 
		} else {
			buildFieldBasedArgumentInformation(allowedFields, builder, objects);
		}
		String argumentInfo = builder.toString();
		return argumentInfo.trim();
	}	

	public String getArgumentInfo(JoinPoint joinPoint) {
		return getArgumentInfoByFilteringFields(joinPoint, null);
	}
	
	private void buildFieldBasedArgumentInformation(String[] allowedFields, StringBuilder builder, Object[] objects) {
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
	}

	private void buildAllArgumentInformation(StringBuilder builder, Object[] objects) {
		for (Object object : objects) {
			builder
			.append(object==null?"null argument":object.getClass().getName())
			.append("=[")
			.append((object==null?"null value":object))
			.append("] ");
		}
	}

}