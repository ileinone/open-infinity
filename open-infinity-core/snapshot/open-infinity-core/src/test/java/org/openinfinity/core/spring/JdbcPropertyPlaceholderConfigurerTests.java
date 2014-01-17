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
package org.openinfinity.core.spring;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

/**
 * Integration test for JDBC property placeholder.
 * 
 * @author Ilkka Leinonen
 * @version 1.0.0
 * @since 1.0.0
 */
public class JdbcPropertyPlaceholderConfigurerTests {

	private JdbcPropertyPlaceholderConfigurer jdbcPropertyPlaceholderConfigurer;

	@Before
	public void setUp() throws Exception {
		DataSource dataSource = createTestDataSource();
		jdbcPropertyPlaceholderConfigurer = new JdbcPropertyPlaceholderConfigurer(dataSource);
	}

	@Test
	public void givenDataSourceWhenAccessingSharedPropertiesThenMustReadKeysAndValuesFromDataSourceToProperties() {
		Properties properties = jdbcPropertyPlaceholderConfigurer.readPropertiesFromDataSource();
		assertEquals(6, properties.size());
	}

	private DataSource createTestDataSource() {
		return new EmbeddedDatabaseBuilder()
			.setName("SHARED_PROPERTIES")
			.addScript("classpath:/sql/schema.sql")
			.addScript("classpath:/sql/test-data.sql")
			.build();
	}
}
