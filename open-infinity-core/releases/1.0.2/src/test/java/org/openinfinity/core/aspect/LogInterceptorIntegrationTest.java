/*
 * Copyright (c) 2012 the original author or authors.
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

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.openinfinity.core.common.IntegrationTest;

/**
 * Integration test class for log aspect.
 * 
 * @author Ilkka Leinonen
 * @version 1.0.0
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/t-core-integration-test-context.xml")
public class LogInterceptorIntegrationTest extends
		AbstractJUnit38SpringContextTests {

	private File file;

	@Autowired
	private IntegrationTest integrationTest;

	@Before
	public void setUp() {
		file = new File("target/logging.log");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testWhenAccessingMethodAllTheParametersMustBeLogged() {
		try {
			boolean expected = Boolean.TRUE;
			this.integrationTest.logMe("Logging you!");
			boolean actual = readLogFileContent().contains("Logging you!");
			assertEquals(expected, actual);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			fail();
		}
	}

	public String readLogFileContent() throws Throwable {
		return FileUtils.readFileToString(file);	
	}

	@After
	public void tearDown() {
		file = null;
	}

}