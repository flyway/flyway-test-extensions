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
package org.flywaydb.test.sample.testng;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Simple Test to show {@link FlywayTest} annotation together with {@link #org.testng.annotations.BeforeClass}
 * annotation.
 */
@ContextConfiguration(locations = {"/context/simple_applicationContext.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class})
@PropertySource("classpath:flyway.properties ")

public class BeforeClassTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private ApplicationContext context;

    private final Log logger = LogFactory.getLog(getClass());

    private static int customerCount = 2;

    private BaseDbHelper baseDbHelper;


    @BeforeClass
    @FlywayTest(locationsForMigrate = {"loadmsql"})
    public static void beforeClass() {
    }

    /**
     * Open a connection to database for test execution statements
     *
     * @throws Exception
     */
    @BeforeMethod
    public void beforeMethod() throws Exception {
        baseDbHelper = new BaseDbHelper(context);
        baseDbHelper.beforeMethod();
    }

    /**
     * Close the connection
     *
     * @throws Exception
     */
    @AfterMethod
    public void afterMethod() throws Exception {
        baseDbHelper.afterMethod();
        customerCount++;
        logger.info(String.format("After test the customer count must be %d.", customerCount));
    }


    @Test
    public void simpleCountWithoutAny() throws Exception {
        int res = countCustomer();

        assertEquals("Customer count musst be ", customerCount, res);

        addCustomer("simpleCountWithoutAny");

        res = countCustomer();

        assertEquals("Customer count musst be ", customerCount+1, res);
    }

    @Test
    public void additionalCountWithoutAny() throws Exception {
        int res = countCustomer();

        assertEquals("Customer count musst be ", customerCount, res);

        addCustomer("additionalCountWithoutAny");

        res = countCustomer();

        assertEquals("Customer count musst be ", customerCount+1, res);
    }

    private int countCustomer() throws Exception {
        return baseDbHelper.countCustomer();
    }

    private void addCustomer(String name) throws SQLException {
        baseDbHelper.addCustomer(name);
    }
}
