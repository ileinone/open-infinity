package org.openinfnity.core.domain.entity;

import java.util.Collection;
import java.util.Date;

import lombok.Data;

@Data
public class BaseEntity <I> {
	
	private I id;
	
	private Date creationDate;
	
	private Collection<UpdateInformation<I>> updateInformations;
	
	private Date terminationDate;
	
}