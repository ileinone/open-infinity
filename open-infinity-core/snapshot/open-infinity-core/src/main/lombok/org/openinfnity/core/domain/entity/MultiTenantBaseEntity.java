package org.openinfnity.core.domain.entity;

import lombok.Data;

@Data
public class MultiTenantBaseEntity<T, I> extends BaseEntity<I> {
	
	private T tenantId;
	
	@SuppressWarnings("unchecked")
	public <T> void setTenantId(T tenantId) {
	//	this.tenantId = tenantId;
	}

}
