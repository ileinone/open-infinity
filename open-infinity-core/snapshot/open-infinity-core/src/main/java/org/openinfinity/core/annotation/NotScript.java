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
package org.openinfinity.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.openinfinity.core.validation.NotScriptValidation;

/**
 * Field validator for validating XSS attacks. 
 * 
 * @author Ilkka Leinonen
 * @version 1.0.0
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Constraint(validatedBy = NotScriptValidation.class)
public @interface NotScript {
	
	/**
	 * Default XSS script pattern: [^<>;]*"
	 */
	public static final String DEFAULT_INPUT_PATTERN = "[^<>;]*";
	
	/**
	 * Allowed input pattern for validating fields. Uses DEFAULT_INPUT_PATTERN as a default pattern.
	 * 
	 * @return String - Regular expression pattern for fields against XSS attacks.
	 */
	public String allowedInputPattern() default DEFAULT_INPUT_PATTERN;
	
	public String message() default "{notscript.validation.message}";
	
	public Class<?>[] groups() default {};
	
	public Class<? extends Payload>[] payload() default {};
	
}