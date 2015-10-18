package org.flywaydb.test.sample.spring4;


import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit.FlywayTestExecutionListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@FlywayTest
@ContextConfiguration(locations = {"/context/simple_applicationContext.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class, SqlScriptsTestExecutionListener.class})
public class FlywayTestWithSqlScriptsTestExecutionListenerTest extends BaseDBHelper {

    @FlywayTest(locationsForMigrate = "loadmsql")
    @Test
    public void laodFlywayTestInsertWithoutSqlScriptExecutionTest() throws Exception {
        int countCustomer = countCustomer();

        assertEquals("Only Flyway customer should be loaded.", 2, countCustomer);
    }

    @FlywayTest
    @Sql(scripts = "/testSqlFiles/loadSqlScriptExecutionsTest.sql")
    @Test
    public void loadSqlScriptExecutionsWithoutFlywayTestInsertTest() throws Exception {
        int countCustomer = countCustomer();

        assertEquals("Only Sql script customer should be loaded.", 1, countCustomer);
    }

    @FlywayTest(locationsForMigrate = "loadmsql")
    @Sql(scripts = "/testSqlFiles/loadSqlScriptExecutionsTest.sql")
    @Test
    public void loadSqlScriptExecutionsWithFlywayTestInsertTest() throws Exception {
        int countCustomer = countCustomer();

        assertEquals("All customer (Flyway and Sql script) should be loaded.", 3, countCustomer);
    }
}
