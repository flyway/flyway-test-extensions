/**
 * Copyright (C) 2011-2019 the original author or authors.
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
package org.flywaydb.test.sample.junit5;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Simple base class for test make no duplicate code for SQL executions.</p>
 *
 * This class is not usable for production test.
 *
 * @author Florian
 *
 * @version 1.0
 *
 */
public abstract class BaseDBHelper {

    @Autowired
    protected ApplicationContext context;

    protected Connection con;

    private static int index = -1000;


    /**
     * Open a connection to database for test execution statements
     * @throws Exception
     */
    @BeforeEach
    public void baseBeforeEach() throws Exception {

        DataSource ds = (DataSource) context.getBean("dataSourceRef");

        con = ds.getConnection();
    }

    /**
     * Close the connection
     *
     * @throws Exception
     */
    @AfterEach
    public void baseAfterEach() throws Exception {
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

        try (Statement stmt = con.createStatement()) {
            String query = "select count(*) from Customer";

            try (ResultSet rs = stmt.executeQuery(query)) {
                rs.next();
                Long cnt = rs.getLong(1);
                result = cnt.intValue();
            }
        }

        return result;
    }

    /**
     * Add customer with the given name to the database.
     *
     * @param name of the customer to add.
     *
     * @return update count of the insert statement
     *
     * @throws SQLException
     */
    protected int addCustomer(String name) throws SQLException {

        int newIndex = --index;

        try (Statement stmt = con.createStatement()) {
            String statement = String.format("insert into Customer (CUS_ID,CUS_NAME) VALUES (%d, '%s')",
                    newIndex, name);

            int updateCount = stmt.executeUpdate(statement);

            con.commit();

            return updateCount;
        }
    }

    protected BaseDBHelper() {
        super();
    }

}