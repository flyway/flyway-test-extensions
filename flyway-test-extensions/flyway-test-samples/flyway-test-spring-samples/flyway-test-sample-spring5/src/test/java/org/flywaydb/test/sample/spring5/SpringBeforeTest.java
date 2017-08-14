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
package org.flywaydb.test.sample.spring5;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationInfoService;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit.FlywayTestExecutionListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Simple Test to show {@link FlywayTest} annotation together with {@link #Before}
 * annotation.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"/context/simple_applicationContext.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class})
@FlywayTest
@PropertySource("classpath:flyway.properties ")
public class SpringBeforeTest extends BaseDBHelper {
    private final Log logger = LogFactory.getLog(getClass());

    @Rule
    public TestName testName = new TestName();

    @After
    public void after() {
        Flyway flyway = context.getBean(Flyway.class);
        logger.info(String.format("\t***** AFTER %s **********", testName.getMethodName()));

        MigrationInfoService
                info = flyway.info();
        System.out.println(info);
        MigrationInfo[] mig = info.all();
        for (MigrationInfo mi : mig) {
            logger.info(String.format("\t%s\t%s\t%s", mi.getVersion(), mi.getScript(), mi.getType()));
        }
        List list = Arrays.asList(info.all());
        logger.info("\t***** AFTER **********");
    }

    @Before
    @FlywayTest(locationsForMigrate = {"loadmsqlbefore"})  // use this inserte for each test
    public void before() {
    }

    /**
     * Test will clean db the database once!
     * <ol>
     * <li>during {link #before}</li>
     * </ol>
     *
     * @throws Exception
     */
    @Test
    public void migrationOnlyWithBefore() throws Exception {
        int res = countCustomer();

        assertThat("Count of customer", res, is(3));
    }

    /**
     * Test will clean db the database twice!
     * <ol>
     * <li>during {link #before}</li>
     * </li>with test annotation but do not use the sql locations from {link #before}.
     * Only customer from test locations are available</li>
     * </ol>
     *
     * @throws Exception
     */
    @Test
    @FlywayTest(locationsForMigrate = {"loadmsql"})
    public void migrationWithAdditionalFlywayTest() throws Exception {
        int res = countCustomer();

        assertThat("Count of customer", res, is(2));
    }

    /**
     * Test will reset the database once during {link #before}.
     * The additional location from before is necessary because otherwise a
     * missing migration validation will occur.
     * Work also with {@link Flyway#setIgnoreMissingMigrations(boolean)} set to <code>false</code>.
     *
     * @throws Exception
     */
    @Test
    @FlywayTest(locationsForMigrate = {"loadmsqlbefore", "loadmsql"}, invokeCleanDB = false)
    public void migrationWithAdditionalFlywayTestWithoutClean() throws Exception {
        int res = countCustomer();

        assertThat("Count of customer", res, is(5));
    }

    /**
     * Test will reset the database once during {link #before}.
     * No location from before is given,
     * work only with {@link Flyway#setIgnoreMissingMigrations(boolean)} to <code>true</code>.
     *
     * @throws Exception
     */
    @Test
    @FlywayTest(locationsForMigrate = {"loadmsql"}, invokeCleanDB = false)
    public void migrationWithAdditionalFlywayTestWithoutCleanNoBeforePath() throws Exception {
        int res = countCustomer();

        assertThat("Count of customer", res, is(5));
    }

}