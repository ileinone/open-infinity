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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.openinfinity.common.infrastructure.AnnotatedEmbedderUsingSpring;
import org.openinfinity.core.common.UnknownException;
import org.openinfinity.core.exception.ApplicationException;
import org.openinfinity.core.exception.BusinessViolationException;
import org.openinfinity.core.exception.SystemException;
import org.springframework.stereotype.Component;

/**
 * Functional test class for exception aspect.
 * 
 * @author Ilkka Leinonen
 * @version 1.0.0
 * @since 1.0.0
 */
@Component("exceptionTranslationBehaviour")
public class ExceptionTranslationBehaviour extends AnnotatedEmbedderUsingSpring {

	private static final String EXCEPTION_MESSAGE = "Exception behaviour.";
	private ExceptionTranslatorAspect exceptionTranslator;
	private Throwable actual;

	@BeforeScenario
	public void setupScenario() {
		actual = null;
	}
	
	@Given("an aspect to be called")
	public void aspectForExceptionTranslation() {
		exceptionTranslator = new ExceptionTranslatorAspect();
	}
	
	@When("executing business operation and application exception occurs")
	public void businessOperationHasBeenCalledAndApplicationExceptionOccurs() {
		try {
			exceptionTranslator.translateServiceException(new ApplicationException(EXCEPTION_MESSAGE));
		} catch (Throwable throwable) {
			assertNotNull(throwable);
			actual = throwable;
		}
	}
	
	@When("executing business operation and system exception occurs")
	public void businessOperationHasBeenCalledAndSystemExceptionOccurs() {
		try {
			exceptionTranslator.translateServiceException(new SystemException(EXCEPTION_MESSAGE));
		} catch (Throwable throwable) {
			assertNotNull(throwable);
			actual = throwable;
		}
	}
	
	@When("executing business operation and business violation exception occurs")
	public void businessOperationHasBeenCalledAndBusinessViolationExceptionOccurs() {
		try {
			exceptionTranslator.translateServiceException(new BusinessViolationException(EXCEPTION_MESSAGE));
		} catch (Throwable throwable) {
			assertNotNull(throwable);
			actual = throwable;
		}
	}
	
	@When("executing business operation and unknown exception occurs")
	public void businessOperationHasBeenCalledAndUnknownExceptionOccurs() {
		try {
			exceptionTranslator.translateServiceException(new UnknownException(EXCEPTION_MESSAGE));
		} catch (Throwable throwable) {
			assertNotNull(throwable);
			actual = throwable;
		}
	}

	@Then("exception should be bypassed by the aspect and must be of type %expected")
	public void knownExceptionsMustBeByPassed(@Named(value="expected") String expected) {
		assertNotNull(actual);
		assertNotNull(expected);
		assertEquals(expected, actual.getClass().getName());
	}
	
	@Then("exception should be converted by the aspect and must be of type %expected")
	public void unknownExceptionsMustBeConvertedToSystemException(@Named(value="expected") String expected) {
		assertNotNull(actual);
		assertNotNull(expected);
		assertEquals(expected, actual.getClass().getName());
	}

	@Override
	protected String getStoryName() {
		return "exception_translation.story";
	}
	
}
