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
package com.googlecode.flyway.test.dbunit;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;

/**
 * Interface that must be implemented if the default connection factory
 * fit not for the own usage or used database.<p/>
 *
 * @author Florian
 *
 * @version 1.7
 * @since 1.7.0
 * @version 2012-10-02
 */
public interface DatabaseConnectionFactory {

	/**
	 * Create a database connection ({@link IDatabaseConnection}) that can be used for the
	 * dbunit tests.
	 *
	 * @param con connection to the database supported by the driver
	 * @param databaseMetaData meta data from the database.
	 *
	 * @return a new created database connection
	 *
	 * @throws SQLException
	 * @throws DatabaseUnitException
	 */
	public IDatabaseConnection createConnection(final Connection con, final DatabaseMetaData databaseMetaData) throws SQLException, DatabaseUnitException;
}
