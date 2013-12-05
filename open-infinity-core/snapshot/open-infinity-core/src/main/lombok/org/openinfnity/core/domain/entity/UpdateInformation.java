package org.openinfnity.core.domain.entity;

import java.util.Date;

import lombok.Data;

@Data
public class UpdateInformation<I> {
	
	private I updateById;
	
	private Date updateDate;

}