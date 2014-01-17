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
package org.openinfinity.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.openinfinity.core.aspect.ArgumentStrategy;

/**
 * This annotation represents the metadata added to the runtime method.
 * The metadata executes the <code>org.openinfinity.core.aspect.AuditTrailAspect</code> aspect when defined. Annotation can be defined in the method level.
 * <br/><br/>
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
 * 
 * @author Ilkka Leinonen
 * @version 1.1.0 - Default behaviour for the argument strategy is ALL.
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.LOCAL_VARIABLE, ElementType.PACKAGE, ElementType.TYPE})
public @interface AuditTrail {

	/**
	 * Prints the session based username by default.
	 */
	public static final boolean PRINTING_BY_DEFAULT_USERNAME = true;	

	/**
	 * Prints the timestamp of the accessed method by default.
	 */
	public static final boolean PRINTING_BY_DEFAULT_TIMESTAMP = true;
	
	/**
	 * Prints the session based roles by default.
	 */
	public static final boolean PRINTING_BY_DEFAULT_ROLES = true;
	
	/**
	 * Prints the session based username by PRINTING_BY_DEFAULT_USERNAME static field by default. Field can be changed to <code>true</code> or <code>false</code>.
	 */
	public boolean isUsernameEnabled() default PRINTING_BY_DEFAULT_USERNAME;
	
	/**
	 * Prints the timestamp by PRINTING_BY_DEFAULT_TIMESTAMP static field by default. Field can be changed to <code>true</code> or <code>false</code>.
	 */
	public boolean isTimeStampEnabled() default PRINTING_BY_DEFAULT_TIMESTAMP;
	
	/**
	 * Prints the session based username by PRINTING_BY_DEFAULT_USERNAME static field by default. Field can be changed to <code>true</code> or <code>false</code>.
	 */
	public boolean isRolesEnabled() default PRINTING_BY_DEFAULT_ROLES;

	/**
	 * Accessed object fields can be defined separately by using XPATH notation.
	 * 
	 * <br /><br />
	 * <code>
	 * @AuditTrail({"id","auto/name","company/address", "//"})
	 * </code>
	 * 
	 * @return String[] - Return the defined XPATH fields for the accessible objects.
	 */
	public String[] value() default {};
	
	/**
	 * Defines the argument strategy policy:
	 * <br/>
	 * <ul>
	 * 		<li>NONE - none of the objects or object fields will be shown in the audit trail</li>
	 * 		<li>ALL - all of the objects or object fields will be shown in the audit trail</li>
	 * 		<li>CUSTOM - defines the custom usage of fields</li>
	 * 
	 * @return ArgumentStrategy - based on the strategy.
	 */
	public ArgumentStrategy argumentStrategy() default ArgumentStrategy.ALL;

}