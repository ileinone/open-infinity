package org.openinfinity.core.domain.entity;

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
public class UpdateInformation<I> {
	
	/**
	 * Represent the unique id of the person or system whom made the update.
	 */
	private I updateById;
	
	/**
	 * Represent the actual update lifecycle event for the domain entity.
	 */
	private Date updateDate;

}