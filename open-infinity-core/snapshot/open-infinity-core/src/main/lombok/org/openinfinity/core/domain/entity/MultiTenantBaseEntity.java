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
 * @param <TENANT_ID> Represents the type of the tenant id.
 * @param <I> Represent the type safe id for the base entity.
 * @param <I> Represent the type safe id for the base entity.
 */
@EqualsAndHashCode (callSuper = true)
public class MultiTenantBaseEntity<TENANT_ID, ENTITY_ID, USER_ID> extends BaseEntity<ENTITY_ID, USER_ID> {

	/**
	 * Represents the tenant id for shared schema based SaaS applications.
	 */
	@Getter
	public TENANT_ID tenantId;

}