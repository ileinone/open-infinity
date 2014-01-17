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

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for core exceptions.
 * 
 * @author Ilkka Leinonen
 * @version 1.0.0
 * @since 1.0.0
 */
public class AbstractCoreExceptionUnitTest {
	
	private static final String UNIQUE_ID_EXCEPTION = "Unique id of the exception";
	private ApplicationException applicationException;
	private SystemException systemException;
	private BusinessViolationException businessViolationException;
	
	@Before
	public void setUp() {
		this.applicationException = new ApplicationException();
		this.businessViolationException = new BusinessViolationException();
		this.systemException = new SystemException();
	}
	
	@Test
	public void givenApplicationExceptionLevelAndUniqueIdWhenThrowingExceptionThenExceptionMustContainExceptionLevelBasedErrorMessages() {
		String actualErrorMessage = null;
		String expectedErrorMessage = UNIQUE_ID_EXCEPTION;
		int expectedSizeOfTheInformativeLevelMessages = 1;
		applicationException.addExceptionLevelBasedUniqueId(ExceptionLevel.INFORMATIVE, UNIQUE_ID_EXCEPTION);
		applicationException.addExceptionLevelBasedUniqueId(ExceptionLevel.WARNING, UNIQUE_ID_EXCEPTION);
		applicationException.addExceptionLevelBasedUniqueId(ExceptionLevel.ERROR, UNIQUE_ID_EXCEPTION);
		Collection<String> errorMessages = applicationException.getErrorLevelExceptionIds();
		for(String errorMessage : errorMessages) actualErrorMessage = errorMessage;
		int actualSizeOfTheInformativeLevelMessages = errorMessages.size();
		assertEquals(expectedErrorMessage, actualErrorMessage);
		assertEquals(expectedSizeOfTheInformativeLevelMessages, actualSizeOfTheInformativeLevelMessages);
		try {
			applicationException.addExceptionLevelBasedUniqueId(ExceptionLevel.INFORMATIVE, UNIQUE_ID_EXCEPTION);
		} catch(Throwable throwable) {
			assertEquals(SystemException.class, throwable.getClass());
		}
	}
	
	@Test
	public void givenSystemExceptionLevelAndUniqueIdWhenThrowingExceptionThenExceptionMustContainExceptionLevelBasedErrorMessages() {
		String actualErrorMessage = null;
		String expectedErrorMessage = UNIQUE_ID_EXCEPTION;
		int expectedSizeOfTheInformativeLevelMessages = 1;
		systemException.addExceptionLevelBasedUniqueId(ExceptionLevel.INFORMATIVE, UNIQUE_ID_EXCEPTION);
		systemException.addExceptionLevelBasedUniqueId(ExceptionLevel.WARNING, UNIQUE_ID_EXCEPTION);
		systemException.addExceptionLevelBasedUniqueId(ExceptionLevel.ERROR, UNIQUE_ID_EXCEPTION);
		Collection<String> errorMessages = systemException.getErrorLevelExceptionIds();
		for(String errorMessage : errorMessages) actualErrorMessage = errorMessage;
		int actualSizeOfTheInformativeLevelMessages = errorMessages.size();
		assertEquals(expectedErrorMessage, actualErrorMessage);
		assertEquals(expectedSizeOfTheInformativeLevelMessages, actualSizeOfTheInformativeLevelMessages);
		try {
			systemException.addExceptionLevelBasedUniqueId(ExceptionLevel.INFORMATIVE, UNIQUE_ID_EXCEPTION);
		} catch(Throwable throwable) {
			assertEquals(SystemException.class, throwable.getClass());
		}
	}
	
	@Test
	public void givenBusinessViolationExceptionLevelAndUniqueIdWhenThrowingExceptionThenExceptionMustContainExceptionLevelBasedErrorMessages() {
		String actualErrorMessage = null;
		String expectedErrorMessage = UNIQUE_ID_EXCEPTION;
		int expectedSizeOfTheInformativeLevelMessages = 1;
		businessViolationException.addExceptionLevelBasedUniqueId(ExceptionLevel.INFORMATIVE, UNIQUE_ID_EXCEPTION);
		businessViolationException.addExceptionLevelBasedUniqueId(ExceptionLevel.WARNING, UNIQUE_ID_EXCEPTION);
		businessViolationException.addExceptionLevelBasedUniqueId(ExceptionLevel.ERROR, UNIQUE_ID_EXCEPTION);
		Collection<String> errorMessages = businessViolationException.getErrorLevelExceptionIds();
		for(String errorMessage : errorMessages) actualErrorMessage = errorMessage;
		int actualSizeOfTheInformativeLevelMessages = errorMessages.size();
		assertEquals(expectedErrorMessage, actualErrorMessage);
		assertEquals(expectedSizeOfTheInformativeLevelMessages, actualSizeOfTheInformativeLevelMessages);
		try {
			businessViolationException.addExceptionLevelBasedUniqueId(ExceptionLevel.INFORMATIVE, UNIQUE_ID_EXCEPTION);
		} catch(Throwable throwable) {
			assertEquals(SystemException.class, throwable.getClass());
		}
	}
	
	@Test
	public void givenLogStateWhenWritingToLogThenStateOfTheLoggingMustBeSet() {
		Boolean expected = Boolean.TRUE;
		applicationException.setLogged(expected);
		businessViolationException.setLogged(expected);
		systemException.setLogged(expected);
		Boolean actualApplicationExceptionLogInformation = applicationException.isLogged();
		Boolean actualBusinessViolationExceptionLogInformation = applicationException.isLogged();
		Boolean actualSystemExceptionLogInformation = applicationException.isLogged();
		assertEquals(expected, actualApplicationExceptionLogInformation);
		assertEquals(expected, actualBusinessViolationExceptionLogInformation);
		assertEquals(expected, actualSystemExceptionLogInformation);
	}
	
	@Test
	public void givenApplicationExceptionWhenVerifyingErrorLevelBasedStatusThenErrorsMustKnowErrorLevelBasedStatus() {
		boolean expectedInformativeLevelMessages = Boolean.TRUE;
		boolean expectedWarningLevelMessages = Boolean.TRUE;
		boolean expectedErrorLevelMessages = Boolean.TRUE;
		applicationException.addExceptionLevelBasedUniqueId(ExceptionLevel.INFORMATIVE, UNIQUE_ID_EXCEPTION);
		applicationException.addExceptionLevelBasedUniqueId(ExceptionLevel.WARNING, UNIQUE_ID_EXCEPTION);
		applicationException.addExceptionLevelBasedUniqueId(ExceptionLevel.ERROR, UNIQUE_ID_EXCEPTION);
		boolean actualInformativeLevelMessages = applicationException.isInformativeLevelExceptionMessagesIncluded();
		boolean actualWarningLevelMessages = applicationException.isWarningLevelExceptionMessagesIncluded();
		boolean actualErrorLevelMessages = applicationException.isErrorLevelExceptionMessagesIncluded();
		assertEquals(expectedWarningLevelMessages, actualWarningLevelMessages);
		assertEquals(expectedInformativeLevelMessages, actualInformativeLevelMessages);
		assertEquals(expectedErrorLevelMessages, actualErrorLevelMessages);
	}
	
	@Test
	public void givenSystemExceptionWhenVerifyingErrorLevelBasedStatusThenErrorsMustKnowErrorLevelBasedStatus() {
		boolean expectedInformativeLevelMessages = Boolean.TRUE;
		boolean expectedWarningLevelMessages = Boolean.TRUE;
		boolean expectedErrorLevelMessages = Boolean.TRUE;
		systemException.addExceptionLevelBasedUniqueId(ExceptionLevel.INFORMATIVE, UNIQUE_ID_EXCEPTION);
		systemException.addExceptionLevelBasedUniqueId(ExceptionLevel.WARNING, UNIQUE_ID_EXCEPTION);
		systemException.addExceptionLevelBasedUniqueId(ExceptionLevel.ERROR, UNIQUE_ID_EXCEPTION);
		boolean actualInformativeLevelMessages = systemException.isInformativeLevelExceptionMessagesIncluded();
		boolean actualWarningLevelMessages = systemException.isWarningLevelExceptionMessagesIncluded();
		boolean actualErrorLevelMessages = systemException.isErrorLevelExceptionMessagesIncluded();
		assertEquals(expectedWarningLevelMessages, actualWarningLevelMessages);
		assertEquals(expectedInformativeLevelMessages, actualInformativeLevelMessages);
		assertEquals(expectedErrorLevelMessages, actualErrorLevelMessages);
	}
	
	@Test
	public void givenBusinessViolationExceptionWhenVerifyingErrorLevelBasedStatusThenErrorsMustKnowErrorLevelBasedStatus() {
		boolean expectedInformativeLevelMessages = Boolean.TRUE;
		boolean expectedWarningLevelMessages = Boolean.TRUE;
		boolean expectedErrorLevelMessages = Boolean.TRUE;
		businessViolationException.addExceptionLevelBasedUniqueId(ExceptionLevel.INFORMATIVE, UNIQUE_ID_EXCEPTION);
		businessViolationException.addExceptionLevelBasedUniqueId(ExceptionLevel.WARNING, UNIQUE_ID_EXCEPTION);
		businessViolationException.addExceptionLevelBasedUniqueId(ExceptionLevel.ERROR, UNIQUE_ID_EXCEPTION);
		boolean actualWarningLevelMessages = businessViolationException.isWarningLevelExceptionMessagesIncluded();
		boolean actualInformativeLevelMessages = businessViolationException.isInformativeLevelExceptionMessagesIncluded();
		boolean actualErrorLevelMessages = businessViolationException.isErrorLevelExceptionMessagesIncluded();
		assertEquals(expectedWarningLevelMessages, actualWarningLevelMessages);
		assertEquals(expectedInformativeLevelMessages, actualInformativeLevelMessages);
		assertEquals(expectedErrorLevelMessages, actualErrorLevelMessages);
	}
	 
	@Test
	public void givenApplicationExceptionWhenAddingAllErrorLevelBasedMessagesThenErrorsMustBeMappedToCorrectLevels() {
		try {
			applicationException.addAllExceptionLevelMessages(systemException);
		} catch(Throwable actual) {
			assertEquals(SystemException.class, actual.getClass());
		}
		ApplicationException applicationException2 = new ApplicationException();
		applicationException2.addExceptionLevelBasedUniqueId(ExceptionLevel.ERROR, "test");
		applicationException2.addExceptionLevelBasedUniqueId(ExceptionLevel.ERROR, "test2");
		applicationException.addExceptionLevelBasedUniqueId(ExceptionLevel.ERROR, UNIQUE_ID_EXCEPTION);
		applicationException.addAllExceptionLevelMessages(applicationException2);
		assertEquals(Boolean.TRUE, applicationException.isErrorLevelExceptionMessagesIncluded());
		assertEquals(applicationException.getErrorLevelExceptionIds().size(), 3);
	}
	
	@Test
	public void givenSystemExceptionWhenAddingAllErrorLevelBasedMessagesThenErrorsMustBeMappedToCorrectLevels() {
		try {
			systemException.addAllExceptionLevelMessages(businessViolationException);
		} catch(Throwable actual) {
			assertEquals(SystemException.class, actual.getClass());
		}
		SystemException systemException2 = new SystemException();
		systemException2.addExceptionLevelBasedUniqueId(ExceptionLevel.ERROR, "test");
		systemException2.addExceptionLevelBasedUniqueId(ExceptionLevel.ERROR, "test2");
		systemException.addExceptionLevelBasedUniqueId(ExceptionLevel.ERROR, UNIQUE_ID_EXCEPTION);
		systemException.addAllExceptionLevelMessages(systemException2);
		assertEquals(Boolean.TRUE, systemException.isErrorLevelExceptionMessagesIncluded());
		assertEquals(systemException.getErrorLevelExceptionIds().size(), 3);
	}
	
	@Test
	public void givenBusinessViolationExceptionWhenAddingAllErrorLevelBasedMessagesThenErrorsMustBeMappedToCorrectLevels() {
		try {
			businessViolationException.addAllExceptionLevelMessages(applicationException);
		} catch(Throwable actual) {
			assertEquals(SystemException.class, actual.getClass());
		}
		BusinessViolationException businessViolationException2 = new BusinessViolationException();
		businessViolationException2.addExceptionLevelBasedUniqueId(ExceptionLevel.ERROR, "test");
		businessViolationException2.addExceptionLevelBasedUniqueId(ExceptionLevel.ERROR, "test2");
		businessViolationException.addExceptionLevelBasedUniqueId(ExceptionLevel.ERROR, UNIQUE_ID_EXCEPTION);
		businessViolationException.addAllExceptionLevelMessages(businessViolationException2);
		assertEquals(Boolean.TRUE, businessViolationException.isErrorLevelExceptionMessagesIncluded());
		assertEquals(businessViolationException.getErrorLevelExceptionIds().size(), 3);
	}
	
	@After
	public void tearDown() {
		applicationException = null;
		systemException = null;
		businessViolationException = null;
	}

}
