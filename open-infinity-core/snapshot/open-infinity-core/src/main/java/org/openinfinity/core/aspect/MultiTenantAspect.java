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
package org.openinfinity.core.aspect;

import java.lang.reflect.Field;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.openinfinity.core.annotation.MultiTenant;
import org.openinfinity.core.converter.PassthroughConverter;
import org.openinfinity.core.converter.TypeConverter;
import org.openinfinity.core.domain.entity.MultiTenantBaseEntity;
import org.openinfinity.core.security.principal.Identity;
import org.openinfinity.core.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ReflectionUtils;

/**
 * This class is responsible of the tenant id injection into the domain entity which extends <code>org.openinfinity.core.domain.entity.MultiTenantBaseEntity</code>.
 * <code>org.openinfinity.core.security.principal.Identity</code> implements <code>org.springframework.security.core.Authentication</code> interface and is therefore available through Spring security's 
 * <code>org.springframework.security.core.context.SecurityContext</code>.
 * 
 * @author Ilkka Leinonen
 * @version 1.0.0
 * @since 1.4.0
 */
@Aspect
public class MultiTenantAspect extends ArgumentGatheringJoinPointInterceptor implements Ordered {
	
	/**
	 * Represent the tenant id field name.
	 */
	private static final String TENANT_ID_FIELD = "tenantId";
	
	/**
	 * Represents the main logger for the application.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MultiTenantAspect.class);
	
	/**
	 * Represents the execution order of the aspect.
	 */
	private int order;
	
	/**
	 * Setter for the order.
	 * 
	 * @param order Represents the execution order of the aspect.
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * Type converter for converting specific type of tenant id. 
	 */
	private TypeConverter<Object, Object> typeConverter = new PassthroughConverter();
	
	/**
	 * Setter for the type converter which converts tenant id types.
	 * 
	 * @param typeConverter Represent the actual type converter between entity and tenant id.
	 */
	public void setTypeConverter(TypeConverter<Object, Object> typeConverter) {
		this.typeConverter = typeConverter;
	}

	/**
	 *  Uses <code>org.openinfinity.core.annotation.MultiTenant</code> annotation for the point cut resolving.
	 */
	@Pointcut("@annotation(org.openinfinity.core.annotation.MultiTenant)")
	public void multiTenantMethod(){}
	
	
	/**
	 * Injection of tenant id will be done based on the <code>org.openinfinity.core.annotation.MultiTenant</code> annotation on method level. After injection of the tenant id 
	 * <code>org.openinfinity.core.domain.entity.MultiTenantBaseEntity</code> can be used to retrieve the actual tenant id. <code>org.openinfinity.core.domain.entity.MultiTenantBaseEntity</code> 
	 * can be extended by <code>org.openinfinity.core.domain.entity.MultiTenantBaseEntity</code>. 
	 * 
	 * @param method Represents the method to be executed when exposing <code>org.openinfinity.core.annotation.MultiTenant</code> metadata to it.
	 * @param multiTenant Represents the annotation which executed by the aspect.
	 * @return Object Represents the original arguments for the method with injected tenant id.
	 * @throws Throwable Represents the occurred exception during the execution of the aspect.
	 */
	@Around("multiTenantMethod() && @annotation(multiTenant)")
	public Object populateTenantIdToMultiTenantEntity(ProceedingJoinPoint method, MultiTenant multiTenant) throws Throwable {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("MultiTenantAspect.populateTenantIdToMultiTenantEntity initialized.");
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		if (authentication instanceof Identity) {
			Identity identity = (Identity) authentication;
			Object[] arguments = method.getArgs();
			for (Object object : arguments) {
				if (object instanceof MultiTenantBaseEntity) { 
					if (LOGGER.isDebugEnabled())
						LOGGER.debug("MultiTenantAspect.populateTenantIdToMultiTenantEntity arguments is istance of MultiTenantBaseEntity.");
					MultiTenantBaseEntity<?, ?, ?> multiTenantBaseEntity = (MultiTenantBaseEntity<?, ?, ?>) object;
					Object tenantId = identity.getTenantPrincipal().getId();
					Field tenantIdField = multiTenantBaseEntity.getClass().getField(TENANT_ID_FIELD);
					if (!tenantIdField.isAccessible()) {
						tenantIdField.setAccessible(true);
					}
					Object convertedTenantId = typeConverter.convert(tenantId);
					if (tenantIdField.getType().isAssignableFrom(convertedTenantId.getClass())) {
						if (LOGGER.isDebugEnabled())
							LOGGER.debug("MultiTenantAspect.populateTenantIdToMultiTenantEntity tenant id is assignable from [" + convertedTenantId.getClass().getName() + ".");
						ReflectionUtils.setField(tenantIdField, multiTenantBaseEntity, convertedTenantId);
						if (LOGGER.isInfoEnabled())
							LOGGER.info("MultiTenantAspect.populateTenantIdToMultiTenantEntity injected tenant id ["+ convertedTenantId.toString() +"] to the entity.");
					} else {
						ExceptionUtil.throwSystemException("Field [" + tenantIdField.getType().getName() + "] is not assignable from [" + convertedTenantId.getClass().getName());
					}
				}
			}
		}
		return method.proceed();
	}

	@Override
	public int getOrder() {
		return order;
	}	

}