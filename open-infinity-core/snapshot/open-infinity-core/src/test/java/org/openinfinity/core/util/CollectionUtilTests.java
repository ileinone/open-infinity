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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Unit test for collection utility.
 * 
 * @author Ilkka Leinonen
 * @version 1.0.0
 * @since 1.2.0
 */
public class CollectionUtilTests {
	
	static int ACTUAL_SIZE_OF_THE_COLLECTION = 0;
	static int ACTUAL_SIZE_OF_THE_SET = 0;
	static int ACTUAL_SIZE_OF_THE_MAP = 0;
	
	@Test
	public void verifyCollectionCallbackFunctionality() {
		Collection<String> expected = new ArrayList<String>();
		expected.add("foo");
		expected.add("bar");
		CollectionElementUtil.iterate(expected, new CollectionElementCallback<String>() {
			public void callback(String callbackObject) {
				System.out.println(callbackObject);	
				ACTUAL_SIZE_OF_THE_COLLECTION++;
			}			
		});
		Assert.assertEquals(expected.size(), ACTUAL_SIZE_OF_THE_COLLECTION);
		ACTUAL_SIZE_OF_THE_COLLECTION = 0;
	}
	
	@Test
	public void verifyCollectionCallbackFunctionalityWithNullValues() {
		Collection<String> expected = new ArrayList<String>();
		expected.add("foo");
		expected.add("bar");
		expected.add(null);
		CollectionElementUtil.iterate(expected, new CollectionElementCallback<String>() {
			public void callback(String callbackObject) {
				System.out.println(callbackObject);	
				ACTUAL_SIZE_OF_THE_COLLECTION++;
			}			
		});
		Assert.assertEquals(expected.size()-1, ACTUAL_SIZE_OF_THE_COLLECTION);
		ACTUAL_SIZE_OF_THE_COLLECTION = 0;
	}
	
	@Test
	public void verifySetCallbackFunctionality() {
		Set<String> expected = new HashSet<String>();
		expected.add("foo");
		expected.add("bar");
		CollectionElementUtil.iterate(expected, new CollectionElementCallback<String>() {
			public void callback(String key) {	
				ACTUAL_SIZE_OF_THE_SET++;	
			}			
		});
		Assert.assertEquals(expected.size(), ACTUAL_SIZE_OF_THE_SET);
		ACTUAL_SIZE_OF_THE_SET = 0;
	}
	
	@Test
	public void verifySetCallbackFunctionalityWithNullValues() {
		Set<String> expected = new HashSet<String>();
		expected.add("foo");
		expected.add("bar");
		expected.add(null);
		CollectionElementUtil.iterate(expected, new CollectionElementCallback<String>() {
			public void callback(String key) {
				System.out.println(key);	
				ACTUAL_SIZE_OF_THE_SET++;	
			}			
		});
		Assert.assertEquals(expected.size()-1, ACTUAL_SIZE_OF_THE_SET);
		ACTUAL_SIZE_OF_THE_SET = 0;
	}
	
	@Test
	public void verifyMapCallbackFunctionality() {
		Map<String, String> expected = new HashMap<String, String>();
		expected.put("foo","me1");
		expected.put("bar", "me2");
		CollectionElementUtil.iterate(expected, new MapElementCallback<String, String>() {
			public void callback(String key, String value) {
				System.out.println(key + ":" + value);	
				ACTUAL_SIZE_OF_THE_MAP++;
			}			
		});
		Assert.assertEquals(expected.size(), ACTUAL_SIZE_OF_THE_MAP);
		ACTUAL_SIZE_OF_THE_MAP = 0;
	}
	
	@Test
	public void verifyMapCallbackFunctionalityWithNullValues() {
		Map<String, String> expected = new HashMap<String, String>();
		expected.put("foo","me1");
		expected.put("bar", "me2");
		expected.put(null, null);
		CollectionElementUtil.iterate(expected, new MapElementCallback<String, String>() {
			public void callback(String key, String value) {
				System.out.println(key + ":" + value);	
				ACTUAL_SIZE_OF_THE_MAP++;
			}			
		});
		Assert.assertEquals(expected.size()-1, ACTUAL_SIZE_OF_THE_MAP);
		ACTUAL_SIZE_OF_THE_MAP = 0;
	}

}
