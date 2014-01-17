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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.openinfinity.core.exception.SystemException;
import org.springframework.aop.framework.AopConfigException;

/**
 * Utility for handling aspect specific features.
 * 
 * @author Ilkka Leinonen
 * @version 1.0.0
 * @since 1.0.0
 */
public class AspectUtil {
	
	/**
	 * Returns the specified annotation.
	 * 
	 * @param joinPoint Represents the join point.
	 * @return 
	 * @return LogLevel representing the log level.
	 */
	public static <T extends Annotation> T getAnnotation(JoinPoint joinPoint, Class<T> requiredAnnotationClass) {
		Method[] methods = joinPoint.getTarget().getClass().getMethods();
		for (Method method : methods) {
			if (method.getName().equals(joinPoint.getSignature().getName())) {
				Annotation[] annotations = method.getDeclaredAnnotations();
				for(Annotation annotation : annotations) {				
					if (requiredAnnotationClass.isAssignableFrom(annotation.getClass())) {
						return (T)annotation;
					}	
				}
			}
		}
		throw new SystemException(new AopConfigException("Annotation not found."));
	}
	
	public static String createJoinPointTraceName(JoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		StringBuilder sb = new StringBuilder();
		sb.append(signature.getDeclaringType().getSimpleName());
		sb.append('.').append(signature.getName());
		return sb.toString();
	}


}