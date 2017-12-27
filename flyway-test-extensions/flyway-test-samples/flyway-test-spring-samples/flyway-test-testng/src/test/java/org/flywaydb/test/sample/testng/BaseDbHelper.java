package org.flywaydb.test.sample.testng;

import org.springframework.context.ApplicationContext;
import org.testng.annotations.AfterMethod;

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
public class BaseDbHelper {
    protected ApplicationContext context;

    protected Connection con;

    private static int index = -1000;

    public BaseDbHelper(ApplicationContext context) {
        this.context = context;
    }

    /**
     * Open a connection to database for test execution statements
     *
     * @throws Exception
     */
    public void beforeMethod() throws Exception {
        DataSource ds = (DataSource) context.getBean("dataSourceRef");

        con = ds.getConnection();
    }

    /**
     * Close the connection
     *
     * @throws Exception
     */
    @AfterMethod
    public void afterMethod() throws Exception {
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

}
