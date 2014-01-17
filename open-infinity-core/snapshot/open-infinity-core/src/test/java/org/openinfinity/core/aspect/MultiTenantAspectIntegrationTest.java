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

import java.util.ArrayList;
import java.util.Collection;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openinfinity.core.common.IntegrationTest;
import org.openinfinity.core.common.domain.Account;
import org.openinfinity.core.security.principal.Identity;
import org.openinfinity.core.security.principal.RolePrincipal;
import org.openinfinity.core.security.principal.TenantPrincipal;
import org.openinfinity.core.security.principal.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class MultiTenantAspectIntegrationTest extends AbstractJUnit4SpringContextTests {
	
	private static final String UNIQUE_TENANT_ID = "test-tenant";
	
	@Autowired
	private IntegrationTest integrationTest;

	@Before
	public void setUp() {
		injectIdentityBasedSecurityContext();
	}
	
	private void injectIdentityBasedSecurityContext() {
		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		SecurityContextHolder.setContext(securityContext);
		Identity identity = new Identity();
		Collection<RolePrincipal> rolePrincipals = new ArrayList<RolePrincipal>();
		identity.setAuthenticated(true);
		UserPrincipal userPrincipal = new UserPrincipal("test-name");
		TenantPrincipal<String> tenantPrincipal = new TenantPrincipal<String>(UNIQUE_TENANT_ID);
		RolePrincipal rolePrincipal = new RolePrincipal("test-role");
		rolePrincipals.add(rolePrincipal);
		identity.setUserPrincipal(userPrincipal);
		identity.setRolePrincipals(rolePrincipals);
		identity.setTenantPrincipal(tenantPrincipal);
		SecurityContextHolder.getContext().setAuthentication(identity);
	}

	@Test
	public void givenKnownArgumentsWhenAccessingMethodThenAllTheArgumentsMustBeIncludidInAuditTrail() throws Throwable {
		Account account = new Account("1", "Name1");
		Account populatedAccount = integrationTest.addMeTenantId(account);
		assertEquals(UNIQUE_TENANT_ID, populatedAccount.getTenantId());
	}
		
	@After
	public void tearDown() {
		SecurityContextHolder.getContext().setAuthentication(null);
		SecurityContextHolder.clearContext();
	}

}