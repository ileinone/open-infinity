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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents a base entity class to be utilized with domain entities. Following Attributes are provided:
 * <ul>
 * 	<li>id, which represents the unique id of the entity</li>
 *  <li>creationDate, which represents the starting point of the entity lifecycle</li>
 *  <li>updateInformations, which represents the collection of the lifecycle events for the domain entity</li>
 *  <li>terminationDate, which represents the termination date of the domain entity's lifecycle</li>
 * </ul>
 * @author Ilkka Leinonen
 * @version 1.0.0
 * @since 1.4.0
 * @param <ENTITY_ID> Represents the type of the unique id for the entity.
 */
@Data
@EqualsAndHashCode
public class BaseEntity <ENTITY_ID, USER_ID> implements Serializable {
	
	/**
	 * Represents the unique id of the entity.
	 */
	private ENTITY_ID id;
	
	/**
	 * Represents the starting point of the entity lifecycle.
	 */
	private Date creationDate;
	
	/**
	 * Represents the collection of the lifecycle events for the domain entity.
	 */
	private Collection<UpdateInformation<ENTITY_ID, USER_ID>> updateInformations = new ArrayList<UpdateInformation<ENTITY_ID, USER_ID>>();
	
	/**
	 * Add update information 
	 * 
	 * @param updateInformation Represents the update information concerning the entity.
	 */
	public void addUpdateInformation(UpdateInformation<ENTITY_ID, USER_ID> updateInformation) {
		updateInformations.add(updateInformation);
	}
	
	/**
	 * Represents the termination date of the domain entity's lifecycle.
	 */
	private Date terminationDate;
	
}