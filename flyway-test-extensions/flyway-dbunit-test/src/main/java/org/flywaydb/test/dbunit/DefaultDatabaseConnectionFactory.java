/**
 * Copyright (C) 2011-2017 the original author or authors.
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
package org.flywaydb.test.dbunit;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;

/**
 * Default implementation of a connection factory used by the dbunit support.<p/>
 *
 * Current implementation:
 * <pre>
	if (driverName.toLowerCase().contains("oracle")) {
		// oracle schema name is the user name
		connection = new DatabaseConnection(con, databaseMetaData
				.getUserName().toUpperCase());
	} else {
		if (driverName.contains("H2")) {
			// H2
			connection = new DatabaseConnection(con);
		} else {
			// all other
			connection = new DatabaseConnection(con);
		}
    }
 * </pre>
 *
 * @author Florian
 * @version 1.7
 * @since 1.7.0
 * @version 2012-10-02
 *
 */
public class DefaultDatabaseConnectionFactory implements DatabaseConnectionFactory
{
	// @@ Construction
	protected final Log logger = LogFactory.getLog(getClass());

	// implementation
	public IDatabaseConnection createConnection(final Connection con, final DatabaseMetaData databaseMetaData) throws SQLException, DatabaseUnitException
	{
		IDatabaseConnection connection = null;

		// FIXME not nice I found not a fast possibility to generate inside H2
		// the tables inside a
		// schema as oracle do.
		final String driverName = databaseMetaData.getDriverName();

		if (driverName.toLowerCase().contains("oracle")) {
			// oracle schema name is the user name
			connection = new DatabaseConnection(con, databaseMetaData
					.getUserName().toUpperCase());
		} else {
			if (driverName.contains("H2")) {
				// H2
				connection = new DatabaseConnection(con);
			} else {
				// all other
				connection = new DatabaseConnection(con);
			}
		}

		// final DatabaseConfig config = connection.getConfig();
		// // oracle 10g
		// // FIXME at the moment we have a hard coded oracle notation
		// config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new
		// Oracle10DataTypeFactory());

		return connection;
	}

}
