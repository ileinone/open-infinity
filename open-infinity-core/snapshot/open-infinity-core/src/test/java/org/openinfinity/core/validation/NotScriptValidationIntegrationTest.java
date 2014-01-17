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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openinfinity.core.common.IntegrationTest;
import org.openinfinity.core.common.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration test for validator.
 * 
 * @author Ilkka Leinonen
 * @version 1.0.0
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/t-core-integration-test-context.xml")
public class NotScriptValidationIntegrationTest extends AbstractJUnit38SpringContextTests {
	
	@Autowired
	private IntegrationTest integrationTest;

	@Test
	public void givenCorrectAccoutWhenAccessingMethodThenValidationShouldBeBypassed() throws Throwable {
		Account account = new Account("1", "Name1");
		account.setAddress("test");
		boolean expected = true;
		boolean actual = integrationTest.validateMe(account);
		assertEquals(expected, actual);
	}
	
	@Test
	public void givenManipulatedAccoutWhenAccessingMethodThenValidationShouldNotBeBypassed() throws Throwable {
		Account account = new Account("<script>", "</script>");
		account.setAddress(";");
		boolean expected = false;
		boolean actual = integrationTest.validateMe(account);
		assertEquals(expected, actual);
	}
	
}