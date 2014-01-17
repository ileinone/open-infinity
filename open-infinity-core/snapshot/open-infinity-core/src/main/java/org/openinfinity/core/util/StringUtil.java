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
package org.openinfinity.core.util;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Helper class for handling Strings.
 * 
 * @author Ilkka Leinonen
 * @version 1.0.0
 * @since 1.0.0
 */
public class StringUtil {
	
	/**
	 * Creates the object as String.
	 * 
	 * @param object - Object to be serialized to String.
	 * @return String - Presentation of the Object.
	 */
	public static String toString(Object object) {
		ToStringBuilder toStringBuilder = new ToStringBuilder(object);
		return toStringBuilder.toString();
	}

}
