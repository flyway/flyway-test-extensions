/**
 * Copyright (C) 2011-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flywaydb.test.sample.helper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Simple base class for test make no duplicate code for sql executions
 * @author Florian
 *
 */
public class BaseDBHelper {

	@Autowired
	protected ApplicationContext context;

	protected Connection con;

	/**
	 * Open a connection to database for test execution statements
	 * @throws Exception
	 */
	@Before
	public void setup() throws Exception {

		DataSource ds = (DataSource) context.getBean("dataSourceRef");

		con = ds.getConnection();
	}

	/**
	 * Close the connection
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		if (con != null) {
			if (!con.isClosed()) {
				con.rollback();
				con.close();
			}
		}
		con = null;
	}

	/**
	 * Simple counter query to have simple test inside test methods.
	 * 
	 * @return number of customer in database
	 * @throws Exception
	 */
	public int countCustomer() throws Exception {
		int result = -1;
		Statement stmt = con.createStatement();
		String query = "select count(*) from Customer";

		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		Long cnt = rs.getLong(1);
		result = cnt.intValue();

		rs.close();
		stmt.close();
		
		return result;
	}

	public BaseDBHelper() {
		super();
	}

}