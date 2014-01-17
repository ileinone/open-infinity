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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openinfinity.core.common.IntegrationTest;
import org.openinfinity.core.common.domain.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration test for cipher aspect behaviour.
 * 
 * @author Ilkka Leinonen
 * @version 1.0.0
 * @since 1.3.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/t-core-integration-test-context.xml")
public class CryptoAspectIntegrationTest extends AbstractJUnit4SpringContextTests {

	private static final Logger LOGGER = LoggerFactory.getLogger(CryptoAspectIntegrationTest.class);
	
	private Account EXPECTED_ACCOUNT;
	
	public static String EXPECTED_ID = "Open";
	public static String EXPECTED_NAME = "Infinity";
	public static String EXPECTED_ADDRESS = "Rocks";
	
	@Autowired
	private IntegrationTest integrationTest;

	@Before
	public void setUp() {
		EXPECTED_ACCOUNT = new Account(EXPECTED_ID, EXPECTED_NAME);
		EXPECTED_ACCOUNT.setAddress(EXPECTED_ADDRESS);
	}

	@Test
	public void givenKnownPopulatedObjectWhenCallingEncryptAnnotatedMethodThenAllOfTheObjectAttributesMustBeEncrypted() {
		long startTime = System.currentTimeMillis();
		Account expected = integrationTest.encryptMe(EXPECTED_ACCOUNT);
		LOGGER.debug("Encryption took: " + (System.currentTimeMillis()-startTime) + " ms");
		assertFalse(expected.getId().equals(EXPECTED_ID));
		LOGGER.debug("Expected: "+ expected);
		assertFalse(expected.getName().equals(EXPECTED_NAME));
		assertFalse(expected.getAddress().equals(EXPECTED_ADDRESS));
	}
	
	@Test 
	public void givenKnownEncryptedObjectWhenCallingDecryptAnnotatedMethodThenAllOfTheObjectAttributesMustBePlainText() {
		long startTime = System.currentTimeMillis();
		Account expected = integrationTest.encryptMe(EXPECTED_ACCOUNT);
		LOGGER.debug("Decryption took: " + (System.currentTimeMillis()-startTime) + " ms");
		LOGGER.debug("Expected: " + expected);
		expected = integrationTest.decryptMe(expected);
		assertTrue(expected.getId().equals(EXPECTED_ID));
		assertTrue(expected.getName().equals(EXPECTED_NAME));
		assertTrue(expected.getAddress().equals(EXPECTED_ADDRESS));
	}
	
	@Test
	public void givenKnownPopulatedAttributesOfObjectWhenCallingEncryptAnnotatedMethodThenAllOfTheObjectAttributesMustBeEncrypted() {
		long startTime = System.currentTimeMillis();
		Account expected = integrationTest.encryptMeWithSpecifiedAttributes(EXPECTED_ACCOUNT);
		LOGGER.debug("Encryption took: " + (System.currentTimeMillis()-startTime) + " ms");
		assertTrue(expected.getId().equals(EXPECTED_ID));
		assertFalse(expected.getName().equals(EXPECTED_NAME));
		assertFalse(expected.getAddress().equals(EXPECTED_ADDRESS));
	}
	
	@Test
	public void givenKnownEncryptedAttributesOfObjectWhenCallingDecryptAnnotatedMethodThenAllOfTheObjectAttributesMustBePlainText() {
		long startTime = System.currentTimeMillis();
		Account encryptedAccount = integrationTest.encryptMeWithSpecifiedAttributes(EXPECTED_ACCOUNT);
		LOGGER.debug("Encryption took: " + (System.currentTimeMillis()-startTime) + " ms");
		Account expected = integrationTest.decryptMeWithSpecifiedAttributes(encryptedAccount);
		LOGGER.debug("Decryption took: " + (System.currentTimeMillis()-startTime) + " ms");
		LOGGER.debug("Expected:" + expected);
		assertTrue(expected.getId().equals(EXPECTED_ID));
		assertTrue(expected.getName().equals(EXPECTED_NAME));
		assertTrue(expected.getAddress().equals(EXPECTED_ADDRESS));
	}
	
	@After
	public void tearDown() {
		LOGGER.debug("Tearing down the integration test.");
	}

}