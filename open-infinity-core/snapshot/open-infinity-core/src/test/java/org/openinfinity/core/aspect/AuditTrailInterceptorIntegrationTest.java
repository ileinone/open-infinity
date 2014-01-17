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
 * Integration test for audit trail.
 * 
 * @author Ilkka Leinonen
 * @version 1.0.0
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/t-core-integration-test-context.xml")
public class AuditTrailInterceptorIntegrationTest extends AbstractJUnit4SpringContextTests {

	private File auditTrailFile;
	private File warningFile; 
	
	@Autowired
	private IntegrationTest integrationTest;

	@Before
	public void setUp() {
		auditTrailFile = new File("target/audittrail.log");
		warningFile = new File("target/exception.log");		
		createFile(auditTrailFile);
		createFile(warningFile);
	}

	private void createFile(File file) {
		if(!auditTrailFile.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void givenKnownArgumentsWhenAccessingMethodThenAllTheArgumentsMustBeIncludidInAuditTrail() throws Throwable {
		Account account = new Account("1", "Name1");
		integrationTest.auditMe(account);
		integrationTest.auditMeWithAllArguments(account);
		String actual = readLogFileContent(auditTrailFile);
		assertEquals(true, actual.contains("Name1"));
	}

	@Test
	public void givenKnownArgumentsWhenAccessingMethodThenAllArgumentsMustNotBePassedToAuditTrail() throws Throwable {
		Account account = new Account("2", "Name3");
		integrationTest.auditMeWithNoArguments(account);
		String actual = readLogFileContent(auditTrailFile);
		assertEquals(false, actual.contains("Name3"));
	}
	
	@Test
	public void givenKnownArgumentWhenAccessingMethodThenKnownArgumentMustBePassedToAuditTrail() throws Throwable {
		String expected = "testId13131313122";
		integrationTest.auditMeWithPrimitiveFields(expected);
		String actual = readLogFileContent(auditTrailFile);
		assertEquals(true, actual.contains(expected));
	}

	@Test
	public void givenKnownArgumentsWhenAccessingMethodTheFalseFieldsMustBeInformedInAuditTrail() throws Throwable {
		Account account = new Account("<script>", "Name2");
		integrationTest.auditMeWithFalseField(account);
		String actual = readLogFileContent(warningFile);
		assertEquals(true, actual.contains("No value for xpath"));
	}
	
	public String readLogFileContent(File file) throws Throwable {
		return FileUtils.readFileToString(file, "UTF-8");
	}
	
	@After
	public void tearDown() {
		auditTrailFile = null;
		warningFile = null;
	}

}