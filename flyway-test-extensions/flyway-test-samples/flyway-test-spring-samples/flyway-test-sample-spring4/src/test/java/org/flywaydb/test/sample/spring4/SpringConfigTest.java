package org.flywaydb.test.sample.spring4;

import org.apache.commons.dbcp.BasicDataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit.FlywayTestExecutionListener;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Show a Flyway Test with Spring configuration and two different flyway configurations.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        SpringConfigTest.TestConfig.class
})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class})
@FlywayTest
public class SpringConfigTest extends BaseDBHelper {

    @Rule
    public TestName testName = new TestName();

    /**
     * Normal test method nothing done per startup.
     * All startup code is be done during class setup.
     */
    @Test
    public void dummyTestNoLoad() throws Exception {
        int res = countCustomer();

        assertTrue("This test must runs without an error, because we can not guarantee that this test method run as first. " + res, true);
    }

    /**
     * Made a clean init migrate usage before execution of test method.
     * SQL statements will be loaded from the default location.
     */
    @Test
    @FlywayTest(flywayName = "flywayOne")
    public void useFlywayOne_noCustomer() throws Exception {
        int res = countCustomer();

        assertEquals("Count of customer", 0, res);
    }

    /**
     * Made a clean init migrate usage before execution of test method.
     * SQL statements will be loaded from the default location.
     */
    @Test
    @FlywayTest(flywayName = "flywaySecond")
    public void useFlywaySecond_withCustomer() throws Exception {
        int res = countCustomer();

        assertEquals("Count of customer", 4, res);
    }

    /**
     * Made a clean init migrate usage before execution of test method and
     * load SQL statements from two directories.
     */
    @Test
    @FlywayTest(locationsForMigrate = {"loadmsql"}, flywayName = "flywayOne")
    public void useFlywayOne_loadMultibleSQLs() throws Exception {
        int res = countCustomer();

        assertEquals("Count of customer", 2, res);
    }

    /**
     * Made a clean init migrate usage before execution of test method and
     * load SQL statements from two directories.
     */
    @Test
    @FlywayTest(locationsForMigrate = {"loadmsql"}, flywayName = "flywaySecond")
    public void useFlywaySecond_loadMultibleSQLs() throws Exception {
        int res = countCustomer();

        assertEquals("Count of customer", 6, res);
    }

    /**
     * Made a clean init migrate usage before execution of test method.
     * SQL statements will be loaded from the default location.
     */
    @Test
    @FlywayTest(invokeBaselineDB = true, flywayName = "flywayOne")
    public void useFlywayOne_testMethodLoadWithBaseline() throws Exception {
        int res = countCustomer();

        assertEquals("Count of customer", 0, res);
    }

    /**
     * Made a clean init migrate usage before execution of test method.
     * SQL statements will be loaded from the default location.
     */
    @Test
    @FlywayTest(invokeBaselineDB = true, flywayName = "flywaySecond")
    public void useFlywaySecond_testMethodLoadWithBaseline() throws Exception {
        int res = countCustomer();

        assertEquals("Count of customer", 4, res);
    }

     // Spring 4 Java Configuration Example


    @Configuration
    @PropertySources({
            @PropertySource(name = "flywayprop", value = "classpath:flyway.properties"),
            @PropertySource(name = "jdbcprop", value = "classpath:jdbc_h2.properties")
    })
    public static class TestConfig {

        @Value("${flyway.locations}")
        public String flywayLocations;

        public String flywaySecondLocation = "/secondLocation";

        @Value("${jdbc.driver}")
        public String driverClassName;
        @Value("${jdbc.url}")
        public String url;
        @Value("${jdbc.username}")
        public String username;
        @Value("${jdbc.password}")
        public String password;

        /**
         * First flyway configuration use "flyway.locations" from properties file.
         */
        @Bean(name = "flywayOne")
        public Flyway flywayOne(DataSource dataSource) {
            Flyway flyway = new Flyway();

            flyway.setDataSource(dataSource);
            flyway.setLocations(flywayLocations);

            return flyway;
        }

        /**
         * Second flyway configuration use "flyway.locations" hard coded value.
         */
        @Bean(name = "flywaySecond")
        public Flyway flywaySecond(DataSource dataSource) {
            Flyway flyway = new Flyway();

            flyway.setDataSource(dataSource);
            flyway.setLocations(flywaySecondLocation);

            return flyway;
        }


        /**
         * Datasource configuration.
         * Alias name only needed for test helper.
         */
        @Bean(destroyMethod = "close", name = {"dataSource", "dataSourceRef"})
        public DataSource dataSource() {
            BasicDataSource dataSource = new BasicDataSource();

            dataSource.setMaxActive(-1);

            dataSource.setUsername(username);

            dataSource.setPassword(password);
            dataSource.setUrl(url);
            dataSource.setDriverClassName(driverClassName);

            return dataSource;
        }

        /*
        * Needed to get properties correct loaded.
        */
        @Bean
        public static PropertySourcesPlaceholderConfigurer propertyConfigIn() {
            return new PropertySourcesPlaceholderConfigurer();
        }

    }
}
