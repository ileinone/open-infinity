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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.JXPathContext;
import org.aspectj.lang.JoinPoint;
import org.openinfinity.core.exception.SystemException;
import org.openinfinity.core.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

/**
 * Builder class for argument information. Can be used with logging and audit trail.
 * 
 * @author Ilkka Leinonen
 * @version 1.2.0
 * @since 1.2.0
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
	
	private static Map<String, Field> FIELD_CACHE;
	
	static {
		FIELD_CACHE = new HashMap<String, Field>();
	}

	/**
	 * Executes field callbacks on found and defined attribute names.
	 * 
	 * @param argumentGatheringCallback Represents the actual callback method.
	 * @param arguments Represents the arguments for method.
	 * @param allowedFields Represents the allowed argument's field names.
	 */
	public void executeArgumentGatheringCallbackBasedOnDefinedFields(ArgumentGatheringFieldCallback<Field, Object> argumentGatheringCallback, Object[] arguments, String[] allowedFields) {
		for (String allowedField : allowedFields) {
			for (Object object : arguments) {
				try {
					JXPathContext context = JXPathContext.newContext(object);
					Object value = context.getValue(allowedField);
					String argument = object == null ? "null argument" : object.getClass().getName();
					generateKeyValueString(builder, object, value, argument);
					Field field = getField(allowedField, object, argument);
					if (!field.isAccessible()) {
						field.setAccessible(true);
					}
					argumentGatheringCallback.onField(field, object);
				} catch(Throwable throwable) {
					LOGGER.warn(ExceptionUtil.getStackTraceString(throwable));
				}
			}
		}
	}

	/**
	 * Executes field callbacks on all attribute names.
	 * 
	 * @param argumentGatheringCallback Represents the actual callback method.
	 * @param arguments Represents the arguments for method.
	 */
	public void executeArgumentGatheringCallbackOnAllFields(ArgumentGatheringFieldCallback<Field, Object> argumentGatheringCallback, Object[] arguments) {
		doRecursiveFieldLookUpAndCallFieldCallback(argumentGatheringCallback, arguments);
	}
	
	private void doRecursiveFieldLookUpAndCallFieldCallback(final ArgumentGatheringFieldCallback<Field, Object> argumentGatheringCallback, final Object[] objects) {
		for (final Object object : objects) {
			try {
				if (object != null) {
					ReflectionUtils.doWithFields(object.getClass(), new FieldCallback() {
						public void doWith(Field field) {
							try {
								if (!field.isAccessible()) {
									field.setAccessible(Boolean.TRUE);
								}
								if (!(Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()))) {
									argumentGatheringCallback.onField(field, object);						
									LOGGER.debug("Accessing field: " + field.getName());
								}
							} catch (Throwable e) {
								LOGGER.error("Failure occurred while accessing object field.", e);
							} 
						} 
					});
				}
			} catch (Throwable throwable) {
				throw new SystemException(throwable);
			}
		}
	}
	
	private void generateKeyValueString(StringBuilder builder, Object object, Object value, String argument) {
		builder
		.append(object==null ? "null argument" : argument)
		.append("=[")
		.append((value==null ? "null value" : (value)))
		.append("] ");
	}
	
	private Field getField(String allowedField, Object object, String argument) {
		Field field;
		LOGGER.debug(object.getClass().getName() + "." + allowedField);
		field = ReflectionUtils.findField(object.getClass(), allowedField);
		LOGGER.debug("field name: " + field.getName());
		field.setAccessible(true);
		FIELD_CACHE.put(argument, field);
		return field;
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

	/**
	 * Returns builded argument information.
	 * 
	 * @param name
	 * @param argumentInfo
	 * @return
	 */
	public void buildReturnValueDetails(String name, Object returnValue) {
		builder.append(name)
				.append(": ")
				.append((returnValue != null ? returnValue
				: "without any return value (void)"));
	}

	
	/**
	 * Returns builded argument information based on allowed field names presented by using XPATH.
	 * 
	 * @param name
	 * @param argumentInfo
	 * @return
	 */
	public void extractArgumentInfoByFilteringFields(JoinPoint joinPoint, String[] allowedFields) {
		Object[] objects = joinPoint.getArgs();
		if (objects.length > 0) {
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
		} else {
			builder.append("without any parameters");
		}
	}	

	/**
	 * Returns builded argument information.
	 * 
	 * @param name
	 * @param argumentInfo
	 * @return
	 */
	public void extractArgumentInfo(JoinPoint joinPoint) {
		Object[] objects = joinPoint.getArgs();		
		if (objects.length > 0) {		
			for (Object object : objects) {
				builder
				.append(object==null?"null argument":object.getClass().getName())
				.append("=[")
				.append((object==null?"null value":object))
				.append("] ");
			}
		} else {
			builder.append("without any parameters");
		}
	}

	/**
	 * Appends text to the current builder.
	 * 
	 * @param name
	 * @param argumentInfo
	 * @return
	 */
	public ArgumentBuilder append(String text) {
		builder.append(text);
		return this;
	}
	
	/**
	 * Returns builded argument information.
	 * 
	 * @param name
	 * @param argumentInfo
	 * @return
	 */
	public ArgumentBuilder append(long currentTimeMillis) {
		builder.append(currentTimeMillis);
		return this;
	}
	
	@Override
	public String toString() {
		return builder.toString();
	}

}