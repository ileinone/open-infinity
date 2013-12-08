package org.openinfinity.core.domain.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Represents the domain entity for multitenant Software as a Service applications with shared schema model.
 * 
 * @author Ilkka Leinonen
 * @version 1.0.0
 * @since 1.4.0
 *
 * @param <T> Represents the type of the tenant id.
 * @param <I> Represent the type safe id for the base entity.
 */
@EqualsAndHashCode (callSuper = true)
public class MultiTenantBaseEntity<T, I> extends BaseEntity<I> {

	/**
	 * Represents the tenant id for shared schema based SaaS applications.
	 */
	@Getter
	public T tenantId;

}