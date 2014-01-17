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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openinfinity.core.common.IntegrationTest;
import org.openinfinity.core.common.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration test class for log aspect.
 * 
 * @author Ilkka Leinonen
 * @version 1.1.0
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/t-core-integration-test-context.xml")
public class LogInterceptorIntegrationTest extends AbstractJUnit4SpringContextTests {

	private File file;
	private File exceptionLogFile;

	@Autowired
	private IntegrationTest integrationTest;

	@Before
	public void setUp() {
		file = createFileIfNotExisting(file, "target/logging.log");
		exceptionLogFile = createFileIfNotExisting(exceptionLogFile, "target/exception.log");
	}

	private File createFileIfNotExisting(File file, String fileName) {
		file = new File(fileName);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	@Test
	public void givenMethodWhenAccessingThenExceptionMustNotBeThrown() {
		try {
			this.integrationTest.log();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void givenMethodAndArgumentsWhenAccessingThenExceptionMustNotBeThrownAndNotLoggingTheArguments() {
		try {
			this.integrationTest.logMeWithNoArguments("foo");
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void givenObjectAsAnArgumentWhenAccessingMethodThenAllTheParametersOfTheObjectMustBeLogged() {
		try {
			this.integrationTest.logMe("Logging you!", new Long(23423));
			boolean actualString = readLogFileContent(file).contains("Logging you!");
			boolean actualLong = readLogFileContent(file).contains("23423");
			assertTrue(actualString);
			assertTrue(actualLong);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void givenObjectAsAnArgumentAndSpecifiedFieldsWhenAccessingMethodThenAllTheParametersOfTheObjectMustBeLogged() {
		try {
			Account account = new Account("2", "Name3");
			this.integrationTest.logMeWithSpecifiedArguments(account);
			boolean actual = readLogFileContent(file).contains("Name3");
			assertTrue(actual);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void givenObjectAsAnArgumentAndSpecifiedFieldsWhenAccessingMethodThenAllTheParametersOfTheObjectMustBeLoggedExceptFalseOnes() {
		try {
			boolean expected = Boolean.TRUE;
			Account account = new Account("2", "Name3");
			this.integrationTest.logMeWithFalseArguments(account);
			boolean actual = readLogFileContent(file).contains("Name3");
			assertEquals(expected, actual);
			boolean expectedFalseFieldMessageExists = readLogFileContent(exceptionLogFile).contains("No value for xpath");
			assertTrue(expectedFalseFieldMessageExists);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			fail();
		}
	}

	public String readLogFileContent(File file) throws Throwable {
		return FileUtils.readFileToString(file, "UTF-8");	
	}

	@After
	public void tearDown() {
		file = null;
	}

}