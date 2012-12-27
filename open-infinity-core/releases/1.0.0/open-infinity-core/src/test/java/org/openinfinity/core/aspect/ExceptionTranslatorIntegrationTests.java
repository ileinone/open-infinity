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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.ExpectedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.openinfinity.core.common.IntegrationTest;
import org.openinfinity.core.exception.ApplicationException;
import org.openinfinity.core.exception.BusinessViolationException;
import org.openinfinity.core.exception.SystemException;

/**
 * Integration test for exception translator aspect.
 * 
 * @author Ilkka Leinonen
 * @version 1.0.0
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:META-INF/spring/t-core-integration-test-context.xml")
public class ExceptionTranslatorIntegrationTests extends AbstractJUnit38SpringContextTests {
	
	@Autowired
	private IntegrationTest integrationTest;
	
	@Test
	@ExpectedException(BusinessViolationException.class)
	public void whenThrowingBusinessViolationExceptionAspectMustByPassIt() {
		integrationTest.throwBusinessViolationException();
	}
	
	@Test
	@ExpectedException(SystemException.class)
	public void whenThrowingSystemExceptionAspectMustByPassIt() {
		integrationTest.throwSystemException();
	}
	
	@Test
	@ExpectedException(ApplicationException.class)
	public void whenThrowingApplicationExceptionAspectMustByPassIt() {
		integrationTest.throwApplicationException();
	}
	
	@Test
	@ExpectedException(SystemException.class)
	public void whenThrowingUnknownExceptionAspectMustTranslateItToKnownException() {
		integrationTest.throwUnknownException();
	}
		
}