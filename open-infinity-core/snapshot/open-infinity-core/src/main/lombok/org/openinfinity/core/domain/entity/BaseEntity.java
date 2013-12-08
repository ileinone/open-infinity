package org.openinfinity.core.domain.entity;

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
 * @param <I> Represents the type of the unique id for the entity.
 */
@Data
@EqualsAndHashCode
public class BaseEntity <I> {
	
	/**
	 * Represents the unique id of the entity.
	 */
	private I id;
	
	/**
	 * Represents the starting point of the entity lifecycle.
	 */
	private Date creationDate;
	
	/**
	 * Represents the collection of the lifecycle events for the domain entity.
	 */
	private Collection<UpdateInformation<I>> updateInformations;
	
	/**
	 * Represents the termination date of the domain entity's lifecycle.
	 */
	private Date terminationDate;
	
}