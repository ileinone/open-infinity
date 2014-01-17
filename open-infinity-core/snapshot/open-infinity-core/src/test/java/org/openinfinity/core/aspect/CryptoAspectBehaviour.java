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

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.mockito.Mockito;
import org.openinfinity.common.infrastructure.AnnotatedEmbedderUsingSpring;
import org.openinfinity.core.annotation.Encrypt;
import org.openinfinity.core.common.domain.Account;
import org.springframework.stereotype.Component;

/**
 * Functional test class for cipher aspect.
 * 
 * @author Ilkka Leinonen
 * @version 1.0.0
 * @since 1.3.0
 */
@Component("cipherAspectBehaviour")
public class CryptoAspectBehaviour extends AnnotatedEmbedderUsingSpring {

	private CryptoAspect cipherAspect;
	private Account actual;
	
	public static String EXPECTED_ID = "Open-Infinity";
	public static String EXPECTED_NAME = "Open";
	public static String EXPECTED_ADDRESS = "Infinify";

	@BeforeScenario
	public void setupScenario() {
		actual = new Account(EXPECTED_ID, EXPECTED_NAME);
		actual.setAddress(EXPECTED_ADDRESS);
	}
	
	@Given("an aspect to be called")
	public void aspectForExceptionTranslation() {
		cipherAspect = new CryptoAspect();
	}
	
	@When("executing business operation and encryption of an object graphs is needed")
	public void executingBusinessOperationAndEncryptionOfAnObjectGraphsIsNeeded() {
		//TODO: imitate proceed functionality of the aspect
		ProceedingJoinPoint joinPoint = (ProceedingJoinPoint) Mockito.mock(Joinpoint.class);
		Encrypt encrypt = Mockito.mock(Encrypt.class);
		when(joinPoint.getArgs()).thenReturn(new Object[]{actual});
		when(encrypt.argumentStrategy()).thenReturn(ArgumentStrategy.ALL);
		cipherAspect.encryptObjectContentAfterMethod(joinPoint, encrypt);
		System.out.println("Doing stuff");
		assertTrue(EXPECTED_ID.equals(actual.getId()));
	}
	
	@When("executing business operation and decryption of an object graphs is needed")
	public void executingBusinessOperationAndDecryptionOfAnObjectGraphsIsNeeded() {
		
	}
	
	@Then("object graphs spefied fields must be encrypted")
	public void knownExceptionsMustBeByPassed() {

	}
	
	@Then("object graphs none of the fields must not be encrypted")
	public void objectGraphsNoneOfTheFieldsMustNotBeEncrypted() {
	
	}
	
	@Then("object graphs spefied fields must be decrypted")
	public void objectGraphsSpefiedFieldsMustBeDecrypted() {
	
	}
	
	@Then("object graphs all fields must be decrypted")
	public void objectGraphsAllFieldsMustBeDecrypted() {
	
	}
	
	@Then("object graphs none of the fields must not be decrypted")
	public void objectGraphsNoneOfTheFieldsMustNotBeDecrypted() {
	
	}

	@Override
	protected String getStoryName() {
		return "crypto_aspect.story";
	}
	
}
