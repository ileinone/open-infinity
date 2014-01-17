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
package org.openinfinity.core.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.openinfinity.core.annotation.NotScript;

/**
 * Validator for form manipulation attempts like XSS attacks.
 * 
 * @author Ilkka Leinonen
 * @Version 1.0.0
 * @Since 1.0.0
 */
public class NotScriptValidation implements ConstraintValidator<NotScript, String> {

	private Pattern notScriptPattern = Pattern.compile(NotScript.DEFAULT_INPUT_PATTERN);

	public void initialize(NotScript notScript) {
		if(!(notScript.allowedInputPattern().equals(NotScript.DEFAULT_INPUT_PATTERN))) {
			notScriptPattern = Pattern.compile(notScript.allowedInputPattern());			
		}
	}

	/**
	 * Challenges the class field against defined pattern.
	 */
	public boolean isValid(String value, ConstraintValidatorContext context) {
		boolean isValid = notScriptPattern.matcher(value).matches();
		if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{notscript.validation.message}").addConstraintViolation();
        }
		return isValid;
	}

}