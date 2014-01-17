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

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

/**
 * Property configurator class which enables property reading from the shared database.
 * 
 * @author Ilkka Leinonen
 * @version 1.0.2
 * @since 1.0.0
 */
public class JdbcPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	/**
	 * Query sequel when executing queries.
	 */
	private static final String PROPERTIES_QUERY_SQL = "SELECT KEY_COLUMN, VALUE_COLUMN FROM SHARED_PROPERTIES";

	/**
	 * Shared properties instance.
	 */
	private Properties properties;
	
	/**
	 * JDBC template for accessing shared database.
	 */
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * Default constructor for the class.
	 */
	public JdbcPropertyPlaceholderConfigurer() {}
	
	/**
	 * Public constructor for the class.
	 * 
	 * @param dataSource Represents the shared database data source.
	 */
	public JdbcPropertyPlaceholderConfigurer(DataSource dataSource) {
		super();
		Assert.notNull(dataSource, "Please define data source for reading properties from the shared data repository.");
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * Reads properties from the shared data source.
	 * 
	 * @return Properties Returns properties fetched from the shared database.
	 */
	public Properties readPropertiesFromDataSource() {
		properties = new Properties();
		jdbcTemplate.query(PROPERTIES_QUERY_SQL, new RowMapper<Properties>(){
			@Override
			public Properties mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				String key = resultSet.getString("KEY_COLUMN");
				String value = resultSet.getString("VALUE_COLUMN");
				properties.setProperty(key, value);
				return properties;					
			}
		});
		return properties;
	}
	
	/**
	 * Overrides the loadProperties method and adds properties found from the shared database and calls actual super classes method loadProperties with populated properties.
	 */
	@Override
	protected void loadProperties(Properties props) throws IOException {
		properties = readPropertiesFromDataSource();
		if (properties.size() > 0) {
			props.putAll(properties);
		}
		super.loadProperties(props);
	}

}