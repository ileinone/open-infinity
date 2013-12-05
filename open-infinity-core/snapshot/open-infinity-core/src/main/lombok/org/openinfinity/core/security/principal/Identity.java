/*
 * Copyright (c) 2011-2013 the original author or authors.
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
package org.openinfinity.core.security.principal;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Class for maintaining the state of the federated identity. Implements <code>org.springframework.security.core.Authentication</code> interface.
 * 
 * @author Ilkka Leinonen
 * @version 1.0.0
 * @since 1.4.0
 */
public class Identity implements Authentication, Serializable {

	private UserPrincipal userPrincipal;
	
	private boolean authenticated;
	
	private Collection<RolePrincipal> rolePrincipals;
	
	private TenantPrincipal tenantPrincipal;

	public UserPrincipal getUserPrincipal() {
		return userPrincipal;
	}

	public void setUserPrincipal(UserPrincipal userPrincipal) {
		this.userPrincipal = userPrincipal;
	}

	public TenantPrincipal getTenantPrincipal() {
		return tenantPrincipal;
	}

	public void setTenantPrincipal(TenantPrincipal tenantPrincipal) {
		this.tenantPrincipal = tenantPrincipal;
	}

	public Collection<RolePrincipal> getRolePrincipals() {
		return rolePrincipals;
	}

	public void setRolePrincipals(Collection<RolePrincipal> rolePrincipals) {
		this.rolePrincipals = rolePrincipals;
	}
	
	public List<String> getRoles() {
		List<String> roles = new ArrayList<String>();
		for (RolePrincipal rolePrincipal : rolePrincipals) {
			roles.add(rolePrincipal.getName());
		}
		return Collections.unmodifiableList(roles);
	}
	
	public Collection<Principal> getAllPrincipalsForIdentity() {
		Collection<Principal> principals = new ArrayList<Principal>();
		principals.add(userPrincipal);
		principals.add(tenantPrincipal);
		principals.addAll(rolePrincipals);
		return Collections.unmodifiableCollection(principals);
	}
 	
	public void clear() {
		this.userPrincipal.clear();
		this.tenantPrincipal.clear();
		for (RolePrincipal rolePrincipal : rolePrincipals) {
			rolePrincipal.clear();
		}
		this.rolePrincipals.clear();
		this.userPrincipal = null;
		this.tenantPrincipal = null;
		this.rolePrincipals = null;
	}
	
	public String checksum(String salt) {
		StringBuilder builder = new StringBuilder();
		builder.append(salt);
		if (userPrincipal != null && userPrincipal.getName() != null && userPrincipal.getName().length() > 0) {
			builder.append(userPrincipal.getName());
		}
		if (tenantPrincipal != null && tenantPrincipal.getName() != null && tenantPrincipal.getName().length() > 0) {
			builder.append(tenantPrincipal.getName());
		}
		for (RolePrincipal rolePrincipal : rolePrincipals) {
			if (rolePrincipal != null && rolePrincipal.getName() != null && rolePrincipal.getName().length() > 0) {
				builder.append(rolePrincipal.getName());
			}	
		}
		String checksum = DigestUtils.sha512Hex(builder.toString());
		return checksum;
	}

	public Collection<GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		for (Principal principal : getAllPrincipalsForIdentity()) {
			GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(principal.getName());
			grantedAuthorities.add(grantedAuthority);
		}
		return grantedAuthorities;
	}

	public String getPassword() {
		return "Sweet-Smell-Of-Danger-Not-Exposed-2-U";
	}

	public String getName() {
		return userPrincipal.getName();
	}

	public Object getCredentials() {
		return userPrincipal.getName();
	}

	public Object getDetails() {
		return this;
	}

	public Object getPrincipal() {
		return userPrincipal;
	}

	public boolean isAuthenticated() {
		return true;
	}

	public void setAuthenticated(boolean isAuthenticated)
			throws IllegalArgumentException {
		this.authenticated = isAuthenticated;
		
	}

	@Override
	public String toString() {
		return "Identity [userPrincipal=" + userPrincipal + ", authenticated="
				+ authenticated + ", rolePrincipals=" + rolePrincipals
				+ ", tenantPrincipal=" + tenantPrincipal + "]";
	}
	
	
	

}
