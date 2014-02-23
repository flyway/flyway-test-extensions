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
/**
 *
 */
package com.googlecode.flyway.test.sample.spring3;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;

import com.googlecode.flyway.test.dbunit.DefaultDatabaseConnectionFactory;

/**
 * Helper class to show how a connection factory can be changed or extend.
 *
 * @author Florian
 * @version 1.7
 * @since 1.7.0
 * @version 2012-10-02
 *
 */
public class TestDatabaseConnectionFactory extends
		DefaultDatabaseConnectionFactory {

	@Override
	public IDatabaseConnection createConnection(final Connection con,
			final DatabaseMetaData databaseMetaData) throws SQLException,
			DatabaseUnitException {

		logger.warn(String.format(">>>>> %s invoked to create a connection!\n",this.getClass().getSimpleName()));

		IDatabaseConnection result =  super.createConnection(con, databaseMetaData);

		logger.warn(String.format("<<<<<  %s returns connection %s!\n",this.getClass().getSimpleName(),result));

		return result;
	}

}
