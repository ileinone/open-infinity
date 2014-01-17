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
package org.openinfinity.core.common;

import org.openinfinity.core.common.domain.Account;

/**
 * Integration test Spring bean interface.
 * 
 * @author Ilkka Leinonen
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IntegrationTest {

	public boolean log();
	
	public String logMeWithNoArguments(String notLoggedMessage);
	
	public boolean logMe(String loggedMessage, Long loggedLong);
	
	public void logMeWithSpecifiedArguments(Account account);
	
	public void logMeWithFalseArguments(Account account);
	
	public void auditMeWithFalseField(Account account);
	
	public void auditMeWithNoArguments(Account account);
	
	public void auditMeWithAllArguments(Account account);
	
	public void auditMe(Account account);
	
	public Account encryptMe(Account account);
	
	public Account decryptMe(Account account);
	
	public Account encryptMeWithSpecifiedAttributes(Account account);

	public Account decryptMeWithSpecifiedAttributes(Account account);
	
	public void auditMeWithPrimitiveFields(String accountId);
	
	public void throwSystemException();
	
	public void throwApplicationException();
	
	public void throwBusinessViolationException();
	
	public void throwSecurityViolationException();
	
	public void throwUnknownException();
	
	public boolean validateMe(Account account);
	
	public Account addMeTenantId(Account account);
	
}