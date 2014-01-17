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
 * The metadata executes the <code>org.openinfinity.core.aspect.LogAspect</code> aspect when defined. Annotation can be defined in the method level.
 * 
 * @author Ilkka Leinonen 
 * @version 1.0.0
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Log {
	/**
	 * Represents the log levels (TRACE=0, DEBUG=1, INFO=2, WARN=3, ERROR=4).
	 */
	public enum LogLevel { 
		  
		/**
		 * Used when log level should be TRACE
		 */
		TRACE(0), 
		/**
		 * Used when log level should be DEBUG
		 */
		DEBUG(1), 
		/**
		 * Used when log level should be INFO
		 */
		INFO(2), 
		/**
		 * Used when log level should be WARN
		 */
		WARN(3), 
		/**
		 * Used when log level should be ERROR
		 */
		ERROR(4);
			
		private Integer value;
			
		private LogLevel(Integer value) {
			this.value = value;
		}
	
		/**
		 * Returns the value of the level represented as an <code>java.lang.Integer</code>
		 * 
		 * @return Integer representing the value of the level as an <code>java.lang.Integer</code> (TRACE=0, DEBUG=1, INFO=2, WARN=3, ERROR=4).
		 */
		public Integer getValue() {
			return this.value;
		}
		
	};
	
	/**
	 * Represents the log level. Default is <code>org.openinfinity.core.annotation.LogLevel.DEBUG</code>
	 * 
	 * @return Returns the log level (TRACE=0, DEBUG=1, INFO=2, WARN=3, ERROR=4).
	 */
	public LogLevel level() default LogLevel.DEBUG;
	
	/**
	 * Accessed object fields can be defined separately by using XPATH notation.
	 * 
	 * <br /><br />
	 * <code>
	 * @AuditTrail({"id","auto/name","company/address", "//space"})
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