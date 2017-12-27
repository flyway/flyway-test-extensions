package org.flywaydb.test.sample.testng;

import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit.FlywayTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.testng.AssertJUnit.assertEquals;


@ContextConfiguration(locations = {"/context/simple_applicationContext.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class})
@Test
public class BeforeMethodTest extends AbstractTestNGSpringContextTests {

    @Autowired
    protected ApplicationContext context;

    protected Connection con;

    private int index = -1000;

    /**
     * Open a connection to database for test execution statements
     *
     * @throws Exception
     */
    @BeforeMethod
    @FlywayTest(locationsForMigrate = {"loadmsql"})
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

    @Test
    public void simpleCountWithoutAny() throws Exception {
        int res = countCustomer();

        assertEquals("Customer count musst be ", 2, res);

        addCustomer("simpleCountWithoutAny");

        res = countCustomer();

        assertEquals("Customer count musst be ", 3, res);
    }

    @Test
    public void additionalCountWithoutAny() throws Exception {
        int res = countCustomer();

        assertEquals("Customer count musst be ", 2, res);

        addCustomer("additionalCountWithoutAny");

        res = countCustomer();

        assertEquals("Customer count musst be ", 3, res);
    }

    /**
     * Simple counter query to have simple test inside test methods.
     *
     * @return number of customer in database
     * @throws Exception
     */
    private int countCustomer() throws Exception {
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

    private void addCustomer(String name) throws SQLException {

        int newIndex = ++index;

        try (Statement stmt = con.createStatement()) {
            String statement = String.format("insert into Customer (CUS_ID,CUS_NAME) VALUES (%d, '%s')",
                    newIndex, name);

            if (!stmt.execute(statement)) {
                //            throw new SQLException("Cannot insert statement '"+ statement + "'");
            }

            con.commit();
        }
    }
}
