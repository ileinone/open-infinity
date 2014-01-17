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
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents the lifecycle events of the domain entity.
 * 
 * @author Ilkka Leinonen
 * @version 1.0.0
 * @since 1.4.0
 * @param <I> Represents the actual type of the updater of the entity.
 */
@Data
@EqualsAndHashCode
public class UpdateInformation<USER_ID, UPDATE_ID> implements Serializable {
	
	private UPDATE_ID updateInformationId;
	
	/**
	 * Represent the unique id of the person or system whom made the update.
	 */
	private USER_ID updateById;
	
	/**
	 * Represent the actual update lifecycle event for the domain entity.
	 */
	private Date updateDate;

}