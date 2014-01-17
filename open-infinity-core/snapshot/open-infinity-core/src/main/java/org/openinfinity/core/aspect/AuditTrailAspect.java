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

import java.util.Collection;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.openinfinity.core.annotation.AuditTrail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Class is responsible for creating audit trail information. Audit trail storage system can be defined through Log4j property files (JDBCAppender, FileAppender, JMSAppender etc).
 *  
 * @author Ilkka Leinonen 
 * @version 1.1.0 - Spring Security 3.1.X-modifications.
 * @since 1.0.0
 */
@Aspect
public class AuditTrailAspect implements Ordered {

	/**
	 * Logger for this class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AuditTrailAspect.class);
	
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
	 *  Uses <code>org.openinfinity.core.annotation.AuditTrail</code> annotation for the AspectJ's pointcut resolving.
	 */
	@Pointcut("execution (* ((@org.openinfinity.core.annotation.AuditTrail *))+.*(..))")
	public void anyAuditTrailClass() {}
	
	@Pointcut("@annotation(org.openinfinity.core.annotation.AuditTrail)")
	public void auditTrailAnnotatedMethod() {}
	
	/**
	 *  Method prints audit trail information to a Log4j appender.
	 *  Uses <code>org.openinfinity.core.annotation.AuditTrail</code> annotation for the AspectJ's pointcut resolving.
	 *  <br/><br/>
	 *  Usage: <br/><br/>
	 *  Simple usage: <br/><br/>
	 *  <code>
	 *  @Auditrail
	 *  public SomeObject someMethod(AnotherObject anotherObject() {...}
	 *  </code>
	 *  <br/><br/>
	 *  Advanced usage: fields added to audit trail can be defined separately<br/><br/>
	 *  <code>
	 *  @AuditTrail(isUsernameEnabled = true, isRolesEnabled = false, isTimeStampEnabled=true, value={"auto/tire/screw","personId"}, argumentStrategy=CUSTOM)
	 *  public SomeObject someMethod(AnotherObject anotherObject() {...}
	 *  </code>
	 */
	@Before(value="auditTrailAnnotatedMethod() && @annotation(auditTrail)")
	public void auditTrailedMethod(JoinPoint joinPoint, AuditTrail auditTrail) throws Throwable {
		ArgumentBuilder builder = new ArgumentBuilder();
		writeTimestampToAuditTrailIfEnabled(builder, auditTrail);
		writeMethodToAuditTrailIfEnabled(joinPoint, builder);
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		writeUsernameToAuditTrailIfEnabled(builder, auditTrail, authentication);
		writeRolesToAuditTrailIfEnabled(builder, auditTrail, authentication);
		writeArgumentDetailsToAuditTrailBasedOnDesicionLogic(joinPoint, builder, auditTrail);
		String auditTrailInformation = builder.toString();
		LOGGER.info(auditTrailInformation);
	}

	private void writeArgumentDetailsToAuditTrailBasedOnDesicionLogic(JoinPoint joinPoint, ArgumentBuilder builder, AuditTrail auditTrail) {
		ArgumentStrategy argumentStrategy = auditTrail.argumentStrategy();
		switch(argumentStrategy) {
			case ALL : addAllArgumentsToAuditTrail(joinPoint, builder); return;
			case CUSTOM : addCustomFieldsToAuditTrail(joinPoint, builder, auditTrail); return;
			case NONE : return;
			default: return;
		}
	}
	
	private void addCustomFieldsToAuditTrail(JoinPoint joinPoint, ArgumentBuilder builder, AuditTrail auditTrail) {
		builder.extractArgumentInfoByFilteringFields(joinPoint, auditTrail.value());
	}

	private void addAllArgumentsToAuditTrail(JoinPoint joinPoint, ArgumentBuilder builder) {
		builder.extractArgumentInfo(joinPoint);
	}

	private void writeMethodToAuditTrailIfEnabled(JoinPoint joinPoint, ArgumentBuilder builder) {
		builder.append(" Method: [").append(joinPoint.getSignature().getName()).append("] ");
	}

	private void writeRolesToAuditTrailIfEnabled(ArgumentBuilder builder, AuditTrail auditTrail, Authentication authentication) {
		if (auditTrail.isRolesEnabled() && authentication != null && authentication.getAuthorities() != null) {
			Collection<? extends GrantedAuthority> grantedAuthorities = authentication.getAuthorities();
			builder.append(" with granted authorities: [");
			for (GrantedAuthority grantedAuthority : grantedAuthorities) 
				builder.append("{" + grantedAuthority.getAuthority() + "}");
			builder.append("] ");
		}
	}

	private void writeUsernameToAuditTrailIfEnabled(ArgumentBuilder builder, AuditTrail auditTrail, Authentication authentication) {
		if (auditTrail.isUsernameEnabled()) {	
			String username = authentication!=null?authentication.getName():"user not authenticated";
			builder.append(" Username: ").append("[").append(username).append("] ");
		}
	}

	private void writeTimestampToAuditTrailIfEnabled(ArgumentBuilder builder, AuditTrail auditTrail) {
		if (auditTrail.isTimeStampEnabled()) { 
			DateTime dateTime = new DateTime();
			DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
			String timeStamp = fmt.print(dateTime);
			builder.append("Timestamp: ").append("[").append(timeStamp).append("] ");
		}
	}

	@Override
	public int getOrder() {
		return order;
	}
	
}