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

import static org.openinfinity.core.aspect.ArgumentStrategy.ALL;
import static org.openinfinity.core.aspect.ArgumentStrategy.CUSTOM;
import static org.openinfinity.core.aspect.ArgumentStrategy.NONE;

import java.io.IOException;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;

import org.openinfinity.core.annotation.AuditTrail;
import org.openinfinity.core.annotation.Decrypt;
import org.openinfinity.core.annotation.Encrypt;
import org.openinfinity.core.annotation.Log;
import org.openinfinity.core.annotation.MultiTenant;
import org.openinfinity.core.aspect.ArgumentStrategy;
import org.openinfinity.core.common.domain.Account;
import org.openinfinity.core.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

/**
 * Integration test Spring bean.
 * 
 * @author Ilkka Leinonen
 * @version 1.3.0
 * @since 1.0.0
 */
@Component
@Controller
@Service
public class IntegrationTestBean implements IntegrationTest {

	/**
	 * Logger for this class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(IntegrationTestBean.class);
	
	public static final String FALSE_FIELD = "asdfasdfasdf";
	
	@Autowired
	private Validator validator;
		
	@Log
	public boolean log() {
		return Boolean.TRUE;
	}

	@Log(argumentStrategy=NONE)
	public String logMeWithNoArguments(String notLoggedMessage) {
		return notLoggedMessage;
	}
	
	@Log
	public boolean logMe(String loggedMessage, Long loggedLong) {
		return Boolean.TRUE;
	}
	
	@Log(argumentStrategy = CUSTOM, value = {"id", "name"})
	public void logMeWithSpecifiedArguments(Account account) {
	}
	
	@Log(argumentStrategy = CUSTOM, value = {"id", "name", FALSE_FIELD})
	public void logMeWithFalseArguments(Account account) {
	}
	
	@AuditTrail({"id","name","asdasd"})
	public void auditMeWithFalseField(Account account) {
	}

	@AuditTrail(argumentStrategy=NONE)
	public void auditMeWithNoArguments(Account account) {
	}
	
	@AuditTrail(argumentStrategy=ALL)
	public void auditMeWithAllArguments(Account account) {
	}
	
	@AuditTrail(isUsernameEnabled = false, isRolesEnabled = false, isTimeStampEnabled=true, value={"id","name", FALSE_FIELD}, argumentStrategy=CUSTOM)
	public void auditMe(Account account) {
	}
	
	@AuditTrail(argumentStrategy=ALL)
	public void auditMeWithPrimitiveFields(String accountId) {
	}
	
	public void throwSystemException() {
		ExceptionUtil.throwSystemException("Cause i can", new IOException("Because i fail"));
	}
	
	public void throwApplicationException() {
		ExceptionUtil.throwApplicationException("Cause i can", new IOException("Because i fail"));
	}

	public void throwBusinessViolationException() {
		ExceptionUtil.throwBusinessViolationException("Cause i can", new IOException("Because i fail"));
	}

	public void throwSecurityViolationException() {
	}
	
	public void throwUnknownException() {
		throw new UnknownException("I must be converted to SystemException.");
	}
	
	@Log
	public boolean validateMe(@Valid Account account) {
		Set<ConstraintViolation<Account>> failures = validator.validate(account);
		return failures.isEmpty() ? true : false;
	}

	@Encrypt(argumentStrategy = ArgumentStrategy.ALL)
	public Account encryptMe(Account account) {
		LOGGER.debug("Account: " + account.toString());
		return account;
	}

	@Decrypt(argumentStrategy = ArgumentStrategy.ALL)
	public Account decryptMe(Account account) {
		LOGGER.debug("Account: " + account.toString());
		return account;
	}
	
	@Encrypt(argumentStrategy = ArgumentStrategy.CUSTOM,  value = {"name", "address"})
	public Account encryptMeWithSpecifiedAttributes(Account account) {
		LOGGER.debug("Account: " + account.toString());
		return account;
	}

	@Decrypt(argumentStrategy = ArgumentStrategy.CUSTOM,  value = {"name", "address"})
	public Account decryptMeWithSpecifiedAttributes(Account account) {
		LOGGER.debug("Account: " + account.toString());
		return account;
	}

	@MultiTenant
	public Account addMeTenantId(Account account) {
		return account;
	}
	
}