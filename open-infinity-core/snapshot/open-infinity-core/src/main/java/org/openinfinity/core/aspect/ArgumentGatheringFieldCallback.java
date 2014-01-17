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

/**
 * Field callback interface.
 * 
 * @author Ilkka Leinonen
 *
 * @param <Field>
 * @param <V>
 * 
 * @version 1.0.0
 * @since 1.3.0
 */
public interface ArgumentGatheringFieldCallback<Field, V> {
	
	/**
	 * Called on each found field of an entity.
	 * 
	 * @param field Represent the actual field of an object.
	 * @param object Represents the actual object where the field is found.
	 */
	public void onField(Field field, V object);

}
