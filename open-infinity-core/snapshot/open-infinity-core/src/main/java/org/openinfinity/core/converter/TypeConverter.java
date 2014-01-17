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
package org.openinfinity.core.converter;

/**
 * Converter interface for making type safe conversations between objects.
 * 
 * @author Ilkka Leinonen
 *
 * @version 1.0.0
 * @since 1.4.0
 * 
 * @param <K> Represents the actual object to be converted.
 * @param <V> Represents the converted object.
 */
public interface TypeConverter<K,V> {
	
	/**
	 * Converts given object to another.
	 * 
	 * @param object Represents the data to be converted.
	 * @return object Represents the converted object.
	 */
	public V convert(K object);

}
